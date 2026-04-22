package org.example.config;


import lombok.Getter;
import lombok.Setter;

/**
 * Настройки Selenium‑драйвера, используемые всеми парсерами
 */
@Getter
@Setter
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
}

