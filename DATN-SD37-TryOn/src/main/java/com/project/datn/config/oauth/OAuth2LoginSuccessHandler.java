package com.project.datn.config.oauth;

import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.repository.VaiTroRepository;
import com.project.datn.repository.VaiTroTaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private VaiTroTaiKhoanRepository vaiTroTaiKhoanRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
//        String email = oauth2User.getName();
//        System.out.println(email);
//        Optional<Account> cus = accountRepository.FindByEmail(email);
//        if (cus.isEmpty()) {
//            Account c = new Account();
//            c.setUsername(oauth2User.getNameReal());
//            c.setCode("");
//            c.setEmail(email);
//            c.setPassword(bCryptPasswordEncoder.encode("123@ABCxyzalualua"));
//            c.setCreateDate(new Date());
//            c.setGender(true);
//            c.setImage("");
//            c.setPhone("0977562019");
//            c.setStatus(1);
//            accountRepository.save(c);
//            Optional<Role> a = roleRepository.findById(3L);
//            RoleAccount urole = new RoleAccount(0L, c, a.get());
//            roleAccountRepository.save(urole);
//        }
//
//        super.onAuthenticationSuccess(request, response, authentication);
//    }

}
