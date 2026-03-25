package org.example.service;


import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Product;
import org.example.base.AbstractParser;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ParsingService {
    private final ProductRepository productRepository;
    private final List<AbstractParser> parsers;
    private final PriceAlertService priceAlertService;

    public ParsingService(ProductRepository productRepository, List<AbstractParser> parsers, PriceAlertService priceAlertService) {
        this.productRepository = productRepository;
        this.parsers = parsers;
        this.priceAlertService = priceAlertService;
    }

    @Transactional
    public void parseAndSaveAll(){
        log.info("НАЧИНАЕМ ПАРСИНГ И СОХРАНЕНИЕ");

        List<Product> allProducts = new ArrayList<>();
        for (AbstractParser parser : parsers) {
            String siteName = parser.getSiteName();
            log.info("Парсинг сайта {}", siteName);

            LinkedHashSet<Product> products = parser.parse();
            log.info("Найдено продуктов для {}: {}", siteName, products.size());

            allProducts.addAll(products);
        }

        log.info("всего продуктов для сохранения: {}", allProducts.size());
        List<Product> toSave = upsertProducts(allProducts);
        log.info("к сохранению: {}", toSave.size());

        if (!toSave.isEmpty()) {
            List<Product> saved = productRepository.saveAll(toSave);
            log.info("СОХРАНЕНО В БД: {}", saved.size());
        } else {
            log.error("toSave пустой");
        }
    }

    private List<Product> upsertProducts(List<Product> newProducts) {
        List<Product> toSave = new ArrayList<>();
        LinkedHashMap<String, Product> uniqueByUrl = new LinkedHashMap<>();

        for (Product product : newProducts) {
            if (product.getUrl() != null && !product.getUrl().trim().isEmpty()) {
                uniqueByUrl.put(product.getUrl().trim(), product);
            } else {
                toSave.add(product);
                log.debug("Продукт без URL: {}", product.getName());
            }
        }
        for (Product newProd : uniqueByUrl.values()) {
            Optional<Product> existingOpt = productRepository.findByUrl(newProd.getUrl().trim());

            if (existingOpt.isPresent()) {
                Product existing = existingOpt.get();
                BigDecimal oldPrice = existing.getPrice();
                BigDecimal newPrice = newProd.getPrice();

                if (oldPrice != null && newPrice != null) {
                    BigDecimal diff = newPrice.subtract(oldPrice);
                    existing.setPreviousPrice(oldPrice);
                    existing.setPriceChange(diff);
                    existing.setLastParsedAt(LocalDateTime.now());
                    if (diff.abs().compareTo(new BigDecimal("200")) > 0) {
                        priceAlertService.onPriceChange(existing, oldPrice, newPrice, diff);
                    }
                }

                existing.setPrice(newProd.getPrice());

                existing.setArticle(newProd.getArticle());

                existing.setName(newProd.getName());

                existing.setLastParsedAt(LocalDateTime.now());

                toSave.add(existing);
            } else {
                newProd.setLastParsedAt(LocalDateTime.now());
                toSave.add(newProd);
            }
        }
        return toSave;
    }

}
