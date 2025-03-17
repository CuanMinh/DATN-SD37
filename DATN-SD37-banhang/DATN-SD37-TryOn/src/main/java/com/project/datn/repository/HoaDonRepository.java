package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository  extends JpaRepository<HoaDon, Long> {
    @Query(value = "SELECT * FROM hoa_don WHERE trang_thai = 1", nativeQuery = true)
    List<HoaDon> findByHoaDonTrangThai();

    @Query(value = "SELECT * FROM hoa_don WHERE ma_hoa_don = :maHoaDon", nativeQuery = true)
    Optional<HoaDon> findByHoaDonMaHoaDon(@Param("maHoaDon") String maHoaDon);

}
