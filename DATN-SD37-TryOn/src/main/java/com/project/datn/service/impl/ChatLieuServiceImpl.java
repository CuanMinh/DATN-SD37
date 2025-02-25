package com.project.datn.service.impl;

import com.project.datn.entity.ChatLieu;
import com.project.datn.repository.ChatLieuRepository;
import com.project.datn.service.IChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatLieuServiceImpl implements IChatLieuService {

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Override
    public List<ChatLieu> findAll() {
        return this.chatLieuRepository.findAllByTrangThai(1);
    }

    @Override
    public void save(ChatLieu chatLieu) {
        this.chatLieuRepository.save(chatLieu);
    }

    @Override
    public Optional<ChatLieu> findById(Long id) {
        return this.chatLieuRepository.findById(id);
    }
}
