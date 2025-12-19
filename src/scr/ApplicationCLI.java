package scr;

import java.util.List;
import java.util.ArrayList;

public class ApplicationCLI {

    private String nom;
    private String version;
    private List<Playlist> playlists;
    public Playlist playListActive;
    private String repertoireCourant;

    public ApplicationCLI(String nom, String version) {
        this.nom = nom;
        this.version = version;
        this.playlists = new ArrayList<>();
        this.repertoireCourant = System.getProperty("user.dir");
    }

    public void executer(String[] args) {
        System.out.println(nom + " v" + version);
        System.out.println("Répertoire courant : " + repertoireCourant);
        analyserArguments(args);
    }

    public void analyserArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("Erreur : aucun paramètre fourni.\n");
            afficherAide();
            return;
        }

        boolean modeFichier = false;
        boolean modeRepertoire = false;

        String cheminExport = null;
        String formatExport = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-f":
                    if (modeRepertoire) {
                        erreurExclusivite();
                        return;
                    }
                    if (i + 1 < args.length) {
                        modeFichier = true;
                        modeFichier(args[++i]);
                    }
                    break;

                case "-d":
                    if (modeFichier) {
                        erreurExclusivite();
                        return;
                    }
                    if (i + 1 < args.length) {
                        modeRepertoire = true;
                        modeRepertoire(args[++i]);
                    }
                    break;

                case "-o":
                    if (i + 1 < args.length) {
                        cheminExport = args[++i];
                        formatExport = extraireFormat(cheminExport);
                    }
                    break;

                case "-h":
                    afficherAide();
                    return;

                default:
                    System.out.println("Argument invalide : " + args[i]);
                    afficherAide();
                    return;
            }
        }

        if (playListActive != null && cheminExport != null && formatExport != null) {
            exporterPlaylist(playListActive, cheminExport, formatExport);
        }
    }

    private void erreurExclusivite() {
        System.out.println("Erreur : les options -f et -d sont exclusives.\n");
        afficherAide();
    }

    private String extraireFormat(String chemin) {
        int index = chemin.lastIndexOf('.');
        if (index == -1) return null;
        return chemin.substring(index + 1).toLowerCase();
    }

    public void modeFichier(String chemin) {
        System.out.println("Mode fichier : " + chemin + "\n");

        FichierMp3 mp3 = new FichierMp3(chemin);
        mp3.lireMetadonnees();

        Metadonnee md = mp3.getMetadonnees();
        afficherMetadonnees(md);

        Playlist playlist = new Playlist("Playlist depuis fichier");
        playlist.ajouterPiste(mp3);

        playlists.add(playlist);
        playListActive = playlist;
    }

    public void modeRepertoire(String chemin) {
        System.out.println("Mode répertoire : " + chemin + "\n");

        ExplorateurRepertoire explorateur = new ExplorateurRepertoire();
        List<FichierMp3> fichiers = explorateur.analyser(chemin);

        Playlist playlist = new Playlist("Playlist depuis répertoire");
        playlist.ajouterPistes(fichiers);

        playlists.add(playlist);
        playListActive = playlist;

        for (FichierMp3 mp3 : fichiers) {
            Metadonnee md = mp3.getMetadonnees();
            afficherMetadonnees(md);
        }
    }

    private void afficherMetadonnees(Metadonnee md) {
        System.out.println("Titre : " + md.getTitre());
        System.out.println("Artiste : " + md.getArtiste());
        System.out.println("Album : " + md.getAlbum());
        System.out.println("Année : " + md.getAnnee());
        System.out.println("Durée : " + md.getDuree());
        System.out.println("Genre : " + md.getGenre());
        System.out.println("Numéro de piste : " + md.getNumeroPiste());
        System.out.println();
    }

    public void exporterPlaylist(Playlist pl, String chemin, String format) {
        System.out.println("Export de la playlist : " + pl.getNom());

        ExporteurPlaylist exporteur;

        switch (format) {
            case "m3u":
            case "m3u8":
                exporteur = new ExporteurM3u8();
                break;
            case "xspf":
                exporteur = new ExporteurXspf();
                break;
            case "jspf":
                exporteur = new ExporteurJspf();
                break;
            default:
                System.out.println("Format non supporté : " + format);
                return;
        }

        exporteur.exporter(pl, chemin);
        System.out.println("Playlist exportée dans : " + chemin);
    }

    public void afficherAide() {
        System.out.println("Utilisation :");
        System.out.println("  -f <fichier.mp3>       Analyser un fichier MP3");
        System.out.println("  -d <repertoire>        Explorer un répertoire");
        System.out.println("  -o <fichier>           Exporter la playlist (m3u, m3u8, xspf, jspf)");
        System.out.println("  -h                     Afficher l'aide");
        System.out.println();
        System.out.println("Notes :");
        System.out.println("  - Les options -f et -d sont exclusives");
    }
}
