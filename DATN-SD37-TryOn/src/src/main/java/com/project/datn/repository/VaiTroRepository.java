package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroRepository  extends JpaRepository<VaiTro, Long> {
}
