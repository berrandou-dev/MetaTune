package scr;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.images.Artwork;

public class Metadonnee {
    // Attributs
    private String titre;
    private String artiste;
    private String album;
    private int annee;
    private int duree; // en secondes
    private String genre;
    private int numeroPiste;
    private byte[] pochette;

    // Constructeur principal
    public Metadonnee(String titre, String artiste, String album, int annee, int duree, String genre, int numeroPiste, byte[] pochette) {
        this.titre = titre;
        this.artiste = artiste;
        this.album = album;
        this.annee = annee;
        this.duree = duree;
        this.genre = genre;
        this.numeroPiste = numeroPiste;
        this.pochette = pochette;
    }

    // Constructeur à partir d'un fichier MP3
    public Metadonnee(File fichier) {
        try {
            // Supprimer les logs d'infos de Jaudiotagger
            Logger.getLogger("org.jaudiotagger").setLevel(Level.SEVERE);

            AudioFile audioFile = AudioFileIO.read(fichier);
            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();

            if (tag != null) {
                this.titre = safeGet(tag, FieldKey.TITLE, "Inconnu");
                this.artiste = safeGet(tag, FieldKey.ARTIST, "Inconnu");
                this.album = safeGet(tag, FieldKey.ALBUM, "Inconnu");

                // Fallback pour récupérer l'année
                String[] possibleYearKeys = {"YEAR", "TYER", "TDRC", "Date"};
                String yearStr = "";
                for (String key : possibleYearKeys) {
                    yearStr = safeGet(tag, key, "");
                    if (!yearStr.isEmpty()) break;
                }

                if (yearStr.isEmpty()) {
                    this.annee = -1;
                } else {
                    try {
                        // Prendre uniquement les 4 premiers chiffres si présent
                        if (yearStr.length() >= 4)
                            this.annee = Integer.parseInt(yearStr.substring(0, 4));
                        else
                            this.annee = Integer.parseInt(yearStr);
                    } catch (NumberFormatException e) {
                        this.annee = -1;
                    }
                }

                this.genre = safeGet(tag, FieldKey.GENRE, "Inconnu");

                String trackStr = tag.getFirst(FieldKey.TRACK);
                this.numeroPiste = (trackStr == null || trackStr.isEmpty()) ? -1 : Integer.parseInt(trackStr);

                Artwork artwork = tag.getFirstArtwork();
                this.pochette = (artwork != null) ? artwork.getBinaryData() : null;
            } else {
                // Aucun tag : valeurs par défaut
                this.titre = "Inconnu";
                this.artiste = "Inconnu";
                this.album = "Inconnu";
                this.annee = -1;
                this.genre = "Inconnu";
                this.numeroPiste = -1;
                this.pochette = null;
            }

            // Toujours récupérer la durée si l'header est présent
            this.duree = (header != null) ? header.getTrackLength() : 0;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture métadonnées : " + e.getMessage(), e);
        }
    }

    // Méthode utilitaire pour sécuriser la récupération d'un champ FieldKey
    private String safeGet(Tag tag, FieldKey key, String defaultValue) {
        try {
            String value = tag.getFirst(key);
            return (value != null && !value.isEmpty()) ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // Méthode utilitaire pour sécuriser la récupération d'un champ par nom brut (ex: "TYER", "TDRC", "Date")
    private String safeGet(Tag tag, String keyName, String defaultValue) {
        try {
            String value = tag.getFirst(keyName);
            return (value != null && !value.isEmpty()) ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // Getters
    public String getTitre() { return titre; }
    public String getArtiste() { return artiste; }
    public String getAlbum() { return album; }
    public int getAnnee() { return annee; }
    public int getDuree() { return duree; }
    public String getGenre() { return genre; }
    public int getNumeroPiste() { return numeroPiste; }
    public byte[] getPochette() { return pochette; }
}

