package markoni.web.controllers;

import markoni.models.services.ProductServiceModel;
import markoni.models.views.CategoryAllViewModel;
import markoni.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return this.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home(HttpSession session,
                             ModelAndView modelAndView) {
    	
        if (session.getAttribute("username") != null) {
            List<CategoryAllViewModel> categoryAllViewModels = this.categoryService.getAllCategories().stream()
                    .map(category -> this.modelMapper.map(category, CategoryAllViewModel.class))
                    .collect(Collectors.toList());
            modelAndView.addObject("categories", categoryAllViewModels);
//            List<ProductServiceModel> productServiceModels = this.categoryService.getCategoryByName();
            return view("home", modelAndView);
        }
        return this.redirect("/");
    }
}
