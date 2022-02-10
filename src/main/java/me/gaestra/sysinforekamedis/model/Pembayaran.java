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
public class Pembayaran extends Model {
    
    public int recipe_id;
    public int pegawai_id;
    public String created_at;
    
    // Attribute for apps
    public int total_tagihan;

    public Pembayaran() {}
    
    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public int getPegawai_id() {
        return pegawai_id;
    }

    public void setPegawai_id(int pegawai_id) {
        this.pegawai_id = pegawai_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    
    public Recipe Recipe() {
        return Recipe.find(this.recipe_id, () -> new Recipe());
    }
    
    public Pegawai Pegawai() {
        return Pegawai.find(this.pegawai_id, () -> new Pegawai());
    }
    
    // Misc
    @MethodIgnorable
    public String getId_full() { return "PMB" + String.format("%04d", this.getId()); }
    @MethodIgnorable
    public String getRsp_id() { return "RSP" + String.format("%04d", this.Recipe().getId()); }
    @MethodIgnorable
    public String getNama_pasien() { return this.Recipe().Pasien().nama; }
    @MethodIgnorable
    public String getTotal_tagihan() { return "Rp." + UtilityMiscHelper.formatCurrency(String.valueOf(getTotalTagihan())); }
    @MethodIgnorable
    public int getTotalTagihan() {
        int tagihan = 0;
        Recipe resep = this.Recipe();
        
        if (resep.exist()) {
            resep.parseToObatCol();
            for (Obat item : resep.obatCol) {
                tagihan += item.harga * item.stock_keluar;
            }
        }
        
        this.total_tagihan = tagihan;
        return tagihan;
    }
}
