package com.project.datn.controller.user;

import com.project.datn.entity.Email;
import com.project.datn.entity.TaiKhoan;
import com.project.datn.entity.VaiTro;
import com.project.datn.entity.VaiTroTaiKhoan;
import com.project.datn.model.request.taikhoan.ChangePassword;
import com.project.datn.model.request.taikhoan.SignupRequest;
import com.project.datn.repository.EmailRepository;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.repository.VaiTroRepository;
import com.project.datn.repository.VaiTroTaiKhoanRepository;
import com.project.datn.service.IEmailTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class TaiKhoanController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    TaiKhoanRepository accountRepository;

    @Autowired
    VaiTroRepository roleRepository;

    @Autowired
    VaiTroTaiKhoanRepository roleAccountRepository;

    @Autowired
    IEmailTemplateService iEmailTemplateService;
    @Autowired
    EmailRepository iEmailTemplateRepository;

    @Autowired
    HttpSession session;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/login")
    public ModelAndView loginForm(ModelMap model, @RequestParam("error") Optional<String> error) {
        String errorString = error.orElse("false");
        if (errorString.equals("true")) {
            model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
        }
        return new ModelAndView("/user/login/login", model);
    }

    @RequestMapping("/logout")
    public String login() {

        return "redirect:/home";
    }

    @GetMapping("/register")
    public ModelAndView registerForm(ModelMap model) {
        model.addAttribute("customer", new SignupRequest());
        return new ModelAndView("/user/login/register", model);
    }

    @PostMapping("/register")
    public String register(ModelMap model, @Valid @ModelAttribute("customer") SignupRequest dto, BindingResult result,
                           @RequestParam("password") String password) {
        if (result.hasErrors()) {
            return "/user/login/register";
        }
        if (!checkEmail(dto.getEmail())) {
            model.addAttribute("error", "Email này đã được sử dụng!");
            return "/user/login/register";
        }
        session.removeAttribute("otp");
        // Gửi mã xác nhận đến email
        Integer mailType = createVerificationCode();
        iEmailTemplateService.sendMaXacNhanToEmail(mailType);

        Email xacNhanEmail = new Email();
        xacNhanEmail.setMa(mailType);
        xacNhanEmail.setTieuDe("ok");
        xacNhanEmail.setMoTa("Subject");
        iEmailTemplateRepository.save(xacNhanEmail);

        model.addAttribute("customer", dto);
        model.addAttribute("message", "Mã OTP đã được gửi tới Email, hãy kiểm tra Email của bạn!");

        return "/user/login/confirmOtpRegister";
    }

    @PostMapping("/confirmOtpRegister")
    public ModelAndView confirmRegister(ModelMap model, @ModelAttribute("customer") SignupRequest dto, @RequestParam("password") String password,
                                        @RequestParam("otp") Integer otp) {
        Email emailTemplate = new Email();
        // Kiểm tra thông tin xác nhận email trong cơ sở dữ liệu
        Optional<Email> optionalXacNhanEmail = iEmailTemplateRepository.findByMa(otp);
        if (optionalXacNhanEmail.isPresent()) {
            if (otp.equals(optionalXacNhanEmail.get().getMa())) {
                dto.setPassword(bCryptPasswordEncoder.encode(password));
                TaiKhoan c = new TaiKhoan();
                BeanUtils.copyProperties(dto, c);
                c.setNgayTao(new Date());
                c.setTrangThai(1);

                accountRepository.save(c);
                Optional<VaiTro> a = roleRepository.findById(3L);
                VaiTroTaiKhoan urole = new VaiTroTaiKhoan(0L, c, a.get());
                roleAccountRepository.save(urole);

                session.removeAttribute("otp");
                model.addAttribute("message", "Đăng kí thành công");
                return new ModelAndView("/user/login/login");
            }
        }
        model.addAttribute("customer", dto);
        model.addAttribute("error", "Mã OTP không đúng, hãy thử lại!");
        return new ModelAndView("/user/login/confirmOtpRegister", model);


    }

    //    quên mk
    @GetMapping("/forgotPassword")
    public ModelAndView forgotFrom() {
        return new ModelAndView("/user/login/forgotPassword");
    }

    @PostMapping("/forgotPassword")
    public ModelAndView forgotPassowrd(ModelMap model, @RequestParam("email") String email) {
        List<TaiKhoan> listC = accountRepository.findAll();
        for (TaiKhoan c : listC) {
            if (email.trim().equals(c.getEmail())) {
                session.removeAttribute("otp");
                // Gửi mã xác nhận đến email
                Integer mailType = createVerificationCode();
                iEmailTemplateService.sendMaXacNhanToEmail(mailType);

                Email xacNhanEmail = new Email();
                xacNhanEmail.setMa(mailType);
                xacNhanEmail.setTieuDe("ok");
                xacNhanEmail.setMoTa("Subject");
                iEmailTemplateRepository.save(xacNhanEmail);

                model.addAttribute("email", email);
                model.addAttribute("message", "Mã OTP đã được gửi tới Email, hãy kiểm tra Email của bạn!");
                return new ModelAndView("/user/login/confirmOtp", model);
            }
        }
        model.addAttribute("error", "Email này không tồn tại trong hệ thống!");
        return new ModelAndView("/user/login/forgotPassword", model);
    }

    @PostMapping("/confirmOtp")
    public ModelAndView confirm(ModelMap model, @RequestParam("otp") Integer otp, @RequestParam("email") String email) {
        Optional<Email> optionalXacNhanEmail = iEmailTemplateRepository.findByMa(otp);
        if (optionalXacNhanEmail.isPresent()) {
            if (otp.equals(optionalXacNhanEmail.get().getMa())) {
                model.addAttribute("email", email);
                model.addAttribute("newPassword", "");
                model.addAttribute("confirmPassword", "");
                model.addAttribute("changePassword", new ChangePassword());
                return new ModelAndView("/user/login/changePassword", model);
            }
        }
        model.addAttribute("error", "Mã OTP không trùng, vui lòng kiểm tra lại!");
        return new ModelAndView("/user/login/confirmOtp", model);
    }

    @PostMapping("/changePassword")
    public ModelAndView changeForm(ModelMap model,
                                   @Valid @ModelAttribute("changePassword") ChangePassword changePassword, BindingResult result,
                                   @RequestParam("email") String email, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword) {
        if (result.hasErrors()) {

            model.addAttribute("newPassword", newPassword);
            model.addAttribute("newPassword", confirmPassword);
//			model.addAttribute("changePassword", changePassword);
            model.addAttribute("email", email);
            return new ModelAndView("/user/login/changePassword", model);
        }

        if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {

            model.addAttribute("newPassword", newPassword);
            model.addAttribute("newPassword", confirmPassword);
//			model.addAttribute("changePassword", changePassword);
            model.addAttribute("error", "error");
            model.addAttribute("email", email);
            return new ModelAndView("/user/login/changePassword", model);
        }
        Optional<TaiKhoan> optionalAccount = accountRepository.FindByEmail(email);
        if (!optionalAccount.isPresent()) {
            // Handle case where account with email is not found
            model.addAttribute("error", "Không tìm thấy tài khoản với email đã nhập (Account with entered email not found)");
            model.addAttribute("email", email);
            return new ModelAndView("/user/login/changePassword", model);
        }

        TaiKhoan account = optionalAccount.get(); // Safe access after checking isPresent()

        // Update account details
        account.setTrangThai(1);
        account.setPassword(encoder.encode(newPassword));
        accountRepository.save(account);
        //
        model.addAttribute("message", "Đổi mật khẩu thành công!");
        model.addAttribute("email", "");
        session.removeAttribute("otp");
        return new ModelAndView("/user/login/changePassword", model);
    }

    //random mã xác nhận gmail
    public static int createVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(1000000);
        String codeStr = String.format("%06d", code);
        return Integer.parseInt(codeStr);
    }

    // check email
    public boolean checkEmail(String email) {
        List<TaiKhoan> list = accountRepository.findAll();
        for (TaiKhoan c : list) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }
}
