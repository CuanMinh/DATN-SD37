package com.project.datn.controller.admin.Thongke;

import com.project.datn.controller.admin.DTO.ThongKeDoanhThuDTO;
import com.project.datn.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Controller
@RequestMapping("/thong-ke")
public class ThongKeController {

    @Autowired
    private ThongKeService thongKeService;

    @GetMapping
    public String thongKePage(@RequestParam(required = false, defaultValue = "ngay") String type, Model model) {
        List<ThongKeDoanhThuDTO> result;
        switch (type) {
            case "thang":
                result = thongKeService.thongKeDoanhThuTheoThang();
                break;
            case "nam":
                result = thongKeService.thongKeDoanhThuTheoNam();
                break;
            default:
                result = thongKeService.thongKeDoanhThuTheoNgay();
        }
        model.addAttribute("thongKeList", result);
        return "thong_ke"; // Tên file JSP trong thư mục /WEB-INF/views/
    }
}


