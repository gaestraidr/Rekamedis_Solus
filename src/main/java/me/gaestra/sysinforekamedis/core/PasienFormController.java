/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.helper.ControlTransformHelper;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.model.Pasien;

/**
 *
 * @author Ganesha
 */
public class PasienFormController {
    
    public Pasien selectedModel;
    
    // <editor-fold defaultstate="collapsed" desc="FXML Control Component">
    // Main
    public JFXButton pasienMainSelectedLabel;
    public JFXTextField pasienMainNomorAnt;
    public JFXTextField pasienMainNama;
    public JFXTextField pasienMainUmur;
    public ToggleGroup pasienMainJK;
    public JFXTextArea pasienMainAlamat;
    public JFXComboBox pasienMainLayanan;
    public ToggleGroup pasienMainUrgent;
    public JFXButton pasienMainUpdateButton;
    public JFXButton pasienMainDeleteButton;
    public JFXButton pasienMainCKButton;
    public TableView pasienMainListTable;
    
    // Create
    public JFXTextField pasienCreateNomorAnt;
    public JFXTextField pasienCreateNama;
    public JFXTextField pasienCreateUmur;
    public ToggleGroup pasienCreateJK;
    public JFXTextArea pasienCreateAlamat;
    public JFXComboBox pasienCreateLayanan;
    public ToggleGroup pasienCreateUrgent;
    public JFXButton pasienCreateTambahButton;
    public JFXButton pasienCreateCetakButton;
    // </editor-fold>
    
    public PasienFormController(MainFormController ctl) {
        setupControl(ctl);
        hookMethodControl();
        
        setupMainView();
        setupCreateView();
    }
    
    public void setupControl(MainFormController ctl) {
        pasienMainSelectedLabel = ctl.pasienMainSelectedLabel;
        pasienMainNomorAnt = ctl.pasienMainNomorAnt;
        pasienMainNama = ctl.pasienMainNama;
        pasienMainUmur = ctl.pasienMainUmur;
        pasienMainJK = ctl.pasienMainJK;
        pasienMainAlamat = ctl.pasienMainAlamat;
        pasienMainLayanan = ctl.pasienMainLayanan;
        pasienMainUrgent = ctl.pasienMainUrgent;
        pasienMainUpdateButton = ctl.pasienMainUpdateButton;
        pasienMainDeleteButton = ctl.pasienMainDeleteButton;
        pasienMainCKButton = ctl.pasienMainCKButton;
        pasienMainListTable = ctl.pasienMainListTable;
        pasienCreateNomorAnt = ctl.pasienCreateNomorAnt;
        pasienCreateNama = ctl.pasienCreateNama;
        pasienCreateUmur = ctl.pasienCreateUmur;
        pasienCreateJK = ctl.pasienCreateJK;
        pasienCreateAlamat = ctl.pasienCreateAlamat;
        pasienCreateLayanan = ctl.pasienCreateLayanan;
        pasienCreateUrgent = ctl.pasienCreateUrgent;
        pasienCreateTambahButton = ctl.pasienCreateTambahButton;
        pasienCreateCetakButton = ctl.pasienCreateCetakButton;  
    }
    
    public void hookMethodControl() {
        pasienMainListTable.setOnMouseClicked((e) -> onSelectModel(e));
        pasienMainUpdateButton.setOnAction((e) -> updateModel());
        pasienMainDeleteButton.setOnAction((e) -> deleteModel());
        pasienMainCKButton.setOnAction((e) -> {
            if (selectedModel == null)
                return;
            
            showCetakPopup(selectedModel);
        });
        
        pasienCreateTambahButton.setOnAction((e) -> addModel());
        pasienCreateCetakButton.setOnAction((e) -> addModel(true));
    }
    
