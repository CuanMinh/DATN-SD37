package com.project.datn.controller.admin.sanpham;

import com.project.datn.entity.KichCo;
import com.project.datn.service.IKichCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/kichco")
public class KichCoController {

    @Autowired
    private IKichCoService iKichCoService;

    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("listKichCo", this.iKichCoService.findAll());
        return new ModelAndView("/admin/thuoctinhsanpham/kichco/kichco", model);
    }

    @GetMapping("/them-moi")
    public ModelAndView createIndex(ModelMap model) {
        model.addAttribute("kichCo", new KichCo());
        return new ModelAndView("/admin/thuoctinhsanpham/kichco/themmoi", model);
    }

    @PostMapping("/them-moi")
    public ModelAndView create(ModelMap model, @Valid @ModelAttribute("kichCo") KichCo kichCo, BindingResult result) {
        this.iKichCoService.save(kichCo);
        return new ModelAndView("redirect:/admin/kichco");
    }

    @GetMapping("/cap-nhat/{id}")
    public ModelAndView updateIndex(ModelMap model, @PathVariable Long id) {
        Optional<KichCo> kichCo = iKichCoService.findById(id);
        kichCo.ifPresent(cl -> model.addAttribute("kichCo", cl));
        return new ModelAndView("/admin/thuoctinhsanpham/kichco/capnhat", model);
    }

    @PostMapping("/cap-nhat/{id}")
    public ModelAndView update(ModelMap model, @PathVariable Long id, @Valid @ModelAttribute("kichCo") KichCo kichCoUpdate, BindingResult result) {
        Optional<KichCo> kichCo = iKichCoService.findById(id);
        if (kichCo.isPresent()) {
            kichCo.get().setTen(kichCoUpdate.getTen());
            kichCo.get().setMoTa(kichCoUpdate.getMoTa());
            this.iKichCoService.save(kichCo.get());
        }
        return new ModelAndView("redirect:/admin/kichco");
    }

    @GetMapping("/xoa/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable Long id) {
        Optional<KichCo> kichCo = iKichCoService.findById(id);
        if (kichCo.isPresent()) {
            kichCo.get().setTrangThai(0);
            this.iKichCoService.save(kichCo.get());
        }
        return new ModelAndView("redirect:/admin/kichco");
    }
}
