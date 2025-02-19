package com.project.datn.service;

import com.project.datn.entity.TaiKhoan;
import com.project.datn.entity.VaiTro;
import com.project.datn.entity.VaiTroTaiKhoan;
import com.project.datn.repository.TaiKhoanRepository;
import com.project.datn.repository.VaiTroRepository;
import com.project.datn.repository.VaiTroTaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private VaiTroTaiKhoanRepository vaiTroTaiKhoanRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Override
    public UserDetails loadUserByUsername(String ten) throws UsernameNotFoundException {
        Optional<TaiKhoan> userOpt = taiKhoanRepository.findByTen(ten);

        if (userOpt.isEmpty()) {
            System.out.println("Tên Không tìm thấy! " + ten);
            throw new UsernameNotFoundException("Tên: " + ten + " không được tìm thấy trong cơ sở dữ liệu");
        }

        System.out.println("Found User: " + userOpt.get());
        Optional<VaiTroTaiKhoan> urole = vaiTroTaiKhoanRepository.findByTaiKhoan_Id(Long.valueOf(userOpt.get().getId()));
        Optional<VaiTro> arole = vaiTroRepository.findById(urole.get().getVaiTro().getId());

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority(arole.get().getTen());
        grantList.add(authority);

        System.out.println(arole.get().getTen());

        UserDetails userDetails = (UserDetails) new User(userOpt.get().getEmail(),
                userOpt.get().getPassword().trim(), grantList);

        return userDetails;
    }
}
