package com.project.datn.service;

import com.project.datn.entity.ThuongHieu;

import java.util.List;
import java.util.Optional;

public interface IThuongHieuService {

    List<ThuongHieu> findAll();

    void save(ThuongHieu thuongHieu);

    Optional<ThuongHieu> findById(Long id);
}
