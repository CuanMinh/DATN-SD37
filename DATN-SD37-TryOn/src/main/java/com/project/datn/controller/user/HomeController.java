package com.project.datn.controller.user;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.TaiKhoan;
import com.project.datn.entity.VaiTroTaiKhoan;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.repository.VaiTroTaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {
    @Autowired
    ChiTietSanPhamRepository productDetailRepository;
    @Autowired
    TaiKhoanRepository accountRepository;

    @Autowired
    VaiTroTaiKhoanRepository roleAccountRepository;

    @RequestMapping(value = {"/home", "/"})
    public ModelAndView home(ModelMap model, Principal principal) {
        boolean isLogin = false;
        if (principal != null) {
            isLogin = true;
        }
        model.addAttribute("isLogin", isLogin);

        if (principal != null) {
            Optional<TaiKhoan> c = accountRepository.FindByEmail(principal.getName());
            Optional<VaiTroTaiKhoan> uRole = roleAccountRepository.findByTaiKhoan_Id(Long.valueOf(c.get().getId()));
            if (uRole.get().getVaiTro().getTen().equals("ROLE_ADMIN")) {
                return new ModelAndView("forward:/admin/sanpham", model);
            }
        }

        Page<ChiTietSanPham> listP = productDetailRepository.findAll(PageRequest.of(0, 6));

        int totalPage = listP.getTotalPages();
        if (totalPage > 0) {
            int start = 1;
            int end = Math.min(2, totalPage);
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("products", listP);
        model.addAttribute("slide", true);
        return new ModelAndView("/user/index", model);
    }
}
