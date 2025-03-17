package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.MaGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Long> {
    Optional<MaGiamGia> findById(Long id);

    @Query("SELECT m FROM MaGiamGia m WHERE m.trangThai = :trangThai AND LOWER(m.ten) LIKE LOWER(CONCAT('%', :ten, '%'))")
    List<MaGiamGia> findByTrangThaiAndTenContainingIgnoreCase(@Param("trangThai") Integer trangThai, @Param("ten") String ten);

    @Query("SELECT m FROM MaGiamGia m WHERE m.trangThai = :trangThai AND LOWER(m.ten) LIKE LOWER(CONCAT('%', :ten, '%'))")
    Optional<MaGiamGia> findByTrangThaiAndTenOptional(@Param("trangThai") Integer trangThai, @Param("ten") String ten);

    @Query("SELECT m FROM MaGiamGia m WHERE m.trangThai = :trangThai")
    Page<MaGiamGia> findByTrangThaiAllMaGiamGia(@Param("trangThai") Integer trangThai, Pageable pageable);

    @Query("SELECT m FROM MaGiamGia m WHERE m.trangThai = :trangThai AND m.ten = :ten")
    Optional<MaGiamGia> findByTenAndTrangThai(@Param("ten") String ten, @Param("trangThai") Integer trangThai);

    @Query("SELECT m FROM MaGiamGia m WHERE LOWER(m.ten) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MaGiamGia> searchByTenGiamGia(@Param("keyword") String keyword);

    @Modifying
    @Query("UPDATE MaGiamGia m SET m.trangThai = 0 WHERE m.ngayKetThuc < CURRENT_DATE AND m.trangThai = 1")
    void capNhatTrangThaiHetHan();

    @Modifying
    @Query("UPDATE MaGiamGia m SET m.trangThai = 0 WHERE m.id = :id")
    void ngungHoatDong(@Param("id") Long id);
}
