package com.project.datn.service;

import com.project.datn.entity.SanPham;

import java.util.List;

public interface ISanPhamService {

    SanPham save(SanPham sanPham);

    List<SanPham> findAll();
}
