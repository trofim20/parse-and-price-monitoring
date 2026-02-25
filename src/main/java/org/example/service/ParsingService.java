package org.example.service;

import jakarta.transaction.Transactional;

import org.example.Product;
import org.example.base.AbstractParser;
import org.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParsingService {
    private static final Logger log = LoggerFactory.getLogger(ParsingService.class);
    private final ProductRepository productRepository;
    private final List<AbstractParser> parsers;

    public ParsingService(ProductRepository productRepository, List<AbstractParser> parsers) {
        this.productRepository = productRepository;
        this.parsers = parsers;
    }

    @Transactional
    public void parseAndSaveAll(){
        List<Product> allProducts = new ArrayList<>();
        for (AbstractParser parser : parsers) {
            String siteName = parser.getSiteName();
            log.info("Парсинг сайта  " + siteName);

            List<Product> products = parser.parse();
            log.info("Количество продуктов " + products.size());
            allProducts.addAll(products);
        }
        List<Product> toSave = upsertProducts(allProducts);
        calculatePriceChanges(toSave);
        productRepository.saveAll(toSave);
        log.info("Сохранение данных в бд  " + allProducts.size());

    }

    private List<Product> upsertProducts(List<Product> newProducts) {
        List<Product> toSave = new ArrayList<>();


        Map<String, Product> uniqueByUrl = new HashMap<>();
        for (Product p : newProducts) {
            if (p.getUrl() != null && !p.getUrl().isEmpty()) {
                uniqueByUrl.put(p.getUrl(), p);
            }
        }
        log.info("Уникальных по URL: {}", uniqueByUrl.size());
        log.info("Уникальных по URL: {}", uniqueByUrl.size());


        for (Product newProd : uniqueByUrl.values()) {

            var existingOpt = productRepository.findByUrl(newProd.getUrl());

            if (existingOpt.isPresent()) {

                Product existing = existingOpt.get();
                existing.setPrice(newProd.getPrice());
                existing.setArticle(newProd.getArticle());
                toSave.add(existing);

                log.debug("🔄 Update {}", newProd.getUrl());
            } else {
                toSave.add(newProd);
                log.debug("➕ New {}", newProd.getUrl());
            }
        }
        return toSave;
    }


    private void calculatePriceChanges(List<Product> products) {
        products.forEach(p -> {
            if (p.getPrice() != null && p.getPreviousPrice() != null) {
                p.setPriceChange(p.getPrice().subtract(p.getPreviousPrice()));
            }
        });
    }

}
