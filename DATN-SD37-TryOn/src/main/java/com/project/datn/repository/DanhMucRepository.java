package com.project.datn.repository;

import com.project.datn.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {

    List<DanhMuc> findAllByTrangThai(Integer trangThai);
}
