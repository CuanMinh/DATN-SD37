package com.project.datn.controller.admin.taikhoan;

import com.project.datn.entity.TaiKhoan;
import com.project.datn.entity.VaiTro;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.service.TaiKhoanService;
import com.project.datn.service.VaiTroService;
import com.project.datn.service.VaiTroTaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/khachhang")

public class KhachHangController {

    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    VaiTroService vaiTroService;

    @Autowired
    private VaiTroTaiKhoanService vaiTroTaiKhoanService;

    @RequestMapping("")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("menuK", "menu");
        return new ModelAndView("/admin/taikhoan/khachhang/khachhang", model);
    }

    @GetMapping
    public String danhSachKhachHang(Model model) {
        Long vaiTroKhachHang = 3L; // Giả sử 3L là ID của vai trò KHÁCH HÀNG
        List<TaiKhoan> danhSachKhachHang = taiKhoanService.getTaiKhoanByVaiTro(vaiTroKhachHang);
        List<VaiTro> danhSachVaiTro = vaiTroService.findAll();
        model.addAttribute("danhSachKhachHang", danhSachKhachHang);
        model.addAttribute("danhSachVaiTro", danhSachVaiTro);
        return "/admin/taikhoan/khachhang/khachhang"; // Hiển thị file khach_hang.html
    }

    @PostMapping("/block/{id}")
    public String blockAccount(@PathVariable Long id, RedirectAttributes redirect) {
        taiKhoanService.blockAccount(id);
        redirect.addFlashAttribute("message", "Tài khoản đã bị khóa.");
        return "redirect:/admin/khachhang";
    }

    @PostMapping("/open/{id}")
    public String openAccount(@PathVariable Long id, RedirectAttributes redirect) {
        taiKhoanService.openAccount(id);
        redirect.addFlashAttribute("message", "Tài khoản đã được mở khóa.");
        return "redirect:/admin/khachhang";
    }

    @GetMapping("/them-khachhang")
    public String showAddTaiKhoanForm(Model model) {
        model.addAttribute("taiKhoan", new TaiKhoan());
        model.addAttribute("danhSachVaiTro", vaiTroService.findAll()); // Gọi từ service
        return "/admin/taikhoan/khachhang/them-tai-khoan";
    }

    @PostMapping("/them-khachhang")
    public String addTaiKhoan(@RequestParam String ten,
                              @RequestParam String email,
                              @RequestParam String soDienThoai,
                              @RequestParam List<Long> vaiTroIds,
                              RedirectAttributes redirectAttributes) {
        taiKhoanService.addTaiKhoan(ten, email, soDienThoai, vaiTroIds);
        redirectAttributes.addFlashAttribute("message", "Thêm tài khoản thành công!");
        return "redirect:/admin/khachhang";
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateKhachHang(@PathVariable Long id,
                                             @RequestBody Map<String, Object> payload) {
        String ten = (String) payload.get("ten");
        String email = (String) payload.get("email");
        String soDienThoai = (String) payload.get("soDienThoai");

        List<Long> vaiTroIds = ((List<?>) payload.get("vaiTroIds"))
                .stream()
                .map(item -> Long.valueOf(item.toString()))
                .collect(Collectors.toList());

        taiKhoanService.updateTaiKhoan(id, ten, email, soDienThoai, vaiTroIds);
        return ResponseEntity.ok("Cập nhật thành công");
    }
    @GetMapping("/kiem-tra-trung")
    public ResponseEntity<String> checkDuplicate(
            @RequestParam(required = false) String ten,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String soDienThoai) {

        if (ten != null && taiKhoanService.existsByTen(ten)) {
            return ResponseEntity.badRequest().body("Tên đã tồn tại");
        }
        if (email != null && taiKhoanService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }
        if (soDienThoai != null && taiKhoanService.existsBySoDienThoai(soDienThoai)) {
            return ResponseEntity.badRequest().body("Số điện thoại đã tồn tại");
        }
        return ResponseEntity.ok("Hợp lệ");
    }


    @GetMapping("/check-duplicate-update")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateUpdate(
            @RequestParam Long id,
            @RequestParam(required = false) String ten,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String soDienThoai) {

        Map<String, Boolean> response = new HashMap<>();

        if (ten != null) {
            response.put("tenExists", taiKhoanService.isDuplicate(id, ten, null, null));
        }
        if (email != null) {
            response.put("emailExists", taiKhoanService.isDuplicate(id, null, email, null));
        }
        if (soDienThoai != null) {
            response.put("soDienThoaiExists", taiKhoanService.isDuplicate(id, null, null, soDienThoai));
        }

        return ResponseEntity.ok(response);
    }
}
