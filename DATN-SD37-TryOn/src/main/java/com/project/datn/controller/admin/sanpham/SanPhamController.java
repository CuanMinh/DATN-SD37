package com.project.datn.controller.admin.sanpham;

import com.project.datn.entity.SanPham;
import com.project.datn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin/sanpham")
public class SanPhamController {

    @Autowired
    private IDanhMucService iDanhMucService;

    @Autowired
    private IThuongHieuService iThuongHieuService;

    @Autowired
    private IChatLieuService iChatLieuService;

    @Autowired
    private IMauSacService iMauSacService;

    @Autowired
    private IKichCoService iKichCoService;

    @GetMapping()
    public ModelAndView list(ModelMap model) {
        model.addAttribute("listSanPham", new ArrayList<SanPham>());
        return new ModelAndView("/admin/sanpham/sanpham", model);
    }

    @GetMapping("them-moi")
    public ModelAndView themMoiIndex(ModelMap model) {
        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("listChatLieu", this.iChatLieuService.findAll());
        model.addAttribute("listMauSac", this.iMauSacService.findAll());
        model.addAttribute("listKichCo", this.iKichCoService.findAll());
        model.addAttribute("listThuongHieu", this.iThuongHieuService.findAll());
        model.addAttribute("listDanhMuc", this.iDanhMucService.findAll());

        return new ModelAndView("/admin/sanpham/themmoi", model);
    }
}
