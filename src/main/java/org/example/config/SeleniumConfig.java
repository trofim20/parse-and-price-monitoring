package org.example.config;



/**
 * Настройки Selenium‑драйвера, используемые всеми парсерами
 */
public class SeleniumConfig {

    /**
     * Путь к исполняемому файлу chromedriver
     */
    private String chromeDriverPath;

    /**
     * User-Agent, который будет проброшен в браузер
     */
    private String userAgent;

    /**
     * Размер окна браузера
     */
    private String windowSize;

    /**
     * Таймаут загрузки страницы
     */
    private int pageLoadTimeout;

    /**
     * Таймаут неявного ожидания элементов
     */
    private int implicitWaitTimeout;

    /**
     * Флаг запуска браузера в headless‑режиме
     */
    private boolean headless;

    public String getChromeDriverPath() {
        return chromeDriverPath;
    }

    public void setChromeDriverPath(String chromeDriverPath) {
        this.chromeDriverPath = chromeDriverPath;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(String windowSize) {
        this.windowSize = windowSize;
    }

    public int getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public void setPageLoadTimeout(int pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public int getImplicitWaitTimeout() {
        return implicitWaitTimeout;
    }

    public void setImplicitWaitTimeout(int implicitWaitTimeout) {
        this.implicitWaitTimeout = implicitWaitTimeout;
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }
}

