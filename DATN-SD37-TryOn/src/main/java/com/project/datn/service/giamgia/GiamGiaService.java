package com.project.datn.service.giamgia;

import com.project.datn.repository.MaGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiamGiaService {

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Chạy vào 00:00 mỗi ngày
    @Transactional
    public void capNhatTrangThaiMaGiamGia() {
        maGiamGiaRepository.capNhatTrangThaiHetHan();
        System.out.println("Đã cập nhật trạng thái mã giảm giá lúc 00:00!");
    }

    @Transactional
    public void ngungHoatDong(Long id) {
        maGiamGiaRepository.ngungHoatDong(id);
    }
}
