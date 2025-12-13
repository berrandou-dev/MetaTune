package scr;
import java.io.FileWriter;
import java.io.IOException;

public class ExporteurM3u8 implements ExporteurPlaylist {

    @Override
    public void exporter(Playlist playlist, String cheminSortie) {
        StringBuilder contenu = new StringBuilder();

        // En-tête 
        contenu.append("#EXTM3U\n");

        for (FichierMp3 mp3 : playlist.getPistes()) {
            // La Durée en secondes 
            int duree = mp3.getMetadonnees() != null ? mp3.getMetadonnees().getDuree() : -1;
            String titre = mp3.getMetadonnees() != null ? mp3.getMetadonnees().getTitre() : mp3.getChemin();

            // EXTINF
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

    @Override
    public String formaterChemin(FichierMp3 mp3) {
        return mp3.getChemin().replace("\\", "/");
    }
}
