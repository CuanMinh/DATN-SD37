package com.project.datn.service.taikhoan.impl;

import com.project.datn.entity.VaiTro;
import com.project.datn.repository.VaiTroRepository;
import com.project.datn.service.taikhoan.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaiTroServiceImpl implements VaiTroService {
    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Override
    public List<VaiTro> findAll() {
        return vaiTroRepository.findAll();
    }
}
