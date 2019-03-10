package markoni.web.controllers;

import markoni.models.bindings.CategoryCreateBindingModel;
import markoni.models.services.CategoryServiceModel;
import markoni.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public ModelAndView create(HttpSession session) {
        if (!session.getAttribute("role").equals("ADMIN")) {
            return this.redirect("/");
        }
        return this.view("category");
    }

    @PostMapping("/create")
    public ModelAndView createConfirm(@ModelAttribute @Valid CategoryCreateBindingModel categoryCreate,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {

        CategoryServiceModel categoryServiceModel = this.modelMapper.map(categoryCreate, CategoryServiceModel.class);
        if (bindingResult.hasErrors() ||
                this.categoryService.categoryExist(categoryCreate.getName()) ||
                this.categoryService.saveCategory(categoryServiceModel) == null) {

            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
            return this.redirect("/category/create");
        }
        return this.redirect("/home");
    }
}
