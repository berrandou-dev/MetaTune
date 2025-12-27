package scr;

import java.util.List;
import java.util.ArrayList;

/**
 * Application en ligne de commande pour gérer des playlists MP3.
 * Permet d'analyser des fichiers ou répertoires, et d'exporter des playlists.
 */
public class ApplicationCLI {

    /** Nom de l'application */
    private String nom;

    /** Version de l'application */
    private String version;

    /** Liste des playlists créées */
    private List<Playlist> playlists;

    /** Playlist actuellement active */
    public Playlist playListActive;

    /** Répertoire courant pour l'exécution */
    private String repertoireCourant;

    /**
     * Constructeur principal.
     * @param nom Nom de l'application
     * @param version Version de l'application
     */
    public ApplicationCLI(String nom, String version) {
        this.nom = nom;
        this.version = version;
        this.playlists = new ArrayList<>();
        this.repertoireCourant = System.getProperty("user.dir");
    }

    /**
     * Exécute l'application avec les arguments passés en ligne de commande.
     * @param args Arguments de la ligne de commande
     */
    public void executer(String[] args) {
        System.out.println(nom + " v" + version);
        System.out.println("Répertoire courant : " + repertoireCourant);
        analyserArguments(args);
    }

    /**
     * Analyse les arguments passés à l'application et exécute les actions correspondantes.
     * @param args Arguments de la ligne de commande
     */
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

    /** Affiche un message d'erreur lorsqu'on utilise simultanément -f et -d */
    private void erreurExclusivite() {
        System.out.println("Erreur : les options -f et -d sont exclusives.\n");
        afficherAide();
    }

    /** Extrait le format de fichier à partir du nom (extension) */
    private String extraireFormat(String chemin) {
        int index = chemin.lastIndexOf('.');
        if (index == -1) return null;
        return chemin.substring(index + 1).toLowerCase();
    }

    /**
     * Analyse un fichier MP3 et crée une playlist contenant ce fichier.
     * @param chemin Chemin du fichier MP3
     */
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

    /**
     * Analyse un répertoire et crée une playlist avec tous les fichiers MP3 trouvés.
     * @param chemin Chemin du répertoire
     */
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

    /** Affiche les métadonnées d'un morceau */
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

    /**
     * Exporte une playlist dans un format donné.
     * @param pl Playlist à exporter
     * @param chemin Chemin du fichier de sortie
     * @param format Format du fichier (m3u, m3u8, xspf, jspf)
     */
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

    /** Affiche l'aide et la syntaxe d'utilisation */
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

