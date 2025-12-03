import java.io.FileWriter;
import java.io.IOException;

public class ExporteurXspf implements ExporteurPlaylist {

    @Override
    public void exporter(Playlist playlist, String cheminSortie) {
        //ecrire un textblock 
        StringBuilder xml = new StringBuilder(
                String.format("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <playlist version="1" xmlns="http://xspf.org/ns/0/">
                          <title>%s</title>
                          <trackList>
                        """, playlist.getNom())
        );

        for (FichierMp3 mp3 : playlist.getPistes()) {
            //recuperer les URL des fichiers MP3 
            xml.append(String.format("""
                            <track>
                              <location>%s</location>
                              <title>%s</title>
                              <creator>%s</creator>
                              <album>%s</album>
                            </track>
                            """,
                    formaterChemin(mp3),
                    mp3.getMetadonnees().titre,
                    mp3.getMetadonnees().artiste,
                    mp3.getMetadonnees().album
            ));
        }

        xml.append("""
                  </trackList>
                </playlist>
                """);

        try (FileWriter w = new FileWriter(cheminSortie)) {
            w.write(xml.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String formaterChemin(FichierMp3 mp3) {
        return mp3.getChemin().replace("\\", "/");
    }
}
