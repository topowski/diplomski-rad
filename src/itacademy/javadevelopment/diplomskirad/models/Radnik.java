package itacademy.javadevelopment.diplomskirad.models;

public class Radnik {

    private int ID;
    private String ime;
    private String prezime;
    private String korisnickoIme;
    private String sifra;
    private int plata;
    private Odjeljenje odjeljenje;

    public Radnik(int ID, String ime, String prezime, String korisnickoIme, String sifra, int plata, Odjeljenje odjeljenje) {
        this.ID = ID;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;
        this.plata = plata;
        this.odjeljenje = odjeljenje;
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

    public Odjeljenje getOdjeljenje() {
        return odjeljenje;
    }

    public void setOdjeljenje(Odjeljenje odjeljenje) {
        this.odjeljenje = odjeljenje;
    }

    @Override
    public String toString() {
        return "RADNIK: " + ime + " " + prezime + " (" + korisnickoIme + ")";
    }
}
