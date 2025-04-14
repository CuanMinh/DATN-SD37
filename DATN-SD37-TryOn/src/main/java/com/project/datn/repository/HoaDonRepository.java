package com.project.datn.repository;

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
        @Query(value = "SELECT * FROM hoa_don WHERE trang_thai = 1", nativeQuery = true)
        List<HoaDon> findByHoaDonTrangThai();

        @Query(value = "SELECT * FROM hoa_don WHERE ma_hoa_don = :maHoaDon", nativeQuery = true)
        Optional<HoaDon> findByHoaDonMaHoaDon(@Param("maHoaDon") String maHoaDon);

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

        @Query("SELECT h FROM HoaDon h ORDER BY h.ngayTaoHoaDon DESC")
        List<HoaDon> findAllCustom(Pageable pageable);

        @Query("SELECT h FROM HoaDon h " +
                "WHERE (:maHoaDon IS NULL OR h.maHoaDon LIKE %:maHoaDon%) " +
                "AND (:startDate IS NULL OR h.ngayTaoHoaDon >= :startDate) " +
                "AND (:endDate IS NULL OR h.ngayTaoHoaDon <= :endDate) " +
                "AND (:trangThai IS NULL OR h.trangThai = :trangThai) " +
                "ORDER BY h.ngayTaoHoaDon DESC")
        Page<HoaDon> searchHoaDon(
                @Param("maHoaDon") String maHoaDon,
                @Param("startDate") Date startDate,
                @Param("endDate") Date endDate,
                @Param("trangThai") Integer trangThai,
                Pageable pageable);

        Page<HoaDon> findAllByTrangThaiAndTaiKhoan_Id(Integer trangThai, Long taiKhoanId, Pageable pageable);
        Page<HoaDon> findByTaiKhoan_Id(Long id, Pageable pageable);
        @Query(value = "SELECT DATEPART(YEAR, CONVERT(DATE, ngay_tao_hoa_don)) AS nam, " +
                "DATEPART(MONTH, CONVERT(DATE, ngay_tao_hoa_don)) AS thang, " +
                "DATEPART(DAY, CONVERT(DATE, ngay_tao_hoa_don)) AS ngay, " +
                "SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "WHERE trang_thai = 5 " +
                "AND (:startDate IS NULL OR ngay_tao_hoa_don >= :startDate) " +
                "AND (:endDate IS NULL OR ngay_tao_hoa_don <= :endDate) " +
                "AND (:ngay IS NULL OR DATEPART(DAY, ngay_tao_hoa_don) = :ngay) " +
                "AND (:thang IS NULL OR DATEPART(MONTH, ngay_tao_hoa_don) = :thang) " +
                "AND (:nam IS NULL OR DATEPART(YEAR, ngay_tao_hoa_don) = :nam) " +
                "GROUP BY DATEPART(YEAR, CONVERT(DATE, ngay_tao_hoa_don)), " +
                "DATEPART(MONTH, CONVERT(DATE, ngay_tao_hoa_don)), " +
                "DATEPART(DAY, CONVERT(DATE, ngay_tao_hoa_don)) " +
                "ORDER BY nam, thang, ngay", nativeQuery = true)
        List<Object[]> getDoanhThu(@Param("startDate") Date startDate,
                                   @Param("endDate") Date endDate,
                                   @Param("ngay") Integer ngay,
                                   @Param("thang") Integer thang,
                                   @Param("nam") Integer nam);
        @Query(value = "SELECT * FROM hoa_don " +
                "WHERE (:ngay IS NULL OR DAY(ngay_tao_hoa_don) = :ngay) " +
                "AND (:thang IS NULL OR MONTH(ngay_tao_hoa_don) = :thang) " +
                "AND (:nam IS NULL OR YEAR(ngay_tao_hoa_don) = :nam)", nativeQuery = true)

        List<HoaDon> findByNgayThangNam(@Param("ngay") Integer ngay,
                                        @Param("thang") Integer thang,
                                        @Param("nam") Integer nam);


}

