package com.project.datn.service.sanpham;

import com.project.datn.entity.SanPham;

import java.util.List;
import java.util.Optional;

public interface ISanPhamService {

    SanPham save(SanPham sanPham);

    List<SanPham> findAll();

    Optional<SanPham> findById(Long id);
}
