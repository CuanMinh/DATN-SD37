package com.project.datn.repository;

import com.project.datn.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {

    List<ThuongHieu> findAllByTrangThai(Integer trangThai);
}
