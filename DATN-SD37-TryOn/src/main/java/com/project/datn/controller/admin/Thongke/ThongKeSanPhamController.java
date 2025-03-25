package com.project.datn.controller.admin.Thongke;

import com.project.datn.DTO.SanPhamThongKeDTO;
import com.project.datn.service.impl.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/thongkesanpham")
public class ThongKeSanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("")
    public String thongKeSanPham(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {

        // Nếu không có ngày bắt đầu và ngày kết thúc, mặc định lấy 30 ngày gần nhất
        if (startDate == null || endDate == null) {
            Calendar cal = Calendar.getInstance();
            endDate = cal.getTime(); // Ngày hiện tại
            cal.add(Calendar.DAY_OF_MONTH, -30);
            startDate = cal.getTime(); // 30 ngày trước
        }

        List<SanPhamThongKeDTO> danhSachThongKe = sanPhamService.getThongKeSanPham(startDate, endDate);
        System.out.println("Danh sách sản phẩm bán chạy: " + danhSachThongKe);

        model.addAttribute("danhSachThongKe", danhSachThongKe);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "/admin/thongke/sanpham/thongkesanpham";
    }
}



