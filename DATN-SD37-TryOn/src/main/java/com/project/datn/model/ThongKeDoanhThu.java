package com.project.datn.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThongKeDoanhThu {
    private String ngay;
    private Integer thang;
    private Integer nam;
    private Double doanhThu;

    public ThongKeDoanhThu(String ngay, Double doanhThu) {
        this.ngay = ngay;
        this.doanhThu = doanhThu;
    }

    public ThongKeDoanhThu(Integer nam, Integer thang, Double doanhThu) {
        this.nam = nam;
        this.thang = thang;
        this.doanhThu = doanhThu;
    }

    public ThongKeDoanhThu(Integer nam, Double doanhThu) {
        this.nam = nam;
        this.doanhThu = doanhThu;
    }
}