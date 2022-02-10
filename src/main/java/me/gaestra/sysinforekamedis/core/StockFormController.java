/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import me.gaestra.sysinforekamedis.App;
import me.gaestra.sysinforekamedis.helper.ControlTransformHelper;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.helper.UtilityMiscHelper;
import me.gaestra.sysinforekamedis.model.Obat;
import me.gaestra.sysinforekamedis.model.Stock;

/**
 *
 * @author Ganesha
 */
public class StockFormController {
    
    public Stock selectedStock;
    public Obat selectedModel;
    
    public int manageMode = 1;
    
    public TabPane stockTabPane;
    public VBox stockMainList;
    public JFXButton stockGotoAdd;
    
    public TableView stockMainObatTable;
    public JFXTextField stockManageKode;
    public JFXTextField stockManageNama;
    public JFXTextField stockManageHarga;
    public JFXTextField stockManageStockMasuk;
    public JFXButton stockManageUpdateButton;
    public JFXButton stockManageDeleteButton;
    public TableView stockManageObatTable;
    public JFXButton stockManageSave;
    public JFXButton stockManageSelectedLabel;
    public Label stockManageTanggalLabel;
    public JFXButton stockBacktoMain;
    public JFXTextField stockAddKode;
    public JFXTextField stockAddNama;
    public JFXTextField stockAddHarga;
    public JFXButton stockAddTambahButton;
    
    public StockFormController(MainFormController ctl) {
        setupControl(ctl);
        hookMethodControl();
        
        setupMainView();
        setupManageView();
    }
    
    public void setupControl(MainFormController ctl) {
        stockTabPane = ctl.stockTabPane;
        stockMainList = ctl.stockMainList;
        stockGotoAdd = ctl.stockGotoAdd;
        
        stockMainObatTable = ctl.stockMainObatTable;
        stockManageKode = ctl.stockManageKode;
        stockManageNama = ctl.stockManageNama;
        stockManageHarga = ctl.stockManageHarga;
        stockManageStockMasuk = ctl.stockManageStockMasuk;
        stockManageUpdateButton = ctl.stockManageUpdateButton;
        stockManageDeleteButton = ctl.stockManageDeleteButton;
        stockManageObatTable = ctl.stockManageObatTable;
        stockManageSave = ctl.stockManageSave;
        stockManageSelectedLabel = ctl.stockManageSelectedLabel;
        stockManageTanggalLabel = ctl.stockManageTanggalLabel;
        stockBacktoMain = ctl.stockBacktoMain;
        stockAddKode = ctl.stockAddKode;
        stockAddNama = ctl.stockAddNama;
        stockAddHarga = ctl.stockAddHarga;
        stockAddTambahButton = ctl.stockAddTambahButton;
    }
    
    public void hookMethodControl() {
        stockManageUpdateButton.setOnAction((e) -> this.updateObatToData());
        stockManageDeleteButton.setOnAction((e) -> this.deleteObat());
        stockAddTambahButton.setOnAction((e) -> this.addObatToData());
        stockManageObatTable.setOnMouseClicked((e) -> this.onSelectModel(e));
    }
    
    public void setupMainView() {
        stockMainList.setSpacing(20);
        for (Stock item : Stock.beforeToday()) {
            try {
                Parent list = App.loadFXML("view/ItemList");
                Label tanggal = ((Label)list.getChildrenUnmodifiable().get(1));
                JFXButton action = ((JFXButton)list.getChildrenUnmodifiable().get(2));

                tanggal.setText("Tanggal - " + item.created_at.split(" ")[0].trim());
                action.setOnAction((e) -> {
                    stockTabPane.getSelectionModel().select(1);
                    readPastStock(Stock.find(item.id, () -> new Stock()));
                });

                stockMainList.getChildren().add(list);
            } catch (Exception e) { e.printStackTrace(); }
        }
        
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Obat", "kode", stockMainObatTable, 0.33);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Obat", "nama", stockMainObatTable, 0.3);
        TableColumn column3 = ControlTransformHelper.createColumn("Stock saat ini", "stock", stockMainObatTable, 0.33);
        
        stockMainObatTable.getColumns().addAll(column1, column2, column3);
        
