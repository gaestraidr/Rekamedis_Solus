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
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.model.Pasien;

/**
 *
 * @author Ganesha
 */
public class CetakKartuPasienFormController extends BaseController{
    
    @FXML
    public Label cetakNomorPasien;

    @FXML
    public Label cetakPelayanan;

    @FXML
    public JFXButton cetakPrint;

    @FXML
    public JFXButton cetakCancel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Do nothing
    }
    
    public void initContent(Pasien data) {
        cetakNomorPasien.setText(data.getId_full());
        cetakPelayanan.setText(data.layanan);
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
