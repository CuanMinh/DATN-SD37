package com.project.datn.controller.admin.sanpham;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/thuonghieu")
public class thuonghieucontroller {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuThuongHieu", "menu");
        return new ModelAndView("/admin/thuoctinhsanpham/thuonghieu/thuonghieu", model);
    }
}
