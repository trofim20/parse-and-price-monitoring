package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "site_config")
@Getter
@Setter
public class SiteConfigEntity {

    @Id
    private String siteName;

    private String baseUrl;
    private int maxPages;
    private boolean needDetailPage;
    private String paginationParam;
    private String paginationType;
    private String technology;
    private boolean useGeneric;
    private boolean enabled;

    @OneToMany(mappedBy = "siteConfig", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SiteSelectorEntity> selectors;

    public String getSelector(String key){
        return selectors.stream()
                .filter(s -> s.getSelectorKey().equals(key))
                .map(SiteSelectorEntity::getSelectorVal)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Селектор '" + key + "' не найден для сайта " + siteName));
    }
}
