package markoni.services;

import markoni.entities.Role;
import markoni.entities.User;
import markoni.models.services.UserServiceModel;
import markoni.repositories.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public boolean saveUser(UserServiceModel userService) {
		User user = null;
		try {
			user = this.modelMapper.map(userService, User.class);
			user.setPassword(DigestUtils.sha256Hex(userService.getPassword()));
			setUserRole(user);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		this.userRepository.saveAndFlush(user);
		return true;
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
