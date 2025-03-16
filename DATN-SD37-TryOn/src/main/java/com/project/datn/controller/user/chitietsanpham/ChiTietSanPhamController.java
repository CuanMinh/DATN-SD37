package com.project.datn.controller.user.chitietsanpham;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.GioHang;
import com.project.datn.entity.HinhAnh;
import com.project.datn.entity.KichCo;
import com.project.datn.entity.MauSac;
import com.project.datn.entity.TaiKhoan;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.GioHangRepository;
import com.project.datn.repository.HinhAnhRepository;
import com.project.datn.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chitietsanpham")
public class ChiTietSanPhamController {
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    HinhAnhRepository hinhAnhRepository;

    @GetMapping("/xemchitietsanpham/{idsanpham}")
    public ModelAndView list(@PathVariable("idsanpham") Long idsanpham, ModelMap model) {
        List<ChiTietSanPham> chiTietSanPhamList = chiTietSanPhamRepository.findBySanPham_Id(idsanpham);
        model.addAttribute("chiTietSanPhamList", chiTietSanPhamList);
        if (!chiTietSanPhamList.isEmpty()) {
            model.addAttribute("chiTietSanPham", chiTietSanPhamList.get(0));
            if (chiTietSanPhamList.get(0).getGiamGia() != null) {
                BigDecimal giaCuoiCung = chiTietSanPhamList.get(0).getGia().subtract(chiTietSanPhamList.get(0).getGiamGia());
                model.addAttribute("giaCuoiCung", giaCuoiCung);
            } else {
                BigDecimal giaCuoiCung = chiTietSanPhamList.get(0).getGia();
                model.addAttribute("giaCuoiCung", giaCuoiCung);
            }
            ChiTietSanPham defaultChiTietSanPham = chiTietSanPhamList.get(0);
            model.addAttribute("mauSacDefault", defaultChiTietSanPham.getMauSac().getId());
            model.addAttribute("kichCoDefault", defaultChiTietSanPham.getKichCo().getId());
        }

        // Lấy danh sách hình ảnh
        List<HinhAnh> hinhAnhList = hinhAnhRepository.findBySanPham_Id(idsanpham);
        model.addAttribute("hinhAnhList", hinhAnhList);


        // Lấy danh sách màu sắc duy nhất
        Set<MauSac> uniqueMauSac = new HashSet<>();
        for (ChiTietSanPham ctsp : chiTietSanPhamList) {
            uniqueMauSac.add(ctsp.getMauSac());
        }
        model.addAttribute("mauSacList", new ArrayList<>(uniqueMauSac));

        return new ModelAndView("/user/chitietsanpham/chitietsanpham", model);
    }

    @PostMapping("/giohang/them")
    public ModelAndView themVaoGioHang(@RequestParam("mauSac") Long mauSacId,
                                       @RequestParam("kichCo") Long kichCoId,
                                       @RequestParam("idSanPham") Long idSanPham,
                                       @RequestParam(value = "soLuong", required = false) Integer soLuong,
                                       @RequestParam("giaCuoiCung") BigDecimal giaCuoiCung,
                                       RedirectAttributes redirectAttributes) {
        if (mauSacId == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy màu sắc");
            return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
        }
        if (kichCoId == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy kích cỡ");
            return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
        }
        if (idSanPham == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm id sản phẩm");
            return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
        }
         if (soLuong == null) {
             redirectAttributes.addFlashAttribute("error", "Bạn chưa nhập số lượng");
             return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
         }

        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findByMauSac_IdAndKichCo_IdAndSanPham_Id(mauSacId, kichCoId, idSanPham);

        // Lấy thông tin đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            TaiKhoan taiKhoan = taiKhoanRepository.findByTen(username);

            if (taiKhoan == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản!");
                return new ModelAndView("redirect:/login");
            }

            if(soLuong > chiTietSanPham.getSoLuong() ){
                redirectAttributes.addFlashAttribute("error", "Số lượng bạn thêm quá số lượng trong kho - Số lượng trong kho còn: " + chiTietSanPham.getSoLuong());
                return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
            }else{
                if (chiTietSanPham.getTrangThai() == 0){
                    redirectAttributes.addFlashAttribute("error", "sản phẩm:" + chiTietSanPham.getSanPham().getTen() +"-màu:"+ chiTietSanPham.getMauSac().getTen()
                    + "- kích cỡ:"+ chiTietSanPham.getKichCo().getTen() +"_ hiện tại đang ngừng kinh doanh ở shop TRYON");
                    return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
                }else{
                    Optional<GioHang> gioHangOptional = gioHangRepository.findByTaiKhoan_IdAndChiTietSanPham_Id(taiKhoan.getId(),chiTietSanPham.getId());
                    if (gioHangOptional.isPresent()) {
                        int tongSoLuongMoi = gioHangOptional.get().getSoLuong() + soLuong;
                        GioHang gioHang = gioHangOptional.get();
                        gioHang.setSoLuong(tongSoLuongMoi);
                        gioHangRepository.save(gioHang);
                        redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng, Thành công");
                        return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
                    }else{
                        // Tạo và thiết lập thông tin GioHang
                        GioHang gioHang = new GioHang();
                        gioHang.setSoLuong(soLuong);
                        gioHang.setTaiKhoan(taiKhoan);
                        gioHang.setDonGia(giaCuoiCung);
                        gioHang.setChiTietSanPham(ChiTietSanPham.builder().id(chiTietSanPham.getId()).build()); // Chỉ cần ID là đủ

                        // Lưu GioHang vào database (giả sử bạn có gioHangRepository)
                        gioHangRepository.save(gioHang);
                        redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng, Thành công");
                        return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
                    }
                }
            }

        }

