package org.example.service;


import lombok.extern.slf4j.Slf4j;
import org.example.entity.Product;
import org.example.entity.SiteConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@Slf4j
public class DetailPageService {
    public void enrichProductFromDetailPage(Product product, WebDriver webDriver, SiteConfig siteConfig) {
        String originalWindow = webDriver.getWindowHandle();
        String newTab = null;

        try {
            newTab = openInNewTab(webDriver, product.getUrl());
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

            String articleSelector = siteConfig.getSelector("article");
            WebElement articleElement = wait.until(ExpectedConditions.presenceOfElementLocated(resolveBy(articleSelector)));
            product.setArticle(safeText(articleElement));

            log.debug("Артикул для {}: {}", product.getName(), product.getArticle());
        } catch (Exception e) {
            log.error("Ошибка при парсинге детальной страницы для {}: {}",
                    product.getName(), e.getMessage(), e);
        } finally {
            closeNewTabAndSwitchBack(webDriver, originalWindow, newTab);
        }
    }

    private String openInNewTab(WebDriver webDriver, String url) {
        Set<String> before = webDriver.getWindowHandles();
        ((JavascriptExecutor) webDriver).executeScript("window.open('');");

        Set<String> after = webDriver.getWindowHandles();
        after.removeAll(before);

        String newTab = after.iterator().next();
        webDriver.switchTo().window(newTab);
        webDriver.get(url);
        return newTab;
    }


    private void closeNewTabAndSwitchBack(WebDriver driver, String originalWindow, String newTab) {
        try {
            if (newTab != null && driver.getWindowHandles().contains(newTab)) {
                driver.switchTo().window(newTab);
                driver.close();
            }
        } catch (Exception e) {
            log.debug("Не удалось закрыть новую вкладку: {}", e.getMessage());
        } finally {
            if (driver.getWindowHandles().contains(originalWindow)) {
                driver.switchTo().window(originalWindow);
            }
        }
    }

    private String safeText(WebElement element) {
        try {
            return element.getText().trim().replace("\u00A0", " ");
        } catch (Exception e) {
            return "";
        }
    }

    private By resolveBy(String selector) {
        if (selector.startsWith("xpath:")) {
            return By.xpath(selector.substring(6));
        }
        return By.cssSelector(selector);
    }
}
