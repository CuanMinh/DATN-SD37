package com.project.datn.repository;

import com.project.datn.DTO.SanPhamThongKeDTO;
import com.project.datn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {

    @Query(value = "SELECT sp.id AS sanPhamId, sp.ten AS tenSanPham, " +
            "SUM(hdct.so_luong) AS soLuongBan, SUM(hdct.so_luong * hdct.gia) AS tongDoanhThu " +
            "FROM hoa_don_chi_tiet hdct " +
            "JOIN chi_tiet_san_pham ctsp ON hdct.chi_tiet_san_pham_id = ctsp.id " +
            "JOIN san_pham sp ON ctsp.san_pham_id = sp.id " +
            "JOIN hoa_don hd ON hdct.hoa_don_id = hd.id " +
            "WHERE hd.ngay_tao_hoa_don BETWEEN :startDate AND :endDate " +
            "GROUP BY sp.id, sp.ten " +
            "ORDER BY SUM(hdct.so_luong) DESC",
            nativeQuery = true)
    List<Object[]> thongKeSanPham(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}







