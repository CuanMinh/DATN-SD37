package com.project.datn.controller.admin.Thongke;

import com.project.datn.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThongKeController {
    @Autowired
    private ThongKeService thongKeService;

    @GetMapping("/thong-ke")
    public String thongKeDoanhThu(@RequestParam(value = "loai", defaultValue = "ngay") String loai, Model model) {
        if (loai.equals("ngay")) {
            model.addAttribute("thongKe", thongKeService.getDoanhThuTheoNgay());
        } else if (loai.equals("thang")) {
            model.addAttribute("thongKe", thongKeService.getDoanhThuTheoThang());
        } else if (loai.equals("nam")) {
            model.addAttribute("thongKe", thongKeService.getDoanhThuTheoNam());
        }
        model.addAttribute("loai", loai);
        return "thong_ke"; // Trả về trang JSP
    }
}
