package com.project.datn.controller.user;

import com.project.datn.entity.*;
import com.project.datn.repository.*;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {
    @Autowired
    ChiTietSanPhamRepository productDetailRepository;
    @Autowired
    TaiKhoanRepository accountRepository;
    @Autowired
    DanhMucRepository danhMucRepository;
    @Autowired
    ThuongHieuRepository thuongHieuRepository;

    @Autowired
    HinhAnhRepository hinhAnhRepository;


    @Autowired
    SanPhamRepository sanPhamRepository;

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
            Optional<TaiKhoan> c = accountRepository.FindByTen(principal.getName());
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

        List<SanPham> sanPhams = sanPhamRepository.findAll();
        List<Map<String, Object>> productList = new ArrayList<>();

        for (SanPham sanPham : sanPhams) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("sanPham", sanPham);

            // Lấy danh sách ChiTietSanPham theo sản phẩm
            List<ChiTietSanPham> chiTietSanPhamList = productDetailRepository.findBySanPhamIds(sanPham.getId());
            BigDecimal gia = chiTietSanPhamList.isEmpty() ? BigDecimal.ZERO : chiTietSanPhamList.get(0).getGia();
            productData.put("gia", gia);

            // Lấy danh sách hình ảnh theo sản phẩm
            List<HinhAnh> hinhAnhList = hinhAnhRepository.findBySanPhamId(sanPham.getId());
            String imageUrl = hinhAnhList.isEmpty() ? "/default-image.jpg" : hinhAnhList.get(0).getDuongDan();
            productData.put("url", imageUrl);

            productList.add(productData);
        }

        model.addAttribute("productss", productList);
        model.addAttribute("danhMuc", danhMucRepository.findAll());
        model.addAttribute("thuongHieu", thuongHieuRepository.findAll());

        model.addAttribute("products", listP);
        model.addAttribute("slide", true);
        return new ModelAndView("/user/index", model);
    }

    @GetMapping("/products")
    public String products(Model model){

        List<SanPham> sanPhams = sanPhamRepository.findAll();

        Map<Long, HinhAnh> hinhAnhMap = new HashMap<>();

        for (SanPham sanPham : sanPhams){
            Optional<ChiTietSanPham> chiTietSanPham = productDetailRepository.findBySanPhamId(sanPham.getId());
            BigDecimal gia = chiTietSanPham.get().getGia();

            Long sanPhamId = sanPham.getId();
            HinhAnh hinhAnh = hinhAnhRepository.findTop1BySanPham_Id(sanPhamId);

            if (hinhAnh != null) {
                hinhAnhMap.put(sanPhamId, hinhAnh);
            }

            model.addAttribute("gia", gia);
        }




        model.addAttribute("hinhAnh", hinhAnhMap);

        model.addAttribute("products", sanPhams);
        model.addAttribute("danhMuc", danhMucRepository.findAll());
        model.addAttribute("thuongHieu", thuongHieuRepository.findAll());

        return "/user/products";
    }


    @GetMapping("/filter-products")
    public String filterProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) Long category,
            Model model) {


        // Lọc sản phẩm dựa trên tiêu chí tìm kiếm
        List<SanPham> filteredProducts = sanPhamRepository.filterProducts(search, brand, category);
        List<Map<String, Object>> productList = new ArrayList<>();

        for (SanPham sanPham : filteredProducts) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("sanPham", sanPham);

            // Lấy giá sản phẩm
            List<ChiTietSanPham> chiTietSanPhamList = productDetailRepository.findBySanPhamIds(sanPham.getId());
            BigDecimal gia = chiTietSanPhamList.isEmpty() ? BigDecimal.ZERO : chiTietSanPhamList.get(0).getGia();
            productData.put("gia", gia);

            // Lấy hình ảnh sản phẩm
            List<HinhAnh> hinhAnhList = hinhAnhRepository.findBySanPhamId(sanPham.getId());
            String imageUrl = hinhAnhList.isEmpty() ? "/default-image.jpg" : hinhAnhList.get(0).getDuongDan();
            productData.put("url", imageUrl);

            productList.add(productData);
        }

        // Đưa dữ liệu vào model để hiển thị trên giao diện
        model.addAttribute("products", productList);
        model.addAttribute("thuongHieu", thuongHieuRepository.findAll());
        model.addAttribute("danhMuc", danhMucRepository.findAll());

        // Giữ lại giá trị lọc
        model.addAttribute("selectedSearch", search);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("selectedCategory", category);

        return "/user/products";
    }


}
