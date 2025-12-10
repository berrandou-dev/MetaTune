package scr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Playlist {
    //Attrributs
    private String nom;
    private String format; // M3U, JSPF, XSPF
    private final List<FichierMp3> pistes;

    //Constructeur
    public Playlist(String nom) {
        this.nom = nom;
        this.pistes = new ArrayList<>();
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

    public List<FichierMp3> getPistes() {
        return Collections.unmodifiableList(pistes);
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
