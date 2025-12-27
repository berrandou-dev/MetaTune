package scr;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe permettant d'exporter une playlist au format M3U8.
 */
public class ExporteurM3u8 implements ExporteurPlaylist {

    /**
     * Exporte une playlist au format M3U8 vers un fichier de sortie.
     *
     * @param playlist    La playlist à exporter
     * @param cheminSortie Chemin complet du fichier de sortie
     */
    @Override
    public void exporter(Playlist playlist, String cheminSortie) {
        StringBuilder contenu = new StringBuilder();

        // En-tête M3U
        contenu.append("#EXTM3U\n");

        for (FichierMp3 mp3 : playlist.getPistes()) {
            // Durée en secondes et titre
            int duree = mp3.getMetadonnees() != null ? mp3.getMetadonnees().getDuree() : -1;
            String titre = mp3.getMetadonnees() != null ? mp3.getMetadonnees().getTitre() : mp3.getChemin();

            // Ligne EXTINF
            contenu.append(String.format("#EXTINF:%d,%s\n", duree, titre));

            // Chemin du fichier
            contenu.append(formaterChemin(mp3)).append("\n");
        }

        // Écriture dans le fichier
        try (FileWriter writer = new FileWriter(cheminSortie)) {
            writer.write(contenu.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formate le chemin d'un fichier MP3 pour l'export M3U8.
     *
     * @param mp3 Fichier MP3 à formater
     * @return Chemin absolu canonique du fichier si possible, sinon chemin absolu
     */
    @Override
    public String formaterChemin(FichierMp3 mp3) {
        if (mp3.getFichier() == null) {
            return mp3.getChemin(); // fallback
        }
        try {
            return mp3.getFichier().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return mp3.getFichier().getAbsolutePath();
        }
    }
}

