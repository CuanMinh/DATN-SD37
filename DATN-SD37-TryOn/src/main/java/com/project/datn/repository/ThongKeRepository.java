package com.project.datn.repository;
import com.project.datn.DTO.ThongKeDoanhThuDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Repository
@Transactional(readOnly = true)
public class ThongKeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNgay() {
        String sql = "SELECT CAST(ngay_tao_hoa_don AS DATE) AS ngay, SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY CAST(ngay_tao_hoa_don AS DATE) " +
                "ORDER BY ngay";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(row -> new ThongKeDoanhThuDTO(
                row[0] != null ? row[0].toString() : null,
                row[1] instanceof Number ? BigDecimal.valueOf(((Number) row[1]).doubleValue()) : BigDecimal.ZERO
        )).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoThang() {
        String sql = "SELECT DATEPART(YEAR, ngay_tao_hoa_don) AS nam, " +
                "       DATEPART(MONTH, ngay_tao_hoa_don) AS thang, " +
                "       SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY DATEPART(YEAR, ngay_tao_hoa_don), DATEPART(MONTH, ngay_tao_hoa_don) " +
                "ORDER BY nam, thang";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(row -> new ThongKeDoanhThuDTO(
                row[0] instanceof Number ? ((Number) row[0]).intValue() : null, // Năm
                row[1] instanceof Number ? ((Number) row[1]).intValue() : null, // Tháng
                row[2] instanceof Number ? BigDecimal.valueOf(((Number) row[2]).doubleValue()) : BigDecimal.ZERO // Doanh thu
        )).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNam() {
        String sql = "SELECT DATEPART(YEAR, ngay_tao_hoa_don) AS nam, SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY DATEPART(YEAR, ngay_tao_hoa_don) " +
                "ORDER BY nam";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(row -> new ThongKeDoanhThuDTO(
                row[0] instanceof Number ? ((Number) row[0]).intValue() : null, // Năm
                row[1] instanceof Number ? BigDecimal.valueOf(((Number) row[1]).doubleValue()) : BigDecimal.ZERO // Doanh thu
        )).collect(Collectors.toList());
    }
}
