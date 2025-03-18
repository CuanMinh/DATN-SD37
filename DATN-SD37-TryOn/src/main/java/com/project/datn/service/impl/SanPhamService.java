package com.project.datn.service.impl;

import com.project.datn.DTO.SanPhamThongKeDTO;
import com.project.datn.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    public List<SanPhamThongKeDTO> getThongKeSanPham() {
        return sanPhamRepository.thongKeSanPham();
    }
}

