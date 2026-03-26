package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "selenium_config")
@Getter
@Setter
public class SeleniumConfig {

    @Id
    private Integer id;
    private String userAgent;
    private String windowSize;
    private int pageLoadTimeout;
    private int implicitWaitTimeout;
    private boolean headless;
}
