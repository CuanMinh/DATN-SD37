package com.project.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamTable {

    private Long id;
    private String ten;
    private String moTa;
    private String danhMuc;
    private String thuongHieu;
    private String chatLieu;
    private String kichCo;
    private String mauSac;
    private Integer trangThai;
    private String hinhAnh;
    private Integer soLuong;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;
}
