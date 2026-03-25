package org.example.parsers;

import org.example.base.JsoupBaseParser;

import org.example.config.ParserConfig;
import org.example.entity.Product;
import org.example.entity.SiteConfigEntity;
import org.example.extractor.JsoupProductCardExtractorImpl;
import org.example.util.PageUrlBuilderImpl;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;


@Component
public class CentaurusParser extends JsoupBaseParser {
    public CentaurusParser(ParserConfig parserConfig, PageUrlBuilderImpl pageUrlBuilder, JsoupProductCardExtractorImpl productCardExtractor) {
        super("centaurus", parserConfig, pageUrlBuilder,productCardExtractor);
    }

    @Override
    protected Product parseProductCard(Element card) {
        Element buyDescription = card.selectFirst(" div > div.item_info.catalog_item_overlay_sibling.TYPE_1 > div.sa_block > div.item-stock-pre");
        if (buyDescription != null) {
            String btnText = buyDescription.text().trim().toLowerCase();
            if(btnText.contains("под заказ")){
                return null;
            }
        }
        Product product = super.parseProductCard(card);
        SiteConfigEntity siteConfig = getSiteConfig();
        Element url = card.selectFirst(siteConfig.getSelector("url"));
        String href = url.attr("href");
        if (href != null && href.startsWith("/")){
            href = "https://www.centaurus.com.ru" + href;
        }
        product.setUrl(href);

        return product;
    }
}



