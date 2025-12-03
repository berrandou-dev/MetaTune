package scr;

import java.io.File;

public class FichierMp3 {
    //Attributs
    File fichier;
    Metadonnee metadonnee;
    
    // Constructeur
    public FichierMp3(String chemin) {
        this.fichier = new File(chemin);
        if(fichier.exists() && fichier.isFile()) {
            this.metadonnee = new Metadonnee(fichier);
        } 
        else {
            System.out.println("Le fichier n'existe pas ou n'est pas un fichier valide.");
            this.metadonnee = null;
        }
    }

    // Getters
    public File getFichier() {
        return fichier;
    }
    public Metadonnee getMetadonnee() {
        return metadonnee;
    }
    public String getChemin(){
        return fichier.getAbsolutePath();
    }

    //Mutateurs
    public void lireMetadonnee(){
        if(fichier.exists() && fichier.isFile()) {
            this.metadonnee = new Metadonnee(fichier);
        }
        else {
            System.out.println("Impossible de lire les métadonnées : fichier inexistant ou invalide.");
            this.metadonnee = null;
        }
    }
    }
    public boolean hasPochette(){
        return metadonnee.getPochette() != null;
    }




}
