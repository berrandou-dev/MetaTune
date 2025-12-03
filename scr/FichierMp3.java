package scr;

import java.io.File;

public class FichierMp3 {
    //Attributs
    File fichier;
    Metadonnee metadonnees;
    
    // Constructeur
    public FichierMp3(String chemin) {
        this.fichier = new File(chemin);
        if(fichier.exists() && fichier.isFile()) {
            this.metadonnees = new Metadonnee(fichier);
        } 
        else {
            System.out.println("Le fichier n'existe pas ou n'est pas un fichier valide.");
            this.metadonnees = null;
        }
    }

    // Getters
    public File getFichier() {
        return fichier;
    }
    public Metadonnee getMetadonnees() {
        return metadonnees;
    }
    public String getChemin(){
        return fichier.getAbsolutePath();
    }

    //Mutateurs
    public void lireMetadonnees(){
        if(fichier.exists() && fichier.isFile()) {
            this.metadonnees = new Metadonnee(fichier);
        }
        else {
            System.out.println("Impossible de lire les métadonnées : fichier inexistant ou invalide.");
            this.metadonnees = null;
        }
    }
    
    public boolean hasPochette(){
        return metadonnees.getPochette() != null;
    }




}
