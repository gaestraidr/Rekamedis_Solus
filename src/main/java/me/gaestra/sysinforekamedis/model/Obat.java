/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.model;

import me.gaestra.sysinforekamedis.extras.annotations.MethodIgnorable;
import me.gaestra.sysinforekamedis.helper.UtilityMiscHelper;

/**
 *
 * @author Ganesha
 */
public class Obat extends Model {
    
    public String kode;
    public String nama;
    public int harga;
    public int stock = 0;
    
    // Attribute only for Stock
    public int stock_awal = 0;
    public int stock_masuk = 0;
    public int stock_keluar = 0;
    
    public Obat() {}

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    // Misc
    @MethodIgnorable
    public String getHarga_full() { return "Rp. " + UtilityMiscHelper.formatCurrency(String.valueOf(this.harga)); }
    
    @MethodIgnorable
    public String getTotal_full() { return "Rp. " + UtilityMiscHelper.formatCurrency(String.valueOf(this.harga * this.stock_keluar)); }
    
    @MethodIgnorable
    public int getStock_awal() {
        return stock_awal;
    }
    
    @MethodIgnorable
    public int getStock_masuk() {
        return stock_masuk;
    }
    
    @MethodIgnorable
    public int getStock_keluar() {
        return stock_keluar;
    }
    
    // Helper for Stock
    @MethodIgnorable
    public static int getTotalStockKeluar(Obat obat) {
        int total = 0;
        
        for (Pembayaran pemb : Pembayaran.where("DATE(created_at) = CURDATE()", () -> new Pembayaran())) {
            Recipe resep = pemb.Recipe();
            resep.parseToObatCol();
            
            if (resep.obatCol.contains(obat)) {
                total += resep.obatCol.get(resep.obatCol.indexOf(obat)).stock_keluar;
            }
        }
        
        obat.stock_keluar = total;
        return total;
    }
}
