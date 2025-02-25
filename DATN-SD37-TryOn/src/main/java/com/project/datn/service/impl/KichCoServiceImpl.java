package com.project.datn.service.impl;

import com.project.datn.entity.KichCo;
import com.project.datn.repository.KichCoRepository;
import com.project.datn.service.IKichCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KichCoServiceImpl implements IKichCoService {

    @Autowired
    private KichCoRepository kichCoRepository;

    @Override
    public List<KichCo> findAll() {
        return this.kichCoRepository.findAllByTrangThai(1);
    }

    @Override
    public void save(KichCo kichCo) {
        this.kichCoRepository.save(kichCo);
    }

    @Override
    public Optional<KichCo> findById(Long id) {
        return this.kichCoRepository.findById(id);
    }
}
