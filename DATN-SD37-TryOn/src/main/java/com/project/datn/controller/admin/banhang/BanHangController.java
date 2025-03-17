package com.project.datn.controller.admin.banhang;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.DiaChi;
import com.project.datn.entity.HinhAnh;
import com.project.datn.entity.HoaDon;
import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.entity.MaGiamGia;
import com.project.datn.entity.PhuongThucThanhToan;
import com.project.datn.model.request.banhang.DiaChiRequest;
import com.project.datn.model.request.banhang.HoaDonChiTietRequest;
import com.project.datn.model.request.banhang.HoaDonRequest;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.DiaChiRepository;
import com.project.datn.repository.HinhAnhRepository;
import com.project.datn.repository.HoaDonChiTietRepository;
import com.project.datn.repository.HoaDonRepository;
import com.project.datn.repository.MaGiamGiaRepository;
import com.project.datn.repository.PhuongThucThanhToanRepository;
import com.project.datn.service.banhang.IDiaChiService;
import com.project.datn.service.banhang.IHoaDonChiTietService;
import com.project.datn.service.banhang.IHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    HinhAnhRepository hinhAnhRepository;


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
    public ModelAndView addGioHangHoaDon(ModelMap model, @RequestParam("maHoaDon") String maHoaDon,
                                         @RequestParam(value = "page", defaultValue = "0") int page) throws IOException {

        model.addAttribute("hoadonchitiet", new HoaDonChiTietRequest());
        model.addAttribute("hoadonrq", new HoaDonRequest());
        model.addAttribute("diachirq", new DiaChiRequest());
        model.addAttribute("hoadons", hoaDonRepository.findByHoaDonTrangThai()); // Load tất cả hóa đơn

        // phương thức thanh toán
        List<PhuongThucThanhToan> listPhuongThucThanhToan = phuongThucThanhToanRepository.findAll();
        model.addAttribute("listPhuongThucThanhToan", listPhuongThucThanhToan);

        // mã giảm giá
        Pageable pageableGiamGia = PageRequest.of(0, 1);
        Page<MaGiamGia> listMaGiamGia = maGiamGiaRepository.findByTrangThaiAllMaGiamGia(1, pageableGiamGia);
        model.addAttribute("listMaGiamGia", listMaGiamGia);

        // địa chỉ
        Pageable pageableDiaChi = PageRequest.of(0, 1);
        Page<DiaChi> listDiaChi = diaChiRepository.findByTrangThaiAllDiaChi(1, pageableDiaChi);
        model.addAttribute("listDiaChi", listDiaChi);

        // chi tiết sản phẩm
        Pageable pageableSanPham = PageRequest.of(0, 3);
        Page<ChiTietSanPham> list = chiTietSanPhamRepository.findByTrangThai(1, pageableSanPham);
        model.addAttribute("chitietsanpham", list);

        // Lấy danh sách hình ảnh theo id của từng ProductDetail nhưng chỉ lấy 1 ảnh duy nhất
        Map<Long, HinhAnh> hinhAnh = list.getContent().stream()
                .collect(Collectors.toMap(
                        productDetail -> productDetail.getId(), // Key: productDetailId
                        productDetail -> hinhAnhRepository.findTop1BySanPham_Id(productDetail.getSanPham().getId())
                                .stream().findFirst().orElse(null), // Lấy ảnh đầu tiên
                        (existing, replacement) -> existing // Nếu có trùng key thì giữ nguyên
                ));
        model.addAttribute("hinhAnh", hinhAnh);

        // hoá đơn
        Optional<HoaDon> listHoaDon = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);
        Pageable pageableGioHang = PageRequest.of(page, 3);
        Page<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonId(listHoaDon.get().getId(), pageableGioHang);
        model.addAttribute("listHoaDonChiTiet", listHoaDonChiTiet);

        // Lấy danh sách hình ảnh theo id của từng ProductDetail nhưng chỉ lấy 1 ảnh duy nhất
        Map<Long, HinhAnh> hinhAnhMap = listHoaDonChiTiet.getContent().stream()
                .collect(Collectors.toMap(
                        hoaDonChiTiet -> hoaDonChiTiet.getChiTietSanPham().getId(), // Key: id của ChiTietSanPham
                        hoaDonChiTiet -> hinhAnhRepository.findTop1BySanPham_Id(hoaDonChiTiet.getChiTietSanPham().getSanPham().getId())
                                .stream().findFirst().orElse(null), // Lấy ảnh đầu tiên nếu có
                        (existing, replacement) -> existing // Nếu có trùng key thì giữ nguyên ảnh cũ
                ));

        model.addAttribute("hinhAnhgiohang", hinhAnhMap);

        // tổng tiền
        List<HoaDonChiTiet> listHoaDonChiTietTT = hoaDonChiTietRepository.findByHoaDonAllId(listHoaDon.get().getId());
        double tongTien = listHoaDonChiTietTT.stream()
                .mapToDouble(hoaDonChiTiet -> hoaDonChiTiet.getGia().doubleValue() * hoaDonChiTiet.getSoLuong())
                .sum();
        // tổng tiền cuối cùng cần thanh toán
        double tongTienCuoiCung;
        if (listHoaDon.get().getMaGiamGia() != null) {
            double giamgia = listHoaDon.get().getMaGiamGia().getGiaTriGiamGia().doubleValue();
            String loaiGiamGia = listHoaDon.get().getMaGiamGia().getLoaiGiamGia();
            if (loaiGiamGia.equals("Tiền mặt")) {
                tongTienCuoiCung = tongTien - giamgia;
                model.addAttribute("tiemGiamGia", giamgia);
            } else {
                double discountAmount = tongTien * (giamgia / 100.0);
                tongTienCuoiCung = tongTien - discountAmount;
                model.addAttribute("tiemGiamGia", discountAmount);
            }
        } else {
            tongTienCuoiCung = tongTien;
        }
        model.addAttribute("tongTienCuoiCung", tongTienCuoiCung);
        model.addAttribute("tongTien", tongTien);

        Long idhoadons = listHoaDon.get().getId();
        model.addAttribute("idhoadons", idhoadons);
        if (!listHoaDon.isEmpty()) {
            model.addAttribute("selectedHoaDon", listHoaDon.get());
//            model.addAttribute("success", "Bạn đang ở hóa đơn: " + listHoaDon.get(0).getMaHoaDon());
        } else {
            model.addAttribute("error", "Mã hóa đơn không tồn tại.");
        }

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
//            int tongSoLuongMoi = dto.getSoLuong();
            hoaDonChiTietService.updateHoaDonChiTiet(idhoadon, idchitietsanpham, tongSoLuongMoi);

            Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idhoadon);
            HoaDon hoaDon = optionalHoaDon.get();
            // Tính lại tổng tiền của hóa đơn sau khi xóa sản phẩm
            List<HoaDonChiTiet> listHoaDonChiTiet2 = hoaDonChiTietRepository.findByHoaDonAllId(idhoadon);
            double tongTien = listHoaDonChiTiet2.stream()
                    .mapToDouble(item -> item.getGia().doubleValue() * item.getSoLuong())
                    .sum();
            // Kiểm tra nếu hóa đơn có mã giảm giá
            if (hoaDon.getMaGiamGia() != null) {
                BigDecimal giaTriGiamGia = hoaDon.getMaGiamGia().getGiaTriGiamGiaToiDa();
                // So sánh giaTriGiamGia với tongTien (chuyển tongTien thành BigDecimal)
                if (BigDecimal.valueOf(tongTien).compareTo(giaTriGiamGia) < 0) {
                    // Xóa mã giảm giá khỏi hóa đơn
                    hoaDon.setMaGiamGia(null);
                    hoaDonRepository.save(hoaDon);
                }
            }
        } else {
            // Nếu chưa có sản phẩm nào trong giỏ của hóa đơn hiện tại, thêm mới vào giỏ hàng
            dto.setIdHoaDon(idhoadon);
            dto.setIdChiTietSanPham(idchitietsanpham);
            hoaDonChiTietService.addHoaDonChiTiet(dto);
        }

        redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm vào giỏ hàng thành công!");
        return new ModelAndView("redirect:/admin/banhang/addgiohanghoadon?maHoaDon=" + maHoaDon);
    }
    @PostMapping("/congsoluong/{id}")
    public ModelAndView congSoLuongHoaDonChiTiet(HttpServletRequest request,
                                                 @PathVariable("id") Long idHoaDonChiTiet,
                                                 RedirectAttributes redirectAttributes) {
        // Lấy URL trang trước từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }

        // Lấy chi tiết hóa đơn cần cập nhật
        Optional<HoaDonChiTiet> optionalHoaDonChiTiet = hoaDonChiTietRepository.findById(idHoaDonChiTiet);
        if (!optionalHoaDonChiTiet.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Chi tiết hóa đơn không tồn tại!");
            return new ModelAndView("redirect:" + referer);
        }
        HoaDonChiTiet hoaDonChiTiet = optionalHoaDonChiTiet.get();

        // Tăng số lượng lên 1
        int newQuantity = hoaDonChiTiet.getSoLuong() + 1;
        hoaDonChiTiet.setSoLuong(newQuantity);

        // Lấy thông tin chi tiết sản phẩm từ repository
        Optional<ChiTietSanPham> optionalChiTietSanPham = chiTietSanPhamRepository.findById(
                hoaDonChiTiet.getChiTietSanPham().getId());
        if (!optionalChiTietSanPham.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Chi tiết sản phẩm không tồn tại!");
            return new ModelAndView("redirect:" + referer);
        }
        ChiTietSanPham chiTietSanPham = optionalChiTietSanPham.get();

        // Lấy tất cả hóa đơn chi tiết của sản phẩm đó
        List<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietRepository.findHoaDonChiTietByChiTietSanPhamId(
                hoaDonChiTiet.getChiTietSanPham().getId());

        // Tính tổng số lượng hiện có trong giỏ cho sản phẩm đó
        int totalCartQuantity = listHoaDonChiTiet.stream().mapToInt(HoaDonChiTiet::getSoLuong).sum();

        // Kiểm tra nếu tổng số lượng vượt quá số lượng tồn kho
        if (totalCartQuantity > chiTietSanPham.getSoLuong()) {
            redirectAttributes.addFlashAttribute("error", "Số lượng vượt quá số lượng sản phẩm trong kho! Sản phẩm: "
                    + chiTietSanPham.getSanPham().getTen()
                    + " - chỉ có số lượng: " + chiTietSanPham.getSoLuong()
                    + " - số lượng còn lại: " + "0");
            return new ModelAndView("redirect:" + referer);
        } else {
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }
        return new ModelAndView("redirect:" + referer);
    }

    @PostMapping("/trusoluong/{id}")
    public ModelAndView truSoLuongHoaDonChiTiet(HttpServletRequest request,
                                                @PathVariable("id") Long idHoaDonChiTiet,
                                                RedirectAttributes redirectAttributes) {
        // Lấy URL trang trước từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }

        // Lấy chi tiết hóa đơn cần cập nhật
        Optional<HoaDonChiTiet> optionalHoaDonChiTiet = hoaDonChiTietRepository.findById(idHoaDonChiTiet);
        if (!optionalHoaDonChiTiet.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Chi tiết hóa đơn không tồn tại!");
            return new ModelAndView("redirect:" + referer);
        }
        HoaDonChiTiet hoaDonChiTiet = optionalHoaDonChiTiet.get();

        // Nếu số lượng > 1, trừ đi 1 và cập nhật
        if (hoaDonChiTiet.getSoLuong() > 1) {
            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() - 1);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        } else {
            // Nếu số lượng bằng 1, sau khi trừ sẽ bằng 0 nên xoá luôn chi tiết hóa đơn
            hoaDonChiTietService.deleteHoaDonChiTiet(idHoaDonChiTiet);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã bị xoá khỏi giỏ hàng do số lượng đạt 0!");
        }
        return new ModelAndView("redirect:" + referer);
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
                    List<HoaDonChiTiet> listHoaDonChiTietTT = hoaDonChiTietRepository.findByHoaDonAllId(listHoaDon.get().getId());
                    // Tính tổng tiền (số tiền của từng chi tiết hóa đơn: đơn giá * số lượng)
                    double tongTien = listHoaDonChiTietTT.stream()
                            .mapToDouble(hoaDonChiTiet -> hoaDonChiTiet.getGia().doubleValue() * hoaDonChiTiet.getSoLuong())
                            .sum();

                    // Lấy mã giảm giá từ repository (đảm bảo bạn đã @Autowired MaGiamGiaRepository)
                    Optional<MaGiamGia> optMaGiamGia = maGiamGiaRepository.findById(idMaGiamGia);
                    if (!optMaGiamGia.isPresent()) {
                        redirectAttributes.addFlashAttribute("error", "Mã giảm giá không tồn tại.");
                    } else {
                        BigDecimal discountValue = optMaGiamGia.get().getGiaTriGiamGiaToiDa();
                        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
                        String formattedDiscountValue = numberFormat.format(discountValue) + " VND";
                        if (BigDecimal.valueOf(tongTien).compareTo(discountValue) < 0) {
                            redirectAttributes.addFlashAttribute("error", "Mã giảm giá này chỉ áp dụng cho hoá đơn trên:" + formattedDiscountValue);
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
        // Kiểm tra giỏ hàng (HoaDonChiTiet) của hóa đơn có sản phẩm hay không
        List<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonAllId(idHoaDon);
        if (listHoaDonChiTiet == null || listHoaDonChiTiet.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm chưa có trong giỏ hàng để thanh toán.");
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
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByHoaDonAllId(idHoaDon);

        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            Optional<MaGiamGia> optionalMaGiamGia = Optional.empty(); // Mặc định là rỗng
            if (hoaDon.getMaGiamGia() != null) {
                optionalMaGiamGia = maGiamGiaRepository.findById(hoaDon.getMaGiamGia().getId());
            }
            hoaDon.setNgayCapNhap(new Date());
            hoaDon.setNgayThanhToan(new Date());
            hoaDon.setTrangThaiThanhToan(1);
            hoaDon.setTrangThai(5);
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
//                maGiamGia.setNgayCapNhap(new Date());
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
        // Lấy chi tiết hóa đơn cần xóa
        Optional<HoaDonChiTiet> optionalHoaDonChiTiet = hoaDonChiTietRepository.findById(idHoaDonChiTiet);
        if (!optionalHoaDonChiTiet.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Chi tiết hóa đơn không tồn tại!");
            String referer = request.getHeader("Referer");
            if (referer == null || referer.isEmpty()) {
                referer = "/admin/banhang/banhang"; // fallback nếu không có referer
            }
            return new ModelAndView("redirect:" + referer);
        }

        HoaDonChiTiet hoaDonChiTiet = optionalHoaDonChiTiet.get();
        Long idHoaDon = hoaDonChiTiet.getHoaDon().getId();

        // Xóa sản phẩm khỏi giỏ hàng
        hoaDonChiTietService.deleteHoaDonChiTiet(idHoaDonChiTiet);
        redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");

        // Lấy hóa đơn liên quan
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();

            // Tính lại tổng tiền của hóa đơn sau khi xóa sản phẩm
            List<HoaDonChiTiet> listHoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonAllId(idHoaDon);
            double tongTien = listHoaDonChiTiet.stream()
                    .mapToDouble(item -> item.getGia().doubleValue() * item.getSoLuong())
                    .sum();

            // Kiểm tra nếu hóa đơn có mã giảm giá
            if (hoaDon.getMaGiamGia() != null) {
                BigDecimal giaTriGiamGia = hoaDon.getMaGiamGia().getGiaTriGiamGiaToiDa();
                // So sánh giaTriGiamGia với tongTien (chuyển tongTien thành BigDecimal)
                if (BigDecimal.valueOf(tongTien).compareTo(giaTriGiamGia) < 0) {
                    // Xóa mã giảm giá khỏi hóa đơn
                    hoaDon.setMaGiamGia(null);
                    hoaDonRepository.save(hoaDon);
                    redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
                }
            }
        }

        // Lấy URL trang trước từ header "Referer"
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
        }
        return new ModelAndView("redirect:" + referer);
    }

    //    @PostMapping("/addthongtinkhachhangmoi")
