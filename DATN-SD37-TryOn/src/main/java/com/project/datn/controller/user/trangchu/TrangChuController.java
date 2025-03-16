package com.project.datn.controller.user.trangchu;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.HinhAnh;
import com.project.datn.entity.SanPham;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.HinhAnhRepository;
import com.project.datn.repository.SanPhamRepository;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.repository.VaiTroTaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class TrangChuController {

    @Autowired
    ChiTietSanPhamRepository productDetailRepository;
    @Autowired
    TaiKhoanRepository accountRepository;
    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    VaiTroTaiKhoanRepository roleAccountRepository;
    @Autowired
    HinhAnhRepository hinhAnhRepository;

    @GetMapping("/products")
    public ModelAndView products(ModelMap model) {

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

        model.addAttribute("products", sanPhams);

        return new ModelAndView("/user/trangchu/sanpham", model);
    }
}
