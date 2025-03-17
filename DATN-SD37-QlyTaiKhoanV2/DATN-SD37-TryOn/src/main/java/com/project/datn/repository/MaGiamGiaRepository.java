package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  MaGiamGiaRepository  extends JpaRepository<MaGiamGia, Long> {

    @Query("SELECT m FROM MaGiamGia m WHERE LOWER(m.ten) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MaGiamGia> searchByTenGiamGia(@Param("keyword") String keyword);
}
