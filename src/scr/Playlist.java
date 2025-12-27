package scr;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une playlist de fichiers MP3.
 * <p>
 * Une playlist contient un nom, un format (M3U, JSPF, XSPF) et une liste de pistes.
 * Elle permet d'ajouter, retirer ou consulter des pistes, ainsi que de gérer son contenu.
 * </p>
 * 
 * Exemple d'utilisation :
 * <pre>
 * Playlist pl = new Playlist("Ma Playlist");
 * FichierMp3 mp3 = new FichierMp3("musique.mp3");
 * pl.ajouterPiste(mp3);
 * System.out.println("Taille : " + pl.taille());
 * </pre>
 * 
 * @author Nassim
 * @version 1.0
 */
public class Playlist {
    private String nom;
    private String format; // M3U, JSPF, XSPF
    private final List<FichierMp3> pistes;

    /**
     * Constructeur.
     * 
     * @param nom Nom de la playlist
     */
    public Playlist(String nom) {
        this.nom = nom;
        this.pistes = new ArrayList<>(); // Liste modifiable
    }

    /**
     * Vide toutes les pistes de la playlist.
     */
    public void vider() {
        pistes.clear();
    }

    /**
     * Retourne le nombre de pistes dans la playlist.
     * 
     * @return nombre de pistes
     */
    public int taille() {
        return pistes.size();
    }

    /**
     * Vérifie si la playlist est vide.
     * 
     * @return true si la playlist ne contient aucune piste
     */
    public boolean estVide() {
        return pistes.isEmpty();
    }

    /**
     * Vérifie si la playlist contient un fichier MP3 donné.
     * 
     * @param mp3 Fichier MP3 à vérifier
     * @return true si la piste est présente
     */
    public boolean contient(FichierMp3 mp3) {
        return pistes.contains(mp3);
    }

    /**
     * Ajoute une piste à la playlist.
     * 
     * @param mp3 Fichier MP3 à ajouter
     */
    public void ajouterPiste(FichierMp3 mp3) {
        pistes.add(mp3);
    }

    /**
     * Ajoute plusieurs pistes à la playlist.
     * 
     * @param mp3s Liste de fichiers MP3 à ajouter
     */
    public void ajouterPistes(List<FichierMp3> mp3s) {
        pistes.addAll(mp3s);
    }

    /**
     * Retourne la liste des pistes de la playlist.
     * 
     * @return liste des fichiers MP3
     */
    public List<FichierMp3> getPistes() {
        return pistes;
    }

    /**
     * Retourne le nom de la playlist.
     * 
     * @return nom de la playlist
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom de la playlist.
     * 
     * @param nom Nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le format de la playlist.
     * 
     * @return format (M3U, JSPF, XSPF)
     */
    public String getFormat() {
        return format;
    }

    /**
     * Modifie le format de la playlist.
     * 
     * @param format Nouveau format
     */
    public void setFormat(String format) {
        this.format = format;
    }
}

