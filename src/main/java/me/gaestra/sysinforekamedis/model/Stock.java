/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import me.gaestra.sysinforekamedis.extras.annotations.MethodIgnorable;

/**
 *
 * @author Ganesha
 */
public class Stock extends Model {
    
    public String obat;
    public String created_at;
    
    // Attribute for apps
    public LinkedList<Obat> obatCol = new LinkedList();
    
    public Stock() {}

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
    
    // Misc
    public static Stock today() {
        return Stock.first("DATE(created_at) = CURDATE()", () -> new Stock());
    }
    
    public static Stock yesterday() {
        return Stock.first("DATE(created_at) = CURDATE() - 1", () -> new Stock());
    }
    
    public static List<Stock> beforeToday() {
        return Stock.where("DATE(created_at) <= CURDATE() ORDER BY created_at DESC", () -> new Stock());
    }
    
    public static Stock createStockForToday() {
        if (!today().exist()) {
            Stock stock = new Stock();
            stock.created_at = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
            
            Stock yesterday = yesterday();
            yesterday.parseToObatCol();
            
            for (Obat item : yesterday.obatCol) {
                item.stock_awal = (item.stock_awal + item.stock_masuk) - item.stock_keluar;
                item.stock_masuk = 0;
                item.stock_keluar = Obat.getTotalStockKeluar(item);
                
                stock.obatCol.add(item);
            }
            
            for (Obat item : Obat.all(() -> new Obat())) {
                if (stock.obatCol.indexOf(item) == -1)
                    stock.obatCol.add(item);
            }

            stock.parseFromObatCol();
            stock.save();
        }
        
        return today();
    }
    
    public void addObatStockKeluar(Obat item, int quantity) {
        int index = obatCol.indexOf(item);
        item.stock_keluar = quantity;
        
        if (index == -1) {
            obatCol.add(item);
        }
        else {
            Obat inStock = obatCol.get(index);
            inStock.stock_keluar += quantity;
            
            obatCol.set(index, inStock);
        }
    }
    
    public void parseToObatCol() {
        if (obat == null || obat.isEmpty())
            return;
        
        obatCol.clear();
        String[] col = obat.split(",");
        
        for (String colItem : col) {
            String[] item = colItem.split("x");
            Obat obat = Obat.find(Integer.valueOf(item[0]), () -> new Obat());
            if (obat.exist()) {
                obat.stock_awal = Integer.valueOf(item[1]);
                obat.stock_masuk = Integer.valueOf(item[2]);
                obat.stock_keluar = Integer.valueOf(item[3]);
                obatCol.add(obat);
            }
        }
    }
    
    public void parseFromObatCol() {
        String obatStr = "";
        
        for (var i = 0; i < obatCol.size(); i++) {
            Obat item = obatCol.get(i);
            obatStr += item.id + "x" + item.stock_awal + "x" + item.stock_masuk + "x" + item.stock_keluar + (i == (obatCol.size() - 1) ? "" : ",");
        }
        
        this.obat = obatStr;
    }
    
}
