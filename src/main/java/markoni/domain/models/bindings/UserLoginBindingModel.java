package markoni.domain.models.bindings;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserLoginBindingModel {
	
	private String username;
	private String password;
	
	@NotNull(message = "Username cannot be null.")
	@NotBlank(message = "Username cannot be empty.")
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@NotNull(message = "Password cannot be null.")
	@NotBlank(message = "Password cannot be empty.")
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
