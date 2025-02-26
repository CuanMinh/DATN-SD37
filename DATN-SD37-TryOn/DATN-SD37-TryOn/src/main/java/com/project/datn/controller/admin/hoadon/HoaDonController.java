package com.project.datn.controller.admin.hoadon;

import com.project.datn.entity.HoaDon;
import com.project.datn.repository.HoaDonRepository;
import com.project.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/hoadon")
public class HoaDonController {

    private final HoaDonService hoaDonService;

    public HoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    @RequestMapping("")
    public ModelAndView list(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "ngayTaoHoaDon") String sortBy,
                             @RequestParam(defaultValue = "desc") String direction,
                             ModelMap model) {
        if (page < 0) page = 0; // Tránh lỗi nếu người dùng nhập page < 0
        Page<HoaDon> hoaDonPage = hoaDonService.getAllHoaDons(page, size, sortBy, direction);
        model.addAttribute("hoaDons", hoaDonPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        return new ModelAndView("/admin/hoadon/hoadon", model);
    }

    @GetMapping("/detail/{id}")
    public String detailHoaDon(@PathVariable Long id, Model model) {
        model.addAttribute("hoaDon", hoaDonService.getHoaDonById(id).orElse(null));
        return "/admin/hoadon/chitiethoadon";
    }

    @PostMapping("/cancel/{id}")
    public String cancelHoaDon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        hoaDonService.cancelHoaDon(id);
        redirectAttributes.addFlashAttribute("message", "Hóa đơn đã được hủy thành công!");
        return "redirect:/admin/hoadon";
    }

    @GetMapping("/view-create")
    public String showCreateForm(Model model) {
        model.addAttribute("hoaDon", new HoaDon());
        return "/admin/hoadon/themhoadon";
    }

    @PostMapping("/create")
    public String createHoaDon(@ModelAttribute HoaDon hoaDon, RedirectAttributes redirectAttributes) {
        hoaDonService.createHoaDon(hoaDon);
        redirectAttributes.addFlashAttribute("message", "Hóa đơn đã được tạo thành công!");
        return "redirect:/admin/hoadon";
    }

    @PostMapping("/thanh-toan/{id}")
    public String thanhToanHoaDon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        hoaDonService.thanhToanHoaDon(id);
        redirectAttributes.addFlashAttribute("message", "Hóa đơn đã được thanh toán!");
        return "redirect:/admin/hoadon/detail/" + id;
    }

    @PostMapping("/giao-hang/{id}")
    public String giaoHangHoaDon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        hoaDonService.giaoHangHoaDon(id);
        redirectAttributes.addFlashAttribute("message", "Hóa đơn đang được giao!");
        return "redirect:/admin/hoadon/detail/" + id;
    }
    @GetMapping("/generateCode")
    public ResponseEntity<String> generateInvoiceCode() {
        return ResponseEntity.ok(hoaDonService.generateNewInvoiceCode());
    }
}

