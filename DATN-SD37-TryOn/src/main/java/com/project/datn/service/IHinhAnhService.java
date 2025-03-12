package com.project.datn.service;

import com.project.datn.entity.HinhAnh;

import java.util.List;
import java.util.Optional;

public interface IHinhAnhService {

    HinhAnh save(HinhAnh hinhAnh);

    List<HinhAnh> findBySanPhamId(Long sanPhamId);
}
