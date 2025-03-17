package com.project.datn.service.banhang;

import com.project.datn.entity.HoaDon;
import com.project.datn.model.request.banhang.HoaDonRequest;

public interface IHoaDonService {
    HoaDon addHoaDon(HoaDon hoaDon);
    void addGiamgia( Long idHoaDon, Long idMaGiamGia);
    void thanhToan(Long idHoaDon, HoaDonRequest hoaDonRequest);
}
