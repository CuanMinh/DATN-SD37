package com.project.datn.model.request.taikhoan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SignupRequest {
    private Long id;

    private String ma;

    private String ten;

    private String password;

    private String anhDaiDien;

    private String email;

    private String soDienThoai;

    private Date ngayTao;

    private Date ngayCapNhap;

    private Integer trangThai;

    private Set<String> quyenHan;

    public Set<String> getRole() {
        return this.quyenHan;
    }

    public void setRole(Set<String> role) {
        this.quyenHan = role;
    }
}
