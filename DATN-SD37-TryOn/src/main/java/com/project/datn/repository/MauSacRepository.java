package com.project.datn.repository;

import com.project.datn.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Long> {

    List<MauSac> findAllByTrangThai(Integer trangThai);
}
