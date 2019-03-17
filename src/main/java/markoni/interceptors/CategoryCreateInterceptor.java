package markoni.interceptors;

import markoni.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class CategoryCreateInterceptor extends HandlerInterceptorAdapter {
	
	private final CategoryService categoryService;
	
	@Autowired
	public CategoryCreateInterceptor(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		String role = (String) session.getAttribute("role");
		
		if (!role.equals("ADMIN")) {
			response.sendRedirect("/home");
			return false;
		}
		if (request.getMethod().equalsIgnoreCase("post")) {
			String name = request.getParameter("name");
			
			if (this.categoryService.categoryExist(name)) {
				response.sendRedirect("/category/create");
				return false;
			}
		}
		return true;
	}
}
