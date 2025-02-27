package com.project.datn.controller.admin.sanpham;

import com.project.datn.entity.DanhMuc;
import com.project.datn.service.IDanhMucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/danhmuc")
public class DanhMucController {

    @Autowired
    private IDanhMucService iDanhMucService;

    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("listDanhMuc", this.iDanhMucService.findAll());
        return new ModelAndView("/admin/thuoctinhsanpham/danhmuc/danhmuc", model);
    }

    @GetMapping("/them-moi")
    public ModelAndView createIndex(ModelMap model) {
        model.addAttribute("danhMuc", new DanhMuc());
        return new ModelAndView("/admin/thuoctinhsanpham/danhmuc/themmoi", model);
    }

    @PostMapping("/them-moi")
    public ModelAndView create(ModelMap model, @Valid @ModelAttribute("danhMuc") DanhMuc danhMuc, BindingResult result) {
        this.iDanhMucService.save(danhMuc);
        return new ModelAndView("redirect:/admin/danhmuc");
    }

    @GetMapping("/cap-nhat/{id}")
    public ModelAndView updateIndex(ModelMap model, @PathVariable Long id) {
        Optional<DanhMuc> danhMuc = iDanhMucService.findById(id);
        danhMuc.ifPresent(cl -> model.addAttribute("danhMuc", danhMuc));
        return new ModelAndView("/admin/thuoctinhsanpham/danhmuc/capnhat", model);
    }

    @PostMapping("/cap-nhat/{id}")
    public ModelAndView update(ModelMap model, @PathVariable Long id, @Valid @ModelAttribute("danhMuc") DanhMuc danhMucUpdate, BindingResult result) {
        Optional<DanhMuc> danhMuc = iDanhMucService.findById(id);
        if (danhMuc.isPresent()) {
            danhMuc.get().setTen(danhMucUpdate.getTen());
            danhMuc.get().setMoTa(danhMucUpdate.getMoTa());
            this.iDanhMucService.save(danhMuc.get());
        }
        return new ModelAndView("redirect:/admin/danhmuc");
    }

    @GetMapping("/xoa/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable Long id) {
        Optional<DanhMuc> danhMuc = iDanhMucService.findById(id);
        if (danhMuc.isPresent()) {
            danhMuc.get().setTrangThai(0);
            this.iDanhMucService.save(danhMuc.get());
        }
        return new ModelAndView("redirect:/admin/danhmuc");
    }
}
