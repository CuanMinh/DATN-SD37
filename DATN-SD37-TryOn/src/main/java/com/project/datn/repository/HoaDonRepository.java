package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository  extends JpaRepository<HoaDon, Long> {
    @Query(value = "SELECT * FROM hoa_don", nativeQuery = true)
    List<HoaDon> findAllHoaDon();

    Optional<HoaDon> findHoaDonById(Long id);
    Page<HoaDon> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE HoaDon h SET h.trangThai = 0 WHERE h.id = :id")
    void cancelHoaDon(Long id);

    @Query(value = "INSERT INTO HoaDon (ma_hoa_don, ngay_tao_hoa_don, tong_tien,trang_thai_don_hang,trang_thai_thanh_toan, trang_thai) VALUES (:maHoaDon, :ngayTaoHoaDon, :tongTien,:trangThaiDonHang,:trangThaiThanhToan, :trangThai)", nativeQuery = true)
    void insertHoaDon(@Param("maHoaDon") String maHoaDon,
                      @Param("ngayTaoHoaDon") Date ngayTaoHoaDon,
                      @Param("tongTien") BigDecimal tongTien,
                      @Param("trangThaiDonHang") Integer trangThaiDonHang,
                      @Param("trangThaiThanhToan") Integer trangThaiThanhToan,
                      @Param("trangThai") Integer trangThai);

}
