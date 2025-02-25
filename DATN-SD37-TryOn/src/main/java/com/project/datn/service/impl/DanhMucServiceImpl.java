package com.project.datn.service.impl;

import com.project.datn.entity.DanhMuc;
import com.project.datn.repository.DanhMucRepository;
import com.project.datn.service.IDanhMucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DanhMucServiceImpl implements IDanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Override
    public List<DanhMuc> findAll() {
        return this.danhMucRepository.findAllByTrangThai(1);
    }

    @Override
    public void save(DanhMuc danhMuc) {
        this.danhMucRepository.save(danhMuc);
    }

    @Override
    public Optional<DanhMuc> findById(Long id) {
        return this.danhMucRepository.findById(id);
    }
}
