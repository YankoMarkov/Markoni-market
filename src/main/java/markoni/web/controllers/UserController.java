package markoni.web.controllers;

import markoni.models.bindings.UserLoginBindingModel;
import markoni.models.bindings.UserRegisterBindingModel;
import markoni.models.services.UserServiceModel;
import markoni.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	
	private final UserService userService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public UserController(UserService userService, ModelMapper modelMapper) {
		this.userService = userService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/register")
	public ModelAndView register(@ModelAttribute("userRegister") UserRegisterBindingModel userRegister) {
		return this.view("register");
	}
	
	@PostMapping("register")
	public ModelAndView registerConfirm(@Valid @ModelAttribute("userRegister") UserRegisterBindingModel userRegister,
	                                    BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("register");
		}
		UserServiceModel userServiceModel = this.modelMapper.map(userRegister, UserServiceModel.class);
		
		if (this.userService.saveUser(userServiceModel) == null) {
			return this.view("register");
		}
		return this.redirect("/user/login");
	}
	
	@GetMapping("/login")
	public ModelAndView login(@ModelAttribute("userLogin") UserLoginBindingModel userLogin) {
		return this.view("login");
	}
	
	@PostMapping("/login")
	public ModelAndView loginConfirm(@Valid @ModelAttribute("userLogin") UserLoginBindingModel userLogin,
	                                 BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("login");
		}
		return this.redirect("/home");
	}
	
	@GetMapping("/logout")
	public ModelAndView logout(HttpSession session) {
		if (session.getAttribute("username") == null) {
			this.redirect("/user/login");
		}
		session.invalidate();
		return this.redirect("/");
	}
}
