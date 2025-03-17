package com.project.datn.model.request.banhang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiRequest {

    private Long id;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 3, max = 50, message = "Tên phải từ 3 đến 50 ký tự")
    private String ten;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải đúng 10 chữ số và bắt đầu bằng số 0")
    private String soDienThoai;

    @NotBlank(message = "Thành phố không được để trống.")
    private String thanhPho;

    @NotBlank(message = "Quận/Huyện không được để trống.")
    private String quanHuyen;

    @NotBlank(message = "Phường/Xã không được để trống.")
    private String phuongXa;

    private Date ngayTao;

    private Date ngayCapNhap;

    private Integer trangThai;

    private Long taiKhoan;
}
