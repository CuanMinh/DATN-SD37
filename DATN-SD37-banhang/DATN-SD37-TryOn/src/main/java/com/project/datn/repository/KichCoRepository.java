package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KichCoRepository  extends JpaRepository<KichCo, Long> {
}
