package com.project.datn.controller.admin.sanpham;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/chatlieu")
public class ChatLieuController {
    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuChatLieu", "menu");
        return new ModelAndView("/admin/thuoctinhsanpham/chatlieu/chatlieu", model);
    }
}
