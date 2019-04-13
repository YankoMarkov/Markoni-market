package org.yanmark.markoni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yanmark.markoni.domain.entities.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM products p WHERE lower(p.name) LIKE lower(concat('%', ?1,'%'))")
    List<Product> findAllByName(String name);

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM products p ORDER BY p.name")
    List<Product> findAllOrdered();
}
