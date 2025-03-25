package com.project.datn.service.impl;

import com.project.datn.DTO.ThongKeDoanhThuDTO;
import com.project.datn.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    public List<ThongKeDoanhThuDTO> layDoanhThu(Date startDate, Date endDate, Integer ngay, Integer thang, Integer nam) {
        List<Object[]> rawData = hoaDonRepository.getDoanhThu(startDate, endDate, ngay, thang, nam);

        List<ThongKeDoanhThuDTO> dtos = new ArrayList<>();
        for (Object[] row : rawData) {
            dtos.add(new ThongKeDoanhThuDTO(
                    (Integer) row[0], // Năm
                    (Integer) row[1], // Tháng
                    (Integer) row[2], // Ngày
                    (BigDecimal) row[3] // Doanh thu
            ));
        }
        return dtos;
    }
}

