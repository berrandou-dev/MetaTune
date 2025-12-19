package scr;
import java.io.FileWriter;
import java.io.IOException;

public class ExporteurJspf implements ExporteurPlaylist {

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
    
    //Corrige le problèmes d'affichage de chemin avec toURI()
    @Override
    public String formaterChemin(FichierMp3 mp3) {
        return mp3.getFichier().toURI().toString();
    }

}
