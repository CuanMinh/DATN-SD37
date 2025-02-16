package com.project.datn.controller.admin.DTO;

import java.math.BigDecimal;

public class ThongKeDoanhThuDTO {
    private String ngay;
    private Integer thang;
    private Integer nam;
    private BigDecimal tongTien;

    public ThongKeDoanhThuDTO(String ngay, BigDecimal tongTien) {
        this.ngay = ngay;
        this.tongTien = tongTien;
    }

    public ThongKeDoanhThuDTO(Integer thang, Integer nam, BigDecimal tongTien) {
        this.thang = thang;
        this.nam = nam;
        this.tongTien = tongTien;
    }

    public ThongKeDoanhThuDTO(Integer nam, BigDecimal tongTien) {
        this.nam = nam;
        this.tongTien = tongTien;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public Integer getThang() {
        return thang;
    }

    public void setThang(Integer thang) {
        this.thang = thang;
    }

    public Integer getNam() {
        return nam;
    }

    public void setNam(Integer nam) {
        this.nam = nam;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }
}
