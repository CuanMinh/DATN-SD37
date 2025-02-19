package com.project.datn.controller.admin.Thongke;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/thongkesanpham")
public class ThongKeSanPhamController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuSP", "menu");
        return new ModelAndView("/admin/thongke/sanpham/thongkesanpham", model);
    }
}
