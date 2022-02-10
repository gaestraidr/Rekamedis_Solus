/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.core;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import me.gaestra.sysinforekamedis.model.Pasien;

/**
 *
 * @author Ganesha
 */
public class HomeFormController {
    
    public Label homeAntNumberLabel;
    public Label homeAntDescLabel;
    public JFXButton homeAntNextButton;
    
    public HomeFormController(MainFormController ctl) {
        setupControl(ctl);
        hookMethodControl();
        
        setupMainView();
    }
    
    public void setupControl(MainFormController ctl) {
        homeAntNumberLabel = ctl.homeAntNumberLabel;
        homeAntDescLabel = ctl.homeAntDescLabel;
        homeAntNextButton = ctl.homeAntNextButton;
    }
    
    public void hookMethodControl() {
        homeAntNextButton.setOnAction((e) -> progressAntrian());
    }
    
    public void setupMainView() {
        progressAntrian();
    }
    
    public void progressAntrian() {
        Pasien pasien = Pasien.latestAntrianNoRecipe();
        if (pasien.exist()) {
            homeAntNumberLabel.setText(pasien.getId_full());
            homeAntDescLabel.setText("Silahkan panggil antrian No. " + pasien.getId_full() + " dengan pasien bernama " + pasien.nama);
        }
        else {
            homeAntNumberLabel.setText("-");
            homeAntDescLabel.setText("Belum ada antrian");
        }
    }
    
}
