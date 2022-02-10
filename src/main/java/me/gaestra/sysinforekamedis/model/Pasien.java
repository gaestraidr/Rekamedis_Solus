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
public class Pasien extends Model {
    
    public String nama;
    public String umur;
    public int jenis_kelamin;
    public String alamat;
    public String layanan;
    public boolean urgent;
    
    public Pasien() {}
    
    // Getter
    public String getNama() { return this.nama; };
    public String getUmur() { return this.umur; };
    public int getJenis_kelamin() { return this.jenis_kelamin; };
    public String getAlamat() { return this.alamat; };
    public String getLayanan() { return this.layanan; };
    public boolean getUrgent() { return this.urgent; };
    
    // Setter
    public void setNama(String nama) { this.nama = nama; }
    public void setUmur(String umur) { this.umur = umur; }
    public void setJenis_kelamin(int jenis_kelamin) { this.jenis_kelamin = jenis_kelamin; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setLayanan(String layanan) { this.layanan = layanan; }
    public void setUrgent(boolean urgent) { this.urgent = urgent; }
    
    // Misc
    @MethodIgnorable
    public String getId_full() { return (this.urgent ? "URG" : "ANT") + String.format("%04d", this.getId()); }
    @MethodIgnorable
    public String getJk_str() { return this.jenis_kelamin == 1 ? "Laki-Laki" : "Perempuan"; }
    @MethodIgnorable 
    public String getUrgent_str(){ return this.urgent ? "Ya" : "Tidak"; }
    
    public static Pasien latestAntrianNoRecipe() {
        Pasien pasien = new Pasien();
        
        for (Pasien data : Pasien.all(() -> new Pasien())) {
            if (!data.urgent && !Recipe.first("pasien_id = " + data.id, () -> new Recipe()).exist()) {
                pasien = data;
                break;
            }
        }
        
        return pasien;
    }
}
