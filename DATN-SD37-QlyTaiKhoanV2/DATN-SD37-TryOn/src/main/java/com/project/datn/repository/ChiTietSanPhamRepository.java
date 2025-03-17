package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Long> {

    Optional<ChiTietSanPham> findBySanPhamId(Long sanPhamId);

    @Query("SELECT c FROM ChiTietSanPham c WHERE c.sanPham.id = :sanPhamId ORDER BY c.id ASC")
    List<ChiTietSanPham> findBySanPhamIds(@Param("sanPhamId") Long sanPhamId);

}