        this.refreshMainTable();
    }
    
    public void setupManageView() {
        TableColumn column1 = ControlTransformHelper.createColumn("Kode Obat", "kode", stockManageObatTable, 0.2);
        TableColumn column2 = ControlTransformHelper.createColumn("Nama Obat", "nama", stockManageObatTable, 0.2);
        TableColumn column3 = ControlTransformHelper.createColumn("Stock Awal", "stock_awal", stockManageObatTable, 0.2);
        TableColumn column4 = ControlTransformHelper.createColumn("Stock Masuk", "stock_masuk", stockManageObatTable, 0.2);
        TableColumn column5 = ControlTransformHelper.createColumn("Stock Keluar", "stock_keluar", stockManageObatTable, 0.2);
        
        stockManageObatTable.getColumns().addAll(column1, column2, column3, column4, column5);
        
        // Disable Component
        stockManageKode.setDisable(true); stockManageNama.setDisable(true); 
        stockManageHarga.setDisable(true); stockManageStockMasuk.setDisable(true);
    }
    
    public void manageTodayStock() {
        manageMode = 1;
        
        selectedStock = Stock.today();
        if (!selectedStock.exist()) 
            selectedStock = Stock.createStockForToday();
        
        selectedStock.parseToObatCol();
        stockManageObatTable.getItems().clear();
        
        for (Obat item : selectedStock.obatCol) {
            Obat.getTotalStockKeluar(item);
            stockManageObatTable.getItems().add(item);
        }
        
        for (Obat item : Obat.all(() -> new Obat())) {
            Obat.getTotalStockKeluar(item);
            item.stock_awal = item.stock;
            if (!stockManageObatTable.getItems().contains(item)) 
                stockManageObatTable.getItems().add(item);
        }
        
        stockManageTanggalLabel.setText("Tanggal: " + selectedStock.created_at.split(" ")[0]);
        
        this.truncateManageInput();
        
        // Show Edit Component
        stockManageSave.setVisible(false); stockGotoAdd.setVisible(true); 
        stockManageUpdateButton.setVisible(true); stockManageDeleteButton.setVisible(true);
    }
    
    public void readPastStock(Stock data) {
        manageMode = 2;
        
        selectedStock = data;
        
        selectedStock.parseToObatCol();
        stockManageObatTable.getItems().clear();
        
        for (Obat item : selectedStock.obatCol) {
            Obat.getTotalStockKeluar(item);
            stockManageObatTable.getItems().add(item);
        }
        
        for (Obat item : Obat.all(() -> new Obat())) {
            Obat.getTotalStockKeluar(item);
            item.stock_awal = item.stock;
            if (!stockManageObatTable.getItems().contains(item)) 
                stockManageObatTable.getItems().add(item);
        }
        
        stockManageTanggalLabel.setText("Tanggal: " + selectedStock.created_at.split(" ")[0]);
        
        this.truncateManageInput();
        
        // Hide Edit Component
        stockManageSave.setVisible(false); stockGotoAdd.setVisible(false); 
        stockManageUpdateButton.setVisible(false); stockManageDeleteButton.setVisible(false);
    }
    
    public void saveStockData() {
        if (selectedStock == null)
            return;
        
        selectedStock.parseFromObatCol();
        selectedStock.save();
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Input Tersimpan", "Input data berhasil di simpan", 
                    "Input data yang di masukan telah tersimpan di database!");
        stockTabPane.getSelectionModel().select(0);
        
        this.refreshMainTable();
    }
    
    public void addObatToData() {
        if (!this.verifyInput()) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, "ERROR: Input Invalid", "Input data Invalid", 
                    "Input data yang di masukan tidak lengkap / invalid, mohon cek kembali!");
            return;
        }

        Obat model = new Obat();
        model.kode = stockAddKode.getText();
        model.nama = stockAddNama.getText();
        model.harga = UtilityMiscHelper.formatToNumber(stockAddHarga.getText());
        
        model.save();
        selectedStock.obatCol.add(model);
        selectedStock.parseFromObatCol(); selectedStock.save();
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "SUCCESS: Input Tersimpan", "Input data berhasil di simpan", 
                    "Input data yang di masukan telah tersimpan di database!");
        this.truncateAddInput();
        this.refreshMainTable();
        this.refreshManageTable();
    }
    
    public void updateObatToData() {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Update Data", "Ubah Data", 
                        "Perubahan di Data yang sedang di pilih jika di lanjutkan akan terubah dengan input yang di masukan. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        int index = stockManageObatTable.getItems().indexOf(selectedModel);
        
        selectedModel.kode = stockManageKode.getText();
        selectedModel.nama = stockManageNama.getText();
        selectedModel.harga = UtilityMiscHelper.formatToNumber(stockManageHarga.getText());
        selectedModel.stock_masuk = Integer.valueOf(stockManageStockMasuk.getText());
        
        selectedModel.save();
        selectedStock.obatCol.set(selectedStock.obatCol.indexOf(selectedModel), selectedModel);
        selectedStock.parseFromObatCol(); selectedStock.save();
        
        DialogAlertHelper.showAlert(Alert.AlertType.INFORMATION, "Data Changed", "Data berhasil di ubah", "Data telah berhasil di ubah dan tersimpan di database!");
        stockManageObatTable.getItems().set(index, selectedModel);
        
        this.truncateManageInput();
        this.refreshMainTable();
    }
    
    public void deleteObat() {
        if (selectedModel == null || !DialogAlertHelper.showDialogPrompted(Alert.AlertType.WARNING, "Delete Data", "Hapus Data", 
                        "Data yang terpilih jika di lanjutkan akan di hapus dari database. Lanjut?", "Lanjutkan", "Tidak"))
            return;
        
        stockManageObatTable.getItems().remove(selectedModel);
        stockMainObatTable.getItems().remove(selectedModel);
        selectedStock.obatCol.remove(selectedModel);
        selectedModel.delete();
        selectedStock.parseFromObatCol(); selectedStock.save();
        
        this.truncateManageInput();
        this.refreshMainTable();
    }
    
    public void onSelectModel(MouseEvent event) {
        if (event.getClickCount() != 2 || stockManageObatTable.getItems().size() <= 0) // Checking double click
            return;
        
        // Enable Component
        if (manageMode == 1) {
            stockManageKode.setDisable(false); stockManageNama.setDisable(false); 
            stockManageHarga.setDisable(false); stockManageStockMasuk.setDisable(false);
        }
        
        selectedModel = (Obat)stockManageObatTable.getSelectionModel().getSelectedItem();
        stockManageSelectedLabel.setText(selectedModel.kode);
        
        stockManageKode.setText(selectedModel.kode);
        stockManageNama.setText(selectedModel.nama);
        stockManageHarga.setText(UtilityMiscHelper.formatCurrency(String.valueOf(selectedModel.harga)));
        stockManageStockMasuk.setText(String.valueOf(selectedModel.stock_masuk));
    }
    
    public boolean verifyInput() {
        return !stockAddKode.getText().isEmpty() && 
                !stockAddNama.getText().isEmpty() && 
                !stockAddHarga.getText().isEmpty();
    }
    
    public void refreshManageTable() {
        stockManageObatTable.getItems().clear();
        for (Obat item : selectedStock.obatCol)
            stockManageObatTable.getItems().add(item);
    }
    
    public void refreshMainTable() {
        stockMainObatTable.getItems().clear();
        Stock stockToday = Stock.today();
        if (!stockToday.exist())
            stockToday = Stock.createStockForToday();
        
        stockToday.parseToObatCol();
        for (Obat item : stockToday.obatCol) {
            item.stock = (item.stock_awal + item.stock_masuk) - item.stock_keluar;
            stockMainObatTable.getItems().add(item);
        }
    }
    
    public void truncateManageInput() {
        stockManageKode.setText("");
        stockManageNama.setText("");
        stockManageHarga.setText("");
        stockManageStockMasuk.setText("");
        
        // Disable Component
        stockManageKode.setDisable(true); stockManageNama.setDisable(true); 
        stockManageHarga.setDisable(true); stockManageStockMasuk.setDisable(true);
        
        stockManageSelectedLabel.setText("None");
        selectedModel = null;
    }
    
    public void truncateAddInput() {
        stockAddKode.setText("");
        stockAddNama.setText("");
        stockAddHarga.setText("");
    }
    
}
