package itacademy.javadevelopment.diplomskirad.controllers;

import itacademy.javadevelopment.diplomskirad.dao.HumanResourcesDAO;
import itacademy.javadevelopment.diplomskirad.models.Admin;
import itacademy.javadevelopment.diplomskirad.models.Odjeljenje;
import itacademy.javadevelopment.diplomskirad.models.Radnik;
import itacademy.javadevelopment.diplomskirad.models.Vlasnik;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
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
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDashboardController {

    private Admin prijavljeniAdmin;
    public ListView<Object> listaUsera;
    public TextField poljeIme;
    public TextField poljePrezime;
    public TextField poljeKorisnickoIme;
    public TextField poljeSifra;
    public TextField poljePlata;
    public ChoiceBox<Odjeljenje> poljeOdjeljenje;
    public RadioButton opcijaDodavanje;
    public RadioButton opcijaUredjivanje;
    public RadioButton opcijaAdmin;
    public RadioButton opcijaVlasnik;
    public  RadioButton opcijaRadnik;
    public static HumanResourcesDAO dao;
    public Button pocistiFormuDugme;
    public Button dodajDugme;
    public Button urediDugme;
    public Button obrisiDugme;
    public Button odjaviSeDugme;

    private String sacuvajBrojeve(String str){
        String povratni = "";
        for(int i = 0; i  < str.length(); i++){
            if(Character.isDigit(str.charAt(i))) povratni += str.charAt(i);
        }
        return povratni;
    }


    public AdminDashboardController(Admin prijavljeniAdmin){
        this.prijavljeniAdmin = prijavljeniAdmin;
        dao = HumanResourcesDAO.getInstance();
    }

    @FXML
    public void initialize(){

        poljePlata.setDisable(true);
        poljeOdjeljenje.setDisable(true);
        urediDugme.setDisable(true);
        obrisiDugme.setDisable(true);
        dobaviKorisnike();

        ObservableList<Object> objekti = FXCollections.observableArrayList();
        ArrayList<Admin> sviAdmini = dao.selectAdmini();
        sviAdmini.remove(prijavljeniAdmin);
        objekti.addAll( sviAdmini );
        objekti.addAll(dao.selectVlasnici());
        objekti.addAll( dao.selectRadnici() );
        listaUsera.getItems().clear();
        listaUsera.setItems(objekti);

        ObservableList<Odjeljenje> odjeljenja =  FXCollections.observableArrayList();
        odjeljenja.addAll(dao.selectOdjeljenja());
        poljeOdjeljenje.setItems(odjeljenja);

        poljePlata.textProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            poljePlata.setText(sacuvajBrojeve(novaVrijednost));
        });

        opcijaDodavanje.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                dodajDugme.setDisable(false);
                opcijaAdmin.setDisable(false);
                opcijaVlasnik.setDisable(false);
                opcijaRadnik.setDisable(false);
                urediDugme.setDisable(true);
                obrisiDugme.setDisable(true);
            }
        });

        opcijaUredjivanje.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                dodajDugme.setDisable(true);
                urediDugme.setDisable(false);
                obrisiDugme.setDisable(false);
                if(listaUsera.getItems().size() != 0 && listaUsera.getSelectionModel().getSelectedItem() == null) {
                    listaUsera.getSelectionModel().selectFirst();
                }
                Object selected = listaUsera.getSelectionModel().getSelectedItem();
                if(selected instanceof Admin){
                    opcijaAdmin.setDisable(false);
                    opcijaVlasnik.setDisable(true);
                    opcijaRadnik.setDisable(true);
                    return;
                }
                if(selected instanceof Vlasnik){
                    opcijaVlasnik.setDisable(false);
                    opcijaAdmin.setDisable(true);
                    opcijaRadnik.setDisable(true);
                    return;
                }
                opcijaRadnik.setDisable(false);
                opcijaAdmin.setDisable(true);
                opcijaVlasnik.setDisable(true);
            }
        });

        opcijaAdmin.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                poljePlata.setText("");
                poljePlata.setDisable(true);
                poljeOdjeljenje.setValue(null);
                poljeOdjeljenje.setDisable(true);
            }
        });

        opcijaVlasnik.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                poljePlata.setDisable(false);
                poljeOdjeljenje.setValue(null);
                poljeOdjeljenje.setDisable(true);
            }
        });

        opcijaRadnik.selectedProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost){
                poljePlata.setDisable(false);
                poljeOdjeljenje.setDisable(false);
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
            dodajKorisnika();
        });

        urediDugme.setOnAction(actionEvent -> {
            if(!provjeriFormu()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("POGREŠKA");
                alert.setContentText("Sva polja moraju biti popunjena");
                alert.showAndWait();
                return;
            }
            urediKorisnika();
        });

        obrisiDugme.setOnAction(actionEvent -> {
            Object selected = listaUsera.getSelectionModel().getSelectedItem();
            if(selected instanceof Admin){
                Admin admin = (Admin) selected;
                dao.deleteAdmin(admin);
                dobaviKorisnike();
                pocistiFormu();
                return;
            }

            if(selected instanceof Vlasnik){
                Vlasnik vlasnik = (Vlasnik) selected;
                dao.deleteVlasnik(vlasnik);
                dobaviKorisnike();
                pocistiFormu();
                return;
            }

                Radnik radnik = (Radnik) selected;
                dao.deleteRadnik(radnik);
                dobaviKorisnike();
            pocistiFormu();
        });

        listaUsera.getSelectionModel().selectedItemProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            if(novaVrijednost instanceof Admin){
                opcijaUredjivanje.setSelected(true);
                opcijaAdmin.setSelected(true);
                opcijaAdmin.setDisable(false);
                opcijaVlasnik.setDisable(true);
                opcijaRadnik.setDisable(true);
                Admin admin = (Admin) novaVrijednost;
                poljeIme.setText(admin.getIme());
                poljePrezime.setText(admin.getPrezime());
                poljeKorisnickoIme.setText(admin.getKorisnickoIme());
                poljeSifra.setText(admin.getSifra());
                return;
            }

            if(novaVrijednost instanceof Vlasnik){
                opcijaUredjivanje.setSelected(true);
                opcijaVlasnik.setSelected(true);
                opcijaVlasnik.setDisable(false);
                opcijaAdmin.setDisable(true);
                opcijaRadnik.setDisable(true);
                Vlasnik vlasnik = (Vlasnik) novaVrijednost;
                poljeIme.setText(vlasnik.getIme());
                poljePrezime.setText(vlasnik.getPrezime());
                poljeKorisnickoIme.setText(vlasnik.getKorisnickoIme());
                poljeSifra.setText(vlasnik.getSifra());
                poljePlata.setText(String.valueOf(vlasnik.getPlata()));
                return;
            }

            opcijaUredjivanje.setSelected(true);
            opcijaRadnik.setSelected(true);
            opcijaRadnik.setDisable(false);
            opcijaAdmin.setDisable(true);
            opcijaVlasnik.setDisable(true);
            Radnik radnik = (Radnik) novaVrijednost;
            poljeIme.setText(radnik.getIme());
            poljePrezime.setText(radnik.getPrezime());
            poljeKorisnickoIme.setText(radnik.getKorisnickoIme());
            poljeSifra.setText(radnik.getSifra());
            poljePlata.setText(String.valueOf(radnik.getPlata()));
            poljeOdjeljenje.setValue(radnik.getOdjeljenje());
        });

        odjaviSeDugme.setOnAction(actionEvent -> {
            try {
                Scene trenutnaScena = poljeIme.getScene();
                Stage trenutniProzor = (Stage) trenutnaScena.getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
                trenutniProzor.setScene(new Scene(root));
                trenutniProzor.setTitle("AO Human Resources");
            } catch (IOException e) {
                System.out.println("IZUZETAK: " + e.getMessage());
            }
        });
    }

    private void pocistiFormu(){
        listaUsera.getSelectionModel().clearSelection();
        opcijaAdmin.setSelected(true);
        opcijaDodavanje.setSelected(true);
        opcijaAdmin.setDisable(false);
        opcijaVlasnik.setDisable(false);
        opcijaRadnik.setDisable(false);
        poljeIme.setText("");
        poljePrezime.setText("");
        poljeKorisnickoIme.setText("");
        poljeSifra.setText("");
        poljePlata.setText("");
        poljeOdjeljenje.setValue(null);
    }

    private boolean provjeriFormu(){
        if(opcijaAdmin.isSelected()){
            return !poljeIme.getText().equals("") && !poljePrezime.getText().equals("") &&
                    !poljeKorisnickoIme.getText().equals("") && !poljeSifra.getText().equals("");
        }
        if(opcijaVlasnik.isSelected()){
            return !poljeIme.getText().equals("") && !poljePrezime.getText().equals("") &&
                    !poljeKorisnickoIme.getText().equals("") && !poljeSifra.getText().equals("")
                    && !poljePlata.getText().equals("");
        }
        return !poljeIme.getText().equals("") && !poljePrezime.getText().equals("") &&
                !poljeKorisnickoIme.getText().equals("") && !poljeSifra.getText().equals("")
                && !poljePlata.getText().equals("") && poljeOdjeljenje.getValue() != null;
    }

    private void dobaviKorisnike(){
        ObservableList<Object> objekti = FXCollections.observableArrayList();
        ArrayList<Admin> sviAdmini = dao.selectAdmini();
        sviAdmini.remove(prijavljeniAdmin);
        objekti.addAll( sviAdmini );
        objekti.addAll(dao.selectVlasnici());
        objekti.addAll( dao.selectRadnici() );
        listaUsera.getItems().clear();
        listaUsera.setItems(objekti);
        opcijaUredjivanje.setDisable(listaUsera.getItems().size() == 0);
        pocistiFormu();
    }

    private void dodajKorisnika(){
        try{
        if(opcijaAdmin.isSelected()){
          dao.insertAdmin(new Admin(-1, poljeIme.getText(), poljePrezime.getText(), poljeKorisnickoIme.getText(), poljeSifra.getText()));
          dobaviKorisnike();
          return;
        }
        if(opcijaVlasnik.isSelected()){
            dao.insertVlasnik(new Vlasnik(-1, poljeIme.getText(), poljePrezime.getText(),
                    poljeKorisnickoIme.getText(), poljeSifra.getText(), Integer.parseInt(poljePlata.getText())));
            dobaviKorisnike();
            return;
        }
      dao.insertRadnik(new Radnik(-1, poljeIme.getText(), poljePrezime.getText(),
              poljeKorisnickoIme.getText(), poljeSifra.getText(), Integer.parseInt(poljePlata.getText()), poljeOdjeljenje.getValue()));
        dobaviKorisnike();
        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("POGREŠKA");
            alert.setContentText("IZUZETAK: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void urediKorisnika(){
        try {
            Object selected = listaUsera.getSelectionModel().getSelectedItem();
            if(opcijaAdmin.isSelected()){
                Admin admin = (Admin) selected;
                dao.updateAdmin(new Admin(admin.getID(), poljeIme.getText(), poljePrezime.getText(), poljeKorisnickoIme.getText(), poljeSifra.getText()), admin.getKorisnickoIme());
                dobaviKorisnike();
                return;
            }
            if(opcijaVlasnik.isSelected()){
                Vlasnik vlasnik = (Vlasnik) selected;
                dao.updateVlasnik(new Vlasnik(vlasnik.getID(), poljeIme.getText(), poljePrezime.getText(),
                        poljeKorisnickoIme.getText(), poljeSifra.getText(), Integer.parseInt(poljePlata.getText())), vlasnik.getKorisnickoIme());
                dobaviKorisnike();
                return;
            }
            Radnik radnik = (Radnik) selected;
            dao.updateRadnik(new Radnik(radnik.getID(), poljeIme.getText(), poljePrezime.getText(),
                    poljeKorisnickoIme.getText(), poljeSifra.getText(), Integer.parseInt(poljePlata.getText()), poljeOdjeljenje.getValue()), radnik.getKorisnickoIme());
            dobaviKorisnike();
        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("POGREŠKA");
            alert.setContentText("IZUZETAK: " + e.getMessage());
            alert.showAndWait();
        }

    }

}
