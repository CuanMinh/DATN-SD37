package com.project.datn.controller.user;

import com.project.datn.entity.*;
import com.project.datn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    SanPhamRepository sanPhamRepository;
    @Autowired
    DanhMucRepository danhMucRepository;
    @Autowired
    ThuongHieuRepository thuongHieuRepository;
    @Autowired
    VaiTroTaiKhoanRepository roleAccountRepository;
    @Autowired
    HinhAnhRepository hinhAnhRepository;


    @RequestMapping(value = {"/home", "/"})
    public ModelAndView home(ModelMap model, Principal principal) {
        boolean isLogin = false;
        if (principal != null) {
            isLogin = true;
        }
        model.addAttribute("isLogin", isLogin);

        if (principal != null) {
            Optional<TaiKhoan> c = accountRepository.FindByTen(principal.getName());
            Optional<VaiTroTaiKhoan> uRole = roleAccountRepository.findByTaiKhoan_Id(Long.valueOf(c.get().getId()));
            if (uRole.get().getVaiTro().getTen().equals("ROLE_ADMIN")) {
                return new ModelAndView("forward:/admin/sanpham", model);
            }
        }

        Page<ChiTietSanPham> listP = productDetailRepository.findAll(PageRequest.of(0, 6));

        List<SanPham> sanPhams = sanPhamRepository.findAll();

        Map<Long, HinhAnh> hinhAnhMap = new HashMap<>();

        for (SanPham sanPham : sanPhams) {
            Optional<ChiTietSanPham> chiTietSanPham = productDetailRepository.findById(sanPham.getId());
            BigDecimal gia = chiTietSanPham.get().getGia();

            Long sanPhamId = sanPham.getId();
            HinhAnh hinhAnh = hinhAnhRepository.findTop1BySanPham_Id(sanPhamId)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (hinhAnh != null) {
                hinhAnhMap.put(sanPhamId, hinhAnh);
            }

            model.addAttribute("gia", gia);
        }


        model.addAttribute("hinhAnh", hinhAnhMap);


        int totalPage = listP.getTotalPages();
        if (totalPage > 0) {
            int start = 1;
            int end = Math.min(2, totalPage);
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("products", listP);
        model.addAttribute("productss", sanPhams);
        model.addAttribute("slide", true);
        return new ModelAndView("/user/index", model);
    }

}
