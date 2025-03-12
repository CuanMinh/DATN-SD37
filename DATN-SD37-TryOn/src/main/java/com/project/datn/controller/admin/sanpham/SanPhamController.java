package com.project.datn.controller.admin.sanpham;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.datn.entity.*;
import com.project.datn.model.request.sanpham.ChiTietSanPhamDto;
import com.project.datn.model.request.sanpham.SanPhamDto;
import com.project.datn.model.response.SanPhamTable;
import com.project.datn.service.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/sanpham")
public class SanPhamController {

    @Autowired
    private IDanhMucService iDanhMucService;

    @Autowired
    private IThuongHieuService iThuongHieuService;

    @Autowired
    private IChatLieuService iChatLieuService;

    @Autowired
    private IMauSacService iMauSacService;

    @Autowired
    private IKichCoService iKichCoService;

    @Autowired
    private ISanPhamService iSanPhamService;

    @Autowired
    private IChiTietSanPhamService iChiTietSanPhamService;

    @Autowired
    private IHinhAnhService iHinhAnhService;

    @GetMapping("/xoa/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable Long id) {
        Optional<SanPham> sanPham = this.iSanPhamService.findById(id);
        if (sanPham.isPresent()) {
            sanPham.get().setTrangThai(0);
            this.iSanPhamService.save(sanPham.get());
        }
        return new ModelAndView("redirect:/admin/sanpham");
    }

    @GetMapping()
    public ModelAndView list(ModelMap model) {
        List<SanPham> listSanPham = this.iSanPhamService.findAll();
        List<SanPhamTable> listSanPhamTable = new ArrayList<>();
        listSanPham.forEach(sanPham -> {
            List<ChiTietSanPham> listChiTietSanPham = this.iChiTietSanPhamService.findAllBySanPhamId(sanPham.getId());
            List<HinhAnh> listHinhAnh = this.iHinhAnhService.findBySanPhamId(sanPham.getId());
            listSanPhamTable.add(SanPhamTable.builder()
                    .id(sanPham.getId())
                    .ten(sanPham.getTen())
                    .moTa(sanPham.getMoTa())
                    .thuongHieu(sanPham.getThuongHieu().getTen())
                    .danhMuc(sanPham.getDanhMuc().getTen())
                    .chatLieu(sanPham.getChatLieu().getTen())
                    .trangThai(sanPham.getTrangThai())
                            .mauSac(listChiTietSanPham.stream()
                                    .map(ctsp -> ctsp.getMauSac().getTen())
                                    .distinct()
                                    .collect(Collectors.joining(", ")))
                            .kichCo(listChiTietSanPham.stream()
                                    .map(ctsp -> ctsp.getKichCo().getTen())
                                    .distinct()
                                    .collect(Collectors.joining(", ")))
                            .soLuong(listChiTietSanPham.stream()
                                    .mapToInt(ChiTietSanPham::getSoLuong)
                                    .sum())
                    .ngayTao(sanPham.getNgayTao())
                    .hinhAnh(listHinhAnh.get(0).getDuongDan())
                    .build());
        });
        model.addAttribute("listSanPham", listSanPhamTable);
        return new ModelAndView("/admin/sanpham/sanpham", model);
    }

    @GetMapping("them-moi")
    public ModelAndView themMoiIndex(ModelMap model) {
        model.addAttribute("sanPham", new SanPhamDto());
        model.addAttribute("listChatLieu", this.iChatLieuService.findAll());
        model.addAttribute("listMauSac", this.iMauSacService.findAll());
        model.addAttribute("listKichCo", this.iKichCoService.findAll());
        model.addAttribute("listThuongHieu", this.iThuongHieuService.findAll());
        model.addAttribute("listDanhMuc", this.iDanhMucService.findAll());

        return new ModelAndView("/admin/sanpham/themmoi", model);
    }

    @GetMapping("cap-nhat/{id}")
    public ModelAndView capNhatIndex(ModelMap model, @PathVariable Long id) {
        Optional<SanPham> sanPham = this.iSanPhamService.findById(id);
        SanPhamDto sanPhamDto = new SanPhamDto();
        if (sanPham.isPresent()) {
            sanPhamDto.setId(sanPham.get().getId());
            sanPhamDto.setTen(sanPham.get().getTen());
            sanPhamDto.setMoTa(sanPham.get().getMoTa());
            sanPhamDto.setDanhMucId(sanPham.get().getDanhMuc().getId());
            sanPhamDto.setChatLieuId(sanPham.get().getChatLieu().getId());
            sanPhamDto.setThuongHieuId(sanPham.get().getThuongHieu().getId());

            List<HinhAnh> listHinhAnh = this.iHinhAnhService.findBySanPhamId(sanPham.get().getId());
            sanPhamDto.setAnhUrl(listHinhAnh.get(0).getDuongDan());
            sanPhamDto.setAnhId(listHinhAnh.get(0).getId());

            List<ChiTietSanPham> listChiTietSanPham = this.iChiTietSanPhamService.findAllBySanPhamId(sanPham.get().getId());
            model.addAttribute("listChiTietSanPham", listChiTietSanPham);
        }
        model.addAttribute("sanPham", sanPhamDto);
        model.addAttribute("listChatLieu", this.iChatLieuService.findAll());
        model.addAttribute("listMauSac", this.iMauSacService.findAll());
        model.addAttribute("listKichCo", this.iKichCoService.findAll());
        model.addAttribute("listThuongHieu", this.iThuongHieuService.findAll());
        model.addAttribute("listDanhMuc", this.iDanhMucService.findAll());

        return new ModelAndView("/admin/sanpham/capnhat", model);
    }

