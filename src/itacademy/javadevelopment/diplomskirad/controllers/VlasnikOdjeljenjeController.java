package itacademy.javadevelopment.diplomskirad.controllers;

import itacademy.javadevelopment.diplomskirad.dao.HumanResourcesDAO;
import itacademy.javadevelopment.diplomskirad.models.Admin;
import itacademy.javadevelopment.diplomskirad.models.Odjeljenje;
import itacademy.javadevelopment.diplomskirad.models.Radnik;
import itacademy.javadevelopment.diplomskirad.models.Vlasnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;


public class VlasnikOdjeljenjeController {

    private Vlasnik vlasnikOdjeljenja;
    private Odjeljenje odjeljenje;
    public Label nazivOdjeljenja;
    public Label adresaOdjeljenja;
    public Label kapactitetOdjeljenja;
    public ListView<Radnik> listaRadnika;
    public TextField poljeIme;
    public TextField poljePrezime;
    public TextField poljeKorisnickoIme;
    public TextField poljeSifra;
    public TextField poljePlata;
    public Button dodajDugme;
    public Button urediDugme;
    public Button obrisiDugme;
    public Button vratiSeNazad;
    public Button pocistiFormuDugme;
    public RadioButton opcijaDodavanje;
    public RadioButton opcijaUredjivanje;
    private HumanResourcesDAO dao;

    public VlasnikOdjeljenjeController(Vlasnik vlasnikOdjeljenja, Odjeljenje odjeljenje) {
        this.vlasnikOdjeljenja = vlasnikOdjeljenja;
        this.odjeljenje = odjeljenje;
        dao = HumanResourcesDAO.getInstance();
    }

    @FXML
    public void initialize(){
        nazivOdjeljenja.setText(odjeljenje.getNaziv());
        adresaOdjeljenja.setText(odjeljenje.getAdresa());
        urediDugme.setDisable(true);
        obrisiDugme.setDisable(true);
        dobaviRadnikeOdjeljenja();

        vratiSeNazad.setOnAction(actionEvent -> {
            try {
                Scene trenutnaScena = poljeIme.getScene();
                Stage trenutniProzor = (Stage) trenutnaScena.getWindow();
                VlasnikDashboardController kontroler = new VlasnikDashboardController(vlasnikOdjeljenja);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vlasnik-dashboard.fxml"));
                loader.setController(kontroler);
                Parent root = loader.load();;
                trenutniProzor.setScene(new Scene(root));
                trenutniProzor.setTitle("Vlasnik dashboard");
            } catch (IOException e) {
                // Ignore
            }
        });

        dodajDugme.setOnAction(actionEvent -> {
            try{
                if(!provjeriFormu()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("POGREŠKA");
                    alert.setContentText("Sva polja moraju biti popunjena");
                    alert.showAndWait();
                    return;
                }
                if(listaRadnika.getItems().size() == odjeljenje.getBrojRadnihMjesta()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("POGREŠKA");
                    alert.setContentText("Kapacitet odjeljenja popunjen");
                    alert.showAndWait();
                    return;
                }
                dao.insertRadnik(new Radnik(-1, poljeIme.getText(), poljePrezime.getText(),
                        poljeKorisnickoIme.getText(), poljeSifra.getText(), Integer.parseInt(poljePlata.getText()), odjeljenje));
                dobaviRadnikeOdjeljenja();
                pocistiFormu();
            }catch (SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("POGREŠKA");
                alert.setContentText("IZUZETAK: " + e.getMessage());
                alert.showAndWait();
            }
        });
        pocistiFormuDugme.setOnAction(actionEvent -> {
            pocistiFormu();
        });

        obrisiDugme.setOnAction(actionEvent -> {
            Radnik selected = listaRadnika.getSelectionModel().getSelectedItem();
            if(selected != null){
                dao.deleteRadnik(selected);
                dobaviRadnikeOdjeljenja();
                pocistiFormu();
            }

        });

        listaRadnika.getSelectionModel().selectedItemProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost != null){
                opcijaUredjivanje.setSelected(true);
                poljeIme.setText(novaVrijednost.getIme());
                poljePrezime.setText(novaVrijednost.getPrezime());
                poljeKorisnickoIme.setText(novaVrijednost.getKorisnickoIme());
                poljeSifra.setText(novaVrijednost.getSifra());
                poljePlata.setText(String.valueOf(novaVrijednost.getPlata()));
            }
        });

        opcijaUredjivanje.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                dodajDugme.setDisable(true);
                urediDugme.setDisable(false);
                obrisiDugme.setDisable(false);
                if(listaRadnika.getItems().size() != 0 && listaRadnika.getSelectionModel().getSelectedItem() == null) {
                    listaRadnika.getSelectionModel().selectFirst();
                }
            }
        });

        opcijaDodavanje.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                dodajDugme.setDisable(false);
                urediDugme.setDisable(true);
                obrisiDugme.setDisable(true);
            }
        });

        urediDugme.setOnAction(actionEvent -> {
            if(!provjeriFormu()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("POGREŠKA");
                alert.setContentText("Sva polja moraju biti popunjena");
                alert.showAndWait();
                return;
            }
            Radnik selected = listaRadnika.getSelectionModel().getSelectedItem();
            if(selected != null){
                try {
                    dao.updateRadnik(new Radnik(selected.getID(), poljeIme.getText(), poljePrezime.getText(),
                            poljeKorisnickoIme.getText(), poljeSifra.getText(), Integer.parseInt(poljePlata.getText()), odjeljenje), selected.getKorisnickoIme());
                    dobaviRadnikeOdjeljenja();
                    pocistiFormu();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("POGREŠKA");
                    alert.setContentText("IZUZETAK: " + e.getMessage());
                    alert.showAndWait();
                }

            }

        });

        poljePlata.textProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            poljePlata.setText(sacuvajBrojeve(novaVrijednost));
        });

    }

    private void dobaviRadnikeOdjeljenja(){
        ObservableList<Radnik> radnici = FXCollections.observableArrayList();
        radnici.addAll(dao.selectRadniciOdjeljenja(odjeljenje));
        listaRadnika.setItems(radnici);
        opcijaUredjivanje.setDisable(listaRadnika.getItems().size() == 0);
        kapactitetOdjeljenja.setText("Kapacitet: " + listaRadnika.getItems().size() + "/" + odjeljenje.getBrojRadnihMjesta());
        if(listaRadnika.getItems().size() == odjeljenje.getBrojRadnihMjesta())
            kapactitetOdjeljenja.getStyleClass().add("text-danger");
        else kapactitetOdjeljenja.getStyleClass().removeAll("text-danger");
    }

    private void pocistiFormu(){
        opcijaDodavanje.setSelected(true);
        opcijaUredjivanje.setSelected(false);
        listaRadnika.getSelectionModel().clearSelection();
        poljeIme.setText("");
        poljePrezime.setText("");
        poljeKorisnickoIme.setText("");
        poljeSifra.setText("");
        poljePlata.setText("");
    }

    public boolean provjeriFormu(){
        return !poljeIme.getText().equals("") && !poljePrezime.getText().equals("") &&
                !poljeKorisnickoIme.getText().equals("") && !poljeSifra.getText().equals("")
                && !poljePlata.getText().equals("");
    }

    private String sacuvajBrojeve(String str){
        String povratni = "";
        for(int i = 0; i  < str.length(); i++){
            if(Character.isDigit(str.charAt(i))) povratni += str.charAt(i);
        }
        return povratni;
    }

}
