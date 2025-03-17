package com.project.datn.controller.user;

import com.project.datn.entity.HoaDon;
import com.project.datn.service.banhang.IHoaDonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("customer/trangthai")
public class TheoDoiDonHang {
    private final IHoaDonService billService;

    public TheoDoiDonHang(IHoaDonService billService) {
        this.billService = billService;
    }

    @GetMapping("")
    public String viewCartStatus(Model model,
                                 @RequestParam(required = false) Integer trangThai,
                                 @PageableDefault(size = 5, sort = "ngayTaoHoaDon", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<HoaDon> billPage;

        if (trangThai == null ) {
            billPage = billService.getHoaDonByAccount(pageable);
        } else {
            billPage = billService.getHoaDonByTrangThai(trangThai, pageable);
        }

        model.addAttribute("bills", billPage);
        model.addAttribute("trangThai", trangThai); // Giữ trạng thái đã chọn để active tab
        return "user/cart-status";
    }
    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") String maHoaDon) {
        try {
            billService.cancelOrder(maHoaDon);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}

