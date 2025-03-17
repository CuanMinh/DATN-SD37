package com.project.datn.model.request.banhang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietRequest {

    private Long id;

    private Integer soLuong;

    private BigDecimal gia;

    private Long idChiTietSanPham;

    private Long idHoaDon;
}
