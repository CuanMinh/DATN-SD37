package com.project.datn.controller.admin.TrangChu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/trangchu")
@Controller
public class TrangTruController {
    @RequestMapping("")
    public String trangchu() {
        return "admin/trangchu/trangchu";
    }

}
