package com.project.datn.controller.admin.hoadon;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/hoadon")
public class HoaDonController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuH", "menu");
        return new ModelAndView("/admin/hoadon/hoadon", model);
    }
}
