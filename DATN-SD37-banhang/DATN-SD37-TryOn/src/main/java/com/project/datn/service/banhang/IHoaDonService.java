package com.project.datn.service.banhang;

import com.project.datn.entity.HoaDon;
import com.project.datn.model.request.banhang.HoaDonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IHoaDonService {
    HoaDon addHoaDon(HoaDon hoaDon);
    void addGiamgia( Long idHoaDon, Long idMaGiamGia);
    void thanhToan(Long idHoaDon, HoaDonRequest hoaDonRequest);
    Page<HoaDon> getHoaDonByTrangThai(Integer trangThai, Pageable pageable);
    Page<HoaDon> getHoaDonByAccount(Pageable pageable);
    void cancelOrder(String maHoaDon);
}
