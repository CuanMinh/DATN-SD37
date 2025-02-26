package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonChiTietRepository  extends JpaRepository<HoaDonChiTiet, Long> {
    @Query(value = "SELECT bd FROM HoaDonChiTiet bd WHERE bd.hoaDon.id = :hoaDonId", nativeQuery = false)
    Page<HoaDonChiTiet> findByHoaDonId(@Param("hoaDonId") Long hoaDonId, Pageable pageable);

    @Query(value = "SELECT bd FROM HoaDonChiTiet bd WHERE bd.hoaDon.id = :hoaDonId", nativeQuery = false)
    List<HoaDonChiTiet> findByHoaDonAllId(@Param("hoaDonId") Long hoaDonId);

    @Query("SELECT h FROM HoaDonChiTiet h WHERE h.hoaDon.id = :idHoaDon AND h.chiTietSanPham.id = :idChiTietSanPham")
    Optional<HoaDonChiTiet> findByHoaDonIdAndChiTietSanPhamId(@Param("idHoaDon") Long idHoaDon, @Param("idChiTietSanPham") Long idChiTietSanPham);

    @Query("SELECT hct FROM HoaDonChiTiet hct WHERE hct.chiTietSanPham.id = :chiTietSanPhamId AND hct.hoaDon.trangThai = 1")
    List<HoaDonChiTiet> findHoaDonChiTietByChiTietSanPhamId(@Param("chiTietSanPhamId") Long chiTietSanPhamId);

}
