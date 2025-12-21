package scr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExporteurXspf implements ExporteurPlaylist {

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
            // Récupérer les URL des fichiers MP3 en format URI
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

