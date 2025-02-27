package com.project.datn.service.impl;

import com.project.datn.entity.ThuongHieu;
import com.project.datn.repository.ThuongHieuRepository;
import com.project.datn.service.IThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThuongHieuServiceImpl implements IThuongHieuService {

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Override
    public List<ThuongHieu> findAll() {
        return this.thuongHieuRepository.findAllByTrangThai(1);
    }

    @Override
    public void save(ThuongHieu thuongHieu) {
        this.thuongHieuRepository.save(thuongHieu);
    }

    @Override
    public Optional<ThuongHieu> findById(Long id) {
        return this.thuongHieuRepository.findById(id);
    }
}
