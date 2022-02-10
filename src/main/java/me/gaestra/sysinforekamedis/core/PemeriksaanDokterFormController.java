/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.extras.ActionButtonTableCell;
import me.gaestra.sysinforekamedis.helper.ControlTransformHelper;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.model.Obat;
import me.gaestra.sysinforekamedis.model.Pasien;
import me.gaestra.sysinforekamedis.model.Recipe;

/**
 *
 * @author Ganesha
 */
public class PemeriksaanDokterFormController {
    
    public int inputMode = 1;
    public Pasien selectedPasien;
    
    public JFXComboBox pemeriksaanMainModeInput;
    public JFXTextField pemeriksaanMainNomor;
    public JFXTextField pemeriksaanMainNama;
    public JFXTextField pemeriksaanMainUmur;
    public ToggleGroup pemeriksaanMainJK;
    public JFXTextArea pemeriksaanMainAlamat;
    public JFXComboBox pemeriksaanMainLayanan;
    public JFXButton pemeriksaanGotoHistory;
    public JFXTextArea pemeriksaanMainDiagnosa;
    public JFXComboBox pemeriksaanMainListObat;
    public JFXTextField pemeriksaanMainJumlahObat;
    public JFXButton pemeriksaanMainTambahObat;
    public JFXButton pemeriksaanMainCetakResep;
    public TableView pemeriksaanMainObatTable;
    public JFXButton pemeriksaanBacktoMain;
    public Label pemeriksaanHistoryTitle;
    public JFXComboBox pemeriksaanHistoryTipeSearch;
    public JFXComboBox pemeriksaanHistorySelectModel;
    public JFXTextField pemeriksaanHistoryKode;
    public JFXDatePicker pemeriksaanHistoryTanggal;
    public TableView pemeriksaanHistoryListTable;
    
    public PemeriksaanDokterFormController(MainFormDokterController ctl) {
        setupControl(ctl);
        hookMethodControl();
        
        setupMainView();
        setupHistoryView();
    }
    
    public void setupControl(MainFormDokterController ctl) {
        pemeriksaanMainModeInput = ctl.pemeriksaanMainModeInput;
        pemeriksaanMainNomor = ctl.pemeriksaanMainNomor;
        pemeriksaanMainNama = ctl.pemeriksaanMainNama;
        pemeriksaanMainUmur = ctl.pemeriksaanMainUmur;
        pemeriksaanMainJK = ctl.pemeriksaanMainJK;
        pemeriksaanMainAlamat = ctl.pemeriksaanMainAlamat;
        pemeriksaanMainLayanan = ctl.pemeriksaanMainLayanan;
        pemeriksaanGotoHistory = ctl.pemeriksaanGotoHistory;
        pemeriksaanMainDiagnosa = ctl.pemeriksaanMainDiagnosa;
        pemeriksaanMainListObat = ctl.pemeriksaanMainListObat;
        pemeriksaanMainJumlahObat = ctl.pemeriksaanMainJumlahObat;
        pemeriksaanMainTambahObat = ctl.pemeriksaanMainTambahObat;
        pemeriksaanMainCetakResep = ctl.pemeriksaanMainCetakResep;
        pemeriksaanMainObatTable = ctl.pemeriksaanMainObatTable;
        pemeriksaanBacktoMain = ctl.pemeriksaanBacktoMain;
        
        pemeriksaanHistoryTitle = ctl.pemeriksaanHistoryTitle;
        pemeriksaanHistoryTipeSearch = ctl.pemeriksaanHistoryTipeSearch;
        pemeriksaanHistorySelectModel = ctl.pemeriksaanHistorySelectModel;
        pemeriksaanHistoryKode = ctl.pemeriksaanHistoryKode;
        pemeriksaanHistoryTanggal = ctl.pemeriksaanHistoryTanggal;
        pemeriksaanHistoryListTable = ctl.pemeriksaanHistoryListTable;
    }
    
