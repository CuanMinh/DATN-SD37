package com.project.datn.repository;

import com.project.datn.controller.admin.DTO.ThongKeDoanhThuDTO;

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
        String sql = "SELECT CONVERT(DATE, ngay_tao_hoa_don) AS ngay, SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY CONVERT(DATE, ngay_tao_hoa_don) " +
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
        String sql = "SELECT YEAR(ngay_tao_hoa_don) AS nam, MONTH(ngay_tao_hoa_don) AS thang, SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY YEAR(ngay_tao_hoa_don), MONTH(ngay_tao_hoa_don) " +
                "ORDER BY nam, thang";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(row -> new ThongKeDoanhThuDTO(
                row[1] instanceof Number ? ((Number) row[1]).intValue() : null,
                row[0] instanceof Number ? ((Number) row[0]).intValue() : null,
                row[2] instanceof Number ? BigDecimal.valueOf(((Number) row[2]).doubleValue()) : BigDecimal.ZERO
        )).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNam() {
        String sql = "SELECT YEAR(ngay_tao_hoa_don) AS nam, SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY YEAR(ngay_tao_hoa_don) " +
                "ORDER BY nam";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(row -> new ThongKeDoanhThuDTO(
                row[0] instanceof Number ? ((Number) row[0]).intValue() : null,
                row[1] instanceof Number ? BigDecimal.valueOf(((Number) row[1]).doubleValue()) : BigDecimal.ZERO
        )).collect(Collectors.toList());
    }
}