    public void setupMainView() {
        TableColumn column1 = ControlTransformHelper.createColumn("Nomor Antrian", "id_full", pasienMainListTable, 0.14);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Pasien", "nama", pasienMainListTable, 0.14);
        TableColumn column3 = ControlTransformHelper.createColumn("Umur Pasien", "umur", pasienMainListTable, 0.14);
        TableColumn column4 = ControlTransformHelper.createColumn("Jenis Kelamin", "jk_str", pasienMainListTable, 0.14);
        TableColumn column5 = ControlTransformHelper.createColumn("Alamat", "alamat", pasienMainListTable, 0.14);
        TableColumn column6 = ControlTransformHelper.createColumn("Layanan", "layanan", pasienMainListTable, 0.14);
        TableColumn column7 = ControlTransformHelper.createColumn("Pasien Urgent", "urgent_str", pasienMainListTable, 0.14);
        
        pasienMainListTable.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7);
        
        this.refreshTable();
        
        pasienMainLayanan.getItems().add("Poli 1");
        pasienMainLayanan.getItems().add("Poli 2");
        pasienMainLayanan.getItems().add("Poli 3");
        pasienMainLayanan.getItems().add("Poli 4");
        
        pasienMainLayanan.getSelectionModel().selectFirst();
        
        // Disable Component
        pasienMainNomorAnt.setDisable(true); pasienMainNama.setDisable(true); pasienMainUmur.setDisable(true); 
        pasienMainAlamat.setDisable(true); pasienMainLayanan.setDisable(true);
    }
    
    public void setupCreateView() {
        pasienCreateLayanan.getItems().add("Poli 1");
        pasienCreateLayanan.getItems().add("Poli 2");
        pasienCreateLayanan.getItems().add("Poli 3");
        pasienCreateLayanan.getItems().add("Poli 4");
        
        // Disable Component
        pasienCreateNomorAnt.setDisable(true);
        
        pasienCreateNomorAnt.setText(this.getNewAntrianId());
        pasienCreateUrgent.selectedToggleProperty().addListener((toggle, oldVal, newVal) -> {
//            System.out.println("toggled");
            pasienCreateNomorAnt.setText(getNewAntrianId());
        });
    }
    
    public void addModel() {
        addModel(false);
    }
    
    public void addModel(boolean cetak) {
        if (!this.verifyInput()) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input data Invalid", 
                    "Input data yang di masukan tidak lengkap / invalid, mohon cek kembali!");
            return;
        }
        
        Pasien model = new Pasien();
        
        model.nama = pasienCreateNama.getText();
        model.umur = pasienCreateUmur.getText();
        model.jenis_kelamin = pasienCreateJK.getToggles().indexOf(pasienCreateJK.getSelectedToggle()) + 1;
        model.alamat = pasienCreateAlamat.getText();
        model.layanan = pasienCreateLayanan.getValue().toString();
        model.urgent = pasienCreateUrgent.getToggles().indexOf(pasienCreateUrgent.getSelectedToggle()) == 1;
        
        model.save();
        
        model.id = Pasien.latestId(() -> new Pasien());
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Input Tersimpan", "Input data berhasil di simpan", 
                    "Input data yang di masukan telah tersimpan di database!");
        this.truncateInput();
        this.refreshTable();
        
        if (cetak)
            showCetakPopup(model);
    }
    
    public void updateModel() {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Update Data", "Ubah Data", 
                        "Perubahan di Data yang sedang di pilih jika di lanjutkan akan terubah dengan input yang di masukan. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        int index = pasienMainListTable.getItems().indexOf(selectedModel);
        
        selectedModel.nama = pasienMainNama.getText();
        selectedModel.umur = pasienMainUmur.getText();
        selectedModel.jenis_kelamin = pasienMainJK.getToggles().indexOf(pasienMainJK.getSelectedToggle()) + 1;
        selectedModel.alamat = pasienMainAlamat.getText();
        selectedModel.layanan = pasienMainLayanan.getValue().toString();
        selectedModel.urgent = pasienMainUrgent.getToggles().indexOf(pasienMainUrgent.getSelectedToggle()) == 1;
        
        selectedModel.save();
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Data Changed", "Data berhasil di ubah", "Data telah berhasil di ubah dan tersimpan di database!");
        pasienMainListTable.getItems().set(index, selectedModel);
        
        this.truncateMainInput();
    }
    
    public void deleteModel() {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Delete Data", "Hapus Data", 
                        "Data yang terpilih jika di lanjutkan akan di hapus dari database. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        selectedModel.delete();
        pasienMainListTable.getItems().remove(selectedModel);
        this.truncateMainInput();
    }
    
    public void onSelectModel(MouseEvent event) {
        if (event.getClickCount() != 2 || pasienMainListTable.getItems().size() <= 0) //Checking double click
            return;
        
        if (selectedModel != null && selectedModel.isEdited && 
                !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Discard Change", "Buang perubahan", 
                        "Data yang sedang di pilih telah di edit dan belum di save, jika melanjutkan perubahan akan di buang. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        if (selectedModel != null) 
            selectedModel.isEdited = false;
        
        // Enable Component
        pasienMainNama.setDisable(false); pasienMainUmur.setDisable(false); 
        pasienMainAlamat.setDisable(false); pasienMainLayanan.setDisable(false);
        
        selectedModel = (Pasien)pasienMainListTable.getSelectionModel().getSelectedItem();
        pasienMainSelectedLabel.setText(selectedModel.getId_full());
        
        pasienMainNomorAnt.setText(selectedModel.getId_full());
        pasienMainNama.setText(selectedModel.nama);
        pasienMainUmur.setText(selectedModel.umur);
        pasienMainJK.getToggles().get(selectedModel.jenis_kelamin - 1).setSelected(true);
        pasienMainAlamat.setText(selectedModel.alamat);
        pasienMainLayanan.getSelectionModel().select(selectedModel.layanan);
        pasienMainUrgent.getToggles().get(selectedModel.urgent ? 1 : 0).setSelected(true);

    }
    
    public void showCetakPopup(Pasien data) {
        try {
            FXMLLoader loader = App.getFXMLLoader("view/CetakKartuPasienForm");
            Parent layout = (Parent)loader.load();
            CetakKartuPasienFormController popupController = (CetakKartuPasienFormController)loader.getController();
            Scene scene = new Scene(layout);
            Stage popupStage = new Stage();
            popupStage.getIcons().add(App.getIconImage());
            popupStage.setTitle("Cetak Kartu: " + data.getId_full() + " | " + data.nama);
            
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
        return !pasienCreateNama.getText().isEmpty() &&
                !pasienCreateUmur.getText().isEmpty() &&
                !pasienCreateAlamat.getText().isEmpty() &&
                !pasienCreateLayanan.getValue().toString().isEmpty();
    }
    
    public void truncateInput() {
        pasienCreateNama.setText("");
        pasienCreateUmur.setText("");
        pasienCreateAlamat.setText("");
        pasienCreateLayanan.getSelectionModel().selectFirst();
        pasienCreateJK.getToggles().get(0).setSelected(true);
        pasienCreateUrgent.getToggles().get(0).setSelected(true);
        
        pasienCreateNomorAnt.setText(this.getNewAntrianId());
    }
    
    public void truncateMainInput() {
        pasienMainNama.setText("");
        pasienMainUmur.setText("");
        pasienMainAlamat.setText("");
        pasienMainLayanan.getSelectionModel().selectFirst();
        pasienMainJK.getToggles().get(0).setSelected(true);
        pasienMainUrgent.getToggles().get(0).setSelected(true);
        
        pasienMainNama.setDisable(true); pasienMainUmur.setDisable(true); 
        pasienMainAlamat.setDisable(true); pasienMainLayanan.setDisable(true);
        
        pasienMainSelectedLabel.setText("None");
        
        selectedModel = null;
    }
    
    public void refreshTable() {
        pasienMainListTable.getItems().clear();
        for (Pasien model : Pasien.all(() -> new Pasien()))
            pasienMainListTable.getItems().add(model);
    }
    
    public String getNewAntrianId() {
        boolean urgent = pasienCreateUrgent.getToggles().indexOf(pasienCreateUrgent.getSelectedToggle()) == 1;
        return (urgent ? "URG" : "ANT") + String.format("%04d", Pasien.count(() -> new Pasien()) + 1);
    }
}
