package com.project.datn.controller.admin.banhang;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/banhang")
public class BanHangController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuB", "menu");
        return new ModelAndView("/admin/banhang/banhang", model);
    }
}
