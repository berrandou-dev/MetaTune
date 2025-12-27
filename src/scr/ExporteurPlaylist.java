package scr;

/**
 * Interface représentant un exporteur de playlist.
 * <p>
 * Les classes implémentant cette interface permettent d'exporter
 * une playlist dans différents formats (M3U, XSPF, JSPF, etc.)
 * et de formater les chemins des fichiers MP3 selon le format choisi.
 * </p>
 * 
 * Exemple d'utilisation :
 * <pre>
 * ExporteurPlaylist exporteur = new ExporteurM3u8();
 * exporteur.exporter(maPlaylist, "playlist.m3u8");
 * </pre>
 * 
 * @author Nassim
 * @version 1.0
 */
public interface ExporteurPlaylist {

    /**
     * Exporte la playlist dans un fichier de sortie.
     * 
     * @param playlist La playlist à exporter
     * @param cheminSortie Chemin complet du fichier de sortie
     */
    void exporter(Playlist playlist, String cheminSortie);

    /**
     * Formate le chemin d'un fichier MP3 selon le format de sortie.
     * 
     * @param mp3 Le fichier MP3 à formater
     * @return Le chemin formaté
     */
    String formaterChemin(FichierMp3 mp3);
}

