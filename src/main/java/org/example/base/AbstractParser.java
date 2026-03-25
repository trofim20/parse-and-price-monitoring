package org.example.base;

import lombok.Getter;
import org.example.entity.Product;
import org.example.config.ParserConfig;
import org.example.config.SiteConfig;
import org.example.util.PageUrlBuilderImpl;

import java.math.BigDecimal;
import java.util.LinkedHashSet;

public abstract class AbstractParser {
    @Getter
    protected final String siteName;
    protected final ParserConfig parserConfig;
    protected final PageUrlBuilderImpl pageUrlBuilder;
    protected SiteConfig siteConfig;

    public AbstractParser(String siteName,
                          ParserConfig parserConfig,
                          PageUrlBuilderImpl pageUrlBuilder) {
        this.siteName = siteName;
        this.parserConfig = parserConfig;
        this.pageUrlBuilder = pageUrlBuilder;
    }

    public abstract LinkedHashSet<Product> parse();

    protected SiteConfig getSiteConfig() {
        siteConfig = parserConfig.getSites().get(siteName);
        return siteConfig;
    }

    protected boolean needDetailPage(){
        return getSiteConfig().isNeedDetailPage();
    }

    protected int getMaxPage(){
        return getSiteConfig().getMaxPages();
    }

    protected String getBaseUrl(){
        return getSiteConfig().getBaseUrl();
    }
    protected String getSelector(String elementType){
        return getSiteConfig().getSelectors().get(elementType);
    }

    protected String buildPageUrl(int page){
        return pageUrlBuilder.buildPageUrl(getBaseUrl(),getSiteConfig(),page);
    }

    @Override
    public String toString() {
        return "Parser{" + "siteName='" + siteName + "'}";
    }

    protected boolean isValidProduct(Product product) {
        return product != null
                && product.getName() != null && !product.getName().trim().isEmpty()
                && product.getPrice() != null && product.getPrice().compareTo(BigDecimal.ZERO) > 0
                && product.getUrl() != null && !product.getUrl().trim().isEmpty()
                && product.getArticle() != null && !product.getArticle().trim().isEmpty();
    }


    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
