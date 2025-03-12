package com.project.datn.service;

import com.project.datn.entity.ChiTietSanPham;

import java.util.List;
import java.util.Optional;

public interface IChiTietSanPhamService {

    List<ChiTietSanPham> saveAll(List<ChiTietSanPham> listChiTietSanPham);

    List<ChiTietSanPham> findAllBySanPhamId(Long sanPhamId);

    Optional<ChiTietSanPham> findById(Long id);
}
