package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;

import java.util.List;

public interface PackageService {
	
	PackageServiceModel savePackage(PackageServiceModel packageService);
	
	List<PackageServiceModel> getAllPackagesByStatus(Status status);
	
	List<PackageServiceModel> getAllPackagesByUserAndStatus(UserServiceModel userService, Status status);
	
	PackageServiceModel getPackageById(String id);
}