    public void hookMethodControl() {
        pemeriksaanMainModeInput.setOnAction((e) -> mainInputModeChanged());
        pemeriksaanMainNomor.setOnAction((e) -> onSearchNomorPasien());
        pemeriksaanMainTambahObat.setOnAction((e) -> addObatToList());
        pemeriksaanMainCetakResep.setOnAction((e) -> addAndCetakModel());
        
        pemeriksaanHistoryTipeSearch.setOnAction((e) -> onSearchTypeChanged());
        pemeriksaanHistorySelectModel.setOnAction((e) -> onSearchInputChanged());
        pemeriksaanHistoryTanggal.setOnAction((e) -> onSearchInputChanged());
        pemeriksaanHistoryKode.textProperty().addListener((obs, oldval, newval) -> onSearchInputChanged());
    }
    
    public void setupMainView() {
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Obat", "kode", pemeriksaanMainObatTable, 0.33);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Obat", "nama", pemeriksaanMainObatTable, 0.33);
        TableColumn column3 = ControlTransformHelper.createColumn("Qty.", "stock_keluar", pemeriksaanMainObatTable, 0.33);
        
        pemeriksaanMainObatTable.getColumns().addAll(column1, column2, column3);
        
        for (Obat item : Obat.all(() -> new Obat()))
            pemeriksaanMainListObat.getItems().add(item.kode + " - " + item.nama);
        
        pemeriksaanMainModeInput.getItems().add("Cari Nomor Pasien");
        pemeriksaanMainModeInput.getItems().add("Buat Pasien Urgent");
                
        pemeriksaanMainLayanan.getItems().add("Poli 1");
        pemeriksaanMainLayanan.getItems().add("Poli 2");
        pemeriksaanMainLayanan.getItems().add("Poli 3");
        pemeriksaanMainLayanan.getItems().add("Poli 4");
        
        pemeriksaanMainModeInput.getSelectionModel().selectFirst();
        pemeriksaanMainLayanan.getSelectionModel().selectFirst();
        
