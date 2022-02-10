/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import me.gaestra.sysinforekamedis.helper.ControlTransformHelper;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.model.Pegawai;

/**
 *
 * @author Ganesha
 */
public class PegawaiFormController {
    
    public Pegawai selectedModel;
    
    // <editor-fold defaultstate="collapsed" desc="FXML Control Component">
    // FXML Control Component
    public JFXButton pegawaiMainSelectedModel;
    public JFXTextField pegawaiMainNik;
    public JFXTextField pegawaiMainNama;
    public JFXTextField pegawaiMainJabatan;
    public JFXTextField pegawaiMainUsername;
    public JFXPasswordField pegawaiMainPassword;
    public ToggleGroup pegawaiMainJK;
    public ToggleGroup pegawaiMainRole;
    public JFXTextArea pegawaiMainAlamat;
    public JFXButton pegawaiMainUpdateButton;
    public JFXButton pegawaiMainDeleteButton;
    public TableView pegawaiMainListTable;
    public JFXTextField pegawaiCreateNik;
    public JFXTextField pegawaiCreateNama;
    public JFXTextField pegawaiCreateJabatan;
    public JFXTextField pegawaiCreateUsername;
    public JFXPasswordField pegawaiCreatePassword;
    public ToggleGroup pegawaiCreateJK;
    public ToggleGroup pegawaiCreateRole;
    public JFXTextArea pegawaiCreateAlamat;
    public JFXButton pegawaiCreateTambahButton;
    
    // </editor-fold>
    
    public PegawaiFormController(MainFormController ctl) {
        setupControl(ctl);
        hookMethodControl();
        
        setupMainView();
    }
    
    public void setupControl(MainFormController ctl) {
        pegawaiMainSelectedModel = ctl.pegawaiMainSelectedModel;
        pegawaiMainNik = ctl.pegawaiMainNik;
        pegawaiMainNama = ctl.pegawaiMainNama;
        pegawaiMainJabatan = ctl.pegawaiMainJabatan;
        pegawaiMainUsername = ctl.pegawaiMainUsername;
        pegawaiMainPassword = ctl.pegawaiMainPassword;
        pegawaiMainJK = ctl.pegawaiMainJK;
        pegawaiMainRole = ctl.pegawaiMainRole;
        pegawaiMainAlamat = ctl.pegawaiMainAlamat;
        pegawaiMainUpdateButton = ctl.pegawaiMainUpdateButton;
        pegawaiMainDeleteButton = ctl.pegawaiMainDeleteButton;
        pegawaiMainListTable = ctl.pegawaiMainListTable;
        pegawaiCreateNik = ctl.pegawaiCreateNik;
        pegawaiCreateNama = ctl.pegawaiCreateNama;
        pegawaiCreateJabatan = ctl.pegawaiCreateJabatan;
        pegawaiCreateUsername = ctl.pegawaiCreateUsername;
        pegawaiCreatePassword = ctl.pegawaiCreatePassword;
        pegawaiCreateJK = ctl.pegawaiCreateJK;
        pegawaiCreateRole = ctl.pegawaiCreateRole;
        pegawaiCreateAlamat = ctl.pegawaiCreateAlamat;
        pegawaiCreateTambahButton = ctl.pegawaiCreateTambahButton;
    }
    
    public void hookMethodControl() {
        pegawaiMainListTable.setOnMouseClicked((e) -> onSelectModel(e));
        pegawaiMainUpdateButton.setOnAction((e) -> updateModel());
        pegawaiMainDeleteButton.setOnAction((e) -> deleteModel());
        pegawaiCreateTambahButton.setOnAction((e) -> addModel());
    }
    
    public void setupMainView() {
        TableColumn column1 = ControlTransformHelper.createColumn("NIK", "nik", pegawaiMainListTable, 0.14);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama", "nama", pegawaiMainListTable, 0.14);
        TableColumn column3 = ControlTransformHelper.createColumn("Jabatan", "jabatan", pegawaiMainListTable, 0.14);
        TableColumn column4 = ControlTransformHelper.createColumn("Username", "username", pegawaiMainListTable, 0.14);
        TableColumn column5 = ControlTransformHelper.createColumn("Jenis Kelamin", "jk_full", pegawaiMainListTable, 0.14);
        TableColumn column6 = ControlTransformHelper.createColumn("Role", "role_full", pegawaiMainListTable, 0.14);
        TableColumn column7 = ControlTransformHelper.createColumn("Alamat", "alamat", pegawaiMainListTable, 0.14);
        
        pegawaiMainListTable.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7);
        
        this.refreshTable();
        
