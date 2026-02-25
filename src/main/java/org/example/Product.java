package org.example;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Модель товарной позиции, полученной в результате парсинга сайта
 * о товаре: названия, цен, артикула, принадлежности к сайту и категории
 */
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(
            name = "product_seq",
            sequenceName = "product_id_seq",
            allocationSize = 1
    )
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

    public Product() {
    }

    public Product(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPreviousPrice() {
        return previousPrice;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPriceChange(BigDecimal priceChange) {
        this.priceChange = priceChange;
    }


}
