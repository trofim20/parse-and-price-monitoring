package org.example.extractor;

import org.example.entity.Product;
import org.example.entity.SiteConfigEntity;
import org.jsoup.nodes.Element;

public interface JsoupProductCardExtractor {
    Product extractProductCard(Element card, String siteName, SiteConfigEntity siteConfig);
}
