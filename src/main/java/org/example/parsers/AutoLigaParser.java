package org.example.parsers;

import org.example.entity.Product;
import org.example.base.JsoupBaseParser;
import org.example.config.ParserConfig;
import org.example.entity.SiteConfigEntity;
import org.example.extractor.JsoupProductCardExtractorImpl;
import org.example.util.PageUrlBuilderImpl;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class AutoLigaParser extends JsoupBaseParser {
    public AutoLigaParser(ParserConfig parserConfig, PageUrlBuilderImpl pageUrlBuilder, JsoupProductCardExtractorImpl productCardExtractor) {
        super("auto-liga", parserConfig, pageUrlBuilder, productCardExtractor);
    }

    @Override
    protected Product parseProductCard(Element card) {
        Element buyBtn = card.selectFirst("a.dc-buy-button");
        if (buyBtn != null) {
            String btnText = buyBtn.text().trim().toLowerCase();
            if(btnText.contains("узнать цену")){
                return null;
            }
        }
        Product product = super.parseProductCard(card);
        SiteConfigEntity siteConfig = getSiteConfig();

        Element url = card.selectFirst(siteConfig.getSelector("url"));
        String href = url.attr("href");
        if (href != null && href.startsWith("/")){
            href = "https://автолига.рф" + href;
        }
        product.setUrl(href);


        return product;
    }

    @Override
    protected String buildPageUrl(int page) {
        String base = getBaseUrl();
        if (page <= 1) return base;
        String clean = base.replaceAll("([&?]p=)\\d+", "$1");
        String sep = clean.contains("?") ? "&" : "?";
        return clean + sep + "p=" + page;
    }
}
