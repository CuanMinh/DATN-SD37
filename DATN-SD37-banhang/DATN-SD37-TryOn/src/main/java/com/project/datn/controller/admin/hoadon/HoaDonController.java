package com.project.datn.controller.admin.hoadon;

import com.project.datn.DTO.ChiTietHoaDonDTO;
import com.project.datn.DTO.HoaDonDTO;
import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.HoaDon;
import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.HoaDonChiTietRepository;
import com.project.datn.repository.HoaDonRepository;
import com.project.datn.repository.SanPhamRepository;
import com.project.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/hoadon")
public class HoaDonController {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private HoaDonChiTietRepository chiTietHoaDonRepository;

    private final HoaDonService hoaDonService;
    @Autowired
    private HoaDonRepository hoaDonRepository;

    public HoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    @GetMapping("")
    public ModelAndView list(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(required = false) String maHoaDon,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                             @RequestParam(required = false) Integer trangThai,
                             ModelMap model) {
        if (page < 0) page = 0;

        Page<HoaDon> hoaDonPage = hoaDonService.searchHoaDon(maHoaDon, startDate, endDate, trangThai, page, size);

        System.out.println("Current page: " + page);
        System.out.println("Total pages: " + hoaDonPage.getTotalPages());
        System.out.println("Total elements: " + hoaDonPage.getTotalElements());

        model.addAttribute("hoaDons", hoaDonPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        model.addAttribute("maHoaDon", maHoaDon);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("trangThai", trangThai);

        return new ModelAndView("/admin/hoadon/hoadon", model);
    }


    @PostMapping("/cancel/{id}")
    public String cancelHoaDon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        hoaDonService.cancelHoaDon(id);
        redirectAttributes.addFlashAttribute("message", "Hóa đơn đã được hủy thành công!");
        return "redirect:/admin/hoadon";
    }

    @GetMapping("/view-create")
    public String showCreateForm(Model model) {
        List<ChiTietSanPham> chiTietSanPhams = chiTietSanPhamRepository.findAll();
        chiTietSanPhams.forEach(sp -> System.out.println("ID: " + sp.getId() + ", SanPham: " + (sp.getSanPham() != null ? sp.getSanPham().getTen() : "null")));
        model.addAttribute("sanPhams", chiTietSanPhams);
        model.addAttribute("hoaDonDTO", new HoaDonDTO());
        return "/admin/hoadon/themhoadon";
    }

