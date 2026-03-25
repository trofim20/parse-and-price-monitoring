package org.example.config;




import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Конфигурация отдельного сайта для парсинга.
 * <p>
 * Описывает базовый URL, количество страниц, схему пагинации
 * и CSS‑/XPath‑селекторы для извлечения данных.
 */
@Getter
@Setter
public class SiteConfig {

    /**
     * Базовый URL каталога/поиска, с которого начинается парсинг.
     */
    private String baseUrl;

    /**
     * Максимальное количество страниц, которые следует обходить.
     */
    private int maxPages;

    /**
     * Нужно ли дополнительно заходить на детальную страницу товара.
     */
    private boolean needDetailPage;

    /**
     * Технология парсинга для сайта
     */
    private String technology = "selenium";

    /**
     * Набор селекторов (CSS/XPath) для выборки элементов
     * Ключ — логическое имя селектора,
     * значение — строка селектора.
     */
    private Map<String, String> selectors;

    /**
     * Тип пагинации
     */
    private String paginationType;

    /**
     * Имя параметра, в который подставляется номер страницы
     */
    private String paginationParameter;
}

