package com.project.datn.service;

import com.project.datn.entity.TaiKhoan;
import com.project.datn.entity.VaiTro;

import java.util.List;

public interface TaiKhoanService {
    List<TaiKhoan> findAll();
    TaiKhoan save(TaiKhoan taiKhoan);
    TaiKhoan findById(Long id);
    void deleteById(Long id);
    TaiKhoan blockAccount(Long id);
    TaiKhoan openAccount(Long id);
    List<VaiTro> getVaiTroByTaiKhoan(Long id);
    List<TaiKhoan> getTaiKhoanByVaiTro(Long vaiTroId);
    TaiKhoan addTaiKhoan(String ten, String email, String soDienThoai, List<Long> vaiTroIds);
    public TaiKhoan updateTaiKhoan(Long id, String ten, String email, String soDienThoai, List<Long> vaiTroIds);
    boolean existsByTen(String ten);
    boolean existsByEmail(String email);
    boolean existsBySoDienThoai(String soDienThoai);
    boolean isDuplicate(Long id, String ten, String email, String soDienThoai);

}
