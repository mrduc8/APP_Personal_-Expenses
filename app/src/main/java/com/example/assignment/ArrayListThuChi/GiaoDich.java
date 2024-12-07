package com.example.assignment.ArrayListThuChi;

import java.io.Serializable;
import java.util.Date;

public class GiaoDich implements Serializable {
    private int maGd;
    private String moTaGd;
    private Date ngayGd;
    private int soTien;
    private int maKhoan;
    private String ghiChuGd;

    public GiaoDich() {
    }


    public String getMoTaGd() {
        return moTaGd;
    }


    public GiaoDich(int maGd, String moTaGd, Date ngayGd, int soTien, int maKhoan, String ghiChuGd) {
        this.maGd = maGd;
        this.moTaGd = moTaGd;
        this.ngayGd = ngayGd;
        this.soTien = soTien;
        this.maKhoan = maKhoan;
        this.ghiChuGd = ghiChuGd;
    }

    public int getMaGd() {
        return maGd;
    }

    public void setMaGd(int maGd) {
        this.maGd = maGd;
    }

    public void setMoTaGd(String moTaGd) {
        this.moTaGd = moTaGd;
    }

    public Date getNgayGd() {
        return ngayGd;
    }

    public void setNgayGd(Date ngayGd) {
        this.ngayGd = ngayGd;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }

    public int getMaKhoan() {
        return maKhoan;
    }

    public void setMaKhoan(int maKhoan) {
        this.maKhoan = maKhoan;
    }

    public String getGhiChuGd() {
        return ghiChuGd;
    }

    public void setGhiChuGd(String ghiChuGd) {
        this.ghiChuGd = ghiChuGd;
    }
}
