package com.project.datn.service.banhang.impl;

import com.project.datn.entity.DiaChi;
import com.project.datn.entity.HoaDon;
import com.project.datn.model.request.banhang.DiaChiRequest;
import com.project.datn.repository.DiaChiRepository;
import com.project.datn.repository.HoaDonRepository;
import com.project.datn.service.banhang.IDiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DiaChiServiceImpl implements IDiaChiService {
    @Autowired
    DiaChiRepository diaChiRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Override
    public void addiaChi(Long idHoaDon, DiaChiRequest diaChiRequest) {

        DiaChi diaChi = new DiaChi();
        diaChi.setTen(diaChiRequest.getTen());
        diaChi.setSoDienThoai(diaChiRequest.getSoDienThoai());
        diaChi.setThanhPho(diaChiRequest.getThanhPho());
        diaChi.setQuanHuyen(diaChiRequest.getQuanHuyen());
        diaChi.setPhuongXa(diaChiRequest.getPhuongXa());
        diaChi.setNgayTao(new Date());
        diaChi.setTrangThai(1);
        diaChiRepository.save(diaChi);
        // (Tuỳ chọn) Cập nhật hóa đơn với thông tin địa chỉ mới
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            hoaDon.setDiaChi(diaChi);
            hoaDon.setNgayCapNhap(new Date());
            hoaDonRepository.save(hoaDon);
        }
    }

    @Override
    public void addKhachHangCu(Long idHoaDon, Long idDiaChi) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        Optional<DiaChi> optionalDiaChi = diaChiRepository.findById(idDiaChi);
        if (optionalHoaDon.isPresent() && optionalDiaChi.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            hoaDon.setDiaChi(optionalDiaChi.get());
            hoaDonRepository.save(hoaDon);
        } else {
            throw new RuntimeException("Không tìm thấy hóa đơn hoặc thông tin khách hàng cũ");
        }
    }
}