//    public ModelAndView addThongTinKhachHangmoi(HttpServletRequest request,
//                                                @Valid @ModelAttribute("diachirq") DiaChiRequest dto,
//                                                BindingResult bindingResult,
//                                                RedirectAttributes redirectAttributes,
//                                                @RequestParam("idhoadon") Long idHoaDon) throws IOException {
//
//        // Kiểm tra nếu tên rỗng (dù đã dùng @NotBlank ở DTO, ta kiểm tra thêm ở đây nếu cần)
//        if (dto.getTen() == null || dto.getTen().trim().isEmpty()) {
//            bindingResult.rejectValue("ten", "error.ten", "Tên không được để trống.");
//        }
//
//        // Kiểm tra số điện thoại: không được rỗng và phải đúng 10 chữ số bắt đầu bằng 0
//        if (dto.getSoDienThoai() == null || !dto.getSoDienThoai().matches("^0\\d{9}$")) {
//            bindingResult.rejectValue("soDienThoai", "error.soDienThoai",
//                    "Số điện thoại phải đúng 10 chữ số và bắt đầu bằng số 0.");
//        }
//
//        // Nếu có lỗi validation, trả về lại form và hiển thị lỗi
//        if (bindingResult.hasErrors()) {
//            ModelMap model = new ModelMap();
//            model.addAttribute("idhoadons", idHoaDon);
//            model.addAttribute("diachirq", dto);
//            return new ModelAndView("admin/banhang/banhang :: modalFragment", model);
//        }
//
//        diaChiService.addiaChi(idHoaDon, dto);
//        String referer = request.getHeader("Referer");
//        if (referer == null || referer.isEmpty()) {
//            referer = "/admin/banhang/banhang"; // fallback nếu không có referer
//        }
//        redirectAttributes.addFlashAttribute("success", "Bạn đã thêm thông tin khách hàng mới thành công!");
//        return new ModelAndView("redirect:" + referer);
//    }
    @PostMapping("/addthongtinkhachhangmoi")
    public ResponseEntity<Map<String, Object>> addThongTinKhachHangmoi(
            @Valid @ModelAttribute("diachirq") DiaChiRequest dto,
            BindingResult bindingResult,
            @RequestParam("idhoadon") Long idHoaDon,
            HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();

        // Kiểm tra lỗi validation
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            response.put("status", "error");
            response.put("errors", errors);
            return ResponseEntity.ok(response);
        }

        // Xử lý khi không có lỗi
        diaChiService.addiaChi(idHoaDon, dto);

        // Lấy trang trước đó (referer)
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/admin/banhang/banhang"; // Fallback nếu không có referer
        }

        response.put("status", "success");
        response.put("success", "Bạn đã thêm thông tin khách hàng mới thành công!");
        response.put("redirect", referer); // Chuyển hướng trang

        return ResponseEntity.ok(response);
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

    @GetMapping("/sanpham-modal")
    public ModelAndView loadSanPhamModal(@RequestParam("maHoaDon") String maHoaDon,
                                         @RequestParam(value = "pageSanPham", defaultValue = "0") int pageSanPham,
                                         ModelMap model) {
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);
        if (!hoaDonOpt.isPresent()) {
            model.addAttribute("error", "Hóa đơn không tồn tại");
            return new ModelAndView("admin/banhang/modalsanpham :: modalError", model);
        }
        HoaDon hoaDon = hoaDonOpt.get();
        model.addAttribute("selectedHoaDon", hoaDon);

        Pageable pageable = PageRequest.of(pageSanPham, 3); // 5 sản phẩm mỗi trang
        Page<ChiTietSanPham> sanPhamPage = chiTietSanPhamRepository.findByTrangThai(1, pageable);

        // Lấy danh sách hình ảnh theo id của từng ProductDetail nhưng chỉ lấy 1 ảnh duy nhất
        Map<Long, HinhAnh> hinhAnh = sanPhamPage.getContent().stream()
                .collect(Collectors.toMap(
                        productDetail -> productDetail.getId(), // Key: productDetailId
                        productDetail -> hinhAnhRepository.findTop1BySanPham_Id(productDetail.getSanPham().getId())
                                .stream().findFirst().orElse(null), // Lấy ảnh đầu tiên
                        (existing, replacement) -> existing // Nếu có trùng key thì giữ nguyên
                ));
        model.addAttribute("hinhAnh", hinhAnh);

        model.addAttribute("chitietsanpham", sanPhamPage);
        return new ModelAndView("admin/banhang/modalsanpham :: modalContent", model);
    }

    @GetMapping("/giamgia-modal")
    public ModelAndView loadGiamGiaModal(@RequestParam("maHoaDon") String maHoaDon,
                                         @RequestParam(value = "pageGiamGia", defaultValue = "0") int pageGiamGia,
                                         ModelMap model) {
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);
        if (!hoaDonOpt.isPresent()) {
            model.addAttribute("error", "Hóa đơn không tồn tại");
            // Trả về fragment thông báo lỗi (nếu có)
            return new ModelAndView("admin/banhang/modalgiamgia :: modalError", model);
        }
        HoaDon hoaDon = hoaDonOpt.get();
        model.addAttribute("selectedHoaDon", hoaDon);

        Pageable pageableGiamGia = PageRequest.of(pageGiamGia, 1);
        Page<MaGiamGia> giamgiaPage = maGiamGiaRepository.findByTrangThaiAllMaGiamGia(1, pageableGiamGia);
        model.addAttribute("listMaGiamGia", giamgiaPage);
        return new ModelAndView("admin/banhang/modalgiamgia :: modalContent", model);
    }

    @GetMapping("/khachhangmodal")
    public ModelAndView loadkhachhangModal(@RequestParam("maHoaDon") String maHoaDon,
                                           @RequestParam(value = "pageKhachHang", defaultValue = "0") int pageKhachHang,
                                           ModelMap model) {
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);
        if (!hoaDonOpt.isPresent()) {
            model.addAttribute("error", "Hóa đơn không tồn tại");
            return new ModelAndView("admin/banhang/modalkhachhang :: modalError", model);
        }
        HoaDon hoaDon = hoaDonOpt.get();
        model.addAttribute("selectedHoaDon", hoaDon);

        // Sử dụng biến pageKhachHang để phân trang (2 địa chỉ mỗi trang)
        Pageable pageableDiaChi = PageRequest.of(pageKhachHang, 1); // Đặt page size giống sản phẩm (2)
        Page<DiaChi> listDiaChi = diaChiRepository.findByTrangThaiAllDiaChi(1, pageableDiaChi);
        // Đảm bảo luôn trả về Page, kể cả khi rỗng
        if (listDiaChi == null) {
            listDiaChi = new PageImpl<>(new ArrayList<>(), pageableDiaChi, 0);
        }
        model.addAttribute("listDiaChi", listDiaChi);
        return new ModelAndView("admin/banhang/modalkhachhang :: modalContent", model);
    }

    @GetMapping("/giamgia-modal/search")
    public ModelAndView searchGiamGiaModal(@RequestParam("maHoaDon") String maHoaDon,
                                           @RequestParam("ten") String ten,
                                           @RequestParam(value = "pageGiamGia", defaultValue = "0") int pageGiamGia,
                                           ModelMap model) {
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);
        if (!hoaDonOpt.isPresent()) {
            model.addAttribute("error", "Hóa đơn không tồn tại");
            return new ModelAndView("admin/banhang/modalgiamgia :: modalError", model);
        }
        HoaDon hoaDon = hoaDonOpt.get();
        model.addAttribute("selectedHoaDon", hoaDon);

        Pageable pageableGiamGia = PageRequest.of(pageGiamGia, 1); // 2 mã giảm giá mỗi trang
        List<MaGiamGia> giamGiaList = maGiamGiaRepository.findByTrangThaiAndTenContainingIgnoreCase(1, ten); // Tìm kiếm theo tên, không phân biệt hoa thường
        int start = (int) pageableGiamGia.getOffset();
        int end = Math.min((start + pageableGiamGia.getPageSize()), giamGiaList.size());
        Page<MaGiamGia> giamgiaPage = new PageImpl<>(giamGiaList.subList(start, end), pageableGiamGia, giamGiaList.size());

        if (giamgiaPage == null || giamgiaPage.getContent().isEmpty()) {
            giamgiaPage = new PageImpl<>(new ArrayList<>(), pageableGiamGia, 0);
        }
        model.addAttribute("listMaGiamGia", giamgiaPage);
        return new ModelAndView("admin/banhang/modalgiamgia :: modalContent", model);
    }

    @GetMapping("/khachhangmodal/search")
    public ModelAndView searchDiaChiModal(@RequestParam("maHoaDon") String maHoaDon,
                                          @RequestParam("query") String query,
                                          @RequestParam(value = "pageKhachHang", defaultValue = "0") int pageKhachHang,
                                          ModelMap model) {
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);
        if (!hoaDonOpt.isPresent()) {
            model.addAttribute("error", "Hóa đơn không tồn tại");
            return new ModelAndView("admin/banhang/modalkhachhang :: modalError", model);
        }
        HoaDon hoaDon = hoaDonOpt.get();
        model.addAttribute("selectedHoaDon", hoaDon);

        Pageable pageableDiaChi = PageRequest.of(pageKhachHang, 1); // 2 địa chỉ mỗi trang
        List<DiaChi> diaChiList = diaChiRepository.findByTenContainingIgnoreCaseOrSoDienThoaiContainingIgnoreCase(query); // Tìm kiếm theo tên hoặc số điện thoại
        int start = (int) pageableDiaChi.getOffset();
        int end = Math.min((start + pageableDiaChi.getPageSize()), diaChiList.size());
        Page<DiaChi> diaChiPage = new PageImpl<>(diaChiList.subList(start, end), pageableDiaChi, diaChiList.size());

        if (diaChiPage == null || diaChiPage.getContent().isEmpty()) {
            diaChiPage = new PageImpl<>(new ArrayList<>(), pageableDiaChi, 0);
        }
        model.addAttribute("listDiaChi", diaChiPage);
        return new ModelAndView("admin/banhang/modalkhachhang :: modalContent", model);
    }

    @GetMapping("/sanpham-modal/search")
    public ModelAndView searchSanPhamModal(@RequestParam("maHoaDon") String maHoaDon,
                                           @RequestParam("query") String query,
                                           @RequestParam(value = "pageSanPham", defaultValue = "0") int pageSanPham,
                                           ModelMap model) {
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon);
        if (!hoaDonOpt.isPresent()) {
            model.addAttribute("error", "Hóa đơn không tồn tại");
            return new ModelAndView("admin/banhang/modalsanpham :: modalError", model);
        }
        HoaDon hoaDon = hoaDonOpt.get();
        model.addAttribute("selectedHoaDon", hoaDon);

        Pageable pageable = PageRequest.of(pageSanPham, 3); // 1 sản phẩm mỗi trang (giữ nguyên như hiện tại)
        List<ChiTietSanPham> chiTietSanPhamList = chiTietSanPhamRepository.findByTrangThaiAndSanPhamTenOrMaContainingIgnoreCase(query);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), chiTietSanPhamList.size());
        Page<ChiTietSanPham> sanPhamPage = new PageImpl<>(chiTietSanPhamList.subList(start, end), pageable, chiTietSanPhamList.size());

        if (sanPhamPage == null || sanPhamPage.getContent().isEmpty()) {
            sanPhamPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        // Lấy danh sách hình ảnh theo id của từng ProductDetail nhưng chỉ lấy 1 ảnh duy nhất
        Map<Long, HinhAnh> hinhAnh = sanPhamPage.getContent().stream()
                .collect(Collectors.toMap(
                        productDetail -> productDetail.getId(), // Key: productDetailId
                        productDetail -> hinhAnhRepository.findTop1BySanPham_Id(productDetail.getSanPham().getId())
                                .stream().findFirst().orElse(null), // Lấy ảnh đầu tiên
                        (existing, replacement) -> existing // Nếu có trùng key thì giữ nguyên
                ));
        model.addAttribute("hinhAnh", hinhAnh);
        model.addAttribute("chitietsanpham", sanPhamPage);
        return new ModelAndView("admin/banhang/modalsanpham :: modalContent", model);
    }
}



