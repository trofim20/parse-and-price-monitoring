package org.example.extractor;

import org.example.config.SiteConfig;
import org.example.entity.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SeleniumProductCardExtractorImpl implements SeleniumProductCardExtractor {

    @Override
    public Product extractProductCard(WebElement card, String siteName, SiteConfig siteConfig) {
        Product product = new Product(siteName);
        extractName(card, siteConfig, product);
        extractPrice(card, siteConfig, product);
        extractUrl(card, siteConfig, product);
        extractArticle(card, siteConfig, product);
        return product;
    }

    private WebElement findElement(WebElement card, String selector) {
        if (selector == null) return null;
        if (selector.startsWith("xpath:")) {
            return card.findElement(By.xpath(selector.substring(6)));
        }
        return card.findElement(By.cssSelector(selector));
    }

    private void extractArticle(WebElement card, SiteConfig siteConfig, Product product) {
        if (siteConfig.isNeedDetailPage()) return;
        try {
            String selector = siteConfig.getSelectors().get("article");
            product.setArticle(safeText(findElement(card, selector)));
        } catch (Exception e) {}
    }

    private void extractName(WebElement card, SiteConfig siteConfig, Product product) {
        try {
            String selector = siteConfig.getSelectors().get("name");
            product.setName(safeText(findElement(card, selector)));
        } catch (Exception e) {}
    }

    private void extractPrice(WebElement card, SiteConfig siteConfig, Product product) {
        try {
            String selector = siteConfig.getSelectors().get("price");
            product.setPrice(extractPriceAsBigDecimal(safeText(findElement(card, selector))));
        } catch (Exception e) {}
    }

    private void extractUrl(WebElement card, SiteConfig siteConfig, Product product) {
        try {
            String selector = siteConfig.getSelectors().get("url");
            String href = findElement(card, selector).getAttribute("href");
            product.setUrl(href.trim());
        } catch (Exception e) {}
    }

    public String safeText(WebElement element) {
        try {
            String text = element.getText();
            return text != null ? text.replace("\u00A0", " ").trim() : "";
        } catch (Exception e) {
            return "null";
        }
    }

    public BigDecimal extractPriceAsBigDecimal(String priceText) {
        return new BigDecimal(extractPriceAsIntegerPart(priceText));
    }

    private String extractPriceAsIntegerPart(String priceText) {
        if (priceText == null || priceText.trim().isEmpty()) return "0";
        String cleaned = priceText.trim()
                .replaceAll("[^0-9.,]", "")
                .replace("₽", "")
                .replace(",", ".");
        return cleaned.split("\\.")[0];
    }
}
