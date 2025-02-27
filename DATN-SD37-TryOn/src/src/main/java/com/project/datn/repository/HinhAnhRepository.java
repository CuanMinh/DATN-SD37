package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhRepository  extends JpaRepository<HinhAnh, Long> {

    List<HinhAnh> findAllBySanPhamId(Long sanPhamId);
}
