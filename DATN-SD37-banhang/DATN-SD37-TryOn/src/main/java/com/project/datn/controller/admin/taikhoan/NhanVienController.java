package com.project.datn.controller.admin.taikhoan;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/nhanvien")
public class NhanVienController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuN", "menu");
        return new ModelAndView("/admin/taikhoan/nhanvien/nhanvien", model);
    }
}
