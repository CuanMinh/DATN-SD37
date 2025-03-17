package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.ChiTietSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietSanPhamRepository extends JpaRepository<ChiTietSanPham, Long> {
    Optional<ChiTietSanPham> findById(Long id);
    List<ChiTietSanPham> findBySanPham_Id(Long sanPhamId);

    ChiTietSanPham findByMauSac_IdAndKichCo_IdAndSanPham_Id(Long mauSacId, Long kichCoId, Long sanPhamId);

    @Query("SELECT c FROM ChiTietSanPham c WHERE c.trangThai = :trangThai")
    Page<ChiTietSanPham> findByTrangThai(@Param("trangThai") Integer trangThai, Pageable pageable);

    // Tìm theo trangThai và ten hoặc ma của SanPham
    @Query("SELECT c FROM ChiTietSanPham c WHERE c.trangThai = 1 AND (LOWER(c.sanPham.ten) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(c.sanPham.ma) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<ChiTietSanPham> findByTrangThaiAndSanPhamTenOrMaContainingIgnoreCase(@Param("query") String query);

    @Query("SELECT c FROM ChiTietSanPham c WHERE c.sanPham.id = :sanPhamId ORDER BY c.id ASC")
    List<ChiTietSanPham> findBySanPhamIds(@Param("sanPhamId") Long sanPhamId);

    List<ChiTietSanPham> findAllByTrangThai(Integer trangThai);

    @Query(value = "SELECT ctsp FROM ChiTietSanPham ctsp WHERE ctsp.sanPham.id = :sanPhamId AND ctsp.trangThai = :trangThai")
    List<ChiTietSanPham> findAllBySanPhamIdAndTrangThai(Long sanPhamId, Integer trangThai);

}
