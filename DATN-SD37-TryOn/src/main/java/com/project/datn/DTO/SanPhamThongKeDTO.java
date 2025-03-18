package com.project.datn.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamThongKeDTO {
    private Long sanPhamId;
    private String tenSanPham;
    private Long soLuongBan;
    private Double tongDoanhThu;
}
