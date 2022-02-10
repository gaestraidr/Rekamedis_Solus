/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.model;

import me.gaestra.sysinforekamedis.extras.annotations.MethodIgnorable;

/**
 *
 * @author Ganesha
 */
public class Pegawai extends Model {
    
    public String nik;
    public String nama;
    public String jabatan;
    public String username;
    public String password;
    public int jenis_kelamin;
    public int role;
    public String alamat;

    public Pegawai() {}
    
    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(int jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    
    // Misc
    @MethodIgnorable
    public String getJk_full() { return jenis_kelamin == 1 ? "Laki-laki" : "Perempuan"; }
    @MethodIgnorable
    public String getRole_full() { return role == 1 ? "Staff" : "Dokter"; }
}
