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
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class MainFormDokterController extends BaseController {

    public PemeriksaanDokterFormController pemeriksaanCtl;
    
    // <editor-fold defaultstate="collapsed" desc="FXML Control Component">
    // FXML Control Component
    
    @FXML
    public TabPane formTabPane;

    @FXML
    public TabPane pemeriksaanTabPane;
    
    @FXML
    public JFXComboBox pemeriksaanMainModeInput;

    @FXML
    public JFXTextField pemeriksaanMainNomor;

    @FXML
    public JFXTextField pemeriksaanMainNama;

    @FXML
    public JFXTextField pemeriksaanMainUmur;

    @FXML
    public ToggleGroup pemeriksaanMainJK;

    @FXML
    public JFXTextArea pemeriksaanMainAlamat;

    @FXML
    public JFXComboBox pemeriksaanMainLayanan;

    @FXML
    public JFXButton pemeriksaanGotoHistory;

    @FXML
    public JFXTextArea pemeriksaanMainDiagnosa;

    @FXML
    public JFXComboBox pemeriksaanMainListObat;

    @FXML
    public JFXTextField pemeriksaanMainJumlahObat;

    @FXML
    public JFXButton pemeriksaanMainTambahObat;

    @FXML
    public JFXButton pemeriksaanMainCetakResep;

    @FXML
    public TableView pemeriksaanMainObatTable;

    @FXML
    public JFXButton pemeriksaanBacktoMain;

    @FXML
    public Label pemeriksaanHistoryTitle;
    
    @FXML
    public JFXComboBox pemeriksaanHistoryTipeSearch;

    @FXML
    public JFXComboBox pemeriksaanHistorySelectModel;

    @FXML
    public JFXTextField pemeriksaanHistoryKode;

    @FXML
    public JFXDatePicker pemeriksaanHistoryTanggal;

    @FXML
    public TableView pemeriksaanHistoryListTable;

    @FXML
    public Label appTanggal;

    @FXML
    public Label appJam;

    @FXML
    public Label appNamaPegawai;

    @FXML
    public Label appJabatanPegawai;

    @FXML
    public JFXButton appGotoHome;

    @FXML
    public JFXButton appGotoPemeriksaan;

    @FXML
    public JFXButton appLogout;
    
    // </editor-fold>
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hookControlForm();
        setupMainView();
    }
    
    public void hookControlForm() {
        pemeriksaanCtl = new PemeriksaanDokterFormController(this);
    }
    
    public void setupMainView() {
        appNamaPegawai.setText(App.loggedUser.nama);
        appJabatanPegawai.setText(App.loggedUser.jabatan);
        
        final Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.millis( 500 ),
                event -> {
                    appTanggal.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(LocalDate.now()));
                    appJam.setText(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(LocalTime.now()));
                }
            )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
    }
    
    @FXML
    public void changePaneWindow(ActionEvent event) {
        Object btn = event.getSource();

        // Goto
        if (btn == appGotoHome) {
            formTabPane.getSelectionModel().select(0);
        }
        else if (btn == appGotoPemeriksaan) {
            formTabPane.getSelectionModel().select(1);
        }
        else if (btn == pemeriksaanGotoHistory) {
            pemeriksaanTabPane.getSelectionModel().select(1);
        }
        
        // Backto
        else if (btn == pemeriksaanBacktoMain) {
            pemeriksaanTabPane.getSelectionModel().select(0);
        }
    }

    @FXML
    public void doLogout(ActionEvent event) {
        if (!DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Logout", "Kembali ke Login", 
                        "Sesi akun akan di logout dan aplikasi di arahkan ke halaman login, lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        try {
            stage.close();
            App.showFirstForm(new Stage());
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    
    
    
    
    
    
    
    
    
    

    @FXML
    public void inputChanged(ActionEvent event) {

    }

    @FXML
    public void updateModelData(ActionEvent event) {

    }
    
}
