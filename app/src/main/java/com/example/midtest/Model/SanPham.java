package com.example.midtest.Model;

public class SanPham {
    private String maSP;
    private String GiaSP;
    private String tenSP;
    private String loaiSP;

    public SanPham() {
    }

    public SanPham(String maSP, String giaSP, String tenSP, String loaiSP) {

        this.maSP = maSP;
        GiaSP = giaSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getGiaSP() {
        return GiaSP;
    }

    public void setGiaSP(String giaSP) {
        GiaSP = giaSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "maSP='" + maSP + '\'' +
                ", GiaSP='" + GiaSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", loaiSP='" + loaiSP + '\'' +
                '}';
    }
}
