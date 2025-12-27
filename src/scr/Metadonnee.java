package scr;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

public class Metadonnee {

    private String titre;
    private String artiste;
    private String album;
    private int annee;
    private int duree; // secondes
    private String genre;
    private int numeroPiste;
    private byte[] pochette;

    // Constructeur principal
    public Metadonnee(String titre, String artiste, String album,
                      int annee, int duree, String genre,
                      int numeroPiste, byte[] pochette) {
        this.titre = titre;
        this.artiste = artiste;
        this.album = album;
        this.annee = annee;
        this.duree = duree;
        this.genre = genre;
        this.numeroPiste = numeroPiste;
        this.pochette = pochette;
    }

    // Constructeur depuis un fichier MP3
    public Metadonnee(File fichier) {
        try {
            Logger.getLogger("org.jaudiotagger").setLevel(Level.SEVERE);

            AudioFile audioFile = AudioFileIO.read(fichier);
            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();

            // Valeurs par défaut
            titre = "Inconnu";
            artiste = "Inconnu";
            album = "Inconnu";
            annee = -1;
            genre = "Inconnu";
            numeroPiste = -1;
            pochette = null;

            if (tag != null) {
                titre = get(tag, FieldKey.TITLE, titre);
                artiste = get(tag, FieldKey.ARTIST, artiste);
                album = get(tag, FieldKey.ALBUM, album);
                genre = get(tag, FieldKey.GENRE, genre);

                annee = parseYear(get(tag, FieldKey.YEAR, ""));
                numeroPiste = parseInt(get(tag, FieldKey.TRACK, ""), 0);

                Artwork art = tag.getFirstArtwork();
                if (art != null) {
                    pochette = art.getBinaryData();
                }
            }

            duree = (header != null) ? header.getTrackLength() : 0;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture métadonnées MP3", e);
        }
    }

    /* =====================
       Méthodes utilitaires
       ===================== */

    private String get(Tag tag, FieldKey key, String def) {
        String v = tag.getFirst(key);
        return (v == null || v.isEmpty()) ? def : v;
    }

    private int parseInt(String value, int def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    private int parseYear(String value) {
        if (value == null || value.length() < 4) return -1;
        return parseInt(value.substring(0, 4), -1);
    }

    /* =========
       Getters
       ========= */

    public String getTitre() { return titre; }
    public String getArtiste() { return artiste; }
    public String getAlbum() { return album; }
    public int getAnnee() { return annee; }
    public int getDuree() { return duree; }
    public String getGenre() { return genre; }
    public int getNumeroPiste() { return numeroPiste; }
    public byte[] getPochette() { return pochette; }
}

