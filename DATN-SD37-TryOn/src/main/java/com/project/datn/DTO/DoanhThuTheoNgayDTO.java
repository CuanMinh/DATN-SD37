package com.project.datn.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoanhThuTheoNgayDTO {
    private int nam;
    private int thang;
    private int ngay;
    private BigDecimal doanhThu;

    // Constructors, getters, setters
}