package org.example.repository;

import org.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByUrl(String url);

    @Query("SELECT p FROM Product p WHERE p.previousPrice IS NOT NULL ORDER BY p.id DESC")
    List<Product> findRecentPriceChanges();

    @Query("SELECT p FROM Product p WHERE p.previousPrice IS NOT NULL AND p.lastParsedAt > :since ORDER BY p.lastParsedAt DESC")
    List<Product> findPriceChangesSince(@Param("since") LocalDateTime since);

}
