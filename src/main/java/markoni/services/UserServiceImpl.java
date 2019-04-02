package markoni.services;

import markoni.domain.entities.Role;
import markoni.domain.entities.User;
import markoni.domain.models.services.UserServiceModel;
import markoni.repositories.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public UserServiceModel saveUser(UserServiceModel userService) {
		User user = this.modelMapper.map(userService, User.class);
		user.setPassword(DigestUtils.sha256Hex(userService.getPassword()));
		setUserRole(user);
		try {
			user = this.userRepository.saveAndFlush(user);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this.modelMapper.map(user, UserServiceModel.class);
	}
	
	@Override
	public UserServiceModel getUserById(String id) {
		User user = this.userRepository.findById(id).orElse(null);
		if (user == null) {
			return null;
		}
		return this.modelMapper.map(user, UserServiceModel.class);
	}
	
	@Override
	public UserServiceModel getUserByUsername(String username) {
		User user = this.userRepository.findByUsername(username).orElse(null);
		if (user == null) {
			return null;
		}
		return this.modelMapper.map(user, UserServiceModel.class);
	}
	
	@Override
	public List<UserServiceModel> getAllUsers() {
		List<User> users = this.userRepository.findAll();
		if (users == null) {
			return new ArrayList<>();
		}
		return users.stream()
				.map(user -> this.modelMapper.map(user, UserServiceModel.class))
				.collect(Collectors.toUnmodifiableList());
	}
	
	@Override
	public boolean userExist(String username) {
		return getUserByUsername(username) != null;
	}
	
	@Override
	public boolean isValidUser(String username, String password) {
		return userExist(username) &&
				getUserByUsername(username).getPassword().equals(DigestUtils.sha256Hex(password));
	}
	
	private void setUserRole(User user) {
		user.setRole(this.userRepository.count() == 0 ? Role.ADMIN : Role.USER);
	}
}
