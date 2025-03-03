package com.project.datn.service.impl;

import com.project.datn.DTO.ThongKeDoanhThuDTO;
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
