package org.example.base;

import org.example.Product;
import org.example.config.ParserConfig;
import org.example.config.SiteConfig;
import org.example.util.PageUrlBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractParser {
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

    public abstract List<Product> parse();

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

    public String getSiteName() {
        return siteName;
    }

    @Override
    public String toString() {
        return "Parser{" + "siteName='" + siteName + "'}";
    }


}
