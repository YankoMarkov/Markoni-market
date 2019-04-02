package markoni.domain.models.bindings;

import javax.validation.constraints.*;

public class UserRegisterBindingModel {
	
	private String username;
	private String password;
	private String confirmPassword;
	private String email;
	private String address;
	
	@NotNull(message = "Username cannot be null.")
	@Size(min = 3, max = 15, message = "Username must be in range [3 - 15] symbols.")
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
	
	@NotNull(message = "Confirm Password cannot be null.")
	@NotBlank(message = "Confirm Password cannot be empty.")
	public String getConfirmPassword() {
		return this.confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	@NotNull(message = "Email cannot be null.")
	@NotBlank(message = "Email cannot be empty.")
	@Email(message = "Invalid email.")
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotNull(message = "Address cannot be null.")
	@NotBlank(message = "Address cannot be empty.")
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}
