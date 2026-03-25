package org.example.extractor;

import org.example.entity.Product;
import org.example.entity.SiteConfigEntity;
import org.openqa.selenium.WebElement;

public interface SeleniumProductCardExtractor {
    Product extractProductCard(WebElement webElement, String siteName, SiteConfigEntity siteConfig);

}
