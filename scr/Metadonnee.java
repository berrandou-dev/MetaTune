package scr;

import java.io.File;

public class Metadonnee {
    //Attributs
    private String titre;
    private String artiste;
    private String album;
    private int annee;
    private int duree;
    private String genre;
    private int numeroPiste;
    private byte[] pochette;

    // Constructeur
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

    public Metadonnee(File fichier) {
        try {
            // Lire les 128 derniers octets
            java.io.RandomAccessFile raf = new java.io.RandomAccessFile(fichier, "r");
            raf.seek(raf.length() - 128);

            byte[] buffer = new byte[128];
            raf.read(buffer);
            raf.close();

            // Vérifier la signature "TAG"
            if (!(buffer[0] == 'T' && buffer[1] == 'A' && buffer[2] == 'G')) {
                throw new RuntimeException("Pas de métadonnées ID3v1 trouvées dans : " + fichier.getName());
            }

            // Extraction (trim pour enlever les espaces)
            this.titre = new String(buffer, 3, 30).trim();
            this.artiste = new String(buffer, 33, 30).trim();
            this.album = new String(buffer, 63, 30).trim();
            this.annee = Integer.parseInt(new String(buffer, 93, 4).trim());

            // Pas disponible dans ID3v1, on mets une valeur par défaut
            this.duree = -1;  
            this.numeroPiste = -1;  

            // Le commentaire contient la piste dans ID3v1.1
            // Si buffer[125] == 0 alors buffer[126] = track number
            if (buffer[125] == 0) {
                this.numeroPiste = buffer[126] & 0xFF; // convertir en int positif
            }

            // Genre = dernier octet
            int genreId = buffer[127] & 0xFF;
            this.genre = Integer.toString(genreId);

            // ID3v1 ne contient pas la pochette
            this.pochette = null;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture métadonnées : " + e.getMessage());
        }
    }


    // Getters
    public String getTitre() {
        return titre;
    }
    public String getArtiste() {
        return artiste;
    }
    public String getAlbum() {
        return album;
    }
    public int getAnnee() {
        return annee;
    }
    public int getDuree() {
        return duree;
    }
    public String getGenre() {
        return genre;
    }
    public int getNumeroPiste() {
        return numeroPiste;
    }
    public byte[] getPochette() {
        return pochette;
    }



}