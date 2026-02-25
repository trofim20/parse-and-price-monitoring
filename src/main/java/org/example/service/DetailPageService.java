package org.example.service;


import org.example.Product;
import org.example.config.SiteConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DetailPageService {

    private static final Logger log = LoggerFactory.getLogger(DetailPageService.class);
    public void enrichProductFromDetailPage(Product product, WebDriver webDriver, SiteConfig siteConfig) {
        String originalWindow = webDriver.getWindowHandle();

        try{
            openInNewTable(webDriver, product.getUrl());
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(3));
            String articleSelector = siteConfig.getSelectors().get("article");
            WebElement articleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(articleSelector)));
            product.setArticle(articleElement.getText().trim());

        } catch (NullPointerException e) {
            log.warn("Таймаут при загрузке детальной страницы для {}", product.getName());
        } catch (Exception e) {
            log.error("Ошибка при парсинге детальной страницы для {}: {}",
                    product.getName(), e.getMessage(), e);
        } finally {
            closeCurrentTabAndSwitchBack(webDriver, originalWindow);
        }
    }

    private void openInNewTable(WebDriver webDriver, String url) {
        ((JavascriptExecutor) webDriver).executeScript("window.open(arguments[0], '_blank');", url);

        String originalWindow = webDriver.getWindowHandle();
        for(String windowHandle : webDriver.getWindowHandles()){
            if(!originalWindow.equals(windowHandle)){
                webDriver.switchTo().window(windowHandle);
                return;
            }
        }
    }

    private void closeCurrentTabAndSwitchBack(WebDriver driver, String originalWindow) {
        try {
            if (!driver.getWindowHandle().equals(originalWindow)) {
                driver.close();
            }
        } catch (Exception e) {
            log.warn("Не удалось закрыть вкладку: {}", e.getMessage());
        } finally {
            driver.switchTo().window(originalWindow);
        }
    }
}
