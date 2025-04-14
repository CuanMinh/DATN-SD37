package com.project.datn.service.impl;

import com.project.datn.DTO.ThongKeDoanhThuDTO;
import com.project.datn.entity.HoaDon;
import com.project.datn.repository.HoaDonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonService implements HoaDonInterface {

    private final HoaDonRepository hoaDonRepository;

    public HoaDonService(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }

    @Override
    public List<HoaDon> getAllHoaDons() {
        return hoaDonRepository.findAll();
    }

    @Override
    public Page<HoaDon> getAllHoaDons(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return hoaDonRepository.findAll(pageable);
    }

    @Override
    public Optional<HoaDon> getHoaDonById(Long id) {
        return hoaDonRepository.findById(id); // Sửa findHoaDonById thành findById
    }

    @Override
    @Transactional
    public void saveHoaDon(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    @Override
    @Transactional
    public void cancelHoaDon(Long id) {
        hoaDonRepository.findById(id).ifPresent(hoaDon -> {
            hoaDon.setTrangThai(0); // Hủy đơn
            hoaDonRepository.save(hoaDon);
        });
    }

    @Override
    @Transactional
    public HoaDon createHoaDon(HoaDon hoaDon) {
        hoaDon.setNgayTaoHoaDon(new Date());
        hoaDon.setTrangThai(1); // Trạng thái mặc định: Hóa đơn chờ
        hoaDon.setTrangThaiThanhToan(0); // Chưa thanh toán
        return hoaDonRepository.save(hoaDon);
    }

    @Override
    @Transactional
    public void thanhToanHoaDon(Long id) {
        hoaDonRepository.findById(id).ifPresent(hoaDon -> {
            hoaDon.setTrangThai(5); // Hoàn thành (sửa từ 4 thành 5 để khớp yêu cầu)
            hoaDon.setTrangThaiThanhToan(1); // Đã thanh toán
            hoaDon.setNgayThanhToan(new Date()); // Thêm ngày thanh toán
            hoaDonRepository.save(hoaDon);
        });
    }

    @Override
    @Transactional
    public void giaoHangHoaDon(Long id) {
        hoaDonRepository.findById(id).ifPresent(hoaDon -> {
            hoaDon.setTrangThai(4);
            hoaDon.setNgayGiaoHang(new Date());
            hoaDonRepository.save(hoaDon);
        });
    }

    @Override
    public String generateNewInvoiceCode() {
        long count = hoaDonRepository.count() + 1;
        return String.format("HD%02d", count);
    }

    //    public Page<HoaDon> searchHoaDon(String maHoaDon, Date startDate, Date endDate, Integer trangThai, int page, int size, String sortBy, String direction) {
//        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(page, size, sort);
//        return hoaDonRepository.searchHoaDon(maHoaDon, startDate, endDate, trangThai, pageable);
//    }
    public Page<HoaDon> searchHoaDon(String maHoaDon, Date startDate, Date endDate, Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoaDonRepository.searchHoaDon(maHoaDon, startDate, endDate, trangThai, pageable);
    }
    public List<ThongKeDoanhThuDTO> layDoanhThu(Date startDate, Date endDate, Integer ngay, Integer thang, Integer nam) {
        List<Object[]> rawData = hoaDonRepository.getDoanhThu(startDate, endDate, ngay, thang, nam);

        List<ThongKeDoanhThuDTO> dtos = new ArrayList<>();
        for (Object[] row : rawData) {
            dtos.add(new ThongKeDoanhThuDTO(
                    (Integer) row[0], // Năm
                    (Integer) row[1], // Tháng
                    (Integer) row[2], // Ngày
                    (BigDecimal) row[3] // Doanh thu
            ));
        }
        return dtos;
    }
    public List<HoaDon> layDanhSachHoaDonTheoNgay(Integer ngay, Integer thang, Integer nam) {
        return hoaDonRepository.findByNgayThangNam(ngay, thang, nam);
    }

}

