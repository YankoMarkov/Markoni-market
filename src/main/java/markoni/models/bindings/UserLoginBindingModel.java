package markoni.models.bindings;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserLoginBindingModel {
	
	private String username;
	private String password;
	
	@NotNull
	@NotEmpty(message = "missing username")
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@NotNull
	@NotEmpty(message = "missing password")
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
