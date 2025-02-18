package com.project.datn.controller.admin.Thongke;

import com.project.datn.controller.admin.DTO.ThongKeDoanhThuDTO;
import com.project.datn.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
@Controller
@RequestMapping("/admin/thongkedoanhthu")
public class ThongKeDoanhThuController {

    @Autowired
    private ThongKeService thongKeService;

    /**
     * Endpoint này chỉ trả về trang HTML (Thymeleaf).
     * Khi người dùng truy cập đường dẫn /admin/thongkedoanhthu/page
     * nó sẽ render ra file "thongkedoanhthu.html".
     */
    @GetMapping("")
    public String thongKeDoanhThuPage() {
        // Không trả về danh sách JSON ở đây, chỉ trả về tên view Thymeleaf
        return "admin/thongke/doanhthu/thongkedoanhthu";
    }

    /**
     * Endpoint này trả về dữ liệu JSON cho AJAX/fetch.
     * Ví dụ: /admin/thongkedoanhthu/data?type=ngay
     */
    @GetMapping("/data")
    @ResponseBody
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuData(
            @RequestParam(required = false, defaultValue = "ngay") String type) {
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
        return result; // Trả về dưới dạng JSON
    }
}


