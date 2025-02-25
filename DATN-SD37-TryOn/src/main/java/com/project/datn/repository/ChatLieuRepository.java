package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatLieuRepository extends JpaRepository<ChatLieu, Long> {

    List<ChatLieu> findAllByTrangThai(Integer trangThai);
}
