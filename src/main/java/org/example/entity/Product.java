package org.example.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Модель товарной позиции, полученной в результате парсинга сайта
 * о товаре: названия, цен, артикула, принадлежности к сайту и категории
 */
@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название товара
     */
    @Column(nullable = false)
    private String name;

    /**
     * Цена товара
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * Предыдущая цена товара
     */
    @Column(name = "previous_price")
    private BigDecimal previousPrice;

    /**
     * Артикул или внутренний идентификатор товара на сайте
     */

    private String article;

    /**
     * Полный URL страницы товара на сайте.
     */
    @Column(nullable = false)
    private String url;

    /**
     * Идентификатор или доменное имя сайта, с которого был получен товар
     */
    @Column(nullable = false)
    private String site;

    /**
     * Объём или фасовка товара
     */
    private String volume;

    @Column(name = "price_change")
    private BigDecimal priceChange;

    @Column(name = "last_parsed_at")
    private LocalDateTime lastParsedAt;

    public Product() {
    }

    public Product(String site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
