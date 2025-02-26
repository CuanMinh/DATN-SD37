package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Long> {
    @Query("SELECT c FROM ChiTietSanPham c WHERE c.trangThai = :trangThai")
    Page<ChiTietSanPham> findByTrangThai(@Param("trangThai") Integer trangThai, Pageable pageable);
}
