package com.project.datn.service.sanpham;

import com.project.datn.entity.HinhAnh;

import java.util.List;

public interface IHinhAnhService {

    HinhAnh save(HinhAnh hinhAnh);

    List<HinhAnh> findBySanPhamId(Long sanPhamId);
}