    @PostMapping("cap-nhat")
    public ModelAndView capNhat(ModelMap model, @Valid @ModelAttribute("sanPham") SanPhamDto sanPhamDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ChiTietSanPhamDto> danhSachSanPham = objectMapper.readValue(sanPhamDto.getListSanPham(), new TypeReference<List<ChiTietSanPhamDto>>() {});

        Optional<SanPham> sanPhamUD = this.iSanPhamService.findById(sanPhamDto.getId());
        SanPham sanPham = this.iSanPhamService.save(SanPham.builder()
                .id(sanPhamUD.map(SanPham::getId).orElse(null))
                .ten(sanPhamDto.getTen())
                .moTa(sanPhamDto.getMoTa())
                .chatLieu(ChatLieu.builder().id(sanPhamDto.getChatLieuId()).build())
                .danhMuc(DanhMuc.builder().id(sanPhamDto.getDanhMucId()).build())
                .thuongHieu(ThuongHieu.builder().id(sanPhamDto.getThuongHieuId()).build())
                .ngayTao(sanPhamUD.map(SanPham::getNgayTao).orElse(null))
                .trangThai(sanPhamUD.map(SanPham::getTrangThai).orElse(null))
                .build());

        List<ChiTietSanPham> listChiTietSanPham = new ArrayList<>();
        danhSachSanPham.forEach(ctsp -> {
            Optional<ChiTietSanPham> chiTietSanPham = Optional.empty();
            if (ctsp.getId() != null) {
                chiTietSanPham = this.iChiTietSanPhamService.findById(ctsp.getId());
            }
            listChiTietSanPham.add(ChiTietSanPham.builder()
                    .id(chiTietSanPham.map(ChiTietSanPham::getId).orElse(null))
                    .sanPham(sanPham)
                    .kichCo(chiTietSanPham.map(ChiTietSanPham::getKichCo).orElse(KichCo.builder().id(ctsp.getKichCoId()).build()))
                    .mauSac(chiTietSanPham.map(ChiTietSanPham::getMauSac).orElse(MauSac.builder().id(ctsp.getMauSacId()).build()))
                    .trongLuong(ctsp.getTrongLuong())
                    .gia(ctsp.getGia())
                    .giamGia(ctsp.getGiamGia())
                    .soLuong(ctsp.getSoLuong())
                    .ngayTao(chiTietSanPham.map(ChiTietSanPham::getNgayTao).orElse(null))
                    .trangThai(chiTietSanPham.map(ChiTietSanPham::getTrangThai).orElse(null))
                    .build());
        });

        this.iChiTietSanPhamService.saveAll(listChiTietSanPham);

        String projectDir = new ClassPathResource("").getFile().getAbsolutePath();
        String uploadDir = Paths.get(projectDir, "../uploads/sanpham").normalize().toString();
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + sanPhamDto.getAnh().getOriginalFilename();
        String filePath = Paths.get(uploadDir, fileName).toString();

        sanPhamDto.getAnh().transferTo(new File(filePath));

        this.iHinhAnhService.save(HinhAnh.builder()
                .id(sanPhamDto.getAnhId())
                .duongDan("/uploads/sanpham/" + fileName)
                .sanPhamId(sanPham.getId())
                .build());

        model.addAttribute("filePath", "/uploads/sanpham/" + fileName);
        return new ModelAndView("redirect:/admin/sanpham", model);
    }

    @PostMapping("them-moi")
    public ModelAndView themMoi(ModelMap model, @Valid @ModelAttribute("sanPham") SanPhamDto sanPhamDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ChiTietSanPhamDto> danhSachSanPham = objectMapper.readValue(sanPhamDto.getListSanPham(), new TypeReference<List<ChiTietSanPhamDto>>() {});

        SanPham sanPham = this.iSanPhamService.save(SanPham.builder()
                .ten(sanPhamDto.getTen())
                .moTa(sanPhamDto.getMoTa())
                .chatLieu(ChatLieu.builder().id(sanPhamDto.getChatLieuId()).build())
                .danhMuc(DanhMuc.builder().id(sanPhamDto.getDanhMucId()).build())
                .thuongHieu(ThuongHieu.builder().id(sanPhamDto.getThuongHieuId()).build())
                .build());

        List<ChiTietSanPham> listChiTietSanPham = new ArrayList<>();
        danhSachSanPham.forEach(ctsp -> {
            listChiTietSanPham.add(ChiTietSanPham.builder()
                    .sanPham(sanPham)
                    .kichCo(KichCo.builder().id(ctsp.getKichCoId()).build())
                    .mauSac(MauSac.builder().id(ctsp.getMauSacId()).build())
                    .trongLuong(ctsp.getTrongLuong())
                    .gia(ctsp.getGia())
                    .giamGia(ctsp.getGiamGia())
                    .soLuong(ctsp.getSoLuong())
                    .build());
        });

        this.iChiTietSanPhamService.saveAll(listChiTietSanPham);

        String projectDir = new ClassPathResource("").getFile().getAbsolutePath();
        String uploadDir = Paths.get(projectDir, "../uploads/sanpham").normalize().toString();
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + sanPhamDto.getAnh().getOriginalFilename();
        String filePath = Paths.get(uploadDir, fileName).toString();

        sanPhamDto.getAnh().transferTo(new File(filePath));

        this.iHinhAnhService.save(HinhAnh.builder()
                .duongDan("/uploads/sanpham/" + fileName)
                .sanPhamId(sanPham.getId())
                .build());

        model.addAttribute("filePath", "/uploads/sanpham/" + fileName);
        return new ModelAndView("redirect:/admin/sanpham", model);
    }
}
