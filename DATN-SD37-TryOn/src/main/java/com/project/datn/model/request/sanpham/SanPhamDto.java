package com.project.datn.model.request.sanpham;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.DanhMuc;
import com.project.datn.entity.ThuongHieu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamDto {

    private Long id;
    private String ten;
    private String moTa;
    private Integer trangThai;
    private Long danhMucId;
    private Long chatLieuId;
    private Long thuongHieuId;
    private MultipartFile anh;
    private String anhUrl;
    private String listSanPham;
    private Long anhId;
}
