/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.extras.ActionButtonTableCell;
import me.gaestra.sysinforekamedis.helper.ControlTransformHelper;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.helper.UtilityMiscHelper;
import me.gaestra.sysinforekamedis.model.Obat;
import me.gaestra.sysinforekamedis.model.Pasien;
import me.gaestra.sysinforekamedis.model.Pembayaran;
import me.gaestra.sysinforekamedis.model.Recipe;
import me.gaestra.sysinforekamedis.model.Stock;

/**
 *
 * @author Ganesha
 */
public class PembayaranFormController {
    
    // Core
    public Recipe selectedModel;
    
    // <editor-fold defaultstate="collapsed" desc="FXML Control Component">
    // Main
    public JFXTextField pembayaranMainKode;
    public Label pembayaranMainNomor;
    public Label pembayaranMainNama;
    public Label pembayaranMainUmur;
    public Label pembayaranMainJK;
    public Label pembayaranMainLayanan;
    public Label pembayaranMainTotal;
    public Label pembayaranMainNamaStaff;
    public TableView pembayaranMainObatTable;
    public JFXButton pembayaranGotoHistory;
    public JFXButton pembayaranMainCetakButton;
    
    // History
    public JFXComboBox pembayaranHistoryTipeSearch;
    public JFXComboBox pembayaranHistorySelectModel;
    public JFXTextField pembayaranHistoryKode;
    public JFXDatePicker pembayaranHistoryTanggal;
    public TableView pembayaranHistoryListTable;
    
    // </editor-fold>
    
    public PembayaranFormController(MainFormController ctl) {
        setupControl(ctl);
        hookMethodControl();
        
        setupMainView();
        setupHistoryView();
    }
    
    public void setupControl(MainFormController ctl) {
        pembayaranMainKode = ctl.pembayaranMainKode;
        pembayaranMainNomor = ctl.pembayaranMainNomor;
        pembayaranMainNama = ctl.pembayaranMainNama;
        pembayaranMainUmur = ctl.pembayaranMainUmur;
        pembayaranMainJK = ctl.pembayaranMainJK;
        pembayaranMainLayanan = ctl.pembayaranMainLayanan;
        pembayaranMainTotal = ctl.pembayaranMainTotal;
        pembayaranMainNamaStaff = ctl.pembayaranMainNamaStaff;
        pembayaranMainObatTable = ctl.pembayaranMainObatTable;
        pembayaranGotoHistory = ctl.pembayaranGotoHistory;
        pembayaranMainCetakButton = ctl.pembayaranMainCetakButton;
        pembayaranHistoryTipeSearch = ctl.pembayaranHistoryTipeSearch;
        pembayaranHistorySelectModel = ctl.pembayaranHistorySelectModel;
        pembayaranHistoryKode = ctl.pembayaranHistoryKode;
        pembayaranHistoryTanggal = ctl.pembayaranHistoryTanggal;
        pembayaranHistoryListTable = ctl.pembayaranHistoryListTable;
    }
    
    public void hookMethodControl() {
        pembayaranMainCetakButton.setOnAction((e) -> printAndAddModel());
        pembayaranMainKode.setOnAction((e) -> onEnterRecipeKode());
        
        pembayaranHistoryTipeSearch.setOnAction((e) -> onSearchTypeChanged());
        pembayaranHistorySelectModel.setOnAction((e) -> onSearchInputChanged());
        pembayaranHistoryTanggal.setOnAction((e) -> onSearchInputChanged());
        pembayaranHistoryKode.textProperty().addListener((obs, oldval, newval) -> onSearchInputChanged());
    }
    
    public void setupMainView() {
        TableColumn column1 = ControlTransformHelper.createColumn("Nama Obat", "nama", pembayaranMainObatTable, 0.33);
        TableColumn column2 = ControlTransformHelper.createColumn("Harga per unit", "harga_full", pembayaranMainObatTable, 0.33);
        TableColumn column3 = ControlTransformHelper.createColumn("Qty.", "stock_keluar", pembayaranMainObatTable, 0.33);
        
        pembayaranMainObatTable.getColumns().addAll(column1, column2, column3);
        
        pembayaranMainNamaStaff.setText(App.loggedUser.nama);
    }
    
    public void setupHistoryView() {
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Pembayaran", "id_full", pembayaranHistoryListTable, 0.25);
        TableColumn column2 = ControlTransformHelper.createColumn("Kode Resep", "rsp_id", pembayaranHistoryListTable, 0.25);
        TableColumn column3 = ControlTransformHelper.createColumn("Tanggal Transaksi", "created_at", pembayaranHistoryListTable, 0.25);
        TableColumn column4 = new TableColumn("Cetak");
        column4.prefWidthProperty().bind(pembayaranHistoryListTable.widthProperty().multiply(0.25));
        
        column4.setCellFactory(ActionButtonTableCell.<Pembayaran>forTableColumn("Cetak", "bg-primary, text-white", FontAwesomeIcon.PRINT, (Pembayaran data) -> {
            this.showCetakPopup(data);
            return data;
        }));
        
        pembayaranHistoryListTable.getColumns().addAll(column1, column2, column3, column4);
        
        pembayaranHistoryTipeSearch.getItems().add("Kode PMB / RSP");
        pembayaranHistoryTipeSearch.getItems().add("Nomor Pasien");
        
        // Disable Component
        pembayaranHistorySelectModel.setDisable(true); pembayaranHistoryKode.setDisable(true);
    }
    
