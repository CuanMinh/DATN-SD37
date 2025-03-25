package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

        @Query(value = "SELECT DATEPART(YEAR, CONVERT(DATE, ngay_tao_hoa_don)) AS nam, " +
                "DATEPART(MONTH, CONVERT(DATE, ngay_tao_hoa_don)) AS thang, " +
                "DATEPART(DAY, CONVERT(DATE, ngay_tao_hoa_don)) AS ngay, " +
                "SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "WHERE trang_thai_don_hang = 5 " +
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
}

