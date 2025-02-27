package com.project.datn.model.request.sanpham;

import com.project.datn.entity.KichCo;
import com.project.datn.entity.MauSac;
import com.project.datn.entity.SanPham;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

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
