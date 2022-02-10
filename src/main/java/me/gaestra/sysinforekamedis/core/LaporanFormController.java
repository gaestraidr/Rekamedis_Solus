/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.model.Model;
import me.gaestra.sysinforekamedis.model.Pasien;
import me.gaestra.sysinforekamedis.model.Pegawai;
import me.gaestra.sysinforekamedis.model.Pembayaran;
import me.gaestra.sysinforekamedis.model.Recipe;
import me.gaestra.sysinforekamedis.model.Stock;

/**
 *
 * @author Ganesha
 */
public class LaporanFormController {
    
    public String fromDate;
    public String toDate;
    
    public JFXButton laporanMainPasien;
    public JFXButton laporanMainPemeriksaan;
    public JFXButton laporanMainPembayaran;
    public JFXButton laporanMainStock;
    public JFXButton laporanMainPegawai;
    public JFXDatePicker laporanMainFromDate;
    public JFXDatePicker laporanMainToDate;
    
    public LaporanFormController(MainFormController ctl) {
        setupControl(ctl);
        hookMethodControl();
    }
    
    public void setupControl(MainFormController ctl) {
        laporanMainPasien = ctl.laporanMainPasien;
        laporanMainPemeriksaan = ctl.laporanMainPemeriksaan;
        laporanMainPembayaran = ctl.laporanMainPembayaran;
        laporanMainStock = ctl.laporanMainStock;
        laporanMainPegawai = ctl.laporanMainPegawai;
        laporanMainFromDate = ctl.laporanMainFromDate;
        laporanMainToDate = ctl.laporanMainToDate;
    }
    
    public void hookMethodControl() {
        laporanMainPasien.setOnAction((e) -> printReport(e));
        laporanMainPemeriksaan.setOnAction((e) -> printReport(e));
        laporanMainPembayaran.setOnAction((e) -> printReport(e));
        laporanMainStock.setOnAction((e) -> printReport(e));
        laporanMainPegawai.setOnAction((e) -> printReport(e));
        
        laporanMainFromDate.setOnAction((e) -> checkDateInput(e));
        laporanMainToDate.setOnAction((e) -> checkDateInput(e));
    }
    
    public void printReport(ActionEvent e) {
        JFXButton btn = (JFXButton)e.getSource();
        String sel = btn.getText();
        
        if (sel.equals("Pasien")) {
            openForm(new Pasien());
        }
        else if (sel.equals("Pemeriksaan")) {
            openForm(new Recipe());
        }
        else if (sel.equals("Pembayaran")) {
            openForm(new Pembayaran());
        }
        else if (sel.equals("Stok Obat")) {
            openForm(new Stock());
        }
        else if (sel.equals("Pegawai")) {
            openForm(new Pegawai());
        }
    }
    
    public void openForm(Model model) {
        FXMLLoader loader = App.getFXMLLoader("view/RekapanForm");
        RekapanFormController controller = null;
        try {
            Parent layout = (Parent)loader.load();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            popupStage.getIcons().add(App.getIconImage());
            popupStage.setTitle(App.appTitle);
            controller = (RekapanFormController)loader.getController();
            controller.stage = popupStage;
            
            popupStage.setResizable(false);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            
            controller.setDate(laporanMainFromDate.getValue(), laporanMainToDate.getValue());
            controller.initRekapanData(model);
            
            popupStage.show();
            controller.doPrint();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void checkDateInput(ActionEvent e) {
        JFXDatePicker source = (JFXDatePicker)e.getSource();
        
        if (laporanMainFromDate.getValue() != null && laporanMainToDate.getValue() != null 
                && laporanMainFromDate.getValue().isAfter(laporanMainToDate.getValue())) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input Tanggal invalid", 
                    "'Dari Tanggal' tidak boleh melebihi 'Sampai Tanggal' input, mohon cek kembali!");
            source.setValue(null);
        }
    }
}
