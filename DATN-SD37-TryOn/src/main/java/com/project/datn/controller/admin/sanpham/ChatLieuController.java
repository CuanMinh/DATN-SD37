package com.project.datn.controller.admin.sanpham;

import com.project.datn.entity.ChatLieu;
import com.project.datn.service.IChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/chatlieu")
public class ChatLieuController {

    @Autowired
    private IChatLieuService iChatLieuService;

    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("listChatLieu", this.iChatLieuService.findAll());
        return new ModelAndView("/admin/thuoctinhsanpham/chatlieu/chatlieu", model);
    }

    @GetMapping("/them-moi")
    public ModelAndView createIndex(ModelMap model) {
        model.addAttribute("chatLieu", new ChatLieu());
        return new ModelAndView("/admin/thuoctinhsanpham/chatlieu/themmoi", model);
    }

    @PostMapping("/them-moi")
    public ModelAndView create(ModelMap model, @Valid @ModelAttribute("chatLieu") ChatLieu chatLieu, BindingResult result) {
        this.iChatLieuService.save(chatLieu);
        return new ModelAndView("redirect:/admin/chatlieu");
    }

    @GetMapping("/cap-nhat/{id}")
    public ModelAndView updateIndex(ModelMap model, @PathVariable Long id) {
        Optional<ChatLieu> chatLieu = iChatLieuService.findById(id);
        chatLieu.ifPresent(cl -> model.addAttribute("chatLieu", cl));
        return new ModelAndView("/admin/thuoctinhsanpham/chatlieu/capnhat", model);
    }

    @PostMapping("/cap-nhat/{id}")
    public ModelAndView update(ModelMap model, @PathVariable Long id, @Valid @ModelAttribute("chatLieu") ChatLieu chatLieuUpdate, BindingResult result) {
        Optional<ChatLieu> chatLieu = iChatLieuService.findById(id);
        if (chatLieu.isPresent()) {
            chatLieu.get().setTen(chatLieuUpdate.getTen());
            chatLieu.get().setMoTa(chatLieuUpdate.getMoTa());
            this.iChatLieuService.save(chatLieu.get());
        }
        return new ModelAndView("redirect:/admin/chatlieu");
    }

    @GetMapping("/xoa/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable Long id) {
        Optional<ChatLieu> chatLieu = iChatLieuService.findById(id);
        if (chatLieu.isPresent()) {
            chatLieu.get().setTrangThai(0);
            this.iChatLieuService.save(chatLieu.get());
        }
        return new ModelAndView("redirect:/admin/chatlieu");
    }
}
