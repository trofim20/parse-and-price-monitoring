package org.example.repository;

import org.example.entity.SiteConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteConfigRepository extends JpaRepository<SiteConfigEntity, String> {

    /**
     * Возвращает все включённые сайты, которые должны обрабатываться
     * универсальными парсерами
     */
    List<SiteConfigEntity> findAllByEnabledTrueAndUseGenericTrue();

    /**
     * Возвращает все включённые сайты независимо от типа парсера
     */
    List<SiteConfigEntity> findAllByEnabledTrue();
}
