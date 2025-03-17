package com.project.datn.controller.user.banhangonline;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.GioHang;
import com.project.datn.entity.HinhAnh;
import com.project.datn.entity.MaGiamGia;
import com.project.datn.entity.TaiKhoan;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.GioHangRepository;
import com.project.datn.repository.HinhAnhRepository;
import com.project.datn.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("customer/giohang")
public class GiohangController {
    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    HinhAnhRepository hinhAnhRepository;

    @RequestMapping("")
    public ModelAndView list(@RequestParam(defaultValue = "0") int page, ModelMap model) {
        // Lấy thông tin đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            // Giả sử taiKhoanRepository.findByTen(username) trả về tài khoản của khách hàng
            TaiKhoan taiKhoan = taiKhoanRepository.findByTen(username);

            List<GioHang> gioHangs = gioHangRepository.findByTaiKhoan_Id(taiKhoan.getId());
            BigDecimal tongTien = BigDecimal.ZERO;
            for (GioHang gioHang : gioHangs) {
                BigDecimal thanhTien = gioHang.getDonGia().multiply(BigDecimal.valueOf(gioHang.getSoLuong()));
                tongTien = tongTien.add(thanhTien);
            }
            model.addAttribute("tongTien", tongTien);

            // Tính số lượng đơn hàng (số sản phẩm trong giỏ hàng)
            int soLuongDonHang = gioHangs.size();
            model.addAttribute("soLuongDonHang", soLuongDonHang);

            // Lấy danh sách hình ảnh
            Map<Long, HinhAnh> hinhAnhMap = new HashMap<>();
            for (GioHang gioHang : gioHangs) {
                Optional<ChiTietSanPham> chiTietSanPham = chiTietSanPhamRepository.findById(gioHang.getChiTietSanPham().getId());

                if (chiTietSanPham.isPresent()) {
                    Long productDetailId = chiTietSanPham.get().getId();
                    Long sanPhamId = chiTietSanPham.get().getSanPham().getId();
                    HinhAnh hinhAnh = hinhAnhRepository.findTop1BySanPham_Id(sanPhamId)
                            .stream()
                            .findFirst()
                            .orElse(null);
                    if (hinhAnh != null) {
                        hinhAnhMap.put(productDetailId, hinhAnh);
                    }
                }
            }
            model.addAttribute("hinhAnh", hinhAnhMap);


            Pageable pageable = PageRequest.of(page, 5);
            Page<GioHang> listGiohang = gioHangRepository.findByTaiKhoan_Id(taiKhoan.getId(), pageable);
            model.addAttribute("listGiohang", listGiohang);
            return new ModelAndView("/user/banhangonline/giohang", model);
        }
        // Nếu chưa đăng nhập, chuyển hướng về trang login
        return new ModelAndView("redirect:/login", model);
    }

    @PostMapping("/xoa")
    public String deleteGioHang(@RequestParam("idGioHang") Long id) {

        gioHangRepository.deleteById(id);
        return "redirect:/customer/giohang";
    }

    @PostMapping("/tang")
    public ModelAndView increaseQuantity(@RequestParam("idGioHang") Long id) {
        Optional<GioHang> optionalGioHang = gioHangRepository.findById(id);
        if (optionalGioHang.isPresent()) {
            GioHang gioHang = optionalGioHang.get();
            gioHang.setSoLuong(gioHang.getSoLuong() + 1);
            gioHangRepository.save(gioHang);
        }
        return new ModelAndView("redirect:/customer/giohang");
    }

    @PostMapping("/giam")
    public ModelAndView decreaseQuantity(@RequestParam("idGioHang") Long id) {
        Optional<GioHang> optionalGioHang = gioHangRepository.findById(id);
        if (optionalGioHang.isPresent()) {
            GioHang gioHang = optionalGioHang.get();
            if (gioHang.getSoLuong() > 1) {
                gioHang.setSoLuong(gioHang.getSoLuong() - 1);
                gioHangRepository.save(gioHang);
            } else {
                gioHangRepository.delete(gioHang);
            }
        }
        return new ModelAndView("redirect:/customer/giohang");
    }

    @PostMapping("/chuyentrangthanhtoan")
    public String chuyenTrangThanhToan(RedirectAttributes redirectAttributes) {
        // Lấy thông tin đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Lấy tài khoản của khách hàng
        TaiKhoan taiKhoan = taiKhoanRepository.findByTen(username);

        // Lấy danh sách giỏ hàng
        List<GioHang> gioHangs = gioHangRepository.findByTaiKhoan_Id(taiKhoan.getId());

        if (gioHangs.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Chưa có sản phẩm trong giỏ hàng để thanh toán.");

            return "redirect:/customer/giohang";
        }

        for (GioHang gioHang : gioHangs) {
            Optional<ChiTietSanPham> chiTietSanPham = chiTietSanPhamRepository.findById(gioHang.getChiTietSanPham().getId());


            // Kiểm tra nếu sản phẩm tồn tại
            if (chiTietSanPham.isPresent()) {
                if (chiTietSanPham.get().getTrangThai() == 0){
                    redirectAttributes.addFlashAttribute("error", "Sản phẩm: " + chiTietSanPham.get().getSanPham().getTen()
                            +" - màu:" + chiTietSanPham.get().getMauSac().getTen() + " - kích cỡ:"+ chiTietSanPham.get().getKichCo().getTen()
                            + " - hiện tại đang ngừng bán ở shop TRYON ");
                    return "redirect:/customer/giohang";
                }
                if (chiTietSanPham.get().getSoLuong() < gioHang.getSoLuong()) {
                    redirectAttributes.addFlashAttribute("error", "Sản phẩm: " + chiTietSanPham.get().getSanPham().getTen()
                           +" - màu:" + chiTietSanPham.get().getMauSac().getTen() + " - kích cỡ:"+ chiTietSanPham.get().getKichCo().getTen()
                            + " - số lượng trong kho còn: " + chiTietSanPham.get().getSoLuong());
                    return "redirect:/customer/giohang";
                }
            } else {
                 redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
                return "redirect:/customer/giohang";
            }
        }

        // Nếu kiểm tra thành công, chuyển hướng về trang thanh toán
        return "redirect:/customer/thanhtoan";
    }
    @PostMapping("/xemsanpham")
    public ModelAndView xemSanPham(@RequestParam("idsanpham") Long idsanpham) {
        return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idsanpham);
    }


}
