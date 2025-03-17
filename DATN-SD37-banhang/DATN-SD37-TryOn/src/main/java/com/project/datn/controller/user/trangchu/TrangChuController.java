package com.project.datn.controller.user.trangchu;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.HinhAnh;
import com.project.datn.entity.SanPham;
import com.project.datn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class TrangChuController {

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

        model.addAttribute("danhMuc", danhMucRepository.findAll());
        model.addAttribute("thuongHieu", thuongHieuRepository.findAll());

        return new ModelAndView("/user/trangchu/sanpham", model);
    }

    @GetMapping("/filter-products")
    public ModelAndView products(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Long brand,
            ModelMap model) {

        // 🔹 Nếu có bộ lọc, lấy sản phẩm theo bộ lọc, nếu không lấy tất cả
        List<SanPham> sanPhams = (search != null || category != null || brand != null)
                ? sanPhamRepository.searchProducts(search, category, brand)
                : sanPhamRepository.findAll();

        // 🔹 Dùng LinkedHashMap để chỉ lấy 1 sản phẩm theo danh mục + thương hiệu
        Map<String, SanPham> uniqueSanPhams = new LinkedHashMap<>();
        Map<Long, HinhAnh> hinhAnhMap = new HashMap<>();
        Map<Long, BigDecimal> giaMap = new HashMap<>();

        for (SanPham sanPham : sanPhams) {
            String key = sanPham.getDanhMuc().getId() + "-" + sanPham.getThuongHieu().getId(); // Ghép ID danh mục + thương hiệu

            if (!uniqueSanPhams.containsKey(key)) { // Nếu chưa có sản phẩm nào thuộc danh mục + thương hiệu này
                uniqueSanPhams.put(key, sanPham);

                Long sanPhamId = sanPham.getId();

                // 🔹 Lấy giá sản phẩm an toàn (tránh lỗi .get() trên Optional)
                Optional<ChiTietSanPham> chiTietSanPham = productDetailRepository.findById(sanPhamId);
                giaMap.put(sanPhamId, chiTietSanPham.map(ChiTietSanPham::getGia).orElse(BigDecimal.ZERO));

                // 🔹 Lấy ảnh sản phẩm
                HinhAnh hinhAnh = hinhAnhRepository.findTop1BySanPham_Id(sanPhamId)
                        .stream()
                        .findFirst()
                        .orElse(null);
                if (hinhAnh != null) {
                    hinhAnhMap.put(sanPhamId, hinhAnh);
                }
            }
        }

        // 🔹 Chuyển Map về List để hiển thị trên giao diện
        List<SanPham> filteredSanPhams = new ArrayList<>(uniqueSanPhams.values());

        // 🔹 Gửi dữ liệu về giao diện
        model.addAttribute("hinhAnh", hinhAnhMap);
        model.addAttribute("products", filteredSanPhams);
        model.addAttribute("danhMuc", danhMucRepository.findAll());
        model.addAttribute("thuongHieu", thuongHieuRepository.findAll());
        model.addAttribute("giaMap", giaMap); // Thêm giá sản phẩm
        model.addAttribute("selectedSearch", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedBrand", brand);

        return new ModelAndView("/user/trangchu/sanpham", model);
    }

}
