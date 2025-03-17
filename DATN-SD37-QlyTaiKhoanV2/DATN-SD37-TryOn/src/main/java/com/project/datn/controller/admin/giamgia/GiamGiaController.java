package com.project.datn.controller.admin.giamgia;

import com.project.datn.entity.MaGiamGia;
import com.project.datn.repository.MaGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/giamgia")
public class GiamGiaController {
    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    @GetMapping
    public String danhSachMaGiamGia(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<MaGiamGia> dsMaGiamGia;
        if (keyword != null && !keyword.trim().isEmpty()) {
            dsMaGiamGia = maGiamGiaRepository.searchByTenGiamGia(keyword);
        } else {
            dsMaGiamGia = maGiamGiaRepository.findAll();
        }
        model.addAttribute("mgglist", dsMaGiamGia);
        model.addAttribute("keyword", keyword);
        return "admin/giamgia/giamgia";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("mgg", new MaGiamGia());
        return "admin/giamgia/them-giam-gia";
    }

    @PostMapping("/luu")
    public String luuMaGiamGia(@ModelAttribute MaGiamGia mgg) {
        maGiamGiaRepository.save(mgg);
        return "redirect:/admin/giamgia";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, Model model) {
        Optional<MaGiamGia> optionalMGG = maGiamGiaRepository.findById(id);
        if (optionalMGG.isPresent()) {
            model.addAttribute("mgg", optionalMGG.get());
            return "admin/giamgia/sua-giam-gia";
        }
        return "redirect:/admin/giamgia";
    }

    @PostMapping("/capnhat")
    public String capNhatMaGiamGia(@ModelAttribute("mgg") MaGiamGia mgg, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/giamgia/sua-giam-gia";
        }
        maGiamGiaRepository.save(mgg);
        return "redirect:/admin/giamgia";
    }

    @GetMapping("/xoa/{id}")
    public String xoaMaGiamGia(@PathVariable Long id) {
        if (maGiamGiaRepository.existsById(id)) {
            maGiamGiaRepository.deleteById(id);
        }
        return "redirect:/admin/giamgia";
    }
}
