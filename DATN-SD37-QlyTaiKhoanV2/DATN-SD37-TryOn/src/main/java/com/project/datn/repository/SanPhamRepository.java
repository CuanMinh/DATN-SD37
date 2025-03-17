package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository  extends JpaRepository<SanPham, Long> {

    @Query("SELECT sp FROM SanPham sp " +
            "WHERE (:search IS NULL OR LOWER(sp.ten) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:brand IS NULL OR sp.thuongHieu.id = :brand) " +
            "AND (:category IS NULL OR sp.danhMuc.id = :category) " +
            "AND sp.trangThai = 1")
    List<SanPham> filterProducts(
            @Param("search") String search,
            @Param("brand") Long brand,
            @Param("category") Long category);

    Optional<SanPham> findById(Long id);


}
