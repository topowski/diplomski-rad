PRAGMA FOREIGN_KEYS = ON;

CREATE TABLE Admin (
    ID INTEGER PRIMARY KEY,
    ime TEXT,
    prezime TEXT,
    korisnicko_ime TEXT,
    sifra TEXT
);

INSERT INTO Admin VALUES (1,'admin', 'admin', 'admin', 'admin');

CREATE TABLE Vlasnik(
    ID INTEGER PRIMARY KEY,
    ime TEXT,
    prezime TEXT,
    korisnicko_ime TEXT,
    sifra TEXT,
    plata INTEGER
);

CREATE TABLE Odjeljenje (
  ID INTEGER PRIMARY KEY,
  naziv TEXT,
  adresa TEXT,
  broj_radnih_mjesta INTEGER,
  vlasnik_id INTEGER REFERENCES Vlasnik(ID) ON DELETE SET NULL
);

CREATE TABLE Radnik (
    ID INTEGER PRIMARY KEY,
    ime TEXT,
    prezime TEXT,
    korisnicko_ime TEXT,
    sifra TEXT,
    plata INTEGER,
    odjeljenje_id INTEGER REFERENCES Odjeljenje(ID) ON DELETE SET NULL
);