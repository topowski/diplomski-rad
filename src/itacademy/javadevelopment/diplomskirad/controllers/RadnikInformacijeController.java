package itacademy.javadevelopment.diplomskirad.controllers;

import itacademy.javadevelopment.diplomskirad.models.Radnik;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class RadnikInformacijeController {

    private Radnik prijavljeniRadnik;
    public Label imeRadnika;
    public Label prezimeRadnika;
    public Label korisnickoImeRadnika;
    public Label plataRadnika;
    public Label odjeljenjeRadnika;
    public Label adresaOdjeljenja;
    public Button odjaviSeButton;

    public RadnikInformacijeController(Radnik prijavljeniRadnik){
        this.prijavljeniRadnik = prijavljeniRadnik;
    }

    @FXML
    public void initialize(){
        imeRadnika.setText(prijavljeniRadnik.getIme());
        prezimeRadnika.setText(prijavljeniRadnik.getPrezime());
        korisnickoImeRadnika.setText(prijavljeniRadnik.getKorisnickoIme());
        plataRadnika.setText(String.valueOf(prijavljeniRadnik.getPlata()));
        if(prijavljeniRadnik.getOdjeljenje() != null)
            odjeljenjeRadnika.setText(prijavljeniRadnik.getOdjeljenje().getNaziv());
        else
            odjeljenjeRadnika.setText("-");
        if(prijavljeniRadnik.getOdjeljenje() != null)
            adresaOdjeljenja.setText(prijavljeniRadnik.getOdjeljenje().getAdresa());
        else
            adresaOdjeljenja.setText("-");

        odjaviSeButton.setOnAction(actionEvent -> {
            try {
                Scene trenutnaScena = imeRadnika.getScene();
                Stage trenutniProzor = (Stage) trenutnaScena.getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
                trenutniProzor.setScene(new Scene(root));
                trenutniProzor.setTitle("AO Human Resources");
            } catch (IOException e) {
                System.out.println("IZUZETAK: " + e.getMessage());
            }
        });

    }

}
