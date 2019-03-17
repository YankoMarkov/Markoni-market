package markoni.interceptors;

import markoni.models.services.UserServiceModel;
import markoni.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class UserLoginInterceptor extends HandlerInterceptorAdapter {
	
	private final UserService userService;
	
	@Autowired
	public UserLoginInterceptor(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		if (username != null) {
			response.sendRedirect("/home");
			return false;
		}
		
		if (request.getMethod().equalsIgnoreCase("post")) {
			username = request.getParameter("username");
			String password = request.getParameter("password");
			
			if (password.equals("") || !this.userService.isValidUser(username, password)) {
				response.sendRedirect("/user/login");
				return false;
			}
			UserServiceModel userServiceModel = this.userService.getUserByUsername(username);
			request.getSession().setAttribute("username", username);
			request.getSession().setAttribute("role", userServiceModel.getRole().toString());
		}
		return true;
	}
}
