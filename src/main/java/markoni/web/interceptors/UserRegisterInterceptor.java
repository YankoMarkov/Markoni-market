package markoni.web.interceptors;

import markoni.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class UserRegisterInterceptor extends HandlerInterceptorAdapter {
	
	private final UserService userService;
	
	@Autowired
	public UserRegisterInterceptor(UserService userService) {
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
			String confirmPassword = request.getParameter("confirmPassword");
			
			if (this.userService.userExist(username) || !password.equals(confirmPassword)) {
				response.sendRedirect("/user/register");
				return false;
			}
		}
		return true;
	}
}
