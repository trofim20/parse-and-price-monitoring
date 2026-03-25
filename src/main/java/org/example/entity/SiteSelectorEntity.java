package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "site_selector")
@Getter
@Setter
public class SiteSelectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site_name")
    private SiteConfigEntity siteConfig;

    private String selectorKey;
    private String selectorVal;
}
