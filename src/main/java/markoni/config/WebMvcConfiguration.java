package markoni.config;

import markoni.interceptors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	private final HomeInterceptor homeInterceptor;
	private final UserRegisterInterceptor userRegisterInterceptor;
	private final UserLoginInterceptor userLoginInterceptor;
	private final CategoryCreateInterceptor categoryCreateInterceptor;
	private final ProductCreateInterceptor productCreateInterceptor;
	
	@Autowired
	public WebMvcConfiguration(HomeInterceptor homeInterceptor,
	                           UserRegisterInterceptor userRegisterInterceptor,
	                           UserLoginInterceptor userLoginInterceptor,
	                           CategoryCreateInterceptor categoryCreateInterceptor,
	                           ProductCreateInterceptor productCreateInterceptor) {
		this.homeInterceptor = homeInterceptor;
		this.userRegisterInterceptor = userRegisterInterceptor;
		this.userLoginInterceptor = userLoginInterceptor;
		this.categoryCreateInterceptor = categoryCreateInterceptor;
		this.productCreateInterceptor = productCreateInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.userRegisterInterceptor).addPathPatterns("/user/register");
		registry.addInterceptor(this.userLoginInterceptor).addPathPatterns("/user/login");
		registry.addInterceptor(this.categoryCreateInterceptor).addPathPatterns("/category/create");
		registry.addInterceptor(this.productCreateInterceptor).addPathPatterns("product/create");
		registry.addInterceptor(this.homeInterceptor).addPathPatterns("/home");
	}
}
