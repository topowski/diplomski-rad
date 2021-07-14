package itacademy.javadevelopment.diplomskirad.models;

public class Vlasnik {
    private int ID;
    private String ime;
    private String prezime;
    private String korisnickoIme;
    private String sifra;
    private int plata;

    public Vlasnik(int ID, String ime, String prezime, String korisnickoIme, String sifra, int plata) {
        this.ID = ID;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;
        this.plata = plata;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public int getPlata() {
        return plata;
    }

    public void setPlata(int plata) {
        this.plata = plata;
    }

    @Override
    public String toString() {
        return "VLASNIK: " + ime + " " + prezime + " (" + korisnickoIme + ")";
    }
}
