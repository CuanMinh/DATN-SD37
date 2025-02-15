package com.project.datn.service.banhang;

import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.model.request.banhang.HoaDonChiTietRequest;

public interface IHoaDonChiTietService {
    HoaDonChiTiet addHoaDonChiTiet(HoaDonChiTietRequest hoaDonChiTietRequest);
    void updateHoaDonChiTiet(Long idHoaDon, Long idChiTietSanPham, int soLuongThem);
    void deleteHoaDonChiTiet(Long idHoaDonChiTiet);
}
