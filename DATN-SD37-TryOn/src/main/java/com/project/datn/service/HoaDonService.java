package com.project.datn.service;

import com.project.datn.entity.HoaDon;
import com.project.datn.repository.HoaDonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;

    public HoaDonService(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }

    public List<HoaDon> getAllHoaDons() {
        return hoaDonRepository.findAll();
    }

    public Optional<HoaDon> getHoaDonById(Long id) {
        return hoaDonRepository.findHoaDonById(id);
    }

    public void cancelHoaDon(Long id) {
        hoaDonRepository.cancelHoaDon(id);
    }

    @Transactional
    public HoaDon createHoaDon(HoaDon hoaDon) {
        hoaDon.setNgayTaoHoaDon(new Date());
        hoaDon.setTrangThai(1); // Trạng thái mặc định
        hoaDon.setTrangThaiThanhToan(0); // Chưa thanh toán
        return hoaDonRepository.save(hoaDon);
    }

    // Phân trang hóa đơn
    public Page<HoaDon> getAllHoaDons(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return hoaDonRepository.findAll(pageable);
    }

    // Thanh toán hóa đơn (Cập nhật trạng thái nhanh hơn)
    @Transactional
    public void thanhToanHoaDon(Long id) {
        hoaDonRepository.findById(id).ifPresent(hoaDon -> {
            hoaDon.setTrangThai(4); // Hoàn thành
            hoaDon.setTrangThaiThanhToan(1); // Đã thanh toán
            hoaDonRepository.save(hoaDon);
        });
    }

    // Giao hàng hóa đơn (Cập nhật trạng thái nhanh hơn)
    @Transactional
    public void giaoHangHoaDon(Long id) {
        hoaDonRepository.findById(id).ifPresent(hoaDon -> {
            hoaDon.setTrangThai(3); // Đang giao hàng
            hoaDonRepository.save(hoaDon);
        });
    }
    public String generateNewInvoiceCode() {
        long count = hoaDonRepository.count() + 1; // Lấy số hóa đơn hiện tại + 1
        return String.format("HD%02d", count); // Tạo mã dạng HD01, HD02, ..., HD100, HD101...
    }


}
