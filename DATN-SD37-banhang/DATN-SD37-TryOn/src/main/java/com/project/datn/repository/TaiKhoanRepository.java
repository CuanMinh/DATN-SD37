package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository  extends JpaRepository<TaiKhoan, Long> {
    TaiKhoan findByTen(String ten);

    @Query(value = "SELECT TOP 1 * FROM tai_khoan WHERE email = ? AND trang_thai = 1", nativeQuery = true)
    Optional<TaiKhoan> FindByEmail(String email);

    @Query(value = "SELECT TOP 1 * FROM tai_khoan WHERE ten = ? AND trang_thai = 1", nativeQuery = true)
    Optional<TaiKhoan> FindByTen(String ten);


}
