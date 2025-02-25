package com.project.datn.service.impl;

import com.project.datn.entity.MauSac;
import com.project.datn.repository.MauSacRepository;
import com.project.datn.service.IMauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MauSacServiceImpl implements IMauSacService {

    @Autowired
    private MauSacRepository mauSacRepository;

    @Override
    public List<MauSac> findAll() {
        return this.mauSacRepository.findAllByTrangThai(1);
    }

    @Override
    public void save(MauSac mauSac) {
        this.mauSacRepository.save(mauSac);
    }

    @Override
    public Optional<MauSac> findById(Long id) {
        return this.mauSacRepository.findById(id);
    }
}
