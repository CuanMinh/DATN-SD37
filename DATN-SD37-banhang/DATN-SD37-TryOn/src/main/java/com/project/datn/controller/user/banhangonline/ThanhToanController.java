package com.project.datn.controller.user.banhangonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.datn.config.payment.VNPAYConfig;
import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.DiaChi;
import com.project.datn.entity.GioHang;
import com.project.datn.entity.HinhAnh;
import com.project.datn.entity.HoaDon;
import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.entity.MaGiamGia;
import com.project.datn.entity.PhuongThucThanhToan;
import com.project.datn.entity.TaiKhoan;
import com.project.datn.model.request.banhang.DiaChiRequest;
import com.project.datn.model.request.banhang.HoaDonRequest;
import com.project.datn.payment.vnpay.PaymentDTO;
import com.project.datn.payment.vnpay.PaymentService;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.DiaChiRepository;
import com.project.datn.repository.GioHangRepository;
import com.project.datn.repository.HinhAnhRepository;
import com.project.datn.repository.HoaDonChiTietRepository;
import com.project.datn.repository.HoaDonRepository;
import com.project.datn.repository.MaGiamGiaRepository;
import com.project.datn.repository.PhuongThucThanhToanRepository;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.util.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("customer/thanhtoan")
public class ThanhToanController {
    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @Autowired
    MaGiamGiaRepository maGiamGiaRepository;

    @Autowired
    DiaChiRepository diaChiRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    HinhAnhRepository hinhAnhRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    PaymentService paymentService;

    @Autowired
    private VNPAYConfig vnPayConfig;




