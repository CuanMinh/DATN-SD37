package com.project.datn.service.impl;

import com.project.datn.entity.TaiKhoan;
import com.project.datn.entity.VaiTro;
import com.project.datn.entity.VaiTroTaiKhoan;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.repository.VaiTroRepository;
import com.project.datn.repository.VaiTroTaiKhoanRepository;
import com.project.datn.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private VaiTroTaiKhoanRepository vaiTroTaiKhoanRepository;

    @Override
    public List<TaiKhoan> findAll() {
        return taiKhoanRepository.findAll();
    }

    @Override
    public TaiKhoan findById(Long id) {
        return taiKhoanRepository.findById(id).orElse(null);
    }

    @Override
    public TaiKhoan save(TaiKhoan taiKhoan) {
        taiKhoan.setNgayTao(new Date());
        taiKhoan.setTrangThai(1);
        return taiKhoanRepository.save(taiKhoan);
    }

    @Override
    public void deleteById(Long id) {
        taiKhoanRepository.deleteById(id);
    }

    @Override
    public TaiKhoan blockAccount(Long id) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElse(null);
        if (taiKhoan != null) {
            taiKhoan.setTrangThai(0);
            return taiKhoanRepository.save(taiKhoan);
        }
        return null;
    }

    @Override
    public TaiKhoan openAccount(Long id) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElse(null);
        if (taiKhoan != null) {
            taiKhoan.setTrangThai(1);
            return taiKhoanRepository.save(taiKhoan);
        }
        return null;
    }

    @Override
    public List<VaiTro> getVaiTroByTaiKhoan(Long id) {
        return vaiTroTaiKhoanRepository.findByTaiKhoanId(id)
                .stream()
                .map(vtk -> vtk.getVaiTro())
                .collect(Collectors.toList());
    }
    @Override
    public List<TaiKhoan> getTaiKhoanByVaiTro(Long vaiTroId) {
        return taiKhoanRepository.findByVaiTroId(vaiTroId);
    }
    @Override
    public TaiKhoan addTaiKhoan(String ten, String email, String soDienThoai, List<Long> vaiTroIds) {
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTen(ten);
        taiKhoan.setEmail(email);
        taiKhoan.setSoDienThoai(soDienThoai);
        taiKhoan.setTrangThai(1); // Trạng thái mặc định: 1 (Hoạt động)
        taiKhoan.setNgayTao(new Date()); // Lấy thời gian thực

        // Lưu tài khoản trước
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        // Gán vai trò cho tài khoản
        for (Long vaiTroId : vaiTroIds) {
            VaiTro vaiTro = vaiTroRepository.findById(vaiTroId).orElse(null);
            if (vaiTro != null) {
                VaiTroTaiKhoan vttk = new VaiTroTaiKhoan();
                vttk.setTaiKhoan(taiKhoan);
                vttk.setVaiTro(vaiTro);
                vaiTroTaiKhoanRepository.save(vttk);
            }
        }

        return taiKhoan;
    }
    @Override
    public TaiKhoan updateTaiKhoan(Long id, String ten, String email, String soDienThoai, List<Long> vaiTroIds) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        taiKhoan.setTen(ten);
        taiKhoan.setEmail(email);
        taiKhoan.setSoDienThoai(soDienThoai);
        taiKhoan.setNgayCapNhap(new Date());

        // Cập nhật tài khoản trước
        taiKhoan = taiKhoanRepository.save(taiKhoan);

        // Xóa vai trò cũ
        vaiTroTaiKhoanRepository.deleteByTaiKhoanId(id);

        // Gán vai trò mới
        for (Long vaiTroId : vaiTroIds) {
            VaiTro vaiTro = vaiTroRepository.findById(vaiTroId).orElse(null);
            if (vaiTro != null) {
                VaiTroTaiKhoan vttk = new VaiTroTaiKhoan();
                vttk.setTaiKhoan(taiKhoan);
                vttk.setVaiTro(vaiTro);
                vaiTroTaiKhoanRepository.save(vttk);
            }
        }

        return taiKhoan;
    }
    @Override
    public boolean existsByTen(String ten) {
        return taiKhoanRepository.existsByTen(ten);
    }

    @Override
    public boolean existsByEmail(String email) {
        return taiKhoanRepository.existsByEmail(email);
    }

    @Override
    public boolean existsBySoDienThoai(String soDienThoai) {
        return taiKhoanRepository.existsBySoDienThoai(soDienThoai);
    }
    @Override
    public boolean isDuplicate(Long id, String ten, String email, String soDienThoai) {
        return taiKhoanRepository.existsByTenAndIdNot(ten, id) ||
                taiKhoanRepository.existsByEmailAndIdNot(email, id) ||
                taiKhoanRepository.existsBySoDienThoaiAndIdNot(soDienThoai, id);
    }

}
