package com.project.datn.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamThongKeDTO {
    private Integer sanPhamId;   // Nếu SQL trả về Integer
    private String tenSanPham;
    private Long soLuongBan;
    private BigDecimal tongDoanhThu;
}