    @RequestMapping("")
    public ModelAndView list(@RequestParam(defaultValue = "0") int page, ModelMap model) {
        // Lấy thông tin đăng nhập từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            // Lấy thông tin tài khoản
            TaiKhoan taiKhoan = taiKhoanRepository.findByTen(username);
            model.addAttribute("taiKhoan", taiKhoan);
            String emailne = taiKhoan.getEmail();
            model.addAttribute("emailne", emailne);

            // Lấy danh sách địa chỉ theo ID tài khoản
            List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoan_Id(taiKhoan.getId());
            model.addAttribute("listDiaChi", listDiaChi);

            // phương thức thanh toán
            List<PhuongThucThanhToan> listPhuongThucThanhToan = phuongThucThanhToanRepository.findAll();
            model.addAttribute("listPhuongThucThanhToan", listPhuongThucThanhToan);

            // list giỏ hàng
            List<GioHang> listGiohang2 = gioHangRepository.findByTaiKhoan_Id(taiKhoan.getId());

            //tongTamTinh
            BigDecimal tongTamTinh = listGiohang2.stream()
                    .map(gioHang -> gioHang.getDonGia().multiply(BigDecimal.valueOf(gioHang.getSoLuong())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("tongTamTinh", tongTamTinh);

            //list giỏ hàng phân trang
            Pageable pageable = PageRequest.of(page, 3);
            Page<GioHang> listGiohang = gioHangRepository.findByTaiKhoan_Id(taiKhoan.getId(), pageable);
            model.addAttribute("listGiohang", listGiohang);

            // Lấy danh sách hình ảnh
            Map<Long, HinhAnh> hinhAnhMap = new HashMap<>();
            for (GioHang gioHang : listGiohang) {
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

            return new ModelAndView("/user/banhangonline/thanhtoanonline", model);
        }
        // Nếu chưa đăng nhập, chuyển hướng về trang login
        return new ModelAndView("redirect:/login", model);
    }
    @GetMapping("/thanhtoanthanhcong")
    public ModelAndView thanhToanThanhCong(@RequestParam("maHoaDon") String maHoaDon) {
        ModelAndView modelAndView = new ModelAndView("/user/banhangonline/trangdathangthanhcong");

        // Lấy thông tin hóa đơn từ database
        HoaDon hoaDon = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepository.findByHoaDonAllId(hoaDon.getId());
        DiaChi diaChi = hoaDon.getDiaChi();

        // Xử lý mã giảm giá
        BigDecimal giaTriGiamGia = BigDecimal.ZERO;
        if (hoaDon.getMaGiamGia() != null) {
            Optional<MaGiamGia> discount = maGiamGiaRepository.findById(hoaDon.getMaGiamGia().getId());
            if (discount.isPresent()) {
                MaGiamGia maGiamGia = discount.get();
                if ("Tiền mặt".equalsIgnoreCase(maGiamGia.getLoaiGiamGia())) {
                    giaTriGiamGia = maGiamGia.getGiaTriGiamGia();
                } else if ("Phần trăm".equalsIgnoreCase(maGiamGia.getLoaiGiamGia())) {
                    giaTriGiamGia = hoaDon.getTongTien().multiply(maGiamGia.getGiaTriGiamGia().divide(BigDecimal.valueOf(100)));
                }
            }
        }

        // Truyền dữ liệu sang view
        modelAndView.addObject("giaTriGiamGia", giaTriGiamGia);
        modelAndView.addObject("maHoaDon", hoaDon.getMaHoaDon());
        modelAndView.addObject("chiTietList", chiTietList);
        modelAndView.addObject("tongTien", hoaDon.getTongTien());
        modelAndView.addObject("phiVanChuyen", hoaDon.getPhiVanChuyen());
        modelAndView.addObject("tongTienCuoiCung", hoaDon.getTongTienCuoiCung());
        modelAndView.addObject("tenNguoiNhan", diaChi.getTen());
        modelAndView.addObject("soDienThoai", diaChi.getSoDienThoai());
        modelAndView.addObject("ghiChu", hoaDon.getGhiChu());
        modelAndView.addObject("diaChiGiaoHang", diaChi.getPhuongXa() + ", " + diaChi.getQuanHuyen() + ", " + diaChi.getThanhPho());
        modelAndView.addObject("phuongThucThanhToan", hoaDon.getPhuongThucThanhToan().getId().equals(1L) ? "Thanh toán khi nhận hàng (COD)" : "Chuyển khoản qua ngân hàng");

        return modelAndView;
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkDiscount(@RequestParam(required = false) String code, @RequestParam BigDecimal tongHoaDon) {
        // Nếu không nhập mã thì trả về 0đ ngay
        if (code == null || code.isEmpty()) {
            return ResponseEntity.ok(BigDecimal.ZERO);
        }

        Optional<MaGiamGia> discount = maGiamGiaRepository.findByTrangThaiAndTenOptional(1, code);

        if (discount.isPresent()) {
            MaGiamGia maGiamGia = discount.get();
            BigDecimal giaTriGiamGia = BigDecimal.ZERO;
            BigDecimal giaTriToiDa = maGiamGia.getGiaTriGiamGiaToiDa();
            NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            String giaTriFormatted = formatter.format(giaTriToiDa);

            // Nếu tổng hóa đơn không đủ điều kiện áp mã, thông báo lỗi
            if (tongHoaDon.compareTo(giaTriToiDa) < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Mã giảm giá này chỉ áp dụng cho hóa đơn trên: " + giaTriFormatted + "đ");
            }

            // Tính giá trị giảm giá
            if ("Tiền mặt".equalsIgnoreCase(maGiamGia.getLoaiGiamGia())) {
                giaTriGiamGia = maGiamGia.getGiaTriGiamGia();
            } else if ("Phần trăm".equalsIgnoreCase(maGiamGia.getLoaiGiamGia())) {
                giaTriGiamGia = tongHoaDon.multiply(maGiamGia.getGiaTriGiamGia().divide(BigDecimal.valueOf(100)));
            } else {
                return ResponseEntity.ok(BigDecimal.ZERO); // Nếu loại không hợp lệ thì cũng về 0đ
            }

            return ResponseEntity.ok(giaTriGiamGia);
        }

        // Nếu mã không tồn tại, trả về 0đ
        return ResponseEntity.ok(BigDecimal.ZERO);
    }

    @Transactional
    @PostMapping(value = "/thanhtoan", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> thanhToan(@RequestBody Map<String, Object> requestData,
                                       HttpServletRequest request) {
        try {
            // Lấy dữ liệu từ JSON
            ObjectMapper mapper = new ObjectMapper();
            HoaDonRequest hoaDonRequest = mapper.convertValue(requestData.get("hoadon"), HoaDonRequest.class);
            DiaChiRequest diaChiRequest = mapper.convertValue(requestData.get("diachi"), DiaChiRequest.class);
            Long idDiaChi = requestData.get("idDiaChi") != null ? Long.valueOf(requestData.get("idDiaChi").toString()) : null;
            Long idTaiKhoan = Long.valueOf(requestData.get("idTaiKhoan").toString());
            Long idPhuongThucThanhToan = Long.valueOf(requestData.get("idPhuongThucThanhToan").toString());
            String tenMaGiamGia = (String) requestData.get("tenMaGiamGia");

            // Log để kiểm tra
            System.out.println("HoaDonRequest: " + hoaDonRequest);
            System.out.println("DiaChiRequest: " + diaChiRequest);

            // Validate phương thức thanh toán
            if (idPhuongThucThanhToan == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Bạn chưa chọn phương thức thanh toán"));
            }

            // Validate giỏ hàng
            List<GioHang> gioHangList = gioHangRepository.findByTaiKhoan_Id(idTaiKhoan);
            if (gioHangList.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Giỏ hàng trống, không thể thanh toán"));
            }

            if (idPhuongThucThanhToan.equals(1L)) {
                // Tạo hóa đơn mới
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHoaDon("hd" + String.format("%06d", (int) (Math.random() * 1000000)));
                hoaDon.setNgayTaoHoaDon(new Date());
                hoaDon.setNgayDuKienGiaoHang(null);
                hoaDon.setNgayGiaoHang(null);
                hoaDon.setNgayThanhToan(null);
                hoaDon.setTongTien(hoaDonRequest.getTongTien());
                hoaDon.setTongTienCuoiCung(hoaDonRequest.getTongTienCuoiCung());
                hoaDon.setTrangThaiDonHang(1);
                hoaDon.setTrangThaiThanhToan(0);
                hoaDon.setPhiVanChuyen(hoaDonRequest.getPhiVanChuyen());
                hoaDon.setMaVanChuyen(hoaDonRequest.getMaVanChuyen());
                hoaDon.setGhiChu(hoaDonRequest.getGhiChu());
                hoaDon.setTrangThai(2);
                hoaDon.setTaiKhoan(TaiKhoan.builder().id(idTaiKhoan).build());

                // Xử lý mã giảm giá
                if (tenMaGiamGia != null && !tenMaGiamGia.isEmpty()) {
                    Optional<MaGiamGia> maGiamGiaOpt = maGiamGiaRepository.findByTenAndTrangThai(tenMaGiamGia, 1);
                    if (maGiamGiaOpt.isPresent()) {
                        hoaDon.setMaGiamGia(maGiamGiaOpt.get());
                    } else {
                        return ResponseEntity.badRequest().body(Map.of("error", "Mã giảm giá không hợp lệ hoặc không hoạt động"));
                    }
                } else {
                    hoaDon.setMaGiamGia(null);
                }

                // Xử lý địa chỉ
                if (idDiaChi != null) {
                    hoaDon.setDiaChi(DiaChi.builder().id(idDiaChi).build());
                } else {
                    DiaChi diaChi = new DiaChi();
                    diaChi.setTen(diaChiRequest.getTen());
                    diaChi.setSoDienThoai(diaChiRequest.getSoDienThoai());
                    diaChi.setThanhPho(diaChiRequest.getThanhPho());
                    diaChi.setQuanHuyen(diaChiRequest.getQuanHuyen());
                    diaChi.setPhuongXa(diaChiRequest.getPhuongXa());
                    diaChi.setNgayTao(new Date());
                    diaChi.setTrangThai(1);
                    diaChi.setTaiKhoan(TaiKhoan.builder().id(idTaiKhoan).build());

                    diaChiRepository.save(diaChi);
                    hoaDon.setDiaChi(diaChi);
                }

                hoaDon.setPhuongThucThanhToan(PhuongThucThanhToan.builder().id(idPhuongThucThanhToan).build());
                hoaDonRepository.save(hoaDon);

                // Tạo và lưu hóa đơn chi tiết
                for (GioHang gioHangItem : gioHangList) {
                    HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                    hoaDonChiTiet.setHoaDon(hoaDon);
                    hoaDonChiTiet.setSoLuong(gioHangItem.getSoLuong());
                    hoaDonChiTiet.setGia(gioHangItem.getDonGia());
                    hoaDonChiTiet.setChiTietSanPham(gioHangItem.getChiTietSanPham());
                    hoaDonChiTietRepository.save(hoaDonChiTiet);

                    ChiTietSanPham chiTietSanPham = gioHangItem.getChiTietSanPham();
                    if (chiTietSanPham != null) {
                        int newQuantity = chiTietSanPham.getSoLuong() - gioHangItem.getSoLuong();
                        if (newQuantity < 0) {
                            return ResponseEntity.badRequest().body(Map.of("error",
                                    "Sản phẩm " + chiTietSanPham.getSanPham().getTen() + " không đủ số lượng trong kho"));
                        }
                        chiTietSanPham.setSoLuong(newQuantity);
                        chiTietSanPhamRepository.save(chiTietSanPham);
                    }
                }

                // Xóa giỏ hàng sau khi đặt hàng thành công
                gioHangRepository.deleteByTaiKhoan_Id(idTaiKhoan);


                // Tạo URL chuyển hướng đến trang thanh toán thành công
                String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String redirectUrl = baseUrl + "/customer/thanhtoan/thanhtoanthanhcong?maHoaDon=" + hoaDon.getMaHoaDon();
                return ResponseEntity.ok(Map.of(
                        "message", "Đặt hàng thành công",
                        "maHoaDon", hoaDon.getMaHoaDon(),
                        "hoaDonId", hoaDon.getId(),
                        "redirectUrl", redirectUrl
                ));
            } else if (idPhuongThucThanhToan.equals(2L)) {
                // Tích hợp VNPAY cho thanh toán trực tuyến
                BigDecimal amount = hoaDonRequest.getTongTienCuoiCung(); // Lấy tổng tiền cuối cùng
                if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Số tiền thanh toán không hợp lệ"));
                }

                // Tạo hóa đơn tạm (chưa hoàn tất thanh toán)
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHoaDon("hd" + String.format("%06d", (int) (Math.random() * 1000000)));
                hoaDon.setNgayTaoHoaDon(new Date());
                hoaDon.setTongTien(hoaDonRequest.getTongTien());
                hoaDon.setTongTienCuoiCung(hoaDonRequest.getTongTienCuoiCung());
                hoaDon.setTrangThaiDonHang(1); // Chờ thanh toán
                hoaDon.setTrangThaiThanhToan(0); // Chưa thanh toán
                hoaDon.setPhiVanChuyen(hoaDonRequest.getPhiVanChuyen());
                hoaDon.setMaVanChuyen(hoaDonRequest.getMaVanChuyen());
                hoaDon.setGhiChu(hoaDonRequest.getGhiChu());
                hoaDon.setTrangThai(2); // Chờ xử lý
                hoaDon.setTaiKhoan(TaiKhoan.builder().id(idTaiKhoan).build());

                // Xử lý mã giảm giá
                if (tenMaGiamGia != null && !tenMaGiamGia.isEmpty()) {
                    Optional<MaGiamGia> maGiamGiaOpt = maGiamGiaRepository.findByTenAndTrangThai(tenMaGiamGia, 1);
                    if (maGiamGiaOpt.isPresent()) {
                        hoaDon.setMaGiamGia(maGiamGiaOpt.get());
                    } else {
                        return ResponseEntity.badRequest().body(Map.of("error", "Mã giảm giá không hợp lệ hoặc không hoạt động"));
                    }
                } else {
                    hoaDon.setMaGiamGia(null);
                }

                // Xử lý địa chỉ
                if (idDiaChi != null) {
                    hoaDon.setDiaChi(DiaChi.builder().id(idDiaChi).build());
                } else {
                    DiaChi diaChi = new DiaChi();
                    diaChi.setTen(diaChiRequest.getTen());
                    diaChi.setSoDienThoai(diaChiRequest.getSoDienThoai());
                    diaChi.setThanhPho(diaChiRequest.getThanhPho());
                    diaChi.setQuanHuyen(diaChiRequest.getQuanHuyen());
                    diaChi.setPhuongXa(diaChiRequest.getPhuongXa());
                    diaChi.setNgayTao(new Date());
                    diaChi.setTrangThai(1);
                    diaChi.setTaiKhoan(TaiKhoan.builder().id(idTaiKhoan).build());

                    diaChiRepository.save(diaChi);
                    hoaDon.setDiaChi(diaChi);
                }

                hoaDon.setPhuongThucThanhToan(PhuongThucThanhToan.builder().id(idPhuongThucThanhToan).build());
                hoaDonRepository.save(hoaDon);

                // Chuẩn bị tham số cho VNPAY
                Map<String, String> vnpParams = vnPayConfig.getVNPayConfig();
                vnpParams.put("vnp_Amount", String.valueOf(amount.multiply(BigDecimal.valueOf(100)).longValue())); // Nhân với 100 vì VNPAY dùng đơn vị nhỏ nhất (VNĐ)
                vnpParams.put("vnp_TxnRef", "TXN" + hoaDonRequest.getTongTienCuoiCung().toString() + System.currentTimeMillis()); // Mã giao dịch duy nhất
                vnpParams.put("vnp_OrderInfo", "Thanh toan don hang: " + hoaDon.getMaHoaDon());
                vnpParams.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
                vnpParams.put("vnp_BankCode", "NCB"); // Thêm bank code để buộc hiển thị trang thanh toán

                // Tạo URL thanh toán
                String queryUrl = VNPayUtil.getPaymentURL(vnpParams, true);
                String hashData = VNPayUtil.getPaymentURL(vnpParams, false);
                String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
                String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl + "&vnp_SecureHash=" + vnpSecureHash;

                // Log để debug
                System.out.println("Payment URL: " + paymentUrl);
                System.out.println("vnp_Amount: " + vnpParams.get("vnp_Amount"));

                // Trả về URL thanh toán
                return ResponseEntity.ok(Map.of(
                        "message", "Chuyển hướng đến trang thanh toán VNPAY",
                        "paymentUrl", paymentUrl,
                        "maHoaDon", hoaDon.getMaHoaDon(),
                        "hoaDonId", hoaDon.getId()
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Phương thức thanh toán không hợp lệ"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi: " + e.getMessage()));
        }
    }
}
