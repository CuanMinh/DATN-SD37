package com.project.datn.repository;

import com.project.datn.entity.ChatLieu;
import com.project.datn.entity.TaiKhoan;
import com.project.datn.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository  extends JpaRepository<TaiKhoan, Long> {
    TaiKhoan findByTen(String ten);

    @Query(value = "SELECT TOP 1 * FROM tai_khoan WHERE email = ? AND trang_thai = 1", nativeQuery = true)
    Optional<TaiKhoan> FindByEmail(String email);

    @Query(value = "SELECT TOP 1 * FROM tai_khoan WHERE ten = ? AND trang_thai = 1", nativeQuery = true)
    Optional<TaiKhoan> FindByTen(String ten);


//    TaiKhoan findByCustomer_PhoneNumber(String phoneNumber);
//    TaiKhoan findTopByOrderByIdDesc();

    @Query("SELECT v FROM VaiTro v JOIN VaiTroTaiKhoan vt ON v.id = vt.vaiTro.id WHERE vt.taiKhoan.id = :taiKhoanId")
    List<VaiTro> findVaiTrosByTaiKhoanId(@Param("taiKhoanId") Long taiKhoanId);
    @Query("SELECT tk FROM TaiKhoan tk JOIN VaiTroTaiKhoan vttk ON tk.id = vttk.taiKhoan.id WHERE vttk.vaiTro.id = :vaiTroId")
    List<TaiKhoan> findByVaiTroId(@Param("vaiTroId") Long vaiTroId);
    @Query("SELECT t FROM TaiKhoan t LEFT JOIN FETCH t.vaiTroTaiKhoans v LEFT JOIN FETCH v.vaiTro")
    List<TaiKhoan> findAllWithRoles();
    List<TaiKhoan> findByTrangThai(Integer trangThai);
    void deleteById(Long id);
    Optional<TaiKhoan> findById(Long id);
    boolean existsByTen(String ten);
    boolean existsByEmail(String email);
    boolean existsBySoDienThoai(String soDienThoai);
    boolean existsByTenAndIdNot(String ten, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsBySoDienThoaiAndIdNot(String soDienThoai, Long id);
}
