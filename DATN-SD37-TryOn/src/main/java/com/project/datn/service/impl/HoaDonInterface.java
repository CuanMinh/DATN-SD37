package com.project.datn.service.impl;

import com.project.datn.entity.HoaDon;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface HoaDonInterface {
    List<HoaDon> getAllHoaDons();
    Page<HoaDon> getAllHoaDons(int page, int size, String sortBy, String direction);
    Optional<HoaDon> getHoaDonById(Long id);
    void saveHoaDon(HoaDon hoaDon);
    void cancelHoaDon(Long id);
    HoaDon createHoaDon(HoaDon hoaDon);
    void thanhToanHoaDon(Long id);
    void giaoHangHoaDon(Long id);
    String generateNewInvoiceCode();
}
