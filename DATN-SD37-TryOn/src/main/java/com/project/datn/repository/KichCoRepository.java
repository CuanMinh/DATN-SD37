package com.project.datn.repository;

import com.project.datn.entity.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KichCoRepository extends JpaRepository<KichCo, Long> {

    List<KichCo> findAllByTrangThai(Integer trangThai);
}
