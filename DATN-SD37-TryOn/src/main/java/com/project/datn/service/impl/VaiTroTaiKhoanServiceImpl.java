package com.project.datn.service.impl;

import com.project.datn.entity.TaiKhoan;
import com.project.datn.entity.VaiTro;
import com.project.datn.entity.VaiTroTaiKhoan;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.repository.VaiTroRepository;
import com.project.datn.repository.VaiTroTaiKhoanRepository;
import com.project.datn.service.VaiTroTaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaiTroTaiKhoanServiceImpl implements VaiTroTaiKhoanService {
    @Autowired
    private VaiTroTaiKhoanRepository vaiTroTaiKhoanRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Override
    public void assignRoleToUser(Long taiKhoanId, Long vaiTroId) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(taiKhoanId).orElse(null);
        VaiTro vaiTro = vaiTroRepository.findById(vaiTroId).orElse(null);

        if (taiKhoan != null && vaiTro != null) {
            VaiTroTaiKhoan vaiTroTaiKhoan = new VaiTroTaiKhoan();
            vaiTroTaiKhoan.setTaiKhoan(taiKhoan);
            vaiTroTaiKhoan.setVaiTro(vaiTro);
            vaiTroTaiKhoanRepository.save(vaiTroTaiKhoan);
        }
    }
}