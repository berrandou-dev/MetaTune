package scr;

import java.io.File;

/**
 * Représente un fichier MP3 avec ses métadonnées.
 */
public class FichierMp3 {

    /** Fichier MP3 sur le disque */
    private File fichier;

    /** Métadonnées associées au fichier */
    private Metadonnee metadonnees;

    /**
     * Constructeur : crée un objet FichierMp3 à partir d'un chemin.
     * 
     * @param chemin Chemin du fichier MP3
     */
    public FichierMp3(String chemin) {
        this.fichier = new File(chemin);
        if(fichier.exists() && fichier.isFile()) {
            this.metadonnees = new Metadonnee(fichier);
        } else {
            System.out.println("Le fichier n'existe pas ou n'est pas un fichier valide.");
            this.metadonnees = null;
        }
    }

    /**
     * Retourne le fichier MP3.
     * 
     * @return Fichier MP3
     */
    public File getFichier() {
        return fichier;
    }

    /**
     * Retourne les métadonnées du fichier.
     * 
     * @return Métadonnées ou null si non disponibles
     */
    public Metadonnee getMetadonnees() {
        return metadonnees;
    }

    /**
     * Retourne le chemin absolu du fichier.
     * 
     * @return Chemin absolu
     */
    public String getChemin(){
        return fichier.getAbsolutePath();
    }

    /**
     * Recharge les métadonnées du fichier.
     * Si le fichier n'existe pas ou est invalide, les métadonnées sont mises à null.
     */
    public void lireMetadonnees(){
        if(fichier.exists() && fichier.isFile()) {
            this.metadonnees = new Metadonnee(fichier);
        } else {
            System.out.println("Impossible de lire les métadonnées : fichier inexistant ou invalide.");
            this.metadonnees = null;
        }
    }

    /**
     * Indique si le fichier contient une pochette.
     * 
     * @return true si une pochette est disponible, false sinon
     */
    public boolean hasPochette() {
        return metadonnees != null && metadonnees.getPochette() != null;
    }
}

