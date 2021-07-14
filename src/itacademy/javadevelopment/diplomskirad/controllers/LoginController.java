package itacademy.javadevelopment.diplomskirad.controllers;

import itacademy.javadevelopment.diplomskirad.dao.HumanResourcesDAO;
import itacademy.javadevelopment.diplomskirad.models.Admin;
import itacademy.javadevelopment.diplomskirad.models.Radnik;
import itacademy.javadevelopment.diplomskirad.models.Vlasnik;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {

    private HumanResourcesDAO dao = null;
    public TextField korisnickoImePolje;
    public PasswordField sifraPolje;
    public Button potvrdiDugme;
    public Label greskaLabela;

    @FXML
    public void initialize(){
        dao = HumanResourcesDAO.getInstance();
        potvrdiDugme.setDisable(true);

        korisnickoImePolje.textProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            greskaLabela.setText("");
            if(novaVrijednost.equals("") || sifraPolje.getText().equals("")) potvrdiDugme.setDisable(true);
            else potvrdiDugme.setDisable(false);
        });

        sifraPolje.textProperty().addListener((notUsed, staraVrijednost, novaVrijednost) -> {
            greskaLabela.setText("");
            if(novaVrijednost.equals("") || korisnickoImePolje.getText().equals("")) potvrdiDugme.setDisable(true);
            else potvrdiDugme.setDisable(false);
        });
    }

    public void klikniPotvrdiDugme(ActionEvent actionEvent) throws IOException {
        String korisnickoIme = korisnickoImePolje.getText();
        String sifra = sifraPolje.getText();

        ArrayList<Vlasnik> vlasnici = dao.selectVlasnici();
        ArrayList<Radnik> radnici = dao.selectRadnici();
        ArrayList<Admin> admini = dao.selectAdmini();

        Radnik moguciRadnik = null;
        Admin moguciAdmin = null;
        Vlasnik moguciVlasnik = null;

        for(Radnik radnik: radnici){
            if(radnik.getKorisnickoIme().equals(korisnickoIme) && radnik.getSifra().equals(sifra)){
                moguciRadnik = radnik;
                break;
            }
        }

        for(Admin admin: admini){
            if(admin.getKorisnickoIme().equals(korisnickoIme) && admin.getSifra().equals(sifra)){
                moguciAdmin = admin;
                break;
            }
        }

        for(Vlasnik vlasnik: vlasnici){
            if(vlasnik.getKorisnickoIme().equals(korisnickoIme) && vlasnik.getSifra().equals(sifra)){
                moguciVlasnik = vlasnik;
                break;
            }
        }

        if(moguciRadnik != null) {
            Scene trenutnaScena = korisnickoImePolje.getScene();
            Stage trenutniProzor = (Stage) trenutnaScena.getWindow();
            RadnikInformacijeController kontroler = new RadnikInformacijeController(moguciRadnik);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/radnik-informacije.fxml"));
            loader.setController(kontroler);
            Parent root = loader.load();
            trenutniProzor.setScene(new Scene(root));
            trenutniProzor.setTitle("Radnik dashboard");
            return;
        }

        if(moguciAdmin != null) {
            Scene trenutnaScena = korisnickoImePolje.getScene();
            Stage trenutniProzor = (Stage) trenutnaScena.getWindow();
            AdminDashboardController kontroler = new AdminDashboardController(moguciAdmin);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin-dashboard.fxml"));
            loader.setController(kontroler);
            Parent root = loader.load();
            trenutniProzor.setScene(new Scene(root));
            trenutniProzor.setTitle("Admin dashboard");
            return;
        }

        if(moguciVlasnik != null) {
            Scene trenutnaScena = korisnickoImePolje.getScene();
            Stage trenutniProzor = (Stage) trenutnaScena.getWindow();
            VlasnikDashboardController kontroler = new VlasnikDashboardController(moguciVlasnik);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/vlasnik-dashboard.fxml"));
            loader.setController(kontroler);
            Parent root = loader.load();
            trenutniProzor.setScene(new Scene(root));
            trenutniProzor.setTitle("Vlasnik dashboard");
            return;
        }

        greskaLabela.setText("Pogrešni podaci za prijavu");
    }
}
