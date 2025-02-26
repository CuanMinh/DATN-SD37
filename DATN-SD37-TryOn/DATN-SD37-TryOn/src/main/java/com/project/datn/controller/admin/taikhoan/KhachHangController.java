package com.project.datn.controller.admin.taikhoan;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/khachhang")
public class KhachHangController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuK", "menu");
        return new ModelAndView("/admin/taikhoan/khachhang/khachhang", model);
    }
}