        // Disable Component
        pegawaiMainNik.setDisable(true); pegawaiMainNama.setDisable(true); pegawaiMainJabatan.setDisable(true); 
        pegawaiMainUsername.setDisable(true); pegawaiMainPassword.setDisable(true); pegawaiMainAlamat.setDisable(true);
    }
    
    public void addModel() {
        if (!this.verifyInput()) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input data Invalid", 
                    "Input data yang di masukan tidak lengkap / invalid, mohon cek kembali!");
            return;
        }
        
        Pegawai model = new Pegawai();
        
        model.nik = pegawaiCreateNik.getText();
        model.nama = pegawaiCreateNama.getText();
        model.jabatan = pegawaiCreateJabatan.getText();
        model.username = pegawaiCreateUsername.getText();
        model.password = pegawaiCreatePassword.getText();
        model.jenis_kelamin = pegawaiCreateJK.getToggles().indexOf(pegawaiCreateJK.getSelectedToggle()) + 1;
        model.role = pegawaiCreateRole.getToggles().indexOf(pegawaiCreateRole.getSelectedToggle()) + 1;
        model.alamat = pegawaiCreateAlamat.getText();
        
        model.save();
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Input Tersimpan", "Input data berhasil di simpan", 
                    "Input data yang di masukan telah tersimpan di database!");
        this.truncateInput();
        this.refreshTable();
    }
    
    public void updateModel() {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Update Data", "Ubah Data", 
                        "Perubahan di Data yang sedang di pilih jika di lanjutkan akan terubah dengan input yang di masukan. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        int index = pegawaiMainListTable.getItems().indexOf(selectedModel);
        
        selectedModel.nik = pegawaiMainNik.getText();
        selectedModel.nama = pegawaiMainNama.getText();
        selectedModel.jabatan = pegawaiMainJabatan.getText();
        selectedModel.username = pegawaiMainUsername.getText();
        
        if (!pegawaiMainPassword.getText().isEmpty())
            selectedModel.password = pegawaiMainPassword.getText();
        
        selectedModel.jenis_kelamin = pegawaiMainJK.getToggles().indexOf(pegawaiMainJK.getSelectedToggle()) + 1;
        selectedModel.role = pegawaiMainRole.getToggles().indexOf(pegawaiMainRole.getSelectedToggle()) + 1;
        selectedModel.alamat = pegawaiMainAlamat.getText();
        
        selectedModel.save();
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Data Changed", "Data berhasil di ubah", "Data telah berhasil di ubah dan tersimpan di database!");
        pegawaiMainListTable.getItems().set(index, selectedModel);
        
        this.truncateMainInput();
    }
    
    public void deleteModel() {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Delete Data", "Hapus Data", 
                        "Data yang terpilih jika di lanjutkan akan di hapus dari database. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        selectedModel.delete();
        pegawaiMainListTable.getItems().remove(selectedModel);
        this.truncateMainInput();
    }
    
    public void onSelectModel(MouseEvent event) {
        if (event.getClickCount() != 2 || pegawaiMainListTable.getItems().size() <= 0) //Checking double click
            return;
        
        if (selectedModel != null && selectedModel.isEdited && 
                !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Discard Change", "Buang perubahan", 
                        "Data yang sedang di pilih telah di edit dan belum di save, jika melanjutkan perubahan akan di buang. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        // Enable Component
        pegawaiMainNik.setDisable(false); pegawaiMainNama.setDisable(false); pegawaiMainJabatan.setDisable(false); 
        pegawaiMainUsername.setDisable(false); pegawaiMainPassword.setDisable(false); pegawaiMainAlamat.setDisable(false);
        
        selectedModel = (Pegawai)pegawaiMainListTable.getSelectionModel().getSelectedItem();
        pegawaiMainSelectedModel.setText(selectedModel.nama);
        
        pegawaiMainNik.setText(selectedModel.nik);
        pegawaiMainNama.setText(selectedModel.nama);
        pegawaiMainJabatan.setText(selectedModel.jabatan);
        pegawaiMainUsername.setText(selectedModel.username);
        pegawaiMainJK.getToggles().get(selectedModel.jenis_kelamin - 1).setSelected(true);
        pegawaiMainRole.getToggles().get(selectedModel.role - 1).setSelected(true);
        pegawaiMainAlamat.setText(selectedModel.alamat);
        
    }
    
    public boolean verifyInput() {
        return !pegawaiCreateNik.getText().isEmpty() ||
                !pegawaiCreateNama.getText().isEmpty() ||
                !pegawaiCreateJabatan.getText().isEmpty() ||
                !pegawaiCreateUsername.getText().isEmpty() ||
                (!pegawaiCreatePassword.getText().isEmpty() && pegawaiCreatePassword.getText().length() >= 8) ||
                !pegawaiCreateAlamat.getText().isEmpty();
    }
    
    public void truncateMainInput() {
        pegawaiMainNik.setText("");
        pegawaiMainNama.setText("");
        pegawaiMainJabatan.setText("");
        pegawaiMainUsername.setText("");
        pegawaiMainJK.getToggles().get(0).setSelected(true);
        pegawaiMainRole.getToggles().get(0).setSelected(true);
        pegawaiMainAlamat.setText("");
        
        // Disable Component
        pegawaiMainNik.setDisable(true); pegawaiMainNama.setDisable(true); pegawaiMainJabatan.setDisable(true); 
        pegawaiMainUsername.setDisable(true); pegawaiMainPassword.setDisable(true); pegawaiMainAlamat.setDisable(true);
        
        pegawaiMainSelectedModel.setText("None");
        selectedModel = null;
    }
    
    public void truncateInput() {
        pegawaiCreateNik.setText("");
        pegawaiCreateNama.setText("");
        pegawaiCreateJabatan.setText("");
        pegawaiCreateUsername.setText("");
        pegawaiCreateJK.getToggles().get(0).setSelected(true);
        pegawaiCreateRole.getToggles().get(0).setSelected(true);
        pegawaiCreateAlamat.setText("");
    }
    
    public void refreshTable() {
        pegawaiMainListTable.getItems().clear();
        for (Pegawai item : Pegawai.all(() -> new Pegawai()))
            pegawaiMainListTable.getItems().add(item);
    }
    
}
 