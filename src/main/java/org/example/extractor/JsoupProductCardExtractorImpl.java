package org.example.extractor;

import org.example.config.SiteConfig;
import org.example.entity.Product;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JsoupProductCardExtractorImpl implements JsoupProductCardExtractor {
    @Override
    public Product extractProductCard(Element card, String siteName, SiteConfig siteConfig) {
        Product product = new Product(siteName);
        extractName(card,siteConfig,product);
        extractPrice(card, siteConfig, product);
        extractUrl(card, siteConfig, product);
        extractArticle(card,siteConfig,product);
        return product;
    }

    private Element findElement(Element card, String selector) {
        if (selector == null) return null;
        if (selector.startsWith("xpath:")) {
            return card.selectXpath(selector.substring(6)).first();
        }
        return card.selectFirst(selector);
    }

    public void extractName(Element card, SiteConfig siteConfig, Product product) {
        try {
            product.setName(safeText(findElement(card, siteConfig.getSelectors().get("name"))));
        } catch (Exception e) {}
    }

    public void extractArticle(Element card, SiteConfig siteConfig, Product product) {
        try {
            product.setArticle(safeText(findElement(card, siteConfig.getSelectors().get("article"))));
        } catch (Exception e) {}
    }

    public void extractPrice(Element card, SiteConfig siteConfig, Product product) {
        try {
            String text = safeText(findElement(card, siteConfig.getSelectors().get("price")));
            product.setPrice(extractPriceAsBigDecimal(text));
        } catch (Exception e) {}
    }

    public void extractUrl(Element card, SiteConfig siteConfig, Product product) {
        try {
            Element url = findElement(card, siteConfig.getSelectors().get("url"));
            String href = url.attr("href");
            product.setUrl(href != null ? href.trim() : null);
        } catch (Exception e) {}
    }


    private String safeText(Element element) {
        if (element == null) return "";
        String text = element.text();
        return text != null ? text.replace("\u00A0", " ").trim() : "";
    }

    protected BigDecimal extractPriceAsBigDecimal(String priceText) {
        String price = extractPriceAsIntegerPart(priceText);
        return new BigDecimal(price);
    }

    private String extractPriceAsIntegerPart(String priceText) {
        if (priceText == null || priceText.trim().isEmpty()) return "0";
        String cleaned = priceText.trim()
                .replaceAll("[^0-9.,]", "")
                .replace(",", ".");
        return cleaned.split("\\.")[0];
    }
}
