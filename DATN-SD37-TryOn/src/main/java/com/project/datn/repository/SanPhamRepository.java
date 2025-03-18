package com.project.datn.repository;

import com.project.datn.DTO.SanPhamThongKeDTO;
import com.project.datn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {

    @Query(value = "SELECT sp.id AS sanPhamId, sp.ten AS tenSanPham,\n" +
            "           SUM(hdct.so_luong) AS soLuongBan,\n" +
            "           SUM(hdct.so_luong * hdct.gia) AS tongDoanhThu\n" +
            "    FROM dbo.hoa_don_chi_tiet hdct\n" +
            "    JOIN dbo.chi_tiet_san_pham ctsp ON hdct.chi_tiet_san_pham_id = ctsp.id\n" +
            "    JOIN dbo.san_pham sp ON ctsp.san_pham_id = sp.id\n" +
            "    GROUP BY sp.id, sp.ten\n" +
            "    ORDER BY SUM(hdct.so_luong) DESC;",
            nativeQuery = true)
    List<SanPhamThongKeDTO> thongKeSanPham();

}


