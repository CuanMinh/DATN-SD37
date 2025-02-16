package com.project.datn.service;

import com.project.datn.controller.admin.DTO.ThongKeDoanhThuDTO;
import com.project.datn.model.ThongKeDoanhThu;
import com.project.datn.repository.ThongKeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThongKeService {
    @Autowired
    private ThongKeRepository thongKeRepository;

    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNgay() {
        return thongKeRepository.thongKeDoanhThuTheoNgay();
    }

    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoThang() {
        return thongKeRepository.thongKeDoanhThuTheoThang();
    }

    public List<ThongKeDoanhThuDTO> thongKeDoanhThuTheoNam() {
        return thongKeRepository.thongKeDoanhThuTheoNam();
    }
}
