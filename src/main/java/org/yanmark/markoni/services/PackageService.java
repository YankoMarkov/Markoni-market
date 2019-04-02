package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;

import java.util.List;

public interface PackageService {
	
	PackageServiceModel savePackage(PackageServiceModel packageService);
	
	List<PackageServiceModel> getAllPackagesByStatus(Status status);
	
	List<PackageServiceModel> getAllPackagesByUserAndStatus(String username, Status status);
	
	PackageServiceModel getPackageById(String id);
}
