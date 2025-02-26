package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.MaGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaGiamGiaRepository  extends JpaRepository<MaGiamGia, Long> {
    @Query("SELECT m FROM MaGiamGia m WHERE m.trangThai = :trangThai AND LOWER(m.ten) LIKE LOWER(CONCAT('%', :ten, '%'))")
    List<MaGiamGia> findByTrangThaiAndTenContainingIgnoreCase(@Param("trangThai") Integer trangThai, @Param("ten") String ten);

    @Query("SELECT m FROM MaGiamGia m WHERE m.trangThai = :trangThai")
    Page<MaGiamGia> findByTrangThaiAllMaGiamGia(@Param("trangThai") Integer trangThai, Pageable pageable);
}
