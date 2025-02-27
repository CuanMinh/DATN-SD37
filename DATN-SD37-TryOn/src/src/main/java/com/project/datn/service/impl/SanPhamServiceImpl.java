package com.project.datn.service.impl;

import com.project.datn.entity.SanPham;
import com.project.datn.repository.SanPhamRepository;
import com.project.datn.service.ISanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
