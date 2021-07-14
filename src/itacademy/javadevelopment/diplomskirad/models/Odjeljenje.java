package itacademy.javadevelopment.diplomskirad.models;

public class Odjeljenje {

    private int ID;
    private String naziv;
    private String adresa;
    private int brojRadnihMjesta;
    private Vlasnik vlasnik;

    public Odjeljenje(int ID, String naziv, String adresa, int brojRadnihMjesta, Vlasnik vlasnik) {
        this.ID = ID;
        this.naziv = naziv;
        this.adresa = adresa;
        this.brojRadnihMjesta = brojRadnihMjesta;
        this.vlasnik = vlasnik;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getBrojRadnihMjesta() {
        return brojRadnihMjesta;
    }

    public void setBrojRadnihMjesta(int brojRadnihMjesta) {
        this.brojRadnihMjesta = brojRadnihMjesta;
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    @Override
    public String toString() {
        return naziv + " (" + adresa + ")";
    }
}