        // Nếu chưa đăng nhập, chuyển hướng về trang login
        redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
        return new ModelAndView("redirect:/login");
    }
    @PostMapping("/thanhtoannhanh")
    public ModelAndView thanhToanNhanh(@RequestParam("mauSac") Long mauSacId,
                                       @RequestParam("kichCo") Long kichCoId,
                                       @RequestParam("idSanPham") Long idSanPham,
                                       @RequestParam(value = "soLuong", required = false) Integer soLuong,
                                       @RequestParam("giaCuoiCung") BigDecimal giaCuoiCung,
                                       RedirectAttributes redirectAttributes) {
        if (mauSacId == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy màu sắc");
            return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
        }
        if (kichCoId == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy kích cỡ");
            return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
        }
        if (idSanPham == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm id sản phẩm");
            return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
        }
        if (soLuong == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn chưa nhập số lượng");
            return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
        }

        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findByMauSac_IdAndKichCo_IdAndSanPham_Id(mauSacId, kichCoId, idSanPham);

        // Lấy thông tin đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            TaiKhoan taiKhoan = taiKhoanRepository.findByTen(username);

            if (taiKhoan == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản!");
                return new ModelAndView("redirect:/login");
            }

            if(soLuong > chiTietSanPham.getSoLuong() ){
                redirectAttributes.addFlashAttribute("error", "Số lượng bạn thêm quá số lượng trong kho - Số lượng trong kho còn: " + chiTietSanPham.getSoLuong());
                return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
            }else{
                if (chiTietSanPham.getTrangThai() == 0){
                    redirectAttributes.addFlashAttribute("error", "sản phẩm:" + chiTietSanPham.getSanPham().getTen() +"-màu:"+ chiTietSanPham.getMauSac().getTen()
                            + "- kích cỡ:"+ chiTietSanPham.getKichCo().getTen() +"_ hiện tại đang ngừng kinh doanh ở shop TRYON");
                    return new ModelAndView("redirect:/chitietsanpham/xemchitietsanpham/" + idSanPham);
                }else{
                    Optional<GioHang> gioHangOptional = gioHangRepository.findByTaiKhoan_IdAndChiTietSanPham_Id(taiKhoan.getId(),chiTietSanPham.getId());
                    if (gioHangOptional.isPresent()) {
                        int tongSoLuongMoi = gioHangOptional.get().getSoLuong() + soLuong;
                        GioHang gioHang = gioHangOptional.get();
                        gioHang.setSoLuong(tongSoLuongMoi);
                        gioHangRepository.save(gioHang);
                        return new ModelAndView("redirect:/customer/giohang");
                    }else{
                        // Tạo và thiết lập thông tin GioHang
                        GioHang gioHang = new GioHang();
                        gioHang.setSoLuong(soLuong);
                        gioHang.setTaiKhoan(taiKhoan);
                        gioHang.setDonGia(giaCuoiCung);
                        gioHang.setChiTietSanPham(ChiTietSanPham.builder().id(chiTietSanPham.getId()).build()); // Chỉ cần ID là đủ

                        // Lưu GioHang vào database (giả sử bạn có gioHangRepository)
                        gioHangRepository.save(gioHang);
                        return new ModelAndView("redirect:/customer/giohang");
                    }

                }
            }

        }

        // Nếu chưa đăng nhập, chuyển hướng về trang login
        redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
        return new ModelAndView("redirect:/login");
    }
}
