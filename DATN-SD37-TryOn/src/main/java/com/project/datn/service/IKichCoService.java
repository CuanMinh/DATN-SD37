package com.project.datn.service;

import com.project.datn.entity.KichCo;

import java.util.List;
import java.util.Optional;

public interface IKichCoService {

    List<KichCo> findAll();

    void save(KichCo kichCo);

    Optional<KichCo> findById(Long id);
}
