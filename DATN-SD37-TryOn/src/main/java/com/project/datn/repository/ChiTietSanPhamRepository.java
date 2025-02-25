package com.project.datn.repository;

import com.project.datn.entity.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Long> {

    List<ChiTietSanPham> findAllByTrangThai(Integer trangThai);
}
