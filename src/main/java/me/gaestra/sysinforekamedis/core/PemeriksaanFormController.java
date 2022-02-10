/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import me.gaestra.sysinforekamedis.helper.ControlTransformHelper;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.model.Obat;
import me.gaestra.sysinforekamedis.model.Recipe;

/**
 *
 * @author Ganesha
 */
public class PemeriksaanFormController {
    
    // Core;
    public Recipe selectedModel;
    
    // <editor-fold defaultstate="collapsed" desc="FXML Control Component">
    // Main
    public JFXButton pemeriksaanMainSelectedLabel;
    public JFXTextField pemeriksaanMainNomor;
    public JFXTextField pemeriksaanMainNama;
    public JFXTextArea pemeriksaanMainDiagnosa;
    public TableView pemeriksaanMainObatTable;
    public JFXButton pemeriksaanMainDeleteButton;
    public TableView pemeriksaanMainListTable;
    
    // </editor-fold>
    
    public PemeriksaanFormController(MainFormController ctl) {
        setupControl(ctl);
        hookMethodControl();
        
        setupMainView();
    }
    
    public void setupControl(MainFormController ctl) {
        pemeriksaanMainSelectedLabel = ctl.pemeriksaanMainSelectedLabel;
        pemeriksaanMainNomor = ctl.pemeriksaanMainNomor;
        pemeriksaanMainNama = ctl.pemeriksaanMainNama;
        pemeriksaanMainDiagnosa = ctl.pemeriksaanMainDiagnosa;
        pemeriksaanMainObatTable = ctl.pemeriksaanMainObatTable;
        pemeriksaanMainDeleteButton = ctl.pemeriksaanMainDeleteButton;
        pemeriksaanMainListTable = ctl.pemeriksaanMainListTable;
    }
    
    public void hookMethodControl() {
        pemeriksaanMainListTable.setOnMouseClicked((e) -> onSelectModel(e));
        pemeriksaanMainDeleteButton.setOnAction((e) -> deleteModel());
    }
    
    public void setupMainView() {
        TableColumn column1 = ControlTransformHelper.createColumn("Nomor Resep", "id_full", pemeriksaanMainListTable, 0.25);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Pasien", "nama_pasien", pemeriksaanMainListTable, 0.25);
        TableColumn column3 = ControlTransformHelper.createColumn("Pemeriksa", "nama_pegawai", pemeriksaanMainListTable, 0.25);
        TableColumn column4 = ControlTransformHelper.createColumn("Diagnosa", "diagnosa", pemeriksaanMainListTable, 0.25);
        
        pemeriksaanMainListTable.getColumns().addAll(column1, column2, column3, column4);
        
        TableColumn obatcolumn1 = ControlTransformHelper.createColumn("Kode Obat", "kode", pemeriksaanMainObatTable, 0.33);
        TableColumn obatcolumn2 = ControlTransformHelper.createColumn("Nama Obat", "nama", pemeriksaanMainObatTable, 0.33);
        TableColumn obatcolumn3 = ControlTransformHelper.createColumn("Qty.", "stock_keluar", pemeriksaanMainObatTable, 0.33);
        
        pemeriksaanMainObatTable.getColumns().addAll(obatcolumn1, obatcolumn2, obatcolumn3);
        
        this.refreshTable();
        
        // Disable Component
        pemeriksaanMainNomor.setDisable(true); pemeriksaanMainNama.setDisable(true); pemeriksaanMainDiagnosa.setDisable(true);
    }
    
    public void deleteModel() {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Delete Data", "Hapus Data", 
                        "Data yang terpilih jika di lanjutkan akan di hapus dari database. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        selectedModel.delete();
        pemeriksaanMainListTable.getItems().remove(selectedModel);
        this.truncateMainInput();
    }
    
    public void onSelectModel(MouseEvent event) {
        if (event.getClickCount() != 2 || pemeriksaanMainListTable.getItems().size() <= 0) //Checking double click
            return;
        
        if (selectedModel != null && selectedModel.isEdited && 
                !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Discard Change", "Buang perubahan", 
                        "Data yang sedang di pilih telah di edit dan belum di save, jika melanjutkan perubahan akan di buang. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        if (selectedModel != null) 
            selectedModel.isEdited = false;
        
        selectedModel = (Recipe)pemeriksaanMainListTable.getSelectionModel().getSelectedItem();
        pemeriksaanMainSelectedLabel.setText(selectedModel.getId_full());
        
        pemeriksaanMainNomor.setText(selectedModel.Pasien().getId_full());
        pemeriksaanMainNama.setText(selectedModel.Pasien().nama);
        pemeriksaanMainDiagnosa.setText(selectedModel.diagnosa);
        
        selectedModel.parseToObatCol();
        pemeriksaanMainObatTable.getItems().clear();
        
        for (Obat item : selectedModel.obatCol) 
            pemeriksaanMainObatTable.getItems().add(item);
    }
    
    public void truncateMainInput() {
        pemeriksaanMainNomor.setText("");
        pemeriksaanMainNama.setText("");
        pemeriksaanMainDiagnosa.setText("");
        pemeriksaanMainObatTable.getItems().clear();
        
        pemeriksaanMainSelectedLabel.setText("None");
        selectedModel = null;
    }
    
    public void refreshTable() {
        pemeriksaanMainListTable.getItems().clear();
        for (Recipe item : Recipe.all(() -> new Recipe())) {
            pemeriksaanMainListTable.getItems().add(item);
        }
    }
}
