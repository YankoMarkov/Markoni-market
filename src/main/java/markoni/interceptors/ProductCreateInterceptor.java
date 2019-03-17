package markoni.interceptors;

import markoni.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class ProductCreateInterceptor extends HandlerInterceptorAdapter {
	
	private final ProductService productService;
	
	@Autowired
	public ProductCreateInterceptor(ProductService productService) {
		this.productService = productService;
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
			
			if (this.productService.productExist(name)) {
				response.sendRedirect("/product/create");
				return false;
			}
		}
		return true;
	}
}
