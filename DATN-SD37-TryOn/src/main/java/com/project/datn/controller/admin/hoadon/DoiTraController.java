package com.project.datn.controller.admin.hoadon;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/doitra")
public class DoiTraController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuD", "menu");
        return new ModelAndView("/admin/doitra/doitra", model);
    }
}
