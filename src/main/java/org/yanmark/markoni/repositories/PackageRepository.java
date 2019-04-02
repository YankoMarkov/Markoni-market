package org.yanmark.markoni.repositories;

import org.yanmark.markoni.domain.entities.Package;
import org.yanmark.markoni.domain.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, String> {
	
	List<Package> findAllByStatus(Status status);
	
	List<Package> findAllByRecipientAndStatus(String username, Status status);
}