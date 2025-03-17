package com.project.datn.model.request.banhang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonRequest {
    private Long id;

    private String maHoaDon;

    private Date ngayTaoHoaDon;

    private Date ngayDuKienGiaoHang;

    private Date ngayGiaoHang;

    private Date ngayThanhToan;

    private BigDecimal tongTien;

    private BigDecimal tongTienCuoiCung;

    private Integer trangThaiDonHang;

    private Integer trangThaiThanhToan;

    private Date ngayCapNhap;

    private BigDecimal phiVanChuyen;

    private String maVanChuyen;

    private Integer trangThaiTraHang;

    private String ghiChu;

    private Integer trangThai;

    private Long idTaiKhoan;

    private Long idMaGiamGia;

    private Long idDiaChi;

    private Long idPhuongThucThanhToan;
}
