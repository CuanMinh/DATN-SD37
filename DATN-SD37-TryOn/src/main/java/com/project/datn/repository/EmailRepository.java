package com.project.datn.repository;

import com.project.datn.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    @Query("SELECT e FROM Email e WHERE e.ma = :ma")
    Optional<Email> findByMa(@Param("ma") Integer ma);

}
