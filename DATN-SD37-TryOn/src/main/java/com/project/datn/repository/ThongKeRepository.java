package com.project.datn.repository;

import com.project.datn.controller.admin.DTO.ThongKeDoanhThuDTO;
import com.project.datn.model.ThongKeDoanhThu;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository

public class ThongKeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNgay() {
        String sql = "SELECT CONVERT(DATE, ngay_tao_hoa_don) AS ngay, SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY CONVERT(DATE, ngay_tao_hoa_don) " +
                "ORDER BY ngay";
//        Query query = entityManager.createNativeQuery(sql, "ThongKeDoanhThuMapping");
//        return query.getResultList();
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(row -> new ThongKeDoanhThuDTO(
                row[0] != null ? row[0].toString() : null,   // ngay
                row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO // tongTien
        )).collect(Collectors.toList());

    }

    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoThang() {
        String sql = "SELECT YEAR(ngay_tao_hoa_don) AS nam, MONTH(ngay_tao_hoa_don) AS thang, SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY YEAR(ngay_tao_hoa_don), MONTH(ngay_tao_hoa_don) " +
                "ORDER BY nam, thang";
//        Query query = entityManager.createNativeQuery(sql, "ThongKeDoanhThuMapping");
//        return query.getResultList();
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(row -> new ThongKeDoanhThuDTO(
                row[1] != null ? Integer.valueOf(row[1].toString()) : null,  // thang ✅ (đúng)
                row[0] != null ? Integer.valueOf(row[0].toString()) : null,  // nam ✅ (đúng)
                row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO // tongTien
        )).collect(Collectors.toList());


    }

    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNam() {
        String sql = "SELECT YEAR(ngay_tao_hoa_don) AS nam, SUM(tong_tien) AS doanhThu " +
                "FROM hoa_don " +
                "GROUP BY YEAR(ngay_tao_hoa_don) " +
                "ORDER BY nam";
//        Query query = entityManager.createNativeQuery(sql, "ThongKeDoanhThuMapping");
//        return query.getResultList();
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = query.getResultList();

        return resultList.stream().map(row -> new ThongKeDoanhThuDTO(
                row[0] != null ? Integer.valueOf(row[0].toString()) : null,  // nam
                row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO // tongTien
        )).collect(Collectors.toList());

    }
}
