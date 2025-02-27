package com.project.datn.service;

import com.project.datn.entity.ChiTietSanPham;

import java.util.List;

public interface IChiTietSanPhamService {

    List<ChiTietSanPham> saveAll(List<ChiTietSanPham> listChiTietSanPham);

    List<ChiTietSanPham> findAllBySanPhamId(Long sanPhamId);
}
