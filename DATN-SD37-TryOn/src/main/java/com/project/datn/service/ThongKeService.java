package com.project.datn.service;

import com.project.datn.model.ThongKeDoanhThu;
import com.project.datn.repository.ThongKeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThongKeService {
    @Autowired
    private ThongKeRepository thongKeRepository;

    public List<ThongKeDoanhThu> getDoanhThuTheoNgay() {
        return thongKeRepository.thongKeDoanhThuTheoNgay();
    }

    public List<ThongKeDoanhThu> getDoanhThuTheoThang() {
        return thongKeRepository.thongKeDoanhThuTheoThang();
    }

    public List<ThongKeDoanhThu> getDoanhThuTheoNam() {
        return thongKeRepository.thongKeDoanhThuTheoNam();
    }
}
