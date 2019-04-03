package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Package;
import org.yanmark.markoni.domain.entities.Status;
import org.yanmark.markoni.domain.entities.User;
import org.yanmark.markoni.domain.models.services.PackageServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.PackageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		try {
			aPackage = this.packageRepository.saveAndFlush(aPackage);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this.modelMapper.map(aPackage, PackageServiceModel.class);
	}
	
	@Override
	public List<PackageServiceModel> getAllPackagesByStatus(Status status) {
		List<Package> packages = this.packageRepository.findAllByStatus(status);
		if (packages == null) {
			return new ArrayList<>();
		}
		return packages.stream()
				.map(pack -> this.modelMapper.map(pack, PackageServiceModel.class))
				.collect(Collectors.toUnmodifiableList());
	}
	
	@Override
	public List<PackageServiceModel> getAllPackagesByUserAndStatus(UserServiceModel userService, Status status) {
		User user = this.modelMapper.map(userService, User.class);
		List<Package> packages = this.packageRepository.findAllByRecipientAndStatus(user, status);
		if (packages == null) {
			return new ArrayList<>();
		}
		return packages.stream()
				.map(pack -> this.modelMapper.map(pack, PackageServiceModel.class))
				.collect(Collectors.toUnmodifiableList());
	}
	
	@Override
	public PackageServiceModel getPackageById(String id) {
		Package aPackage = this.packageRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Package not found!"));
		return this.modelMapper.map(aPackage, PackageServiceModel.class);
	}
}
