package com.project.datn.service.sanpham.impl;

import com.project.datn.entity.SanPham;
import com.project.datn.repository.SanPhamRepository;
import com.project.datn.service.sanpham.ISanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamServiceImpl implements ISanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Override
    public SanPham save(SanPham sanPham) {
        return this.sanPhamRepository.save(sanPham);
    }

    @Override
    public List<SanPham> findAll() {
        return this.sanPhamRepository.findAllByTrangThai(1);
    }

    @Override
    public Optional<SanPham> findById(Long id) {
        return this.sanPhamRepository.findById(id);
    }
}
