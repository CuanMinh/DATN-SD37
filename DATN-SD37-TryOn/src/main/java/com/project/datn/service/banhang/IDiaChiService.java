package com.project.datn.service.banhang;

import com.project.datn.model.request.banhang.DiaChiRequest;
import com.project.datn.model.request.banhang.HoaDonRequest;

public interface IDiaChiService {
    void addiaChi(Long idHoaDon, DiaChiRequest diaChiRequest);
    void addKhachHangCu(Long idHoaDon, Long idDiaChi);
}
