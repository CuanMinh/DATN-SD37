package com.project.datn.model.request.banhang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiRequest {

    private Long id;

    private String ten;

    private String soDienThoai;

    private String thanhPho;

    private String quanHuyen;

    private String phuongXa;

    private Date ngayTao;

    private Date ngayCapNhap;

    private Integer trangThai;

    private Long taiKhoan;
}
