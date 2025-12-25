package scr;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String nom;
    private String format; // M3U, JSPF, XSPF
    private final List<FichierMp3> pistes;

    public Playlist(String nom) {
        this.nom = nom;
        this.pistes = new ArrayList<>(); // Liste modifiable
    }

    public void vider() {
        pistes.clear();
    }

    public int taille() {
        return pistes.size();
    }

    public boolean estVide() {
        return pistes.isEmpty();
    }

    public boolean contient(FichierMp3 mp3) {
        return pistes.contains(mp3);
    }

    // Méthodes pour ajouter des pistes (préférable pour protéger la liste)
    public void ajouterPiste(FichierMp3 mp3) {
        pistes.add(mp3);
    }

    public void ajouterPistes(List<FichierMp3> mp3s) {
        pistes.addAll(mp3s);
    }

    public List<FichierMp3> getPistes() {
        return pistes;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
