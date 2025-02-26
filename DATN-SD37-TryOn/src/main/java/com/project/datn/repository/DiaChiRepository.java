package com.project.datn.repository;

import com.project.datn.entity.DiaChi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Long> {
    @Query("SELECT d FROM DiaChi d WHERE d.trangThai = 1 AND (LOWER(d.ten) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(d.soDienThoai) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<DiaChi> findByTenContainingIgnoreCaseOrSoDienThoaiContainingIgnoreCase(@Param("query") String query);

    @Query("SELECT d FROM DiaChi d WHERE d.trangThai = :trangThai")
    Page<DiaChi> findByTrangThaiAllDiaChi(@Param("trangThai") Integer trangThai, Pageable pageable);
}
