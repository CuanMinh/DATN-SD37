package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository  extends JpaRepository<SanPham, Long> {

    @Query("SELECT s FROM SanPham s " +
            "JOIN ChiTietSanPham ct ON s.id = ct.sanPham.id " +
            "WHERE (:keyword IS NULL OR s.ten LIKE %:keyword%) " +
            "AND (:categoryId IS NULL OR s.danhMuc.id = :categoryId) " +
            "AND (:brandId IS NULL OR s.thuongHieu.id = :brandId)")
    List<SanPham> searchProducts(@Param("keyword") String keyword,
                                 @Param("categoryId") Long categoryId,
                                 @Param("brandId") Long brandId);

    Optional<SanPham> findById(Long id);
}