    @GetMapping("/detail/{id}")
    public String getHoaDonDetail(@PathVariable Long id, Model model) {
        Optional<HoaDon> hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon.isPresent()) {
            model.addAttribute("hoaDon", hoaDon.get());
            List<HoaDonChiTiet> chiTietHoaDons = chiTietHoaDonRepository.findByHoaDonId(id);

            chiTietHoaDons = chiTietHoaDons.stream()
                    .filter(chiTiet -> {
                        ChiTietSanPham ctsp = chiTiet.getChiTietSanPham();
                        return ctsp != null && ctsp.getSanPham() != null && ctsp.getMauSac() != null && ctsp.getKichCo() != null;
                    })
                    .collect(Collectors.toList());

            System.out.println("Số lượng chi tiết hóa đơn: " + chiTietHoaDons.size());
            for (HoaDonChiTiet chiTiet : chiTietHoaDons) {
                ChiTietSanPham ctsp = chiTiet.getChiTietSanPham();
                System.out.println("ChiTietSanPham ID: " + ctsp.getId() +
                        ", SanPham: " + (ctsp.getSanPham() != null ? ctsp.getSanPham().getTen() : "null") +
                        ", MauSac: " + (ctsp.getMauSac() != null ? ctsp.getMauSac().getTen() : "null") +
                        ", KichCo: " + (ctsp.getKichCo() != null ? ctsp.getKichCo().getTen() : "null"));
            }

            model.addAttribute("chiTietHoaDons", chiTietHoaDons);

            return "admin/hoadon/chitiethoadon";
        } else {
            return "redirect:/admin/hoadon";
        }
    }

    @PostMapping("/create")
    public String createHoaDon(@ModelAttribute("hoaDonDTO") HoaDonDTO hoaDonDTO, RedirectAttributes redirectAttributes) {
        try {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setMaHoaDon(hoaDonDTO.getMaHoaDon());
            hoaDon.setTongTien(hoaDonDTO.getTongTien() != null ? hoaDonDTO.getTongTien() : BigDecimal.ZERO);
            hoaDon.setNgayTaoHoaDon(new Date());
            hoaDon.setTrangThaiDonHang(hoaDonDTO.getTrangThaiDonHang());
            hoaDon.setTrangThaiThanhToan(0);
            hoaDon.setTrangThai(1);

            HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

            if (hoaDonDTO.getSanPhams() != null && !hoaDonDTO.getSanPhams().isEmpty()) {
                for (ChiTietHoaDonDTO chiTietDTO : hoaDonDTO.getSanPhams()) {
                    ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(chiTietDTO.getSanPhamId())
                            .orElseThrow(() -> new IllegalArgumentException("Chi tiết sản phẩm không tồn tại: " + chiTietDTO.getSanPhamId()));

                    if (chiTietSanPham.getSanPham() == null || chiTietSanPham.getMauSac() == null || chiTietSanPham.getKichCo() == null) {
                        throw new IllegalArgumentException("Chi tiết sản phẩm không hợp lệ: thiếu thông tin sản phẩm, màu sắc hoặc kích cỡ.");
                    }

                    if (chiTietSanPham.getSoLuong() < chiTietDTO.getSoLuong()) {
                        throw new IllegalArgumentException("Số lượng tồn kho không đủ cho sản phẩm: " + chiTietSanPham.getSanPham().getTen());
                    }

                    HoaDonChiTiet chiTiet = new HoaDonChiTiet();
                    chiTiet.setHoaDon(savedHoaDon);
                    chiTiet.setChiTietSanPham(chiTietSanPham);
                    chiTiet.setSoLuong(chiTietDTO.getSoLuong());
                    chiTiet.setGia(chiTietSanPham.getGia());
                    chiTietSanPham.setSoLuong(chiTietSanPham.getSoLuong() - chiTietDTO.getSoLuong());
                    chiTietSanPhamRepository.save(chiTietSanPham);

                    chiTietHoaDonRepository.save(chiTiet);
                }
            }

            redirectAttributes.addFlashAttribute("message", "Hóa đơn đã được tạo thành công!");
            return "redirect:/admin/hoadon";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/hoadon/view-create";
        }
    }

    @PostMapping("/thanh-toan/{id}")
    public String thanhToanHoaDon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<HoaDon> optionalHoaDon = hoaDonService.getHoaDonById(id);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            if (hoaDon.getTrangThaiDonHang() == 0 && hoaDon.getTrangThaiThanhToan() == 0) {
                hoaDon.setTrangThaiThanhToan(1);
                hoaDon.setTrangThai(5);
                hoaDon.setNgayThanhToan(new Date());
                hoaDonService.saveHoaDon(hoaDon);
                redirectAttributes.addFlashAttribute("message", "Hóa đơn đã được thanh toán và hoàn thành!");
            }
        }
        return "redirect:/admin/hoadon/detail/" + id;
    }

    @PostMapping("/xac-nhan/{id}")
    public String xacNhanDonHang(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<HoaDon> optionalHoaDon = hoaDonService.getHoaDonById(id);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            if (hoaDon.getTrangThaiDonHang() == 1 && hoaDon.getTrangThai() == 2) {
                hoaDon.setTrangThai(3);
                hoaDonService.saveHoaDon(hoaDon);
                redirectAttributes.addFlashAttribute("message", "Đơn hàng đã được xác nhận!");
            }
        }
        return "redirect:/admin/hoadon/detail/" + id;
    }

    @PostMapping("/giao-hang/{id}")
    public String giaoHang(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<HoaDon> optionalHoaDon = hoaDonService.getHoaDonById(id);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            if (hoaDon.getTrangThaiDonHang() == 1 && hoaDon.getTrangThai() == 3) {
                hoaDon.setTrangThai(4);
                hoaDon.setNgayGiaoHang(new Date());
                hoaDonService.saveHoaDon(hoaDon);
                redirectAttributes.addFlashAttribute("message", "Đơn hàng đã bắt đầu giao!");
            }
        }
        return "redirect:/admin/hoadon/detail/" + id;
    }

    @PostMapping("/hoan-thanh/{id}")
    public String hoanThanhDonHang(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<HoaDon> optionalHoaDon = hoaDonService.getHoaDonById(id);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            if (hoaDon.getTrangThaiDonHang() == 1 && hoaDon.getTrangThai() == 4) {
                hoaDon.setTrangThai(5);
                hoaDon.setTrangThaiThanhToan(1);
                hoaDon.setNgayThanhToan(new Date());
                hoaDonService.saveHoaDon(hoaDon);
                redirectAttributes.addFlashAttribute("message", "Đơn hàng đã hoàn thành!");
            }
        }
        return "redirect:/admin/hoadon/detail/" + id;
    }

    @GetMapping("/get-sanphams")
    @ResponseBody
    public List<ChiTietSanPham> getAllSanPhams() {
        return chiTietSanPhamRepository.findAll();
    }

    @PostMapping("/add-product/{id}")
    @ResponseBody
    public ResponseEntity<?> addProductToHoaDonAjax(@PathVariable Long id,
                                                    @RequestParam Long sanPhamId,
                                                    @RequestParam Integer soLuong) {
        Optional<HoaDon> optionalHoaDon = hoaDonService.getHoaDonById(id);
        if (optionalHoaDon.isPresent() && optionalHoaDon.get().getTrangThai() != 0 && optionalHoaDon.get().getTrangThai() < 4) {
            HoaDon hoaDon = optionalHoaDon.get();
            ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(sanPhamId)
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

            if (chiTietSanPham.getTrangThai() != 1) {
                return ResponseEntity.badRequest().body("Sản phẩm không còn hoạt động!");
            }
            if (chiTietSanPham.getSoLuong() < soLuong) {
                return ResponseEntity.badRequest().body("Số lượng tồn kho không đủ!");
            }

            HoaDonChiTiet chiTiet = new HoaDonChiTiet();
            chiTiet.setHoaDon(hoaDon);
            chiTiet.setChiTietSanPham(chiTietSanPham);
            chiTiet.setSoLuong(soLuong);
            chiTiet.setGia(chiTietSanPham.getGia());

            chiTietHoaDonRepository.save(chiTiet);

            chiTietSanPham.setSoLuong(chiTietSanPham.getSoLuong() - soLuong);
            chiTietSanPhamRepository.save(chiTietSanPham);

            hoaDon.setTongTien(hoaDon.getTongTien().add(chiTietSanPham.getGia().multiply(BigDecimal.valueOf(soLuong))));
            hoaDonService.saveHoaDon(hoaDon);

            return ResponseEntity.ok(Map.of("chiTiet", chiTiet));
        }
        return ResponseEntity.badRequest().body("Không thể thêm sản phẩm!");
    }

    @PostMapping("/edit-product/{hoaDonId}/{chiTietId}")
    @ResponseBody
    public ResponseEntity<?> editProductInHoaDonAjax(@PathVariable Long hoaDonId,
                                                     @PathVariable Long chiTietId,
                                                     @RequestParam Integer soLuong) {
        Optional<HoaDon> optionalHoaDon = hoaDonService.getHoaDonById(hoaDonId);
        Optional<HoaDonChiTiet> optionalChiTiet = chiTietHoaDonRepository.findById(chiTietId);
        if (optionalHoaDon.isPresent() && optionalChiTiet.isPresent() && optionalHoaDon.get().getTrangThai() != 0 && optionalHoaDon.get().getTrangThai() < 4) {
            HoaDon hoaDon = optionalHoaDon.get();
            HoaDonChiTiet chiTiet = optionalChiTiet.get();
            ChiTietSanPham chiTietSanPham = chiTiet.getChiTietSanPham();

            int soLuongCu = chiTiet.getSoLuong();
            int deltaSoLuong = soLuong - soLuongCu;

            if (chiTietSanPham.getSoLuong() + soLuongCu < soLuong) {
                return ResponseEntity.badRequest().body("Số lượng tồn kho không đủ!");
            }

            chiTiet.setSoLuong(soLuong);
            chiTietHoaDonRepository.save(chiTiet);

            chiTietSanPham.setSoLuong(chiTietSanPham.getSoLuong() - deltaSoLuong);
            chiTietSanPhamRepository.save(chiTietSanPham);

            hoaDon.setTongTien(hoaDon.getTongTien().add(chiTiet.getGia().multiply(BigDecimal.valueOf(deltaSoLuong))));
            hoaDonService.saveHoaDon(hoaDon);

            Map<String, Object> response = new HashMap<>();
            response.put("maxSoLuong", chiTietSanPham.getSoLuong() + soLuong);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("Không thể chỉnh sửa!");
    }

    @PostMapping("/delete-product/{hoaDonId}/{chiTietId}")
    @ResponseBody
    public ResponseEntity<?> deleteProductFromHoaDonAjax(@PathVariable Long hoaDonId,
                                                         @PathVariable Long chiTietId) {
        try {
            Optional<HoaDon> optionalHoaDon = hoaDonService.getHoaDonById(hoaDonId);
            Optional<HoaDonChiTiet> optionalChiTiet = chiTietHoaDonRepository.findById(chiTietId);

            if (!optionalHoaDon.isPresent() || !optionalChiTiet.isPresent()) {
                return ResponseEntity.badRequest().body("Hóa đơn hoặc chi tiết hóa đơn không tồn tại!");
            }

            HoaDon hoaDon = optionalHoaDon.get();
            HoaDonChiTiet chiTiet = optionalChiTiet.get();

            if (hoaDon.getTrangThai() == 0 || hoaDon.getTrangThai() >= 4) {
                return ResponseEntity.badRequest().body("Không thể xóa sản phẩm trong trạng thái hiện tại của hóa đơn!");
            }

            ChiTietSanPham chiTietSanPham = chiTiet.getChiTietSanPham();
            chiTietSanPham.setSoLuong(chiTietSanPham.getSoLuong() + chiTiet.getSoLuong());
            chiTietSanPhamRepository.save(chiTietSanPham);

            BigDecimal tongTienHienTai = hoaDon.getTongTien();
            BigDecimal tienSanPham = chiTiet.getGia().multiply(BigDecimal.valueOf(chiTiet.getSoLuong()));
            hoaDon.setTongTien(tongTienHienTai.subtract(tienSanPham));
            hoaDonService.saveHoaDon(hoaDon);

            chiTietHoaDonRepository.delete(chiTiet);

            return ResponseEntity.ok("Xóa sản phẩm thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }
}