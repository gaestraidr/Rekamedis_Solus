/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.model;

import java.util.LinkedList;
import me.gaestra.sysinforekamedis.extras.annotations.MethodIgnorable;

/**
 *
 * @author Ganesha
 */
public class Recipe extends Model {
    
    public int pasien_id;
    public int pegawai_id;
    public String diagnosa;
    public String obat;
    public String created_at;
    
    public LinkedList<Obat> obatCol = new LinkedList();

    public Recipe() {}
    
    public int getPasien_id() {
        return pasien_id;
    }

    public void setPasien_id(int pasien_id) {
        this.pasien_id = pasien_id;
    }

    public int getPegawai_id() {
        return pegawai_id;
    }

    public void setPegawai_id(int pegawai_id) {
        this.pegawai_id = pegawai_id;
    }

    public String getDiagnosa() {
        return diagnosa;
    }

    public void setDiagnosa(String diagnosa) {
        this.diagnosa = diagnosa;
    }

    public String getObat() {
        return obat;
    }

    public void setObat(String obat) {
        this.obat = obat;
    }
    
    public String getCreated_at() {
        return created_at;
    }
    
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    
    public Pasien Pasien() {
        return Pasien.find(this.pasien_id, () -> new Pasien());
    }
    
    public Pegawai Pegawai() {
        return Pegawai.find(this.pegawai_id, () -> new Pegawai());
    }
    
    // Misc
    @MethodIgnorable
    public String getId_full() { return "RSP" + String.format("%04d", this.getId()); }
    @MethodIgnorable
    public String getNama_pasien() { return this.Pasien().nama; }
    @MethodIgnorable
    public String getNama_pegawai() { return this.Pegawai().nama; }
    
    public void parseToObatCol() {
        if (obat == null || obat.isEmpty())
            return;
        
        obatCol.clear();
        String[] col = obat.split(",");
        
        for (String colItem : col) {
            String[] item = colItem.split("x");
            Obat obat = Obat.find(Integer.valueOf(item[0]), () -> new Obat());
            if (obat.exist()) {
                obat.stock_keluar = Integer.valueOf(item[1]);
                obatCol.add(obat);
            }
        }
    }
    
    public void parseFromObatCol() {
        String obatStr = "";
        
        for (var i = 0; i < obatCol.size(); i++) {
            Obat item = obatCol.get(i);
            obatStr += item.id + "x" + item.stock_keluar + (i == (obatCol.size() - 1) ? "" : ",");
        }
        
        this.obat = obatStr;
    }
    
    @MethodIgnorable
    public int getTotalTagihan() {
        int tagihan = 0;
        
        this.parseToObatCol();
        for (Obat item : this.obatCol) {
            tagihan += item.harga * item.stock_keluar;
        }
        
        return tagihan;
    }
}
