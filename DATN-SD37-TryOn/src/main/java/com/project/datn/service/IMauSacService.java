package com.project.datn.service;

import com.project.datn.entity.MauSac;

import java.util.List;
import java.util.Optional;

public interface IMauSacService {

    List<MauSac> findAll();

    void save(MauSac mauSac);

    Optional<MauSac> findById(Long id);
}
