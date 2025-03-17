package com.project.datn.DTO;

import java.math.BigDecimal;
import java.util.List;

public class HoaDonDTO {
    private String maHoaDon;
    private BigDecimal tongTien = BigDecimal.ZERO;
    private Integer trangThaiDonHang; // Thêm trường này
    private List<ChiTietHoaDonDTO> sanPhams;

    // Getters và Setters
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public Integer getTrangThaiDonHang() {
        return trangThaiDonHang;
    }

    public void setTrangThaiDonHang(Integer trangThaiDonHang) {
        this.trangThaiDonHang = trangThaiDonHang;
    }

    public List<ChiTietHoaDonDTO> getSanPhams() {
        return sanPhams;
    }

    public void setSanPhams(List<ChiTietHoaDonDTO> sanPhams) {
        this.sanPhams = sanPhams;
    }
}