package scr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExplorateurRepertoire {
    //Analyse un répertoire et retourne tous les fichiers MP3 valides
    public List<FichierMp3> analyser(String cheminRepertoire) {
        File dossier = new File(cheminRepertoire);

        if (!dossier.exists() || !dossier.isDirectory()) {
            System.out.println("Chemin invalide : " + cheminRepertoire);
            return new ArrayList<>();
        }

        List<File> fichiers = listerTousLesFichiers(cheminRepertoire);
        List<FichierMp3> mp3List = new ArrayList<>();

        for (File fichier : fichiers) {
            if (estMp3(fichier)) {
                mp3List.add(new FichierMp3(fichier.getAbsolutePath()));
            }
        }

        return mp3List;
    }

    //Liste récursivement tous les fichiers d’un dossier
    public List<File> listerTousLesFichiers(String chemin) {
        List<File> resultat = new ArrayList<>();
        File dossier = new File(chemin);

        if (!dossier.exists() || !dossier.isDirectory()) {
            return resultat;
        }

        File[] fichiers = dossier.listFiles();
        if (fichiers == null) return resultat;

        for (File f : fichiers) {
            if (f.isFile()) {
                resultat.add(f);
            } 
            else if (f.isDirectory()) {
                resultat.addAll(listerTousLesFichiers(f.getAbsolutePath()));
            }
        }

        return resultat;
    }

    //Vérifie si un fichier est un MP3 en se basant sur son extension
    public boolean estMp3(File fichier) {
        if (fichier == null || !fichier.isFile()) return false;
        String nomFichier = fichier.getName().toLowerCase();
        return nomFichier.endsWith(".mp3");
    }
}
