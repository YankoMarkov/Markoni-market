package markoni.services;

import markoni.entities.Package;
import markoni.models.services.PackageServiceModel;
import markoni.repositories.PackageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageServiceImpl implements PackageService {
	
	private final PackageRepository packageRepository;
	private final ModelMapper modelMapper;
	
	@Autowired
	public PackageServiceImpl(PackageRepository packageRepository, ModelMapper modelMapper) {
		this.packageRepository = packageRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public PackageServiceModel savePackage(PackageServiceModel packageService) {
		Package aPackage = this.modelMapper.map(packageService, Package.class);
		aPackage = this.packageRepository.saveAndFlush(aPackage);
		if (aPackage == null) {
			return null;
		}
		return this.modelMapper.map(aPackage, PackageServiceModel.class);
	}
}
