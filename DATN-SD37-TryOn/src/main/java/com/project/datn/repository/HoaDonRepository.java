package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @Query(value = "SELECT DATEPART(YEAR, ngay_tao_hoa_don) AS nam, " +
            "DATEPART(MONTH, ngay_tao_hoa_don) AS thang, " +
            "DATEPART(DAY, ngay_tao_hoa_don) AS ngay, " +
            "SUM(tong_tien) AS doanhThu " +
            "FROM hoa_don " +
            "WHERE (:ngay IS NULL OR DATEPART(DAY, ngay_tao_hoa_don) = :ngay) " +
            "AND (:thang IS NULL OR DATEPART(MONTH, ngay_tao_hoa_don) = :thang) " +
            "AND (:nam IS NULL OR DATEPART(YEAR, ngay_tao_hoa_don) = :nam) " +
            "GROUP BY DATEPART(YEAR, ngay_tao_hoa_don), DATEPART(MONTH, ngay_tao_hoa_don), DATEPART(DAY, ngay_tao_hoa_don) " +
            "ORDER BY nam, thang, ngay", nativeQuery = true)
    List<Object[]> getDoanhThuTheoNgay(@Param("ngay") Integer ngay,
                                       @Param("thang") Integer thang,
                                       @Param("nam") Integer nam);
}
