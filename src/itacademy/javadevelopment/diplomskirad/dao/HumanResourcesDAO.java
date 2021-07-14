package itacademy.javadevelopment.diplomskirad.dao;

import itacademy.javadevelopment.diplomskirad.models.Admin;
import itacademy.javadevelopment.diplomskirad.models.Odjeljenje;
import itacademy.javadevelopment.diplomskirad.models.Radnik;
import itacademy.javadevelopment.diplomskirad.models.Vlasnik;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HumanResourcesDAO {

    private static HumanResourcesDAO instance = null;
    private static Connection connection = null;

    public static HumanResourcesDAO getInstance(){
        if(instance == null) instance = new HumanResourcesDAO();
        return instance;
    }

    public static void removeInstance(){
        try{
            if(connection != null) connection.close();
            connection = null;
            instance = null;
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    public HumanResourcesDAO(){
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");

            try{
                PreparedStatement query = connection.prepareStatement("SELECT * FROM Admin");
                query.executeQuery();
                 query = connection.prepareStatement("SELECT * FROM Vlasnik");
                query.executeQuery();
                query = connection.prepareStatement("SELECT * FROM Odjeljenje");
                query.executeQuery();
                query = connection.prepareStatement("SELECT * FROM Radnik");
                query.executeQuery();
            }catch (SQLException e){
                System.out.println("IZUZETAK: " + e.getMessage());
                createDatabase();
            }
        } catch (SQLException e) {
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    public void createDatabase(){
        try{
            Scanner input = new Scanner(new FileInputStream(System.getProperty("user.dir") + "/resources/sql/ao_human_resources_baza_podataka.sql"));
            String sqlQuerry = "";
            while( input.hasNext() ){
                sqlQuerry += input.nextLine();
                if( sqlQuerry.length() != 0 && sqlQuerry.charAt( sqlQuerry.length() - 1 ) == ';' ){
                    Statement statement = connection.createStatement();
                    statement.execute(sqlQuerry);
                    sqlQuerry = "";
                }
            }
            input.close();
        } catch (Exception e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    // CRUD Admin
    public ArrayList<Admin> selectAdmini(){
        ArrayList<Admin> dobavljeniAdmini = new ArrayList<>();
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Admin");
            ResultSet resultSet = query.executeQuery();
            while(resultSet.next()){
                dobavljeniAdmini.add( new Admin( resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5) ));
            }
        } catch (SQLException e) {
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return dobavljeniAdmini;
    }

    public Admin selectAdminByID(int ID){
        try{
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Admin WHERE ID=?");
            query.setInt(1, ID);
            ResultSet resultSet = query.executeQuery();
            if(!resultSet.next()) return null;
            return new Admin(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5) );
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return null;
    }

    public void insertAdmin(Admin newAdmin) throws SQLException {
            if( !validirajUsername(newAdmin.getKorisnickoIme()) ) throw new SQLException("Korisnik vec postoji");
            PreparedStatement query = connection.prepareStatement("INSERT INTO Admin VALUES (?,?,?,?,?)");
            query.setInt(1,  nextIndex("Admin"));
            query.setString(2, newAdmin.getIme());
            query.setString(3, newAdmin.getPrezime());
            query.setString(4, newAdmin.getKorisnickoIme());
            query.setString(5, newAdmin.getSifra());
            query.executeUpdate();
    }

    public void updateAdmin(Admin newAdmin, String staroKorisnickoIme) throws SQLException {
            if( !newAdmin.getKorisnickoIme().equals(staroKorisnickoIme) && !validirajUsername(newAdmin.getKorisnickoIme()) ) throw new SQLException("Korisnik vec postoji");
            PreparedStatement query = connection.prepareStatement("UPDATE newAdmin SET ime=?, prezime=?, korisnicko_ime=?, sifra=? WHERE ID=?");
            query.setString(1, newAdmin.getIme());
            query.setString(2, newAdmin.getPrezime());
            query.setString(3, newAdmin.getKorisnickoIme());
            query.setString(4, newAdmin.getSifra());
            query.setInt(5, newAdmin.getID());
            query.executeUpdate();
    }

    public void deleteAdmin(Admin admin){
        try{
            PreparedStatement query = connection.prepareStatement("DELETE FROM Admin WHERE ID=?");
            query.setInt(1, admin.getID());
            query.executeUpdate();
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    // CRUD Vlasnik
    public ArrayList<Vlasnik> selectVlasnici(){
        ArrayList<Vlasnik> dobavljeniVlasnici = new ArrayList<>();
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Vlasnik");
            ResultSet resultSet = query.executeQuery();
            while(resultSet.next()){
                dobavljeniVlasnici.add( new Vlasnik( resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6) ) );
            }
        } catch (SQLException e) {
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return dobavljeniVlasnici;
    }

    public Vlasnik selectVlasnikByID(int ID){
        try{
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Vlasnik WHERE ID=?");
            query.setInt(1, ID);
            ResultSet resultSet = query.executeQuery();
            if(!resultSet.next()) return null;
            return new Vlasnik(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6) );
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return null;
    }

    public void insertVlasnik(Vlasnik newVlasnik) throws SQLException {
            if( !validirajUsername(newVlasnik.getKorisnickoIme()) ) throw new SQLException("Korisnik vec postoji");
            PreparedStatement query = connection.prepareStatement("INSERT INTO Vlasnik VALUES (?,?,?,?,?,?)");
            query.setInt(1,  nextIndex("Vlasnik"));
            query.setString(2, newVlasnik.getIme());
            query.setString(3, newVlasnik.getPrezime());
            query.setString(4, newVlasnik.getKorisnickoIme());
            query.setString(5, newVlasnik.getSifra());
            query.setInt(6, newVlasnik.getPlata());
            query.executeUpdate();
    }

    public void updateVlasnik(Vlasnik newVlasnik, String staroKorisnickoIme) throws SQLException {
            if( !newVlasnik.getKorisnickoIme().equals(staroKorisnickoIme) && !validirajUsername(newVlasnik.getKorisnickoIme()) ) throw new SQLException("Korisnik vec postoji");
            PreparedStatement query = connection.prepareStatement("UPDATE Vlasnik SET ime=?, prezime=?, korisnicko_ime=?, sifra=?, plata=? WHERE ID=?");
            query.setString(1, newVlasnik.getIme());
            query.setString(2, newVlasnik.getPrezime());
            query.setString(3, newVlasnik.getKorisnickoIme());
            query.setString(4, newVlasnik.getSifra());
            query.setInt(5, newVlasnik.getPlata());
            query.setInt(6, newVlasnik.getID());
            query.executeUpdate();
    }

    public void deleteVlasnik(Vlasnik vlasnik){
        try{
            ArrayList<Odjeljenje> svaOdjeljenja = selectOdjeljenja();
            ArrayList<Odjeljenje> vlasnikOdjeljenja = new ArrayList<>();
            for(Odjeljenje odjeljenje: svaOdjeljenja){
                if(odjeljenje.getVlasnik() != null && odjeljenje.getVlasnik().getID() == vlasnik.getID()){
                    vlasnikOdjeljenja.add(odjeljenje);
                }
            }
            for(Odjeljenje odjeljenje: vlasnikOdjeljenja){
                deleteOdjeljenje(odjeljenje);
            }
            PreparedStatement query = connection.prepareStatement("DELETE FROM Vlasnik WHERE ID=?");
            query.setInt(1, vlasnik.getID());
            query.executeUpdate();
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    // CRUD Odjeljenje
    public ArrayList<Odjeljenje> selectOdjeljenja(){
        ArrayList<Odjeljenje> dobavljenaOdjeljenja = new ArrayList<>();
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Odjeljenje");
            ResultSet resultSet = query.executeQuery();
            while(resultSet.next()){
                Vlasnik vlasnikOdjeljenja = selectVlasnikByID(resultSet.getInt(5));
                dobavljenaOdjeljenja.add(new Odjeljenje(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), vlasnikOdjeljenja) );
            }
        } catch (SQLException e) {
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return dobavljenaOdjeljenja;
    }

    public Odjeljenje selectOdjeljenjeByID(int ID){
        try{
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Odjeljenje WHERE ID=?");
            query.setInt(1, ID);
            ResultSet resultSet = query.executeQuery();
            if(!resultSet.next()) return null;
            Vlasnik vlasnikOdjeljenja = selectVlasnikByID(resultSet.getInt(5));
            return new Odjeljenje(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getInt(4), vlasnikOdjeljenja);
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return null;
    }

    public void insertOdjeljenje(Odjeljenje newOdjeljenje){
        try{
            PreparedStatement query = connection.prepareStatement("INSERT INTO Odjeljenje VALUES (?,?,?,?,?)");
            query.setInt(1,  nextIndex("Odjeljenje"));
            query.setString(2, newOdjeljenje.getNaziv());
            query.setString(3,newOdjeljenje.getAdresa());
            query.setInt(4, newOdjeljenje.getBrojRadnihMjesta());
            query.setInt(5, newOdjeljenje.getVlasnik().getID());
            query.executeUpdate();
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    public void updateOdjeljenje(Odjeljenje newOdjeljenje){
        try{
            PreparedStatement query = connection.prepareStatement("UPDATE Odjeljenje SET naziv=?, adresa=?, broj_radnih_mjesta=?, vlasnik_id=? WHERE ID=?");
            query.setString(1, newOdjeljenje.getNaziv());
            query.setString(2, newOdjeljenje.getAdresa());
            query.setInt(3, newOdjeljenje.getBrojRadnihMjesta());
            query.setInt(4, newOdjeljenje.getVlasnik().getID());
            query.setInt(5, newOdjeljenje.getID());
            query.executeUpdate();
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    public void deleteOdjeljenje(Odjeljenje odjeljenje){
        try{
            ArrayList<Radnik> sviRadnici = selectRadnici();
            ArrayList<Radnik>  radniciOdjeljenja = new ArrayList<>();
            for(Radnik radnik: sviRadnici){
                if(radnik.getOdjeljenje() != null && radnik.getOdjeljenje().getID() == odjeljenje.getID()) radniciOdjeljenja.add(radnik);
            }
            for(Radnik radnik: radniciOdjeljenja) deleteRadnik(radnik);
            PreparedStatement query = connection.prepareStatement("DELETE FROM Odjeljenje WHERE ID=?");
            query.setInt(1, odjeljenje.getID());
            query.executeUpdate();
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    public int getBrojOdjeljenjaVlasnika(Vlasnik vlasnik){
        try{
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Odjeljenje WHERE vlasnik_id=?");
            query.setInt(1, vlasnik.getID());
            ResultSet resultSet = query.executeQuery();
            int brojac = 0;
            while(resultSet.next()) brojac++;
            return brojac;
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return 0;
    }

    //CRUD Radnik
    public ArrayList<Radnik> selectRadnici(){
        ArrayList<Radnik> dobavljeniRadnici = new ArrayList<>();
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Radnik");
            ResultSet resultSet = query.executeQuery();
            while(resultSet.next()){
                Odjeljenje odjeljenjeUKojemRadi = selectOdjeljenjeByID(resultSet.getInt(7));
                dobavljeniRadnici.add(new Radnik(resultSet.getInt(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),
                        resultSet.getInt(6),odjeljenjeUKojemRadi));
            }
        } catch (SQLException e) {
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return dobavljeniRadnici;
    }

    public Radnik selectRadnikByID(int ID){
        try{
            PreparedStatement query = connection.prepareStatement("SELECT * FROM Radnik WHERE ID=?");
            query.setInt(1, ID);
            ResultSet resultSet = query.executeQuery();
            if(!resultSet.next()) return null;
            Odjeljenje odjeljenjeUKojemRadi = selectOdjeljenjeByID(resultSet.getInt(7));
           return new Radnik(resultSet.getInt(1),resultSet.getString(2),
                   resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),
                   resultSet.getInt(6),odjeljenjeUKojemRadi);
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return null;
    }

    public void insertRadnik(Radnik newRadnik) throws SQLException {
            if( !validirajUsername(newRadnik.getKorisnickoIme()) ) throw new SQLException("Korisnik vec postoji");
            PreparedStatement query = connection.prepareStatement("INSERT INTO Radnik VALUES (?,?,?,?,?,?,?)");
            query.setInt(1, nextIndex("Radnik"));
            query.setString(2, newRadnik.getIme());
            query.setString(3, newRadnik.getPrezime());
            query.setString(4, newRadnik.getKorisnickoIme());
            query.setString(5, newRadnik.getSifra());
            query.setInt(6, newRadnik.getPlata());
            query.setInt(7, newRadnik.getOdjeljenje().getID());
            query.executeUpdate();
    }

    public void updateRadnik(Radnik newRadnik, String staroKorisnickoIme) throws SQLException {
            if( !newRadnik.getKorisnickoIme().equals(staroKorisnickoIme) && !validirajUsername(newRadnik.getKorisnickoIme()) ) throw new SQLException("Korisnik vec postoji");
            PreparedStatement query = connection.prepareStatement("UPDATE Radnik SET ime=?, prezime=?, korisnicko_ime=?, sifra=?, plata=?,odjeljenje_id=? WHERE ID=?");
            query.setString(1, newRadnik.getIme());
            query.setString(2, newRadnik.getPrezime());
            query.setString(3, newRadnik.getKorisnickoIme());
            query.setString(4, newRadnik.getSifra());
            query.setInt(5, newRadnik.getPlata());
            query.setInt(6, newRadnik.getOdjeljenje().getID());
            query.setInt(7, newRadnik.getID());
            query.executeUpdate();
    }

    public void deleteRadnik(Radnik radnik){
        try{
            PreparedStatement query = connection.prepareStatement("DELETE FROM Radnik WHERE ID=?");
            query.setInt(1, radnik.getID());
            query.executeUpdate();
        }catch (SQLException e){
            System.out.println("IZUZETAK: " + e.getMessage());
        }
    }

    private boolean validirajUsername(String username){
        ArrayList<Admin> admini = selectAdmini();
        ArrayList<Vlasnik> vlasnici = selectVlasnici();
        ArrayList<Radnik> radnici = selectRadnici();
        for(Admin admin: admini) if(admin.getKorisnickoIme().equals(username)) return false;
        for(Vlasnik vlasnik: vlasnici) if(vlasnik.getKorisnickoIme().equals(username)) return false;
        for(Radnik radnik: radnici) if(radnik.getKorisnickoIme().equals(username)) return false;
        return true;
    }

    private int nextIndex(String imeTabele){
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement("SELECT id FROM " + imeTabele + " ORDER BY ID DESC LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            int index = 0;
            while( resultSet.next() )
                index = resultSet.getInt(1);
            return ++index;
        } catch (SQLException e) {
            System.out.println("IZUZETAK: " + e.getMessage());
        }
        return -999;
    }
}
