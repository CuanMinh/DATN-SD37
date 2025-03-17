package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.VaiTroTaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaiTroTaiKhoanRepository  extends JpaRepository<VaiTroTaiKhoan, Long> {
    @Query(value = "SELECT TOP 1 * FROM vai_tro_tai_khoan WHERE tai_khoan_id = ?", nativeQuery = true)
    Optional<VaiTroTaiKhoan> findByTaiKhoan_Id(Long taiKhoanId);

}
