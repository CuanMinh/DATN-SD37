package com.project.datn.service.banhang.impl;

import com.project.datn.entity.ChiTietSanPham;
import com.project.datn.entity.HoaDon;
import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.entity.MaGiamGia;
import com.project.datn.entity.PhuongThucThanhToan;
import com.project.datn.entity.TaiKhoan;
import com.project.datn.model.request.banhang.HoaDonRequest;
import com.project.datn.repository.ChiTietSanPhamRepository;
import com.project.datn.repository.HoaDonChiTietRepository;
import com.project.datn.repository.HoaDonRepository;
import com.project.datn.repository.MaGiamGiaRepository;
import com.project.datn.repository.PhuongThucThanhToanRepository;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.service.banhang.IHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonServiceImpl implements IHoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    MaGiamGiaRepository maGiamGiaRepository;

    @Autowired
    PhuongThucThanhToanRepository phuongThucThanhToanRepository;


    @Override
    public HoaDon addHoaDon(HoaDon hoaDon) {
        // Lấy thông tin đăng nhập từ context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                // Giả sử anh có hàm tìm `Account` theo username:
                TaiKhoan taiKhoan = taiKhoanRepository.findByTen(username);
                hoaDon.setMaHoaDon("hd" + String.format("%06d", (int) (Math.random() * 1000000)));
                hoaDon.setNgayTaoHoaDon(new Date());
                hoaDon.setTrangThaiThanhToan(0);
                hoaDon.setTrangThai(1);
                hoaDon.setTaiKhoan(taiKhoan);
                return hoaDonRepository.save(hoaDon);
            }
        }
        return null;
    }

    @Override
    public void addGiamgia(Long idHoaDon, Long idMaGiamGia) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        Optional<MaGiamGia> optionalMaGiamGia = maGiamGiaRepository.findById(idMaGiamGia);
        if (optionalHoaDon.isPresent() && optionalMaGiamGia.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            hoaDon.setMaGiamGia(optionalMaGiamGia.get());
            hoaDonRepository.save(hoaDon);
        } else {
            throw new RuntimeException("Không tìm thấy hóa đơn hoặc mã giảm giá");
        }
    }

    @Override
    public void thanhToan(Long idHoaDon, HoaDonRequest hoaDonRequest) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (!optionalHoaDon.isPresent()) {
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }
        HoaDon hoaDon = optionalHoaDon.get();

        Optional<PhuongThucThanhToan> optionalPhuongThuc = phuongThucThanhToanRepository.findById(hoaDonRequest.getIdPhuongThucThanhToan());
        List<HoaDonChiTiet> hoaDonChiTiets = hoaDonChiTietRepository.findByHoaDonId(idHoaDon);
        Optional<MaGiamGia> optionalMaGiamGia = Optional.empty(); // Mặc định là rỗng
        if (hoaDon.getMaGiamGia() != null) {
            optionalMaGiamGia = maGiamGiaRepository.findById(hoaDon.getMaGiamGia().getId());
        }

        if (!optionalPhuongThuc.isPresent()) {
            throw new RuntimeException("Phương thức thanh toán không hợp lệ!");
        }
        PhuongThucThanhToan phuongThucThanhToan = optionalPhuongThuc.get();
        // Xử lý theo phương thức thanh toán
        if (phuongThucThanhToan.getId().equals(1L)) { // Thanh toán tiền mặt
            hoaDon.setGhiChu(hoaDonRequest.getGhiChu());
            hoaDon.setTongTien(hoaDonRequest.getTongTien());
            hoaDon.setTongTienCuoiCung(hoaDonRequest.getTongTienCuoiCung());
            hoaDon.setNgayThanhToan(new Date());
            hoaDon.setNgayCapNhap(new Date());
            hoaDon.setTrangThaiDonHang(0);
            hoaDon.setTrangThaiThanhToan(1);
            hoaDon.setTrangThai(4);
            hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
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
        } else if (phuongThucThanhToan.getId().equals(2L)) { // Thanh toán chuyển khoản
            hoaDon.setGhiChu(hoaDonRequest.getGhiChu());
            hoaDon.setTongTien(hoaDonRequest.getTongTien());
            hoaDon.setTongTienCuoiCung(hoaDonRequest.getTongTienCuoiCung());
            hoaDon.setNgayCapNhap(new Date());
            hoaDon.setTrangThaiDonHang(0);
            hoaDon.setTrangThaiThanhToan(0);
            hoaDon.setTrangThai(1);
            hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
        } else {
            throw new RuntimeException("Phương thức thanh toán không được hỗ trợ!");
        }

        hoaDonRepository.save(hoaDon);
    }


}
