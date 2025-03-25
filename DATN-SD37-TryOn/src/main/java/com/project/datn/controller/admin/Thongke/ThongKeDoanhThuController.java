package com.project.datn.controller.admin.Thongke;

import com.project.datn.DTO.ThongKeDoanhThuDTO;
import com.project.datn.service.impl.HoaDonService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/admin/thongkedoanhthu")
public class ThongKeDoanhThuController {

    @Autowired
    private HoaDonService hoaDonService;

    // Hiển thị doanh thu với bộ lọc khoảng ngày
    @GetMapping("")
    public String thongKeDoanhThu(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                  @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                  @RequestParam(value = "ngay", required = false) Integer ngay,
                                  @RequestParam(value = "thang", required = false) Integer thang,
                                  @RequestParam(value = "nam", required = false) Integer nam,
                                  Model model) {
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

}
