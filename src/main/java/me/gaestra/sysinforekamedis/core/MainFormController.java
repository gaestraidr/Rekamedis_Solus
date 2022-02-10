/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class MainFormController extends BaseController {
    
    // Core Controller Component
    public HomeFormController homeCtl;
    public PasienFormController pasienCtl;
    public PemeriksaanFormController pemeriksaanCtl;
    public PembayaranFormController pembayaranCtl;
    public StockFormController stockCtl;
    public LaporanFormController laporanCtl;
    public PegawaiFormController pegawaiCtl;
    
    // <editor-fold defaultstate="collapsed" desc="FXML Control Component">
    // FXML Control Component
    
    @FXML
    public JFXListView<Label> menuListView;

    @FXML
    public TabPane formTabPane;

    @FXML
    public Label homeAntNumberLabel;

    @FXML
    public Label homeAntDescLabel;

    @FXML
    public JFXButton homeAntNextButton;

    @FXML
    public TabPane pasienTabPane;

    @FXML
    public JFXButton pasienGotoCreate;

    @FXML
    public JFXButton pasienMainSelectedLabel;

    @FXML
    public JFXTextField pasienMainNomorAnt;

    @FXML
    public JFXTextField pasienMainNama;

    @FXML
    public JFXTextField pasienMainUmur;

    @FXML
    public ToggleGroup pasienMainJK;

    @FXML
    public JFXTextArea pasienMainAlamat;

    @FXML
    public JFXComboBox pasienMainLayanan;

    @FXML
    public ToggleGroup pasienMainUrgent;

    @FXML
    public JFXButton pasienMainUpdateButton;

    @FXML
    public JFXButton pasienMainDeleteButton;

    @FXML
    public JFXButton pasienMainCKButton;

    @FXML
    public TableView pasienMainListTable;

    @FXML
    public JFXTextField pasienCreateNomorAnt;

    @FXML
    public JFXTextField pasienCreateNama;

    @FXML
    public JFXTextField pasienCreateUmur;

    @FXML
    public ToggleGroup pasienCreateJK;

    @FXML
    public JFXTextArea pasienCreateAlamat;

    @FXML
    public JFXComboBox pasienCreateLayanan;

    @FXML
    public ToggleGroup pasienCreateUrgent;

    @FXML
    public JFXButton pasienCreateTambahButton;

    @FXML
    public JFXButton pasienCreateCetakButton;

    @FXML
    public JFXButton pasienBacktoMain;

    @FXML
    public TabPane pemeriksaanTabPane;

    @FXML
    public JFXButton pemeriksaanMainSelectedLabel;

    @FXML
    public JFXTextField pemeriksaanMainNomor;

    @FXML
    public JFXTextField pemeriksaanMainNama;

    @FXML
    public JFXTextArea pemeriksaanMainDiagnosa;

    @FXML
    public TableView pemeriksaanMainObatTable;

    @FXML
    public JFXButton pemeriksaanMainDeleteButton;

    @FXML
    public TableView pemeriksaanMainListTable;

    @FXML
    public TabPane pembayaranTabPane;

    @FXML
    public JFXTextField pembayaranMainKode;

    @FXML
    public Label pembayaranMainNomor;

    @FXML
    public Label pembayaranMainNama;

    @FXML
    public Label pembayaranMainUmur;

    @FXML
    public Label pembayaranMainJK;

    @FXML
    public Label pembayaranMainLayanan;

    @FXML
    public Label pembayaranMainTotal;

    @FXML
    public Label pembayaranMainNamaStaff;

    @FXML
    public TableView pembayaranMainObatTable;

    @FXML
    public JFXButton pembayaranGotoHistory;

    @FXML
    public JFXButton pembayaranMainCetakButton;

    @FXML
    public JFXComboBox pembayaranHistoryTipeSearch;

    @FXML
    public JFXComboBox pembayaranHistorySelectModel;

    @FXML
    public JFXTextField pembayaranHistoryKode;

    @FXML
    public JFXDatePicker pembayaranHistoryTanggal;

    @FXML
    public TableView pembayaranHistoryListTable;

    @FXML
    public JFXButton pembayaranBacktoMain;

    @FXML
    public TabPane stockTabPane;
    
    @FXML
    public VBox stockMainList;

    @FXML
    public TableView stockMainObatTable;

    @FXML
    public JFXButton stockGotoManage;

    @FXML
    public JFXButton stockGotoAdd;

    @FXML
    public JFXTextField stockManageKode;

    @FXML
    public JFXTextField stockManageNama;

    @FXML
    public JFXTextField stockManageHarga;

    @FXML
    public JFXTextField stockManageStockMasuk;

    @FXML
    public JFXButton stockManageUpdateButton;

    @FXML
    public JFXButton stockManageDeleteButton;

    @FXML
    public TableView stockManageObatTable;

    @FXML
    public JFXButton stockManageSave;

    @FXML
    public JFXButton stockManageSelectedLabel;
    
    @FXML
    public Label stockManageTanggalLabel;

    @FXML
    public JFXButton stockBacktoMain;

    @FXML
    public JFXTextField stockAddKode;

    @FXML
    public JFXTextField stockAddNama;

    @FXML
    public JFXTextField stockAddHarga;

    @FXML
    public JFXButton stockAddTambahButton;

    @FXML
    public JFXButton stockBacktoManage;

    @FXML
    public JFXButton laporanMainPasien;

    @FXML
    public JFXButton laporanMainPemeriksaan;

    @FXML
    public JFXButton laporanMainPembayaran;

    @FXML
    public JFXButton laporanMainStock;

    @FXML
    public JFXButton laporanMainPegawai;

    @FXML
    public JFXDatePicker laporanMainFromDate;

    @FXML
    public JFXDatePicker laporanMainToDate;

    @FXML
    public TabPane pegawaiTabPane;

    @FXML
    public JFXButton pegawaiGotoCreate;

    @FXML
    public JFXButton pegawaiMainSelectedModel;

    @FXML
    public JFXTextField pegawaiMainNik;

    @FXML
    public JFXTextField pegawaiMainNama;

    @FXML
    public JFXTextField pegawaiMainJabatan;

    @FXML
    public JFXTextField pegawaiMainUsername;

    @FXML
    public JFXPasswordField pegawaiMainPassword;

    @FXML
    public ToggleGroup pegawaiMainJK;

    @FXML
    public ToggleGroup pegawaiMainRole;

    @FXML
    public JFXTextArea pegawaiMainAlamat;

    @FXML
    public JFXButton pegawaiMainUpdateButton;

    @FXML
    public JFXButton pegawaiMainDeleteButton;

    @FXML
    public TableView pegawaiMainListTable;

    @FXML
    public JFXTextField pegawaiCreateNik;

    @FXML
    public JFXTextField pegawaiCreateNama;

    @FXML
    public JFXTextField pegawaiCreateJabatan;

    @FXML
    public JFXTextField pegawaiCreateUsername;

    @FXML
    public JFXPasswordField pegawaiCreatePassword;

    @FXML
    public ToggleGroup pegawaiCreateJK;

    @FXML
    public ToggleGroup pegawaiCreateRole;

    @FXML
    public JFXTextArea pegawaiCreateAlamat;

    @FXML
    public JFXButton pegawaiCreateTambahButton;

    @FXML
    public JFXButton pegawaiBacktoMain;

    @FXML
    public Label appTanggal;

    @FXML
    public Label appJam;

    @FXML
    public Label appLoginNama;

    @FXML
    public Label appLoginJabatan;

    @FXML
    public JFXButton appLogoutButton;
    
    // </editor-fold>
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupListView();
        
        setupHookController();
        setupMainView();
    }
    
    public void setupHookController() {
        homeCtl = new HomeFormController(this);
        pasienCtl = new PasienFormController(this);
        pemeriksaanCtl = new PemeriksaanFormController(this);
        pembayaranCtl = new PembayaranFormController(this);
        stockCtl = new StockFormController(this);
        laporanCtl = new LaporanFormController(this);
        pegawaiCtl = new PegawaiFormController(this);
    } 
    
    public void setupListView() {
        Label menu1 = new Label("Home");
        Label menu2 = new Label("Pasien / Antrian");
        Label menu3 = new Label("Pemeriksaan");
        Label menu4 = new Label("Pembayaran");
        Label menu5 = new Label("Stok Obat");
        Label menu6 = new Label("Admin");
        Label menu7 = new Label("Laporan");
        
        menu1.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.HOME, "20px"));
        menu2.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.ID_CARD, "20px"));
        menu3.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.STETHOSCOPE, "20px"));
        menu4.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.MONEY, "20px"));
        menu5.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.MEDKIT, "20px"));
        menu6.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.VCARD, "20px"));
        menu7.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EDIT, "20px"));
        
        menuListView.getItems().addAll(menu1, menu2, menu3, menu4, menu5, menu6, menu7);
        
        menuListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Label> observable, Label oldValue, Label newValue) -> {
            String str = newValue.getText();
            SingleSelectionModel<Tab> select = formTabPane.getSelectionModel();
            
            if (str.contains("Home")) {
                select.select(0);
            }
            else if (str.contains("Pasien / Antrian")) {
                select.select(1);
            }
            else if (str.contains("Pemeriksaan")) {
                select.select(2);
            }
            else if (str.contains("Pembayaran")) {
                select.select(3);
            }
            else if (str.contains("Stok Obat")) {
                stockCtl.refreshMainTable();
                select.select(4);
            }
            else if (str.contains("Laporan")) {
                select.select(5);
            }
            else if (str.contains("Admin")) {
                select.select(6);
            }
        });
    }
    
    public void setupMainView() {
        appLoginNama.setText(App.loggedUser.nama);
        appLoginJabatan.setText(App.loggedUser.jabatan);
        
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
        if (btn == pasienGotoCreate) {
            pasienTabPane.getSelectionModel().select(1);
        }
        else if (btn == pembayaranGotoHistory) {
            pembayaranTabPane.getSelectionModel().select(1);
        }
        else if (btn == stockGotoManage) {
            stockCtl.manageTodayStock();
            stockTabPane.getSelectionModel().select(1);
        }
        else if (btn == stockGotoAdd) {
            stockTabPane.getSelectionModel().select(2);
        }
        else if (btn == pegawaiGotoCreate) {
            pegawaiTabPane.getSelectionModel().select(1);
        }
        
        // Backto
        else if (btn == pasienBacktoMain) {
            pasienTabPane.getSelectionModel().select(0);
        }
        else if (btn == pembayaranBacktoMain) {
            pembayaranTabPane.getSelectionModel().select(0);
        }
        else if (btn == stockBacktoMain) {
            stockTabPane.getSelectionModel().select(0);
        }
        else if (btn == stockBacktoManage) {
            stockTabPane.getSelectionModel().select(1);
        }
        else if (btn == pegawaiBacktoMain) {
            pegawaiTabPane.getSelectionModel().select(0);
        }
    }
    
    @FXML
    public void doLogout() {
        if (!DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Logout", "Kembali ke Login", 
                        "Sesi akun akan di logout dan aplikasi di arahkan ke halaman login, lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        try {
            stage.close();
            App.showFirstForm(new Stage());
        } catch(Exception e) { e.printStackTrace(); }
    }
    
    
    
    
    
    
    @FXML
    public void deleteModelData(ActionEvent event) {

    }

    @FXML
    public void searchModelUsed(ActionEvent event) {

    }

    @FXML
    public void searchTypeChanged(ActionEvent event) {

    }

    @FXML
    public void updateModelData(ActionEvent event) {

    }
}
