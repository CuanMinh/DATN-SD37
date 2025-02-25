package com.project.datn.service;

import com.project.datn.entity.ChatLieu;

import java.util.List;
import java.util.Optional;

public interface IChatLieuService {

    List<ChatLieu> findAll();

    void save(ChatLieu chatLieu);

    Optional<ChatLieu> findById(Long id);
}
