package com.project.datn.controller.admin.Thongke;

import com.project.datn.DTO.ThongKeDoanhThuDTO;
import com.project.datn.entity.HoaDon;
import com.project.datn.entity.HoaDonChiTiet;
import com.project.datn.repository.HoaDonChiTietRepository;
import com.project.datn.service.impl.HoaDonService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/admin/thongkedoanhthu")
public class ThongKeDoanhThuController {

    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    // Hiển thị doanh thu với bộ lọc khoảng ngày
    @GetMapping("")
    public String thongKeDoanhThu(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                  @RequestParam(value = "ngay", required = false) Integer ngay,
                                  @RequestParam(value = "thang", required = false) Integer thang,
                                  @RequestParam(value = "nam", required = false) Integer nam,
                                  Model model) {
        if (startDate == null || endDate == null) {
            Calendar cal = Calendar.getInstance();
            endDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            startDate = cal.getTime();
        }
        List<ThongKeDoanhThuDTO> doanhThuList = hoaDonService.layDoanhThu(startDate, endDate, ngay, thang, nam);
        model.addAttribute("doanhThuList", doanhThuList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("ngay", ngay);
        model.addAttribute("thang", thang);
        model.addAttribute("nam", nam);
        return "admin/thongke/doanhthu/thongkedoanhthu";
    }

    // Xuất báo cáo doanh thu ra file Excel với khoảng ngày
    @GetMapping("/export-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                         @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                                         @RequestParam(value = "ngay", required = false) Integer ngay,
                                                         @RequestParam(value = "thang", required = false) Integer thang,
                                                         @RequestParam(value = "nam", required = false) Integer nam) throws IOException {
        List<ThongKeDoanhThuDTO> doanhThuList = hoaDonService.layDoanhThu(startDate, endDate, ngay, thang, nam);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh thu");

        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Năm");
        headerRow.createCell(1).setCellValue("Tháng");
        headerRow.createCell(2).setCellValue("Ngày");
        headerRow.createCell(3).setCellValue("Doanh Thu");

        // Điền dữ liệu vào sheet
        int rowNum = 1;
        for (ThongKeDoanhThuDTO doanhThu : doanhThuList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(doanhThu.getNam());
            row.createCell(1).setCellValue(doanhThu.getThang());
            row.createCell(2).setCellValue(doanhThu.getNgay());
            row.createCell(3).setCellValue(doanhThu.getDoanhThu().doubleValue());
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=doanhthu.xlsx")
                .body(resource);
    }
    @GetMapping("/hoadon")
    public String hienThiHoaDon(
            @RequestParam(required = false) String maHoaDon,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(required = false) Integer ngay,
            @RequestParam(required = false) Integer thang,
            @RequestParam(required = false) Integer nam,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ) {
        Page<HoaDon> hoaDonPage;

        if (ngay != null && thang != null && nam != null) {
            List<HoaDon> danhSach = hoaDonService.layDanhSachHoaDonTheoNgay(ngay, thang, nam);
            hoaDonPage = new PageImpl<>(danhSach, PageRequest.of(page, size), danhSach.size());
            model.addAttribute("filterDate", String.format("%02d/%02d/%d", ngay, thang, nam));
        } else {
            hoaDonPage = hoaDonService.searchHoaDon(maHoaDon, startDate, endDate, trangThai, page, size);
        }

        model.addAttribute("hoaDons", hoaDonPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());

        model.addAttribute("maHoaDon", maHoaDon);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("trangThai", trangThai);

        return "admin/thongke/doanhthu/chitiethoadon";
    }
}