    public void printAndAddModel() {
        if (selectedModel == null)
            return;
        
        Pembayaran model = new Pembayaran();
        
        model.pegawai_id = App.loggedUser.id; // Staff Logged In Id
        model.recipe_id = selectedModel.id;
        model.created_at = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        
        model.save();
        
        model.id = Pembayaran.latestId(() -> new Pembayaran());
        
        Stock today = Stock.today();
        if (!today.exist())
            today = Stock.createStockForToday();
        
        for (Obat item : selectedModel.obatCol)
            today.addObatStockKeluar(item, item.stock_keluar);
        
        today.parseFromObatCol(); today.save();
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Pembayaran Tercetak", "Input data berhasil di simpan", 
                    "Pembayaran yang di masukan telah tersimpan di database dan bisa di cetak!");
        
        // Show Print Pane
        this.showCetakPopup(model);
        
        pembayaranMainKode.setText("");
        this.truncateMainInput();
    }
    
    public void onEnterRecipeKode() {
        if (pembayaranMainKode.getText().isEmpty() || Recipe.count(() -> new Recipe()) == 0)
            return;
        
        selectedModel = null;
        for (Recipe resep : Recipe.all(() -> new Recipe())) {
            if (resep.getId_full().equals(pembayaranMainKode.getText())) {
                Pasien pasien = resep.Pasien();
                
                pembayaranMainNomor.setText(pasien.getId_full());
                pembayaranMainNama.setText(pasien.nama);
                pembayaranMainUmur.setText(pasien.umur);
                pembayaranMainJK.setText(pasien.getJk_str());
                pembayaranMainLayanan.setText(pasien.layanan);
                pembayaranMainTotal.setText("Rp. " + UtilityMiscHelper.formatCurrency(String.valueOf(resep.getTotalTagihan())));
                
                pembayaranMainObatTable.getItems().clear();
                resep.parseToObatCol();
                for (Obat item : resep.obatCol)
                    pembayaranMainObatTable.getItems().add(item);
                
                selectedModel = resep;
                break;
            }
        }
        
        if (selectedModel == null)
            truncateMainInput();
    }
    
    public void onSearchTypeChanged() {
        String sel = pembayaranHistoryTipeSearch.getValue().toString();
        pembayaranHistorySelectModel.getItems().clear();
        if (sel.equals("Kode PMB / RSP")) {
            pembayaranHistorySelectModel.setDisable(true);
            pembayaranHistoryKode.setDisable(false);
        }
        else if (sel.equals("Nomor Pasien")) {
            pembayaranHistoryKode.setText("");
            pembayaranHistoryKode.setDisable(true);
            pembayaranHistorySelectModel.setDisable(false);
            
            for (Pasien item : Pasien.all(() -> new Pasien()))
                pembayaranHistorySelectModel.getItems().add(item.getId_full() + " - " + item.nama);
        }
        pembayaranHistoryListTable.getItems().clear();
    }
    
    public void onSearchInputChanged() {
        String sel = pembayaranHistoryTipeSearch.getValue().toString(); pembayaranHistoryListTable.getItems().clear();
        for (Pembayaran data : Pembayaran.all(() -> new Pembayaran())) {
            try {
                if (!data.Recipe().exist() || !data.Recipe().Pasien().exist()
                        || pembayaranHistoryTanggal.getValue() != null && !data.created_at.split(" ")[0].equals(pembayaranHistoryTanggal.getValue().toString()))
                    continue;

                if (sel.equals("Kode PMB / RSP")) {
                    if (pembayaranHistoryKode.getText().isEmpty())
                        return;

                    if (data.getId_full().contains(pembayaranHistoryKode.getText())) {
                        pembayaranHistoryListTable.getItems().add(data);
                    }
                    else if (data.Recipe().getId_full().contains(pembayaranHistoryKode.getText())) {
                        pembayaranHistoryListTable.getItems().add(data);
                    }
                }
                else {
                    if (pembayaranHistorySelectModel.getValue() == null)
                        return;

                    String val = pembayaranHistorySelectModel.getValue().toString();

                    if (sel.equals("Nomor Pasien") && data.Recipe().Pasien().getId_full().contains(val.split("-")[0].trim())) {
                        pembayaranHistoryListTable.getItems().add(data);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void showCetakPopup(Pembayaran data) {
        try {
            FXMLLoader loader = App.getFXMLLoader("view/CetakStrukPembForm");
            Parent layout = (Parent)loader.load();
            CetakStrukPembFormController popupController = (CetakStrukPembFormController)loader.getController();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            popupStage.getIcons().add(App.getIconImage());
            popupStage.setTitle("Cetak Struk: " + data.getId_full()+ " | " + data.Recipe().Pasien().nama);
            
            popupStage.setResizable(false);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setScene(scene);
            popupStage.show();
            
            popupController.stage = popupStage;
            popupController.initContent(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void truncateMainInput() {
        pembayaranMainNomor.setText("");
        pembayaranMainNama.setText("");
        pembayaranMainUmur.setText("");
        pembayaranMainJK.setText("");
        pembayaranMainLayanan.setText("");
        pembayaranMainTotal.setText("");
        pembayaranMainObatTable.getItems().clear();
    }
}
