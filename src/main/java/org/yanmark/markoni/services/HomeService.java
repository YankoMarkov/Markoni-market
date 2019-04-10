package org.yanmark.markoni.services;

import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;

import java.util.List;

public interface HomeService {

    List<ProductServiceModel> takeProducts(String categoryId, ModelAndView modelAndView);
}
