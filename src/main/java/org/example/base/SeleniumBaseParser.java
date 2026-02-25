package org.example.base;

import org.example.Product;
import org.example.config.ParserConfig;
import org.example.service.DetailPageService;
import org.example.service.WebDriverService;

import org.example.util.PageUrlBuilderImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;



@Component
public abstract class SeleniumBaseParser extends AbstractParser {
    private static final Logger log = LoggerFactory.getLogger(SeleniumBaseParser.class);

    private final WebDriverService webDriverService;

    private final DetailPageService detailPageService;

    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    public SeleniumBaseParser(String siteName,
                              ParserConfig parserConfig,
                              PageUrlBuilderImpl pageUrlBuilder,
                              WebDriverService webDriverService,
                              DetailPageService detailPageService) {
        super(siteName, parserConfig, pageUrlBuilder);
        this.webDriverService = webDriverService;
        this.detailPageService = detailPageService;
    }

    @Override
    public List<Product> parse() {
        List<Product> products = new ArrayList<>();
        try {
            initializeDriver();
            int page = 1;
            while (page <= getMaxPage()) {
                String currentUrl = buildPageUrl(page);
                log.debug("парсин страницы " + currentUrl + "для сайта " + siteName);

                if (!navigateToPage(currentUrl)) {
                    break;
                }

                List<WebElement> productCards = getProductCards();
                if (productCards.isEmpty()) {
                    break;
                }

                for (WebElement productCard : productCards) {
                    try {
                        Product product = parseProductCard(productCard);
                        if (isValidProduct(product)) {
                            if (needDetailPage() && product.getUrl() != null) {
                                try {
                                    detailPageService.enrichProductFromDetailPage(product,webDriver,getSiteConfig());
                                    log.info("needDetailPage={}, url!=null={}, detailPage={}: {}",
                                            needDetailPage(), product.getUrl() != null, product.getName());
                                } catch (Exception detailEx) {
                                    log.warn("Ошибка деталки для {}: {}", product.getName(), detailEx.getMessage());
                                }
                            }else{
                                try {
                                    WebElement article = productCard.findElement(By.cssSelector(getSelector("article")));
                                    product.setArticle(safeText(article));
                                }catch (Exception detailEx) {
                                    log.warn("Ошибка получения артикула с сайта" + getSiteName());
                                }
                            }
                            products.add(product);

                        }
                    } catch (Exception e) {

                    }
                }
                page++;
                sleep(2000);
            }
        } catch (Exception e) {
            log.error("Ошибка при парсинге сайта " + siteName, e);
        } finally {
            closeDriver();
        }
        return products;
    }

    protected boolean navigateToPage(String url) {
        try {
            webDriver.get(url);
            sleep(2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected List<WebElement> getProductCards() {
        String cardSelector = getSelector("card");
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cardSelector)));
            return webDriver.findElements(By.cssSelector(cardSelector));
        } catch (Exception first) {
            try {
                ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                sleep(800);
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cardSelector)));
                return webDriver.findElements(By.cssSelector(cardSelector));
            } catch (Exception second) {
                log.warn("Не удалось найти карточки товаров", second);
                return new ArrayList<>();
            }
        }
    }

    protected Product parseProductCard(WebElement productCard) {
        Product product = new Product(getSiteName());
        try {
            WebElement productNameElement = productCard.findElement(By.cssSelector(getSelector("name")));
            product.setName(safeText(productNameElement));

            try {
                WebElement priceElement = productCard.findElement(By.cssSelector(getSelector("price")));
                product.setPrice(extractPriceAsBigDecimal(safeText(priceElement)));
            } catch (Exception e) {

            }
            try {
                WebElement price = productCard.findElement(By.cssSelector(getSelector("price")));
                product.setPrice(extractPriceAsBigDecimal(safeText(price)));
            } catch (Exception e) {
            }

            try {
                WebElement urlElement = productCard.findElement(By.cssSelector(getSelector("url")));
                String url = urlElement.getAttribute("href");
                if (url != null && !url.isEmpty()) {
                    product.setUrl(url);

                }
            } catch (Exception e) {
            }

        } catch (Exception e) {

        }
        return product;
    }

    protected boolean isValidProduct(Product product) {
        return product != null && product.getName() != null && !product.getName().trim().isEmpty();
    }

    protected String safeText(WebElement element) {
        if (element == null) return "";
        try {
            String text = element.getText();
            return text != null ? text.replace("\u00A0", " ").trim() : "";
        } catch (Exception e) {
            return "";
        }
    }

    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected BigDecimal extractPriceAsBigDecimal(String priceText) {
        if (priceText == null || priceText.trim().isEmpty()) return null;
        String cleaned = priceText.trim()
                .replaceAll("[^0-9.,]", "")
                .replace(",", ".")
                .replaceAll("р|₽|руб", "");
        try {
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            log.warn("Не удалось распарсить цену: {}", priceText);
            return null;
        }
    }

    private void closeDriver() {
        if (webDriver != null) {
            try {
                webDriver.quit();
            } catch (Exception e) {
                log.warn("Ошибка при закрытии WebDriver", e);
            }
        }
    }

    private void initializeDriver() {
        this.webDriver = webDriverService.createWebDriver();
        int timeout = parserConfig.getSelenium().getPageLoadTimeout();
        this.webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(timeout));
    }
}
