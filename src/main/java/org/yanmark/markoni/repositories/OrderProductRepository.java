package org.yanmark.markoni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yanmark.markoni.domain.entities.OrderProduct;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, String> {
}
