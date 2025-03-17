package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.GioHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Long> {
    Page<GioHang> findByTaiKhoan_Id(Long taiKhoanId, Pageable pageable);
    List<GioHang> findByTaiKhoan_Id(Long taiKhoanId);
    void deleteByTaiKhoan_Id(Long idTaiKhoan);
    Optional<GioHang> findByTaiKhoan_IdAndChiTietSanPham_Id(Long taiKhoanId, Long chiTietSanPhamId);
}
