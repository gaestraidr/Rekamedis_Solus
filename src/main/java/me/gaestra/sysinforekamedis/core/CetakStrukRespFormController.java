/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.helper.ControlTransformHelper;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.model.Obat;
import me.gaestra.sysinforekamedis.model.Pasien;
import me.gaestra.sysinforekamedis.model.Pegawai;
import me.gaestra.sysinforekamedis.model.Recipe;

/**
 *
 * @author Ganesha
 */
public class CetakStrukRespFormController extends BaseController{
    
    @FXML
    public TableView cetakTableView;

    @FXML
    public Label cetakDiagnosa;

    @FXML
    public Label cetakJabatan;

    @FXML
    public Label cetakNamaStaff;

    @FXML
    public Label cetakKode;

    @FXML
    public Label cetakPemeriksa;

    @FXML
    public Label cetakTanggal;

    @FXML
    public JFXButton cetakPrint;

    @FXML
    public JFXButton cetakCancel;

    @FXML
    public Label cetakPemeriksaan;

    @FXML
    public Label cetakUmurPasien;

    @FXML
    public Label cetakNamaPasien;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Do nothing
    }
    
    public void initContent(Recipe data) {
        Pegawai pegawai = data.Pegawai();
        Pasien pasien = data.Pasien();
        
        cetakKode.setText(data.getId_full());
        cetakPemeriksa.setText(pegawai.nama);
        cetakTanggal.setText(data.created_at.split(" ")[0]);
        
        cetakNamaPasien.setText(pasien.nama);
        cetakUmurPasien.setText(pasien.umur);
        cetakPemeriksaan.setText(pasien.layanan);
        
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Obat", "kode", cetakTableView, 0.2);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Obat", "nama", cetakTableView, 0.6);
        TableColumn column3 = ControlTransformHelper.createColumn("Qty", "stock_keluar", cetakTableView, 2);
        
        cetakTableView.getColumns().addAll(column1, column2, column3);
        
        data.parseToObatCol();
        for (Obat item : data.obatCol)
            cetakTableView.getItems().add(item);
        
        cetakDiagnosa.setText(data.diagnosa);
        cetakNamaStaff.setText(pegawai.nama);
        cetakJabatan.setText(pegawai.jabatan);
    }
    
    @FXML
    public void closeForm(ActionEvent event) {
        stage.close();
    }

    @FXML
    public void printForm(ActionEvent event) {
        cetakCancel.setVisible(false); cetakPrint.setVisible(false);
        Platform.runLater(() -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            job.setPrinter(Printer.getDefaultPrinter());
            if (job != null) {
                if (job.printPage(cetakPrint.getParent())) {
                    DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Nota Printed", "Nota berhasil di print!", "Nota telah berhasil di print ke PDF!");
                    job.endJob();
                }
            }
            cetakCancel.setVisible(true); cetakPrint.setVisible(true);
        });
    }
    
}
