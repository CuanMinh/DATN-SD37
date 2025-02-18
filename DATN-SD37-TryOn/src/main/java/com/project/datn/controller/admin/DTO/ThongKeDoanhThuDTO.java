package com.project.datn.controller.admin.DTO;

import java.math.BigDecimal;
import java.util.Objects;

public class ThongKeDoanhThuDTO {
    private String ngay;
    private Integer thang;
    private Integer nam;
    private BigDecimal tongTien;

    // Constructor mặc định (cần thiết cho Spring)
    public ThongKeDoanhThuDTO() {
    }

    // Constructor đầy đủ (có thể sử dụng cho cả ngày, tháng, năm)
    public ThongKeDoanhThuDTO(String ngay, Integer thang, Integer nam, BigDecimal tongTien) {
        this.ngay = ngay;
        this.thang = thang;
        this.nam = nam;
        this.tongTien = tongTien;
    }

    // Constructor cho thống kê theo ngày
    public ThongKeDoanhThuDTO(String ngay, BigDecimal tongTien) {
        this.ngay = ngay;
        this.tongTien = tongTien;
    }

    // Constructor cho thống kê theo tháng
    public ThongKeDoanhThuDTO(Integer thang, Integer nam, BigDecimal tongTien) {
        this.thang = thang;
        this.nam = nam;
        this.tongTien = tongTien;
    }

    // Constructor cho thống kê theo năm
    public ThongKeDoanhThuDTO(Integer nam, BigDecimal tongTien) {
        this.nam = nam;
        this.tongTien = tongTien;
    }

    // Getters và Setters
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

    // Override toString() để dễ debug
    @Override
    public String toString() {
        return "ThongKeDoanhThuDTO{" +
                "ngay='" + ngay + '\'' +
                ", thang=" + thang +
                ", nam=" + nam +
                ", tongTien=" + tongTien +
                '}';
    }

    // Override equals() và hashCode() để đảm bảo so sánh đúng
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThongKeDoanhThuDTO that = (ThongKeDoanhThuDTO) o;
        return Objects.equals(ngay, that.ngay) &&
                Objects.equals(thang, that.thang) &&
                Objects.equals(nam, that.nam) &&
                Objects.equals(tongTien, that.tongTien);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ngay, thang, nam, tongTien);
    }
}
