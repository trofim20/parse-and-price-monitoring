package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "site_selector")
@Getter
@Setter
public class SiteSelector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site_name")
    private SiteConfig siteConfig;

    private String selectorKey;
    private String selectorVal;
}
