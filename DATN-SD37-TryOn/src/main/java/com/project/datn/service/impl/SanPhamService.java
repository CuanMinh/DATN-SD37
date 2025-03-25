package com.project.datn.service.impl;

import com.project.datn.DTO.SanPhamThongKeDTO;
import com.project.datn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    public List<SanPhamThongKeDTO> getThongKeSanPham(Date startDate, Date endDate) {
        List<Object[]> results = sanPhamRepository.thongKeSanPham(startDate, endDate);
        List<SanPhamThongKeDTO> thongKeList = new ArrayList<>();

        for (Object[] row : results) {
            Integer sanPhamId = row[0] != null ? ((Number) row[0]).intValue() : null;
            String tenSanPham = row[1] != null ? row[1].toString() : "";
            Long soLuongBan = row[2] != null ? ((Number) row[2]).longValue() : 0L;
            BigDecimal tongDoanhThu = row[3] != null ? new BigDecimal(row[3].toString()) : BigDecimal.ZERO;

            SanPhamThongKeDTO dto = new SanPhamThongKeDTO(sanPhamId, tenSanPham, soLuongBan, tongDoanhThu);
            thongKeList.add(dto);
        }

        return thongKeList;
    }
}




