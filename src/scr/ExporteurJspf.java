package scr;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe permettant d'exporter une playlist au format JSON JSPF.
 */
public class ExporteurJspf implements ExporteurPlaylist {

    /**
     * Exporte une playlist au format JSPF vers un fichier de sortie.
     *
     * @param playlist    La playlist à exporter
     * @param cheminSortie Chemin complet du fichier de sortie
     */
    @Override
    public void exporter(Playlist playlist, String cheminSortie) {

        StringBuilder json = new StringBuilder(
                String.format("""
                        {
                          "playlist": {
                            "title": "%s",
                            "track": [
                        """, playlist.getNom())
        );

        int size = playlist.getPistes().size();
        for (int i = 0; i < size; i++) {
            FichierMp3 mp3 = playlist.getPistes().get(i);

            json.append(String.format("""
                              {
                                "location": "%s",
                                "title": "%s",
                                "creator": "%s",
                                "album": "%s"
                              }%s
                        """,
                    formaterChemin(mp3),
                    mp3.getMetadonnees().getTitre(),
                    mp3.getMetadonnees().getArtiste(),
                    mp3.getMetadonnees().getAlbum(),
                    (i < size - 1 ? "," : "")
            ));
        }

        json.append("""
                            ]
                          }
                        }
                        """);

        try (FileWriter writer = new FileWriter(cheminSortie)) {
            writer.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formate le chemin d'un fichier MP3 pour l'export JSPF.
     *
     * @param mp3 Fichier MP3 à formater
     * @return Chemin absolu canonique du fichier si possible, sinon chemin absolu
     */
    @Override
    public String formaterChemin(FichierMp3 mp3) {
        if (mp3.getFichier() == null) {
            return mp3.getChemin(); // fallback
        }
        // Retourne le chemin absolu canonique pour résoudre ../ et .
        try {
            return mp3.getFichier().getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return mp3.getFichier().getAbsolutePath();
        }
    }

}

