package scr;

/**
 * Classe de test pour l'application en ligne de commande (CLI).
 * Permet de créer une instance d'ApplicationCLI et de tester l'analyse des arguments.
 */

public class ApplicationCLITest {

    /**
     * Point d'entrée de l'application de test CLI.
     *
     * @param args Arguments passés depuis la ligne de commande
     */
    public static void main(String[] args) {
        // Création de l'application CLI avec nom et version
        ApplicationCLI app = new ApplicationCLI("MP3 & Playlists", "1.5");

        // Analyse des arguments fournis
        app.analyserArguments(args);
    }
}

