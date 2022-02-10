/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.helper.ControlTransformHelper;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.helper.UtilityMiscHelper;
import me.gaestra.sysinforekamedis.model.Model;
import me.gaestra.sysinforekamedis.model.Obat;
import me.gaestra.sysinforekamedis.model.Pasien;
import me.gaestra.sysinforekamedis.model.Pegawai;
import me.gaestra.sysinforekamedis.model.Pembayaran;
import me.gaestra.sysinforekamedis.model.Recipe;
import me.gaestra.sysinforekamedis.model.Stock;

/**
 * FXML Controller class
 *
 * @author Ganesha
 */
public class RekapanFormController extends BaseController {
    
    // Core Component
    public LocalDate from, to;
    
    @FXML public AnchorPane cetakRootLayout;
    
    @FXML public TableView cetakTableView;
    @FXML public VBox cetakBoxLayout;
    
    @FXML public Label cetakStaff;
    @FXML public Label cetakJabatan;
    @FXML public Label cetakDate;
    @FXML public Label cetakTitle;
    
    @FXML public JFXButton cetakCancel;
    @FXML public JFXButton cetakPrint;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Do Nothing
    }
    
    public void setDate(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }
    
    public void initRekapanData(Model data) { initRekapanData(data, 1); }
    
    public void initRekapanData(Model data, int optionMode) {
        if (data instanceof Pasien) {
            cetakTitle.setText("Laporan Data Pasien");
            
            TableColumn column1 = ControlTransformHelper.createColumn("Nomor Pasien", "id_full", cetakTableView, 0.166);
            TableColumn column2 = ControlTransformHelper.createColumn("Nama", "nama", cetakTableView, 0.166);
            TableColumn column3 = ControlTransformHelper.createColumn("Jenis Kelamin", "jk_str", cetakTableView, 0.166);
            TableColumn column4 = ControlTransformHelper.createColumn("Alamat", "alamat", cetakTableView, 0.166);
            TableColumn column5 = ControlTransformHelper.createColumn("Layanan", "layanan", cetakTableView, 0.166);
            TableColumn column6 = ControlTransformHelper.createColumn("Pasien Urgent", "urg_str", cetakTableView, 0.166);

            cetakTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6);
            
            for (Pasien item : Pasien.all(() -> new Pasien()))
                cetakTableView.getItems().add(item);
            
            this.wrapTable(cetakTableView);
        }
        else if (data instanceof Recipe) {
            cetakTitle.setText("Laporan Data Pemeriksaan");

            cetakBoxLayout.getChildren().clear();
            TableView currTable = null;
            String currDate = "";

            for (Recipe item : Recipe.all(() -> new Recipe())) {
                if (!this.isBetweenDate(item.created_at))
                    continue;
                
                String resepDate = this.getDateAnotherFormat(item.created_at.split(" ")[0]);
                if (!currDate.equals(resepDate)) {
                    currDate = resepDate;
                    cetakBoxLayout.getChildren().add(this.getCaptionLabel("Tanggal: " + currDate));
                    currTable = addRecipeTableView();
                }

                currTable.getItems().add(item);
                this.wrapTable(currTable);
            }
            
        }
        else if (data instanceof Pembayaran) {
            cetakTitle.setText("Laporan Data Pembayaran");
            
            cetakBoxLayout.getChildren().clear();
            TableView currTable = null;
            String currDate = "";
            
            for (Pembayaran item : Pembayaran.all(() -> new Pembayaran())) {
                if (!this.isBetweenDate(item.created_at))
                    continue;
                
                String pembDate = this.getDateAnotherFormat(item.created_at.split(" ")[0]);
                if (!currDate.equals(pembDate)) {
                    currDate = pembDate;
                    cetakBoxLayout.getChildren().add(this.getCaptionLabel("Tanggal: " + currDate));
                    currTable = addPembayaranTableView();
                }

                currTable.getItems().add(item);
                this.wrapTable(currTable);
            }
        }
        else if (data instanceof Stock) {
            cetakTitle.setText("Laporan Data Stock");
            
            cetakBoxLayout.getChildren().clear();
            TableView currTable = null;
            
            for (Stock stock : Stock.all(() -> new Stock())) {
                if (!this.isBetweenDate(stock.created_at))
                    continue;
                
                stock.parseToObatCol();
                
                String stockDate = this.getDateAnotherFormat(stock.created_at.split(" ")[0]);
                cetakBoxLayout.getChildren().add(this.getCaptionLabel("Tanggal: " + stockDate));
                currTable = addStockTableView();
                    
                for (Obat item : stock.obatCol) 
                    currTable.getItems().add(item);

                this.wrapTable(currTable);
            }
        }
        else if (data instanceof Pegawai) {
            cetakTitle.setText("Laporan Data Admin");
            
            TableColumn column1 = ControlTransformHelper.createColumn("NIK", "nik", cetakTableView, 0.166);
            TableColumn column2 = ControlTransformHelper.createColumn("Nama", "nama", cetakTableView, 0.166);
            TableColumn column3 = ControlTransformHelper.createColumn("Username", "alamat", cetakTableView, 0.166);
            TableColumn column4 = ControlTransformHelper.createColumn("Jenis Kelamin", "jk_full", cetakTableView, 0.166);
            TableColumn column5 = ControlTransformHelper.createColumn("Role", "role_full", cetakTableView, 0.166);
            TableColumn column6 = ControlTransformHelper.createColumn("Alamat", "alamat", cetakTableView, 0.166);

            cetakTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6);
            
            for (Pegawai item : Pegawai.all(() -> new Pegawai()))
                cetakTableView.getItems().add(item);
            
            this.wrapTable(cetakTableView);
        }
        
        cetakDate.setText("Jakarta, " + DateTimeFormatter.ofPattern("dd MMMM yyyy").format(LocalDateTime.now()));
        cetakJabatan.setText(App.loggedUser.jabatan);
        cetakStaff.setText(App.loggedUser.nama);
        
        cetakStaff.requestFocus();
    }
    
    @FXML
    public void doPrint() {
        cetakCancel.setVisible(false); cetakPrint.setVisible(false);
        Platform.runLater(() -> {
            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.getDefaultPageLayout();

            // Printable Area Size
            double pWidth = pageLayout.getPrintableWidth();
            double pHeight = pageLayout.getPrintableHeight();

            // Root Layout dimensions
            double nWidth = cetakRootLayout.getBoundsInParent().getWidth();
            double nHeight = cetakRootLayout.getBoundsInParent().getHeight();

            // Calculate Spacing
            double widthLeft = pWidth - nWidth;
            double heightLeft = pHeight - nHeight;

            double scale;

            if (widthLeft < heightLeft) scale = pWidth / nWidth;
            else scale = pHeight / nHeight;

            // Preserve Ratio
            cetakRootLayout.getTransforms().add(new Scale(scale, scale));
            stage.close();
            
            PrinterJob job = PrinterJob.createPrinterJob();
            if(job.printPage(pageLayout, cetakRootLayout)){
                DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Rekapan Printed", "Rekapan berhasil di print!", "Rekapan telah berhasil di print ke PDF!");
                job.endJob();
            }
            
            // Undo Change
            cetakRootLayout.getTransforms().remove(scale);
            cetakCancel.setVisible(true); cetakPrint.setVisible(true);
        });
    }
    
    @FXML
    public void doExit() {
        
    }
    
    private TableView addRecipeTableView() {
        TableView retTable = new TableView();
        retTable.setPrefWidth(cetakBoxLayout.getPrefWidth());
        
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Resep", "id_full", retTable, 0.2);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Pasien", "nama_pasien", retTable, 0.3);
        TableColumn column3 = ControlTransformHelper.createColumn("Nama Pemeriksa", "nama_pegawai", retTable, 0.3);
        TableColumn column4 = ControlTransformHelper.createColumn("Tanggal Pemeriksaan", "created_at", retTable, 0.2);
        
        retTable.getColumns().clear();
        retTable.getColumns().addAll(column1, column2, column3, column4);
        cetakBoxLayout.getChildren().add(retTable);
        
        return retTable;
    }
    
    private TableView addPembayaranTableView() {
        TableView retTable = new TableView();
        retTable.setPrefWidth(cetakBoxLayout.getPrefWidth());
        
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Pembayaran", "id_full", retTable, 0.2);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Pasien", "nama_pasien", retTable, 0.3);
        TableColumn column3 = ControlTransformHelper.createColumn("Total Pembayaran", "total_tagihan", retTable, 0.3);
        TableColumn column4 = ControlTransformHelper.createColumn("Tanggal Transaksi", "created_at", retTable, 0.2);
        
        retTable.getColumns().clear();
        retTable.getColumns().addAll(column1, column2, column3, column4);
        cetakBoxLayout.getChildren().add(retTable);
        
        return retTable;
    }
    
    private TableView addStockTableView() {
        TableView retTable = new TableView();
        retTable.setPrefWidth(cetakBoxLayout.getPrefWidth());
        
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Obat", "kode", retTable, 0.1);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Obat", "nama", retTable, 0.4);
        TableColumn column3 = ControlTransformHelper.createColumn("Stock Awal", "stock_awal", retTable, 0.166);
        TableColumn column4 = ControlTransformHelper.createColumn("Stock Masuk", "stock_masuk", retTable, 0.166);
        TableColumn column5 = ControlTransformHelper.createColumn("Stock Keluar", "stock_keluar", retTable, 0.166);
        
        retTable.getColumns().clear();
        retTable.getColumns().addAll(column1, column2, column3, column4, column5);
        cetakBoxLayout.getChildren().add(retTable);
        
        return retTable;
    }
    
    private void wrapTable(TableView table) {
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.01)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }
    
    private String getDateAnotherFormat(String date) {
        var unparsedDate = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date parsedDate = null;
        try { parsedDate = unparsedDate.parse(date); } catch (Exception e) { e.printStackTrace(); }
        return new SimpleDateFormat("dd-MM-yyyy").format(parsedDate);
    }
    
    private Label getCaptionLabel(String caption) {
        Label label = new Label(caption);
        label.getStyleClass().add("bg-secondary"); label.getStyleClass().add("text-white");
        label.setFont(new Font(30));
        label.setPrefWidth(cetakBoxLayout.getPrefWidth());
        label.alignmentProperty().set(Pos.CENTER);
        
        return label;
    }
    
    private boolean isBetweenDate(String date) {
        LocalDate parsedDate = UtilityMiscHelper.formatToDate(date);
        
        if (from != null && parsedDate.isBefore(from))
            return false;
        
        if (to != null && parsedDate.isAfter(to))
            return false;
        
        return true;
    }
}