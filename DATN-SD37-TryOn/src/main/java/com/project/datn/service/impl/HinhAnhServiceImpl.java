package com.project.datn.service.impl;

import com.project.datn.entity.HinhAnh;
import com.project.datn.repository.HinhAnhRepository;
import com.project.datn.service.IHinhAnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HinhAnhServiceImpl implements IHinhAnhService {

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    @Override
    public HinhAnh save(HinhAnh hinhAnh) {
        return this.hinhAnhRepository.save(hinhAnh);
    }

    @Override
    public List<HinhAnh> findBySanPhamId(Long sanPhamId) {
        return this.hinhAnhRepository.findAllBySanPhamId(sanPhamId);
    }
}
