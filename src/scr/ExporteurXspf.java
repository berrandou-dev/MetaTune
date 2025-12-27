package scr;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe permettant d'exporter une playlist au format XML XSPF.
 */
public class ExporteurXspf implements ExporteurPlaylist {

    /**
     * Exporte une playlist au format XSPF vers un fichier de sortie.
     *
     * @param playlist    La playlist à exporter
     * @param cheminSortie Chemin complet du fichier de sortie
     */
    @Override
    public void exporter(Playlist playlist, String cheminSortie) {
        // Créer le XML avec un Text Block
        StringBuilder xml = new StringBuilder(
                String.format("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <playlist version="1" xmlns="http://xspf.org/ns/0/">
                          <title>%s</title>
                          <trackList>
                        """, playlist.getNom())
        );

        for (FichierMp3 mp3 : playlist.getPistes()) {
            // Ajouter chaque morceau dans la playlist
            xml.append(String.format("""
                            <track>
                              <location>%s</location>
                              <title>%s</title>
                              <creator>%s</creator>
                              <album>%s</album>
                            </track>
                            """,
                    formaterChemin(mp3),
                    mp3.getMetadonnees().getTitre(),
                    mp3.getMetadonnees().getArtiste(),
                    mp3.getMetadonnees().getAlbum()
            ));
        }

        xml.append("""
                  </trackList>
                </playlist>
                """);

        // Écriture dans le fichier de sortie
        try (FileWriter w = new FileWriter(cheminSortie)) {
            w.write(xml.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Formate le chemin d'un fichier MP3 pour l'export XSPF.
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

