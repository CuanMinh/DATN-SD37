package com.project.datn.controller.admin.sanpham;

import com.project.datn.entity.ThuongHieu;
import com.project.datn.service.IThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/thuonghieu")
public class ThuongHieuController {

    @Autowired
    private IThuongHieuService iThuongHieuService;

    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("listThuongHieu", this.iThuongHieuService.findAll());
        return new ModelAndView("/admin/thuoctinhsanpham/thuonghieu/thuonghieu", model);
    }

    @GetMapping("/them-moi")
    public ModelAndView createIndex(ModelMap model) {
        model.addAttribute("thuongHieu", new ThuongHieu());
        return new ModelAndView("/admin/thuoctinhsanpham/thuonghieu/themmoi", model);
    }

    @PostMapping("/them-moi")
    public ModelAndView create(ModelMap model, @Valid @ModelAttribute("thuongHieu") ThuongHieu thuongHieu, BindingResult result) {
        this.iThuongHieuService.save(thuongHieu);
        return new ModelAndView("redirect:/admin/thuonghieu");
    }

    @GetMapping("/cap-nhat/{id}")
    public ModelAndView updateIndex(ModelMap model, @PathVariable Long id) {
        Optional<ThuongHieu> thuongHieu = iThuongHieuService.findById(id);
        thuongHieu.ifPresent(cl -> model.addAttribute("thuongHieu", cl));
        return new ModelAndView("/admin/thuoctinhsanpham/thuonghieu/capnhat", model);
    }

    @PostMapping("/cap-nhat/{id}")
    public ModelAndView update(ModelMap model, @PathVariable Long id, @Valid @ModelAttribute("thuongHieu") ThuongHieu thuongHieuUpdate, BindingResult result) {
        Optional<ThuongHieu> thuongHieu = iThuongHieuService.findById(id);
        if (thuongHieu.isPresent()) {
            thuongHieu.get().setTen(thuongHieuUpdate.getTen());
            thuongHieu.get().setMoTa(thuongHieuUpdate.getMoTa());
            this.iThuongHieuService.save(thuongHieu.get());
        }
        return new ModelAndView("redirect:/admin/thuonghieu");
    }

    @GetMapping("/xoa/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable Long id) {
        Optional<ThuongHieu> thuongHieu = iThuongHieuService.findById(id);
        if (thuongHieu.isPresent()) {
            thuongHieu.get().setTrangThai(0);
            this.iThuongHieuService.save(thuongHieu.get());
        }
        return new ModelAndView("redirect:/admin/thuonghieu");
    }
}
