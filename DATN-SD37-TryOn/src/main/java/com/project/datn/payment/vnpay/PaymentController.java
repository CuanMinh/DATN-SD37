package com.project.datn.payment.vnpay;


import com.project.datn.config.payment.VNPAYConfig;
import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.GioHang;
import com.project.datn.entity.HoaDon;
import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.model.response.ResponseObject;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.GioHangRepository;
import com.project.datn.repository.HoaDonChiTietRepository;
import com.project.datn.repository.HoaDonRepository;
import com.project.datn.util.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;
    @Autowired
    private VNPAYConfig vnPayConfig;

    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    @Transactional
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) {
        // Log để debug
        System.out.println("Callback called with parameters: " + request.getQueryString());

        // Lấy các tham số từ VNPAY
        Map<String, String[]> parameterMap = request.getParameterMap();
        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_Amount = request.getParameter("vnp_Amount");
        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");

        // Tạo hash để kiểm tra tính toàn vẹn
        String signValue = VNPayUtil.getPaymentURL(parameterMap.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("vnp_SecureHash"))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0])), false);
        String checkHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), signValue);

        // Tìm hóa đơn dựa trên vnp_OrderInfo
        String maHoaDon = vnp_OrderInfo.split(":")[1].trim();
        HoaDon hoaDon = hoaDonRepository.findByHoaDonMaHoaDon(maHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));

        try {
            if (!checkHash.equals(vnp_SecureHash)) {
                response.sendRedirect("/customer/thanhtoan/thatbai?error=hash-validation-failed");
                return;
            }

            if ("00".equals(vnp_ResponseCode)) {
                // Thanh toán thành công, xử lý hóa đơn
                try {
                    // Cập nhật trạng thái hóa đơn
                    hoaDon.setTrangThaiThanhToan(1); // Thanh toán thành công
                    hoaDon.setNgayThanhToan(new Date());
                    hoaDonRepository.save(hoaDon);

                    // Xử lý giỏ hàng và kho
                    Long idTaiKhoan = hoaDon.getTaiKhoan().getId();
                    List<GioHang> gioHangList = gioHangRepository.findByTaiKhoan_Id(idTaiKhoan);

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
                                throw new IllegalStateException("Sản phẩm không đủ số lượng trong kho");
                            }
                            chiTietSanPham.setSoLuong(newQuantity);
                            chiTietSanPhamRepository.save(chiTietSanPham);
                        }
                    }

                    // Xóa giỏ hàng
                    gioHangRepository.deleteByTaiKhoan_Id(idTaiKhoan);

                    // Chuyển hướng đến trang thanh toán thành công
                    response.sendRedirect("/customer/thanhtoan/thanhtoanthanhcong?maHoaDon=" + hoaDon.getMaHoaDon());
                } catch (Exception e) {
                    response.sendRedirect("/customer/thanhtoan" );
                }
            } else {
                // Giao dịch bị hủy hoặc thất bại
                try {
                    // Xóa hóa đơn
                    hoaDonRepository.delete(hoaDon);

                    // Chuyển hướng đến trang thất bại
                    if ("24".equals(vnp_ResponseCode)) {
                        response.sendRedirect("/customer/thanhtoan");
                    } else {
                        response.sendRedirect("/customer/thanhtoan" );
                    }
                } catch (Exception e) {
                    response.sendRedirect("/customer/thanhtoan" );
                }
            }
        } catch (Exception e) {
            try {
                response.sendRedirect("/customer/thanhtoan" );
            } catch (IOException ioException) {
                // Log lỗi nếu không thể chuyển hướng
                System.err.println("Không thể chuyển hướng: " + ioException.getMessage());
            }
        }
    }
}

