package markoni.config;

import markoni.web.interceptors.*;
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
	private final PackageCreateInterceptor packageCreateInterceptor;
	private final PendingInterceptor pendingInterceptor;
	
	@Autowired
	public WebMvcConfiguration(HomeInterceptor homeInterceptor,
	                           UserRegisterInterceptor userRegisterInterceptor,
	                           UserLoginInterceptor userLoginInterceptor,
	                           CategoryCreateInterceptor categoryCreateInterceptor,
	                           ProductCreateInterceptor productCreateInterceptor,
	                           PackageCreateInterceptor packageCreateInterceptor,
	                           PendingInterceptor pendingInterceptor) {
		this.homeInterceptor = homeInterceptor;
		this.userRegisterInterceptor = userRegisterInterceptor;
		this.userLoginInterceptor = userLoginInterceptor;
		this.categoryCreateInterceptor = categoryCreateInterceptor;
		this.productCreateInterceptor = productCreateInterceptor;
		this.packageCreateInterceptor = packageCreateInterceptor;
		this.pendingInterceptor = pendingInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.homeInterceptor).addPathPatterns("/home");
		registry.addInterceptor(this.userRegisterInterceptor).addPathPatterns("/user/register");
		registry.addInterceptor(this.userLoginInterceptor).addPathPatterns("/user/login");
		registry.addInterceptor(this.categoryCreateInterceptor).addPathPatterns("/category/create");
		registry.addInterceptor(this.productCreateInterceptor).addPathPatterns("/product/create");
		registry.addInterceptor(this.packageCreateInterceptor).addPathPatterns("/package/create");
		registry.addInterceptor(this.pendingInterceptor).addPathPatterns("/package/pending");
	}
}
