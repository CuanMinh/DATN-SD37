package com.project.datn.service.impl;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.service.IChiTietSanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChiTietSanPhamServiceImpl implements IChiTietSanPhamService {

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Override
    public List<ChiTietSanPham> saveAll(List<ChiTietSanPham> listChiTietSanPham) {
        return this.chiTietSanPhamRepository.saveAll(listChiTietSanPham);
    }

    @Override
    public List<ChiTietSanPham> findAllBySanPhamId(Long sanPhamId) {
        return this.chiTietSanPhamRepository.findAllBySanPhamIdAndTrangThai(sanPhamId, 1);
    }

    @Override
    public Optional<ChiTietSanPham> findById(Long id) {
        return this.chiTietSanPhamRepository.findById(id);
    }
}
