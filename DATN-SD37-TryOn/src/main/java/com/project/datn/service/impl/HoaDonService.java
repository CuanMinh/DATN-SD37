package com.project.datn.service.impl;

import com.project.datn.DTO.DoanhThuTheoNgayDTO;
import com.project.datn.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    public List<DoanhThuTheoNgayDTO> layDoanhThu(Integer ngay, Integer thang, Integer nam) {
        List<Object[]> rawData = hoaDonRepository.getDoanhThuTheoNgay(ngay, thang, nam);

        List<DoanhThuTheoNgayDTO> dtos = new ArrayList<>();
        for (Object[] row : rawData) {
            dtos.add(new DoanhThuTheoNgayDTO(
                    (Integer) row[0],
                    (Integer) row[1],
                    (Integer) row[2],
                    (BigDecimal) row[3]
            ));
        }
        return dtos;
    }
}