        this.refreshPasienBoxState();
    }
    
    public void setupHistoryView() {
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Resep", "id_full", pemeriksaanHistoryListTable, 0.25);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Pasien", "nama_pasien", pemeriksaanHistoryListTable, 0.25);
        TableColumn column3 = ControlTransformHelper.createColumn("Tanggal Cetak", "created_at", pemeriksaanHistoryListTable, 0.25);
        TableColumn column4 = new TableColumn("Cetak");
        column4.prefWidthProperty().bind(pemeriksaanHistoryListTable.widthProperty().multiply(0.25));
        
        column4.setCellFactory(ActionButtonTableCell.<Recipe>forTableColumn("Cetak", "bg-primary, text-white", FontAwesomeIcon.PRINT, (Recipe data) -> {
            this.showCetakPopup(data);
            return data;
        }));
        
        pemeriksaanHistoryListTable.getColumns().addAll(column1, column2, column3, column4);
        
        pemeriksaanHistoryTipeSearch.getItems().add("Kode RSP");
        pemeriksaanHistoryTipeSearch.getItems().add("Nomor Pasien");
        
        // Disable Component
        pemeriksaanHistorySelectModel.setDisable(true); pemeriksaanHistoryKode.setDisable(true);
    }
    
    public void onSearchTypeChanged() {
        String sel = pemeriksaanHistoryTipeSearch.getValue().toString();
        pemeriksaanHistorySelectModel.getItems().clear();
        if (sel.equals("Kode RSP")) {
            pemeriksaanHistorySelectModel.setDisable(true);
            pemeriksaanHistoryKode.setDisable(false);
        }
        else if (sel.equals("Nomor Pasien")) {
            pemeriksaanHistoryKode.setText("");
            pemeriksaanHistoryKode.setDisable(true);
            pemeriksaanHistorySelectModel.setDisable(false);
            
            for (Pasien item : Pasien.all(() -> new Pasien()))
                pemeriksaanHistorySelectModel.getItems().add(item.getId_full() + " - " + item.nama);
        }
        pemeriksaanHistoryListTable.getItems().clear();
    }
    
    public void onSearchInputChanged() {
        String sel = pemeriksaanHistoryTipeSearch.getValue().toString(); pemeriksaanHistoryListTable.getItems().clear();
        for (Recipe data : Recipe.all(() -> new Recipe())) {
            try {
                if (!data.Pasien().exist()
                        || pemeriksaanHistoryTanggal.getValue() != null && !data.created_at.split(" ")[0].equals(pemeriksaanHistoryTanggal.getValue().toString()))
                    continue;

                if (sel.equals("Kode RSP")) {
                    if (pemeriksaanHistoryKode.getText().isEmpty())
                        return;

                    if (data.getId_full().contains(pemeriksaanHistoryKode.getText())) {
                        pemeriksaanHistoryListTable.getItems().add(data);
                    }
                }
                else {
                    if (pemeriksaanHistorySelectModel.getValue() == null)
                        return;

                    String val = pemeriksaanHistorySelectModel.getValue().toString();

                    if (sel.equals("Nomor Pasien") && data.Pasien().getId_full().contains(val.split("-")[0].trim())) {
                        pemeriksaanHistoryListTable.getItems().add(data);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void addAndCetakModel() {
        if (!this.verifyInput()) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input data Invalid", 
                    "Input data yang di masukan tidak lengkap / invalid, mohon cek kembali!");
            return;
        }
        
        Recipe model = new Recipe();
        
        if (inputMode == 1) {
            model.pasien_id = selectedPasien.id;
        }
        else {
            Pasien pasien = new Pasien();
            
            pasien.nama = pemeriksaanMainNama.getText();
            pasien.umur = pemeriksaanMainUmur.getText();
            pasien.jenis_kelamin = pemeriksaanMainJK.getToggles().indexOf(pemeriksaanMainJK.getSelectedToggle()) + 1;
            pasien.alamat = pemeriksaanMainAlamat.getText();
            pasien.layanan = pemeriksaanMainLayanan.getValue().toString();
            pasien.urgent = true;
            
            pasien.save();
            pasien.id = Pasien.latestId(() -> new Pasien());
            
            model.pasien_id = pasien.id;
        }
        
        model.pegawai_id = App.loggedUser.id;
        model.diagnosa = pemeriksaanMainDiagnosa.getText();
        model.created_at = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());
        
        for (Object item : pemeriksaanMainObatTable.getItems()) 
            model.obatCol.add((Obat)item);
       
        model.parseFromObatCol();
        
        model.save();
        model.id = Recipe.latestId(() -> new Recipe());
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Input Tersimpan", "Input data berhasil di simpan", 
                    "Input data yang di masukan telah tersimpan di database!");
        
        this.truncateMainView();
        this.showCetakPopup(model);
    }
    
    public void addObatToList() {
        if (pemeriksaanMainListObat.getValue() == null || pemeriksaanMainListObat.getValue().toString().isEmpty() ||
                pemeriksaanMainJumlahObat.getText() == null || pemeriksaanMainJumlahObat.getText().isEmpty())
            return;
        
        String kode = pemeriksaanMainListObat.getValue().toString().split("-")[0].trim();
        Obat item = Obat.first("kode", kode, () -> new Obat());
        item.stock_keluar = Integer.valueOf(pemeriksaanMainJumlahObat.getText());
        pemeriksaanMainObatTable.getItems().add(item);
        
        this.truncateObatBox();
    }
    
    public void mainInputModeChanged() {
        if (pemeriksaanMainModeInput.getValue() == null || pemeriksaanMainModeInput.getValue().toString().isEmpty())
            return;
        
        inputMode = pemeriksaanMainModeInput.getSelectionModel().getSelectedIndex() + 1;
        this.refreshPasienBoxState();
    }
    
    public void refreshPasienBoxState() {
        if (inputMode == 1)  {
            // Enable Nomor
            pemeriksaanMainNomor.setDisable(false);
            
            // Disable Component
            pemeriksaanMainNama.setDisable(true); pemeriksaanMainUmur.setDisable(true); pemeriksaanMainAlamat.setDisable(true);
            pemeriksaanMainLayanan.setDisable(true);
        }
        else { // Input Mode Create New Urgent
            // Disable Nomor
            pemeriksaanMainNomor.setDisable(true);
            
            // Enable Component
            pemeriksaanMainNama.setDisable(false); pemeriksaanMainUmur.setDisable(false); pemeriksaanMainAlamat.setDisable(false);
            pemeriksaanMainLayanan.setDisable(false);
            
            selectedPasien = null;
        }
        
        this.truncatePasienBox();
    }
    
    public void onSearchNomorPasien() {
        if (inputMode == 2)
            return;
        
        String kode = pemeriksaanMainNomor.getText(); selectedPasien = null;
        for (Pasien data : Pasien.all(() -> new Pasien())) {
            if (data.getId_full().equals(kode)) {
                selectedPasien = data;
                break;
            }
        }
        
        if (selectedPasien != null) {
            pemeriksaanMainNama.setText(selectedPasien.nama);
            pemeriksaanMainUmur.setText(selectedPasien.umur);
            pemeriksaanMainAlamat.setText(selectedPasien.alamat);
            pemeriksaanMainLayanan.getSelectionModel().select(selectedPasien.layanan);
            pemeriksaanMainJK.getToggles().get(selectedPasien.jenis_kelamin - 1).setSelected(true);
        }
        else {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Not Found", "Input tidak ditemukan", 
                    "Input data yang di masukan tidak dapat di temukan, mohon cek kembali!");
            
            pemeriksaanMainNama.setText("");
            pemeriksaanMainUmur.setText("");
            pemeriksaanMainAlamat.setText("");
            pemeriksaanMainLayanan.getSelectionModel().selectFirst();
            pemeriksaanMainJK.getToggles().get(0).setSelected(true);
        }
    }
    
    public void showCetakPopup(Recipe data) {
        try {
            FXMLLoader loader = App.getFXMLLoader("view/CetakStrukRespForm");
            Parent layout = (Parent)loader.load();
            CetakStrukRespFormController popupController = (CetakStrukRespFormController)loader.getController();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            popupStage.getIcons().add(App.getIconImage());
            popupStage.setTitle("Cetak Struk: " + data.getId_full() + " | " + data.Pasien().nama);
            
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
    
    public boolean verifyInput() {
        return !pemeriksaanMainNomor.getText().isEmpty() &&
                !pemeriksaanMainDiagnosa.getText().isEmpty() &&
                !pemeriksaanMainListObat.getItems().isEmpty();
    }
    
    public void truncatePasienBox() {
        pemeriksaanMainNama.setText("");
        pemeriksaanMainUmur.setText("");
        pemeriksaanMainAlamat.setText("");
        pemeriksaanMainLayanan.getSelectionModel().selectFirst();
        pemeriksaanMainJK.getToggles().get(0).setSelected(true);
        
        if (inputMode == 1) {
            pemeriksaanMainNomor.setText("");
            selectedPasien = null;
        }
        else pemeriksaanMainNomor.setText(this.getNewAntrianId());
    }
    
    public void truncateObatBox() {
        pemeriksaanMainListObat.setValue(null);
        pemeriksaanMainJumlahObat.setText("");
    }
    
    public void truncateMainView() {
        pemeriksaanMainModeInput.getSelectionModel().selectFirst();
        this.truncatePasienBox();
        this.truncateObatBox();
        pemeriksaanMainDiagnosa.setText("");
        pemeriksaanMainObatTable.getItems().clear();
    }
    
    public String getNewAntrianId() {
        return "URG"  + String.format("%04d", Pasien.count(() -> new Pasien()) + 1);
    }
}
