package com.project.datn.controller.admin.sanpham;

import com.project.datn.entity.MauSac;
import com.project.datn.service.IMauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/mausac")
public class MauSacController {

    @Autowired
    private IMauSacService iMauSacService;

    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("listMauSac", this.iMauSacService.findAll());
        return new ModelAndView("/admin/thuoctinhsanpham/mausac/mausac", model);
    }

    @GetMapping("/them-moi")
    public ModelAndView createIndex(ModelMap model) {
        model.addAttribute("mauSac", new MauSac());
        return new ModelAndView("/admin/thuoctinhsanpham/mausac/themmoi", model);
    }

    @PostMapping("/them-moi")
    public ModelAndView create(ModelMap model, @Valid @ModelAttribute("mauSac") MauSac mauSac, BindingResult result) {
        this.iMauSacService.save(mauSac);
        return new ModelAndView("redirect:/admin/mausac");
    }

    @GetMapping("/cap-nhat/{id}")
    public ModelAndView updateIndex(ModelMap model, @PathVariable Long id) {
        Optional<MauSac> mauSac = iMauSacService.findById(id);
        mauSac.ifPresent(cl -> model.addAttribute("mauSac", cl));
        return new ModelAndView("/admin/thuoctinhsanpham/mausac/capnhat", model);
    }

    @PostMapping("/cap-nhat/{id}")
    public ModelAndView update(ModelMap model, @PathVariable Long id, @Valid @ModelAttribute("mauSac") MauSac mauSacUpdate, BindingResult result) {
        Optional<MauSac> mauSac = iMauSacService.findById(id);
        if (mauSac.isPresent()) {
            mauSac.get().setTen(mauSacUpdate.getTen());
            mauSac.get().setMoTa(mauSacUpdate.getMoTa());
            this.iMauSacService.save(mauSac.get());
        }
        return new ModelAndView("redirect:/admin/mausac");
    }

    @GetMapping("/xoa/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable Long id) {
        Optional<MauSac> mauSac = iMauSacService.findById(id);
        if (mauSac.isPresent()) {
            mauSac.get().setTrangThai(0);
            this.iMauSacService.save(mauSac.get());
        }
        return new ModelAndView("redirect:/admin/mausac");
    }
}
