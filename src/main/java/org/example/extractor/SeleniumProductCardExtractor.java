package org.example.extractor;

import org.example.config.SiteConfig;
import org.example.entity.Product;
import org.openqa.selenium.WebElement;

public interface SeleniumProductCardExtractor {
    Product extractProductCard(WebElement webElement, String siteName, SiteConfig siteConfig);

}
