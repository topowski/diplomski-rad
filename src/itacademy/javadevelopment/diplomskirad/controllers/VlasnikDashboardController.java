package itacademy.javadevelopment.diplomskirad.controllers;

import itacademy.javadevelopment.diplomskirad.dao.HumanResourcesDAO;
import itacademy.javadevelopment.diplomskirad.models.Admin;
import itacademy.javadevelopment.diplomskirad.models.Odjeljenje;
import itacademy.javadevelopment.diplomskirad.models.Radnik;
import itacademy.javadevelopment.diplomskirad.models.Vlasnik;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class VlasnikDashboardController {

    private Vlasnik prijavljeniVlasnik;
    private static HumanResourcesDAO dao;
    public ListView<Odjeljenje> listaOdjeljenja;
    public TextField poljeNaziv;
    public TextField poljeAdresa;
    public TextField poljeBrojRadnihMjesta;
    public RadioButton opcijaDodavanje;
    public RadioButton opcijaUredjivanje;
    public Button dodajDugme;
    public Button urediDugme;
    public Button obrisiDugme;
    public Button pocistiFormuDugme;
    public Label labelaPlata;
    public Button odjaviSeDugme;

    private String sacuvajBrojeve(String str){
        String povratni = "";
        for(int i = 0; i  < str.length(); i++){
            if(Character.isDigit(str.charAt(i))) povratni += str.charAt(i);
        }
        return povratni;
    }


    public VlasnikDashboardController(Vlasnik prijavljeniVlasnik){
        this.prijavljeniVlasnik = prijavljeniVlasnik;
        dao = HumanResourcesDAO.getInstance();
    }

    @FXML
    public void initialize(){
        urediDugme.setDisable(true);
        obrisiDugme.setDisable(true);
        dobaviOdjeljenja();
        labelaPlata.setText("Moja plata: " + prijavljeniVlasnik.getPlata());

        poljeBrojRadnihMjesta.textProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            poljeBrojRadnihMjesta.setText(sacuvajBrojeve(novaVrijednost));
        });

        opcijaDodavanje.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                dodajDugme.setDisable(false);
                urediDugme.setDisable(true);
                obrisiDugme.setDisable(true);
            }
        });

        opcijaUredjivanje.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                dodajDugme.setDisable(true);
                urediDugme.setDisable(false);
                obrisiDugme.setDisable(false);
                if(listaOdjeljenja.getItems().size() != 0 && listaOdjeljenja.getSelectionModel().getSelectedItem() == null) {
                    listaOdjeljenja.getSelectionModel().selectFirst();
                }
            }
        });

        pocistiFormuDugme.setOnAction(actionEvent -> {
            pocistiFormu();
        });

        dodajDugme.setOnAction(actionEvent -> {
            if(!provjeriFormu()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("POGREŠKA");
                alert.setContentText("Sva polja moraju biti popunjena");
                alert.showAndWait();
                return;
            }
            dodajOdjeljenje();
            pocistiFormu();
        });

        urediDugme.setOnAction(actionEvent -> {
            if(!provjeriFormu()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("POGREŠKA");
                alert.setContentText("Sva polja moraju biti popunjena");
                alert.showAndWait();
                return;
            }
            azurirajOdjeljenje();
            pocistiFormu();
        });

        obrisiDugme.setOnAction(actionEvent -> {
            Odjeljenje selected = listaOdjeljenja.getSelectionModel().getSelectedItem();
            dao.deleteOdjeljenje(selected);
            dobaviOdjeljenja();
            pocistiFormu();
        });

        listaOdjeljenja.getSelectionModel().selectedItemProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            opcijaUredjivanje.setSelected(true);
            poljeNaziv.setText(novaVrijednost.getNaziv());
            poljeAdresa.setText(novaVrijednost.getAdresa());
            poljeBrojRadnihMjesta.setText(String.valueOf(novaVrijednost.getBrojRadnihMjesta()));
        });

        listaOdjeljenja.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        Odjeljenje selected = listaOdjeljenja.getSelectionModel().getSelectedItem();
                        if(selected != null) {
                            try {
                                Scene trenutnaScena = poljeNaziv.getScene();
                                Stage trenutniProzor = (Stage) trenutnaScena.getWindow();
                                VlasnikOdjeljenjeController kontroler = new VlasnikOdjeljenjeController(prijavljeniVlasnik, selected);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vlasnik-odjeljenje.fxml"));
                                loader.setController(kontroler);
                                Parent root =  loader.load();
                                trenutniProzor.setScene(new Scene(root));
                                trenutniProzor.setTitle("Odjeljenje");
                            } catch (IOException e) {
                                // Ignore
                            }

                        }
                    }
                }
            }
        });

        odjaviSeDugme.setOnAction(actionEvent -> {
            try {
                Scene trenutnaScena = poljeNaziv.getScene();
                Stage trenutniProzor = (Stage) trenutnaScena.getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
                trenutniProzor.setScene(new Scene(root));
                trenutniProzor.setTitle("AO Human Resources");
            } catch (IOException e) {
                System.out.println("IZUZETAK: " + e.getMessage());
            }
        });
    }

    private void dodajOdjeljenje() {
        dao.insertOdjeljenje(new Odjeljenje(-1, poljeNaziv.getText(), poljeAdresa.getText(), Integer.parseInt(poljeBrojRadnihMjesta.getText()), prijavljeniVlasnik));
        dobaviOdjeljenja();
    }

    private void azurirajOdjeljenje() {
        dao.updateOdjeljenje(new Odjeljenje( listaOdjeljenja.getSelectionModel().getSelectedItem().getID(), poljeNaziv.getText(), poljeAdresa.getText(), Integer.parseInt(poljeBrojRadnihMjesta.getText()), prijavljeniVlasnik));
        dobaviOdjeljenja();
    }

    private boolean provjeriFormu() {
        return !poljeNaziv.getText().equals("") && !poljeAdresa.getText().equals("") && !poljeBrojRadnihMjesta.getText().equals("");
    }

    private void dobaviOdjeljenja(){
        listaOdjeljenja.getItems().clear();
        ArrayList<Odjeljenje> svaOdjeljenja = dao.selectOdjeljenja();
        for(Odjeljenje odjeljenje: svaOdjeljenja){
            if(odjeljenje.getVlasnik() != null && odjeljenje.getVlasnik().getID() == prijavljeniVlasnik.getID()){
                listaOdjeljenja.getItems().add(odjeljenje);
            }
        }
        opcijaUredjivanje.setDisable(listaOdjeljenja.getItems().size() == 0);
    }

    private void pocistiFormu(){
        listaOdjeljenja.getSelectionModel().clearSelection();
        opcijaDodavanje.setSelected(true);
        poljeNaziv.setText("");
        poljeAdresa.setText("");
        poljeBrojRadnihMjesta.setText("");
    }

}
