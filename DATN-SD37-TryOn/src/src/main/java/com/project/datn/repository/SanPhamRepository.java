package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository  extends JpaRepository<SanPham, Long> {

    List<SanPham> findAllByTrangThai(Integer trangThai);
}
