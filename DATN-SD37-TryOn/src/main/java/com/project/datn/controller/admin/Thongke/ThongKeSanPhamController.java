package com.project.datn.controller.admin.Thongke;

import com.project.datn.DTO.SanPhamThongKeDTO;
import com.project.datn.service.impl.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin/thongkesanpham")
public class ThongKeSanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping("")
    public String thongKeSanPham(ModelMap model) {
        List<SanPhamThongKeDTO> danhSachThongKe = sanPhamService.getThongKeSanPham();
        System.out.println("Danh sách sản phẩm bán chạy: " + danhSachThongKe);
        model.addAttribute("sanPhamList", danhSachThongKe);
        return "/admin/thongke/sanpham/thongkesanpham";
    }
}

