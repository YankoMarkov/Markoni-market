package markoni.services;

import markoni.domain.models.services.UserServiceModel;

import java.util.List;

public interface UserService {
	
	UserServiceModel saveUser(UserServiceModel userService);
	
	UserServiceModel getUserById(String id);
	
	UserServiceModel getUserByUsername(String username);
	
	List<UserServiceModel> getAllUsers();
	
	boolean userExist(String username);
	
	boolean isValidUser(String username, String password);
}
