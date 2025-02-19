package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.VaiTro;
import com.project.datn.entity.VaiTroTaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface VaiTroTaiKhoanRepository  extends JpaRepository<VaiTroTaiKhoan, Long> {
    @Query(value = "SELECT TOP 1 * FROM vai_tro_tai_khoan WHERE tai_khoan_id = ?", nativeQuery = true)
    Optional<VaiTroTaiKhoan> findByTaiKhoan_Id(Long taiKhoanId);

    @Modifying
    @Transactional
    @Query("DELETE FROM VaiTroTaiKhoan v WHERE v.taiKhoan.id = :taiKhoanId")
    void deleteByTaiKhoanId(@Param("taiKhoanId") Long taiKhoanId);
    List<VaiTroTaiKhoan> findByTaiKhoanId(Long taiKhoanId);
    @Query("SELECT v.vaiTro FROM VaiTroTaiKhoan v WHERE v.taiKhoan.id = :taiKhoanId")
    List<VaiTro> findVaiTroByTaiKhoanId(@Param("taiKhoanId") Long taiKhoanId);

}
