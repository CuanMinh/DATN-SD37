package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository  extends JpaRepository<HoaDon, Long> {
    @Query(value = "SELECT TOP 5 kh.TenKhachHang, COUNT(hd.Id) AS soLuongDon, SUM(hd.tong_tien) AS tongChiTieu " +
            "FROM hoa_don hd " +
            "JOIN khach_hang kh ON hd.tai_khoan_id = kh.Id " +
            "GROUP BY kh.TenKhachHang " +
            "ORDER BY tongChiTieu DESC", nativeQuery = true)
    List<Object[]> findTopCustomers();
}
