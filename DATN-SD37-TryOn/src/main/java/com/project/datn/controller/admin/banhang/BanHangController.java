package com.project.datn.controller.admin.banhang;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.DiaChi;
import com.project.datn.entity.HoaDon;
import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.entity.MaGiamGia;
import com.project.datn.entity.PhuongThucThanhToan;
import com.project.datn.model.request.banhang.DiaChiRequest;
import com.project.datn.model.request.banhang.HoaDonChiTietRequest;
import com.project.datn.model.request.banhang.HoaDonRequest;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.DiaChiRepository;
import com.project.datn.repository.HoaDonChiTietRepository;
import com.project.datn.repository.HoaDonRepository;
import com.project.datn.repository.MaGiamGiaRepository;
import com.project.datn.repository.PhuongThucThanhToanRepository;
import com.project.datn.service.banhang.IDiaChiService;
import com.project.datn.service.banhang.IHoaDonChiTietService;
import com.project.datn.service.banhang.IHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/banhang")
public class BanHangController {
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    IHoaDonService hoaDonService;

    @Autowired
    ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    IHoaDonChiTietService hoaDonChiTietService;

    @Autowired
    PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    MaGiamGiaRepository maGiamGiaRepository;

    @Autowired
    IDiaChiService diaChiService;

    @Autowired
    DiaChiRepository diaChiRepository;


    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        List<HoaDon> listHoaDon = hoaDonRepository.findByHoaDonTrangThai();
        model.addAttribute("hoadons", listHoaDon); // Load danh sách hóa đơn
        model.addAttribute("hoadon", new HoaDon()); // Tạo đối tượng hóa đơn mới để bind với form
        model.addAttribute("menuB", "menu");
        return new ModelAndView("/admin/banhang/banhang", model);
    }

    @PostMapping("/add")
    public ModelAndView addBIll(ModelMap model, @Valid @ModelAttribute("hoadon") HoaDon hoadon, BindingResult result) throws IOException {
        List<HoaDon> listHoaDon = hoaDonRepository.findByHoaDonTrangThai();
//      Kiểm tra số lượng hóa đơn
        if (listHoaDon.size() >= 5) {
            // Thêm thông báo lỗi nếu số lượng hóa đơn đã đủ 5
            model.addAttribute("error", "Không thể thêm mới hóa đơn vì đã đạt giới hạn 5 hóa đơn ");
            return new ModelAndView("forward:/admin/banhang", model); // Điều hướng về trang thêm hóa đơn cùng với lỗi
        } else {
            hoaDonService.addHoaDon(hoadon); // Thêm hóa đơn mới
            model.addAttribute("success", "Hóa đơn đã được thêm thành công!");
            // Truyền lại danh sách hóa đơn
            model.addAttribute("hoadons", listHoaDon);
            model.addAttribute("hoadon", new HoaDon()); // Reset form
            model.addAttribute("menuB", "menu");
            return new ModelAndView("forward:/admin/banhang", model);
        }
    }

    @GetMapping("/addgiohanghoadon")
    public ModelAndView addGioHangHoaDon(ModelMap model, @RequestParam("maHoaDon") String maHoaDon) throws IOException {
        List<PhuongThucThanhToan> listPhuongThucThanhToan = phuongThucThanhToanRepository.findAll();
        List<MaGiamGia> listMaGiamGia = maGiamGiaRepository.findAll();
        List<DiaChi> listDiaChi = diaChiRepository.findAll();

        model.addAttribute("listMaGiamGia", listMaGiamGia);
        model.addAttribute("listDiaChi", listDiaChi);
        model.addAttribute("listPhuongThucThanhToan", listPhuongThucThanhToan);
        model.addAttribute("hoadonchitiet", new HoaDonChiTietRequest());
        model.addAttribute("hoadonrq", new HoaDonRequest());
        model.addAttribute("diachirq", new DiaChiRequest());
        Pageable pageable = PageRequest.of(0, 5);
        Optional<HoaDon> listHoaDon = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);

        List<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonId(listHoaDon.get().getId());
        // tổng tiền
        double tongTien = listHoaDonChiTiet.stream()
                .mapToDouble(hoaDonChiTiet -> hoaDonChiTiet.getGia().doubleValue() * hoaDonChiTiet.getSoLuong())
                .sum();
        // tổng tiền cuối cùng cần thanh toán
        double tongTienCuoiCung;
        if (listHoaDon.get().getMaGiamGia() != null) {
            double giamgia = listHoaDon.get().getMaGiamGia().getGiaTriGiamGia().doubleValue();
            tongTienCuoiCung = tongTien - giamgia;
        } else {
            tongTienCuoiCung = tongTien;
        }

        model.addAttribute("tongTienCuoiCung", tongTienCuoiCung);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("listHoaDonChiTiet", listHoaDonChiTiet);
        Long idhoadons = listHoaDon.get().getId();
        model.addAttribute("idhoadons", idhoadons);
        if (!listHoaDon.isEmpty()) {
            model.addAttribute("selectedHoaDon", listHoaDon.get());
//            model.addAttribute("success", "Bạn đang ở hóa đơn: " + listHoaDon.get(0).getMaHoaDon());
        } else {
            model.addAttribute("error", "Mã hóa đơn không tồn tại.");
        }
        Page<ChiTietSanPham> list = chiTietSanPhamRepository.findAll(pageable);
        model.addAttribute("chitietsanpham", list);
        model.addAttribute("hoadons", hoaDonRepository.findByHoaDonTrangThai()); // Load tất cả hóa đơn
        return new ModelAndView("admin/banhang/banhang", model);
    }

    @PostMapping("/addgiohang")
    public ModelAndView addGioHang(RedirectAttributes redirectAttributes,
                                   @Valid @ModelAttribute("hoadonchitiet") HoaDonChiTietRequest dto,
                                   @RequestParam("idchitietsanpham") Long idchitietsanpham,
                                   @RequestParam("idhoadon") Long idhoadon,
                                   @RequestParam("maHoaDon") String maHoaDon) throws IOException {
        // Kiểm tra tồn tại id hóa đơn và sản phẩm
        if (idhoadon == null || idchitietsanpham == null) {
            redirectAttributes.addFlashAttribute("error", "ID hóa đơn hoặc sản phẩm không tồn tại");
            return new ModelAndView("redirect:/admin/banhang/addgiohanghoadon?maHoaDon=" + maHoaDon);
        }

        // Kiểm tra số lượng hợp lệ (phải lớn hơn 0)
        if (dto.getSoLuong() == null || dto.getSoLuong() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Số lượng không hợp lệ!");
            return new ModelAndView("redirect:/admin/banhang/addgiohanghoadon?maHoaDon=" + maHoaDon);
        }

        // Lấy thông tin sản phẩm (chi tiết sản phẩm) từ repository
        Optional<ChiTietSanPham> optionalChiTietSanPham = chiTietSanPhamRepository.findById(idchitietsanpham);
        if (!optionalChiTietSanPham.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
            return new ModelAndView("redirect:/admin/banhang/addgiohanghoadon?maHoaDon=" + maHoaDon);
        }
        ChiTietSanPham chiTietSanPham = optionalChiTietSanPham.get();

        // Lấy tất cả hóa đơn chi tiết của sản phẩm có id = idchitietsanpham
        List<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietRepository.findHoaDonChiTietByChiTietSanPhamId(idchitietsanpham);

        // Tính tổng số lượng đã có trong giỏ (cho sản phẩm đó) từ tất cả hóa đơn chi tiết
        int totalCartQuantity = 0;
        for (HoaDonChiTiet hct : listHoaDonChiTiet) {
            totalCartQuantity += hct.getSoLuong();
        }

        // Số lượng còn lại trong kho = tổng số lượng tồn kho - tổng số lượng đã có trong giỏ
        int soLuongConlai = chiTietSanPham.getSoLuong() - totalCartQuantity;

        // Kiểm tra số lượng nhập vào (sẽ cộng với tổng đã có) có vượt quá số lượng tồn kho không
        if (totalCartQuantity + dto.getSoLuong() > chiTietSanPham.getSoLuong()) {
            redirectAttributes.addFlashAttribute("error", "Số lượng vượt quá số lượng sản phẩm trong kho! Sản phẩm: "
                    + chiTietSanPham.getSanPham().getTen()
                    + " - chỉ có số lượng: " + chiTietSanPham.getSoLuong()
                    + " - số lượng còn lại: " + soLuongConlai);
            return new ModelAndView("redirect:/admin/banhang/addgiohanghoadon?maHoaDon=" + maHoaDon);
        }

        // Nếu đã có hóa đơn chi tiết cho sản phẩm này trong hóa đơn hiện tại, cập nhật
        Optional<HoaDonChiTiet> existingItem = hoaDonChiTietRepository.findByHoaDonIdAndChiTietSanPhamId(idhoadon, idchitietsanpham);
        if (existingItem.isPresent()) {
            int tongSoLuongMoi = existingItem.get().getSoLuong() + dto.getSoLuong();
            // Vì ta đã kiểm tra tổng số lượng với tất cả hóa đơn chi tiết rồi nên cập nhật trực tiếp
            hoaDonChiTietService.updateHoaDonChiTiet(idhoadon, idchitietsanpham, tongSoLuongMoi);
        } else {
            // Nếu chưa có sản phẩm nào trong giỏ của hóa đơn hiện tại, thêm mới vào giỏ hàng
            dto.setIdHoaDon(idhoadon);
            dto.setIdChiTietSanPham(idchitietsanpham);
            hoaDonChiTietService.addHoaDonChiTiet(dto);
        }

        redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm vào giỏ hàng thành công!");
        return new ModelAndView("redirect:/admin/banhang/addgiohanghoadon?maHoaDon=" + maHoaDon);
    }

    @PostMapping("/addgimagia")
    public ModelAndView thanhToanHoaDon(HttpServletRequest request, RedirectAttributes redirectAttributes,
                                        ModelMap model, @RequestParam("idhoadon") Long idhoadon,
                                        @RequestParam("idMaGiamGia") Long idMaGiamGia,
                                        @RequestParam("maHoaDon") String maHoaDon) throws IOException {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idhoadon);
        if (!optionalHoaDon.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Hóa đơn không tồn tại.");
        } else {
            HoaDon hd = optionalHoaDon.get();
            // Nếu hóa đơn đã có mã giảm giá
            if (hd.getMaGiamGia() != null) {
                if (hd.getMaGiamGia().getId().equals(idMaGiamGia)) {
                    redirectAttributes.addFlashAttribute("error", "Bạn đã áp dụng mã giảm giá này rồi.");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Hóa đơn chỉ áp dụng 1 mã giảm giá.");
                }
            } else {
                // Lấy hóa đơn theo mã hóa đơn
                Optional<HoaDon> listHoaDon = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);
                if (!listHoaDon.isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "Hóa đơn không tồn tại theo mã hóa đơn.");
                } else {
                    List<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonId(listHoaDon.get().getId());
                    // Tính tổng tiền (số tiền của từng chi tiết hóa đơn: đơn giá * số lượng)
                    double tongTien = listHoaDonChiTiet.stream()
                            .mapToDouble(hoaDonChiTiet -> hoaDonChiTiet.getGia().doubleValue() * hoaDonChiTiet.getSoLuong())
                            .sum();

                    // Lấy mã giảm giá từ repository (đảm bảo bạn đã @Autowired MaGiamGiaRepository)
                    Optional<MaGiamGia> optMaGiamGia = maGiamGiaRepository.findById(idMaGiamGia);
                    if (!optMaGiamGia.isPresent()) {
                        redirectAttributes.addFlashAttribute("error", "Mã giảm giá không tồn tại.");
                    } else {
                        BigDecimal discountValue = optMaGiamGia.get().getGiaTriGiamGia();
                        // So sánh discountValue với tổng tiền (chuyển tongTien thành BigDecimal)
                        if (discountValue.compareTo(BigDecimal.valueOf(tongTien)) > 0) {
                            redirectAttributes.addFlashAttribute("error", "Mã giảm giá này lớn hơn tổng tiền cần thanh toán.");
                        } else {
                            hoaDonService.addGiamgia(idhoadon, idMaGiamGia);
                            redirectAttributes.addFlashAttribute("success", "Mã giảm giá đã được áp dụng thành công!");
                        }
                    }
                }
            }
        }
        // Lấy đường dẫn trang trước đó từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }
        return new ModelAndView("redirect:" + referer, model);
    }

    @GetMapping("/xoamagiamgia/{idhoadon}")
    public ModelAndView xoaMaGiamGia(HttpServletRequest request,
                                     RedirectAttributes redirectAttributes,
                                     @PathVariable("idhoadon") Long idHoaDon) throws IOException {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (!optionalHoaDon.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Hóa đơn không tồn tại.");
        } else {
            HoaDon hoaDon = optionalHoaDon.get();
            if (hoaDon.getMaGiamGia() == null) {
                redirectAttributes.addFlashAttribute("error", "Không có mã giảm giá để xóa.");
            } else {
                // Xóa mã giảm giá bằng cách đặt null
                hoaDon.setMaGiamGia(null);
                hoaDonRepository.save(hoaDon);
                redirectAttributes.addFlashAttribute("success", "Xóa mã giảm giá thành công!");
            }
        }
        // Lấy đường dẫn trang trước đó từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }
        return new ModelAndView("redirect:" + referer);
    }

    @PostMapping("/thanhtoan")
    public ModelAndView thanhToan(HttpServletRequest request, RedirectAttributes redirectAttributes, ModelMap model, @Valid @ModelAttribute("hoadonrq") HoaDonRequest dto,
                                  @RequestParam("idhoadon") Long idHoaDon) throws IOException {
        // Lấy đường dẫn trang trước đó từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null) {
            referer = "/admin/banhang/banhang"; // Mặc định nếu không có referer
        }

        // Kiểm tra hóa đơn có tồn tại không
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (!optionalHoaDon.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Hóa đơn không tồn tại.");
            return new ModelAndView("redirect:" + referer);
        }
        // Kiểm tra id phương thức thanh toán có null không trước khi truy xuất
        if (dto.getIdPhuongThucThanhToan() == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn phương thức thanh toán!");
            return new ModelAndView("redirect:" + referer);
        }
        // Kiểm tra phương thức thanh toán có tồn tại không
        Optional<PhuongThucThanhToan> optionalPhuongThuc = phuongThucThanhToanRepository.findById(dto.getIdPhuongThucThanhToan());
        if (!optionalPhuongThuc.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Phương thức thanh toán không hợp lệ!");
            return new ModelAndView("redirect:" + referer);
        }
        if (optionalPhuongThuc.get().getId().equals(1L)) { // Thanh toán tiền mặt
            hoaDonService.thanhToan(idHoaDon, dto);
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
            return new ModelAndView("redirect:/admin/banhang");
        } else if (optionalPhuongThuc.get().getId().equals(2L)) { // Thanh toán chuyển khoản
            hoaDonService.thanhToan(idHoaDon, dto);
            redirectAttributes.addFlashAttribute("success", "Bạn hãy quét mã QR để thanh toán!");
            return new ModelAndView("redirect:/admin/banhang/trangchuyenkhoan/" + idHoaDon);
        } else {
            redirectAttributes.addFlashAttribute("error", "Thanh toán không thành công");
            return new ModelAndView("redirect:" + referer);
        }

    }

    @GetMapping("trangchuyenkhoan/{idhoadon}")
    public ModelAndView trangChuyenKhoan(@PathVariable("idhoadon") Long idHoaDon, ModelMap model) throws IOException {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            String maHoaDon = hoaDon.getMaHoaDon();
            model.addAttribute("tienThanhToan", hoaDon.getTongTienCuoiCung());
            model.addAttribute("hoadonrq", new HoaDonRequest());
            model.addAttribute("photo", "maqrthanhtoan.png");
            model.addAttribute("idHoaDon", hoaDon.getId());
            model.addAttribute("maHoaDon", maHoaDon);
            model.addAttribute("maHoaDon", hoaDon.getMaHoaDon()); // Lấy mã hóa đơn
        } else {
            model.addAttribute("error", "Không tìm thấy hóa đơn");
        }

        model.addAttribute("menuB", "menu");
        return new ModelAndView("/admin/banhang/trangchuyenkhoan", model);
    }

    @PostMapping("/trangchuyenkhoan")
    public ModelAndView chuyenKhoanThanhCong(RedirectAttributes redirectAttributes, @RequestParam("idhoadon") Long idHoaDon, ModelMap model) throws IOException {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByHoaDonId(idHoaDon);

        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            Optional<MaGiamGia> optionalMaGiamGia = Optional.empty(); // Mặc định là rỗng
            if (hoaDon.getMaGiamGia() != null) {
                optionalMaGiamGia = maGiamGiaRepository.findById(hoaDon.getMaGiamGia().getId());
            }
            hoaDon.setNgayCapNhap(new Date());
            hoaDon.setNgayThanhToan(new Date());
            hoaDon.setTrangThaiThanhToan(1);
            hoaDon.setTrangThai(4);
            hoaDonRepository.save(hoaDon);
            for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
                Optional<ChiTietSanPham> optionalChiTietSanPham = chiTietSanPhamRepository.findById(hoaDonChiTiet.getChiTietSanPham().getId());
                ChiTietSanPham chiTietSanPham = optionalChiTietSanPham.get();
                if (hoaDonChiTiet.getChiTietSanPham().getId() == chiTietSanPham.getId()) {
                    chiTietSanPham.setSoLuong(chiTietSanPham.getSoLuong() - hoaDonChiTiet.getSoLuong());
                    chiTietSanPhamRepository.save(chiTietSanPham);
                }
            }
            if (optionalMaGiamGia.isPresent()) {
                MaGiamGia maGiamGia = optionalMaGiamGia.get();
                maGiamGia.setSoLuong(maGiamGia.getSoLuong() - 1);
                maGiamGia.setNgayCapNhap(new Date());
                maGiamGiaRepository.save(maGiamGia);
            }
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
            return new ModelAndView("redirect:/admin/banhang");
        } else {
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
            return new ModelAndView("redirect:/admin/banhang");
        }
    }

    @PostMapping("/huychuyenkhoan")
    public ModelAndView huychuyenKhoan(HttpServletRequest request, RedirectAttributes redirectAttributes,
                                       @RequestParam("idhoadon") Long idHoaDon,
                                       @RequestParam("maHoaDon") String maHoaDon,
                                       ModelMap model) throws IOException {
        // Lấy đường dẫn trang trước đó từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null) {
            referer = "/admin/banhang/banhang"; // Mặc định nếu không có referer
        }
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            hoaDon.setNgayCapNhap(new Date());
            hoaDon.setNgayThanhToan(new Date());
            hoaDon.setTrangThaiThanhToan(0);
            hoaDon.setTrangThaiDonHang(null);
            hoaDon.setPhuongThucThanhToan(null);
            hoaDon.setTrangThai(1);
            hoaDonRepository.save(hoaDon);
            redirectAttributes.addFlashAttribute("success", "Huỷ thanh toán thành công!!");
            return new ModelAndView("redirect:/admin/banhang/addgiohanghoadon?maHoaDon=" + maHoaDon);
        } else {
            redirectAttributes.addFlashAttribute("error", "Huỷ thất bại!!");
            return new ModelAndView("redirect:" + referer);
        }
    }

    @PostMapping("/huyHoaDon")
    public ModelAndView huyHoaDon(HttpServletRequest request, RedirectAttributes redirectAttributes,
                                  @RequestParam("idhoadon") Long idHoaDon, ModelMap model) throws IOException {
        // Lấy đường dẫn trang trước đó từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null) {
            referer = "/admin/banhang/banhang"; // Mặc định nếu không có referer
        }
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            hoaDon.setNgayCapNhap(new Date());
            hoaDon.setTrangThai(0);
            hoaDonRepository.save(hoaDon);
            redirectAttributes.addFlashAttribute("success", "Bạn đã huỷ hoá đơn thành công!");
            return new ModelAndView("redirect:/admin/banhang");
        } else {
            redirectAttributes.addFlashAttribute("error", "Huỷ thất bại!!");
            return new ModelAndView("redirect:" + referer);
        }
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteHoaDonChiTiet(HttpServletRequest request,
                                            @PathVariable("id") Long idHoaDonChiTiet,
                                            RedirectAttributes redirectAttributes) {
        hoaDonChiTietService.deleteHoaDonChiTiet(idHoaDonChiTiet);
        redirectAttributes.addFlashAttribute("success", "Xoá sản phẩm thành công!");

        // Lấy URL trang trước từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }
        return new ModelAndView("redirect:" + referer);
    }

    @PostMapping("/addthongtinkhachhangmoi")
    public ModelAndView addThongTinKhachHangmoi(HttpServletRequest request, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("diachirq") DiaChiRequest dto,
                                                @RequestParam("idhoadon") Long idHoaDon) throws IOException {

        diaChiService.addiaChi(idHoaDon, dto);

        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }
        redirectAttributes.addFlashAttribute("success", "Bạn đã thêm thông tin khách hàng mới thành công!");
        return new ModelAndView("redirect:" + referer);
    }

    @PostMapping("/addthongtinkhachhangcu")
    public ModelAndView addThongTinKhachHangcu(HttpServletRequest request, RedirectAttributes redirectAttributes,
                                               ModelMap model, @RequestParam("idhoadon") Long idhoadon,
                                               @RequestParam("idDiachi") Long idDiachi) throws IOException {
        // Lấy mã giảm giá từ repository (đảm bảo bạn đã @Autowired MaGiamGiaRepository)
        Optional<DiaChi> optionalDiaChi = diaChiRepository.findById(idDiachi);
        if (!optionalDiaChi.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Thông tin khách hàng không tồn tại?");
        } else {
            diaChiService.addKhachHangCu(idhoadon, idDiachi);
            redirectAttributes.addFlashAttribute("success", "Thêm thông tin khách hàng thành công!");

        }
        // Lấy đường dẫn trang trước đó từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }
        return new ModelAndView("redirect:" + referer, model);

    }

    @GetMapping("/xoathongtinkhachhang/{idhoadon}")
    public ModelAndView xoaThongTinKhachHang(HttpServletRequest request,
                                             RedirectAttributes redirectAttributes,
                                             @PathVariable("idhoadon") Long idHoaDon) throws IOException {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (!optionalHoaDon.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Hóa đơn không tồn tại.");
        } else {
            HoaDon hoaDon = optionalHoaDon.get();
            if (hoaDon.getDiaChi() == null) {
                redirectAttributes.addFlashAttribute("error", "Không có Thông tin khách hàng để xóa.");
            } else {
                hoaDon.setDiaChi(null);
                hoaDonRepository.save(hoaDon);
                redirectAttributes.addFlashAttribute("success", "Xóa thông tin khách hàng thành công!");
            }
        }
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }
        return new ModelAndView("redirect:" + referer);
    }

}


