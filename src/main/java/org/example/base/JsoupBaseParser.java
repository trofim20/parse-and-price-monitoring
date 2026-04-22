package org.example.base;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Product;
import org.example.config.ParserConfig;
import org.example.extractor.JsoupProductCardExtractorImpl;
import org.example.util.PageUrlBuilderImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashSet;

@Slf4j
public class JsoupBaseParser extends AbstractParser{
    private final JsoupProductCardExtractorImpl jsoupProductCardExtractor;
    public JsoupBaseParser(String siteName,
                           ParserConfig parserConfig,
                           PageUrlBuilderImpl pageUrlBuilder,
                           JsoupProductCardExtractorImpl jsoupProductCardExtractor) {
        super(siteName, parserConfig, pageUrlBuilder);
        this.jsoupProductCardExtractor = jsoupProductCardExtractor;
    }

    @Override
    public LinkedHashSet<Product> parse() {
        LinkedHashSet<Product> products = new LinkedHashSet<>();
        try{
            int page = 1;
            while (page<=getMaxPage()){
                String currentUrl = buildPageUrl(page);
                Document document = loadPage(currentUrl);

                Elements productCard = document.select(getSelector("card"));
                if (productCard.isEmpty()){
                    log.info("Товары не найдены на странице {}. Завершение парсинга", currentUrl);
                    break;
                }

                for (Element card : productCard){
                    try{
                        Product product = parseProductCard(card);
                        if (isValidProduct(product)){
                            products.add(product);
                            log.debug("Добавлен товар: {} - {}", product.getName(), product.getPrice());
                        }
                    }catch (Exception e) {
                        log.error("Ошибка при парсинге карточки товара", e);
                    }
                }
                page++;
                sleep(2000);
            }
        }catch (Exception e) {
            log.error("Ошибка при парсинге сайта {}",siteName, e);
        }
        log.info("Всего собрано {} товаров с сайта {}", products.size(), siteName);
        return products;
    }

    protected Document loadPage(String currentUrl) {
        if (currentUrl == null || currentUrl.trim().isEmpty()) {
            log.error("currentUrl пустой!");
            return null;
        }

        log.info("Загружаем: {}", currentUrl);

        try {
            Document document = Jsoup.connect(currentUrl)
                    .userAgent(parserConfig.getSelenium().getUserAgent())
                    .timeout(parserConfig.getSelenium().getPageLoadTimeout())
                    .followRedirects(true)
                    .get();
            return document;

        } catch (Exception e) {
            log.error("Jsoup ошибка для [{}]: {} | HTTP: {}",
                    currentUrl, e.getClass().getSimpleName(), e.getMessage(), e);
            return null;
        }
    }


    protected Product parseProductCard(Element card){
        return jsoupProductCardExtractor.extractProductCard(card, getSiteName(), getSiteConfig());
    }

    protected boolean isValidProduct(Product product){
        return product != null && product.getName() != null && product.getPrice() != null;
    }
}
