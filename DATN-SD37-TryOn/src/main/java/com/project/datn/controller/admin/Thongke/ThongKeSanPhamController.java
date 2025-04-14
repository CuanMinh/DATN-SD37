package com.project.datn.controller.admin.Thongke;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("")
    public String thongKeSanPham(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) throws JsonProcessingException {

        if (startDate == null || endDate == null) {
            Calendar cal = Calendar.getInstance();
            endDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            startDate = cal.getTime();
        }

        List<SanPhamThongKeDTO> danhSachThongKe = sanPhamService.getThongKeSanPham(startDate, endDate);

        String danhSachThongKeJson = objectMapper.writeValueAsString(danhSachThongKe);

        model.addAttribute("danhSachThongKe", danhSachThongKe);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(danhSachThongKe);
        System.out.println("DEBUG JSON: " + json);
        model.addAttribute("danhSachThongKeJson", danhSachThongKeJson);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        System.out.println("JSON: " + danhSachThongKeJson);


        return "admin/thongke/sanpham/thongkesanpham";
    }
}



