package com.project.datn.controller.admin.Thongke;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/thongkedoanhthu")
public class ThongKeDoanhThuController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuDT", "menu");
        return new ModelAndView("/admin/thongke/doanhthu/thongkedoanhthu", model);
    }
}
