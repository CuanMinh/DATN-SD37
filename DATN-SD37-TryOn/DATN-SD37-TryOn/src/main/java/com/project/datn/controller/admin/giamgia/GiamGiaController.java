package com.project.datn.controller.admin.giamgia;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/giamgia")
public class GiamGiaController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuG", "menu");
        return new ModelAndView("/admin/giamgia/giamgia", model);
    }
}
