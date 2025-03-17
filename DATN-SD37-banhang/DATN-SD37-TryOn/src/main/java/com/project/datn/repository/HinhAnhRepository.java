package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhRepository  extends JpaRepository<HinhAnh, Long> {
    List<HinhAnh> findTop1BySanPham_Id(Long sanPhamId);
    List<HinhAnh> findBySanPham_Id(Long sanPhamId);

    @Query("SELECT h FROM HinhAnh h WHERE h.sanPham.id = :sanPhamId ORDER BY h.id ASC")
    List<HinhAnh> findBySanPhamId(@Param("sanPhamId") Long sanPhamId);


    List<HinhAnh> findAllBySanPhamId(Long sanPhamId);
}
