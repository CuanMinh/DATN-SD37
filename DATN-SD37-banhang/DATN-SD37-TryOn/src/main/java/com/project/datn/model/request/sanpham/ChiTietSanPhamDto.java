package com.project.datn.model.request.sanpham;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietSanPhamDto {

    private Long id;
    private Double trongLuong;
    private BigDecimal gia;
    private BigDecimal giamGia;
    private String moTa;
    private Integer soLuong;
    private Long sanPhamId;
    private Long mauSacId;
    private Long kichCoId;
}
