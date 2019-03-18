package markoni.services;

import markoni.entities.Status;
import markoni.models.services.PackageServiceModel;

import java.util.List;

public interface PackageService {
	
	PackageServiceModel savePackage(PackageServiceModel packageService);
	
	List<PackageServiceModel> getAllPackagesByStatus(Status status);
	
	List<PackageServiceModel> getAllPackagesByUserAndStatus(String username, Status status);
	
	PackageServiceModel getPackageById(String id);
}
