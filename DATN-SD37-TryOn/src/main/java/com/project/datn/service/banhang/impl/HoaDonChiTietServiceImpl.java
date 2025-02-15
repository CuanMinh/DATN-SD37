package com.project.datn.service.banhang.impl;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.HoaDon;
import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.model.request.banhang.HoaDonChiTietRequest;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.HoaDonChiTietRepository;
import com.project.datn.service.banhang.IHoaDonChiTietService;
import com.project.datn.service.banhang.IHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HoaDonChiTietServiceImpl implements IHoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Override
    public HoaDonChiTiet addHoaDonChiTiet(HoaDonChiTietRequest hoaDonChiTietRequest) {
        Optional<ChiTietSanPham> chiTietSanPham = chiTietSanPhamRepository.findById(hoaDonChiTietRequest.getIdChiTietSanPham());
        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        hoaDonChiTiet.setSoLuong(hoaDonChiTietRequest.getSoLuong());
        hoaDonChiTiet.setGia(chiTietSanPham.get().getGia());
        hoaDonChiTiet.setHoaDon(HoaDon.builder().id(hoaDonChiTietRequest.getIdHoaDon()).build());
        hoaDonChiTiet.setChiTietSanPham(ChiTietSanPham.builder().id(hoaDonChiTietRequest.getIdChiTietSanPham()).build());
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    @Override
    public void updateHoaDonChiTiet(Long idHoaDon, Long idChiTietSanPham, int soLuongThem) {
        Optional<HoaDonChiTiet> optionalHoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonIdAndChiTietSanPhamId(idHoaDon, idChiTietSanPham);
        if (optionalHoaDonChiTiet.isPresent()) {
            HoaDonChiTiet hoaDonChiTiet = optionalHoaDonChiTiet.get();
            hoaDonChiTiet.setSoLuong(soLuongThem);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại trong giỏ hàng.");
        }
    }

    @Override
    public void deleteHoaDonChiTiet(Long idHoaDonChiTiet) {
        hoaDonChiTietRepository.deleteById(idHoaDonChiTiet);
    }
}
