package com.project.datn.controller.admin.Thongke;

import com.project.datn.DTO.DoanhThuTheoNgayDTO;
import com.project.datn.service.impl.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/thongkedoanhthu")
public class ThongKeDoanhThuController {
@Autowired
private HoaDonService hoaDonService;

    @GetMapping("")
    public String thongKeDoanhThu(@RequestParam(value = "ngay", required = false) Integer ngay,
                                  @RequestParam(value = "thang", required = false) Integer thang,
                                  @RequestParam(value = "nam", required = false) Integer nam,
                                  Model model) {
        List<DoanhThuTheoNgayDTO> doanhThuList = hoaDonService.layDoanhThu(ngay, thang, nam);
        model.addAttribute("doanhThuList", doanhThuList);
        model.addAttribute("ngay", ngay);
        model.addAttribute("thang", thang);
        model.addAttribute("nam", nam);
        return "admin/thongke/doanhthu/thongkedoanhthu"; // file thymeleaf nằm ở thư mục templates/admin/
    }

}