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

/**
 * Classe représentant les métadonnées d'un fichier MP3.
 * <p>
 * Cette classe permet de récupérer et stocker les informations suivantes :
 * titre, artiste, album, année, durée, genre, numéro de piste, et pochette.
 * Elle peut être construite directement à partir d'un fichier MP3 grâce à la
 * bibliothèque jaudiotagger.
 * </p>
 * 
 * Exemple d'utilisation :
 * <pre>
 * File mp3 = new File("musique.mp3");
 * Metadonnee md = new Metadonnee(mp3);
 * System.out.println(md.getTitre());
 * </pre>
 * 
 * @author Nassim
 * @version 1.0
 */

public class Metadonnee {

    private String titre;
    private String artiste;
    private String album;
    private int annee;
    private int duree; // en secondes
    private String genre;
    private int numeroPiste;
    private byte[] pochette;

    /**
     * Constructeur principal.
     *
     * @param titre Titre du morceau
     * @param artiste Nom de l'artiste
     * @param album Nom de l'album
     * @param annee Année de sortie
     * @param duree Durée en secondes
     * @param genre Genre musical
     * @param numeroPiste Numéro de la piste
     * @param pochette Image de la pochette (tableau de bytes)
     */
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

    /**
     * Constructeur à partir d'un fichier MP3.
     * <p>
     * Lit automatiquement les métadonnées via jaudiotagger et initialise
     * les attributs de la classe.
     * </p>
     *
     * @param fichier Fichier MP3
     * @throws RuntimeException si la lecture des métadonnées échoue
     */
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
       Méthodes utilitaires privées
       ===================== */

    /**
     * Récupère la valeur d'un champ Tag ou retourne une valeur par défaut.
     *
     * @param tag Objet Tag jaudiotagger
     * @param key Clé du champ
     * @param def Valeur par défaut
     * @return Valeur du champ ou def si vide
     */
    private String get(Tag tag, FieldKey key, String def) {
        String v = tag.getFirst(key);
        return (v == null || v.isEmpty()) ? def : v;
    }

    /**
     * Convertit une chaîne en entier, retourne la valeur par défaut si échec.
     *
     * @param value chaîne à convertir
     * @param def valeur par défaut
     * @return entier converti ou def
     */
    private int parseInt(String value, int def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Extrait l'année à partir d'une chaîne.
     *
     * @param value chaîne contenant l'année
     * @return année ou -1 si invalide
     */
    private int parseYear(String value) {
        if (value == null || value.length() < 4) return -1;
        return parseInt(value.substring(0, 4), -1);
    }

    /* =========
       Getters
       ========= */

    /**
     * Retourne le titre du morceau.
     *
     * @return Titre du morceau
     */
    public String getTitre() { return titre; }

    /**
     * Retourne le nom de l'artiste.
     *
     * @return Nom de l'artiste
     */
    public String getArtiste() { return artiste; }

    /**
     * Retourne le nom de l'album.
     *
     * @return Nom de l'album
     */
    public String getAlbum() { return album; }

    /**
     * Retourne l'année de sortie.
     *
     * @return Année de sortie ou -1 si inconnue
     */
    public int getAnnee() { return annee; }

    /**
     * Retourne la durée du morceau en secondes.
     *
     * @return Durée en secondes
     */
    public int getDuree() { return duree; }

    /**
     * Retourne le genre musical.
     *
     * @return Genre musical
     */
    public String getGenre() { return genre; }

    /**
     * Retourne le numéro de la piste dans l'album.
     *
     * @return Numéro de la piste ou -1 si inconnu
     */
    public int getNumeroPiste() { return numeroPiste; }

    /**
     * Retourne la pochette sous forme de tableau de bytes.
     *
     * @return Tableau de bytes représentant la pochette, ou null si inexistante
     */
    public byte[] getPochette() { return pochette; }
}

