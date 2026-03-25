package org.example.extractor;

import org.example.config.SiteConfig;
import org.example.entity.Product;
import org.jsoup.nodes.Element;

public interface JsoupProductCardExtractor {
    Product extractProductCard(Element card, String siteName, SiteConfig siteConfig);
}
