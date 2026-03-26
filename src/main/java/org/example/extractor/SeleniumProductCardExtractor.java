package org.example.extractor;

import org.example.entity.Product;
import org.example.entity.SiteConfig;
import org.openqa.selenium.WebElement;

public interface SeleniumProductCardExtractor {
    Product extractProductCard(WebElement webElement, String siteName, SiteConfig siteConfig);

}
