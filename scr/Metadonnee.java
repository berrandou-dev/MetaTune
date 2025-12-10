package scr;

import java.io.File;

public class Metadonnee {
    //Attributs
    private String titre;
    private String artiste;
    private String album;
    private int annee;
    private int duree;
    private String genre;
    private int numeroPiste;
    private byte[] pochette;

    // Constructeur
    public Metadonnee(String titre, String artiste, String album, int annee, int duree, String genre, int numeroPiste, byte[] pochette) {
        this.titre = titre;
        this.artiste = artiste;
        this.album = album;
        this.annee = annee;
        this.duree = duree;
        this.genre = genre;
        this.numeroPiste = numeroPiste;
        this.pochette = pochette;
    }

    public Metadonnee(File fichier){
    }

    // Getters
    public String getTitre() {
        return titre;
    }
    public String getArtiste() {
        return artiste;
    }
    public String getAlbum() {
        return album;
    }
    public int getAnnee() {
        return annee;
    }
    public int getDuree() {
        return duree;
    }
    public String getGenre() {
        return genre;
    }
    public int getNumeroPiste() {
        return numeroPiste;
    }
    public byte[] getPochette() {
        return pochette;
    }



}