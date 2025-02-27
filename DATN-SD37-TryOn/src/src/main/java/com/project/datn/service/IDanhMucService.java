package com.project.datn.service;

import com.project.datn.entity.DanhMuc;

import java.util.List;
import java.util.Optional;

public interface IDanhMucService {

    List<DanhMuc> findAll();

    void save(DanhMuc danhMuc);

    Optional<DanhMuc> findById(Long id);
}
