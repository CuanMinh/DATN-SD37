package com.project.datn.repository;

import com.project.datn.entity.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Long> {

    List<ChiTietSanPham> findAllByTrangThai(Integer trangThai);

    @Query(value = "SELECT ctsp FROM ChiTietSanPham ctsp WHERE ctsp.sanPham.id = :sanPhamId AND ctsp.trangThai = :trangThai")
    List<ChiTietSanPham> findAllBySanPhamIdAndTrangThai(Long sanPhamId, Integer trangThai);
}
