package scr;

import java.util.List;

public class ApplicationCLI {

    private String nom;
    private String version;
    private List<Playlist> playlists;
    public Playlist playListActive;
    private String repertoireCourant;

    public ApplicationCLI(String nom, String version, List<Playlist> playlists) {
        this.nom = nom;
        this.version = version;
        this.playlists = playlists;
        this.repertoireCourant = System.getProperty("user.dir");
    }

    public void executer() {
        System.out.println(nom + " v" + version);
        System.out.println("Répertoire courant : " + repertoireCourant);
        afficherAide();
    }

    public void analyserArguments(String[] args) {
        if (args.length == 0) {
            afficherAide();
            return;
        }

        switch (args[0]) {
            case "-f":
                if (args.length > 1) modeFichier(args[1]);
                break;
            case "-r":
                if (args.length > 1) modeRepertoire(args[1]);
                break;
            case "-e":
                if (args.length > 2 && playListActive != null)
                    exporterPlaylist(playListActive, args[2]);
                break;
            case "-h":
            default:
                afficherAide();
        }
    }

    public void modeFichier(String chemin) {
        System.out.println("Mode fichier : " + chemin);

        FichierMp3 mp3 = new FichierMp3(chemin);
        mp3.lireMetadonnees();

        // Affichage des métadonnées
        Metadonnee md = mp3.getMetadonnees();
        System.out.println("Titre : " + md.getTitre());
        System.out.println("Artiste : " + md.getArtiste());
        System.out.println("Album : " + md.getAlbum());
        System.out.println("Année : " + md.getAnnee());
        System.out.println("Genre : " + md.getGenre());
        System.out.println("Numéro de piste : " + md.getNumeroPiste());
        System.out.println();

        Playlist playlist = new Playlist("Playlist depuis fichier");
        playlist.ajouterPiste(mp3);

        playlists.add(playlist);
        playListActive = playlist;
    }

    public void modeRepertoire(String chemin) {
        System.out.println("Mode répertoire : " + chemin);

        ExplorateurRepertoire explorateur = new ExplorateurRepertoire();
        List<FichierMp3> fichiers = explorateur.analyser(chemin);

        Playlist playlist = new Playlist("Playlist depuis répertoire");
        playlist.ajouterPistes(fichiers);

        playlists.add(playlist);
        playListActive = playlist;

        // Affichage des métadonnées pour chaque MP3
        for (FichierMp3 mp3 : fichiers) {
            Metadonnee md = mp3.getMetadonnees();
            
            System.out.println("Titre : " + md.getTitre());
            System.out.println("Artiste : " + md.getArtiste());
            System.out.println("Album : " + md.getAlbum());
            System.out.println("Année : " + md.getAnnee());
            System.out.println("Genre : " + md.getGenre());
            System.out.println("Numéro de piste : " + md.getNumeroPiste());
            System.out.println();
        }
    }

    public void exporterPlaylist(Playlist pl, String chemin) {
        System.out.println("Export de la playlist : " + pl.getNom());

        ExporteurPlaylist exporteur;

        switch (pl.getFormat()) {
            case "m3u":
                exporteur = new ExporteurM3u8();
                break;
            case "xspf":
                exporteur = new ExporteurXspf();
                break;
            case "jspf":
                exporteur = new ExporteurJspf();
                break;
            default:
                System.out.println("Format non supporté");
                return;
        }

        exporteur.exporter(pl, chemin);
    }

    public void afficherAide() {
        System.out.println("Utilisation :");
        System.out.println("  -f <fichier>       Mode fichier");
        System.out.println("  -r <repertoire>    Mode répertoire");
        System.out.println("  -e <format> <out>  Exporter la playlist");
        System.out.println("  -h                 Afficher l'aide");
    }
}
