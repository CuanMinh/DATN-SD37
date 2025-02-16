package com.project.datn.repository;

import com.project.datn.model.ThongKeDoanhThu;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ThongKeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ThongKeDoanhThu> thongKeDoanhThuTheoNgay() {
        return entityManager.createQuery(
                        "SELECT new com.example.model.ThongKeDoanhThu(CAST(h.ngayThanhToan AS DATE), SUM(h.tongTienCuoiCung)) " +
                                "FROM HoaDon h WHERE h.trangThaiThanhToan = 1 " +
                                "GROUP BY CAST(h.ngayThanhToan AS DATE) " +
                                "ORDER BY h.ngayThanhToan ASC", ThongKeDoanhThu.class)
                .getResultList();
    }

    public List<ThongKeDoanhThu> thongKeDoanhThuTheoThang() {
        return entityManager.createQuery(
                        "SELECT new com.example.model.ThongKeDoanhThu(YEAR(h.ngayThanhToan), MONTH(h.ngayThanhToan), SUM(h.tongTienCuoiCung)) " +
                                "FROM HoaDon h WHERE h.trangThaiThanhToan = 1 " +
                                "GROUP BY YEAR(h.ngayThanhToan), MONTH(h.ngayThanhToan) " +
                                "ORDER BY YEAR(h.ngayThanhToan), MONTH(h.ngayThanhToan)", ThongKeDoanhThu.class)
                .getResultList();
    }

    public List<ThongKeDoanhThu> thongKeDoanhThuTheoNam() {
        return entityManager.createQuery(
                        "SELECT new com.example.model.ThongKeDoanhThu(YEAR(h.ngayThanhToan), SUM(h.tongTienCuoiCung)) " +
                                "FROM HoaDon h WHERE h.trangThaiThanhToan = 1 " +
                                "GROUP BY YEAR(h.ngayThanhToan) " +
                                "ORDER BY YEAR(h.ngayThanhToan)", ThongKeDoanhThu.class)
                .getResultList();
    }
}
