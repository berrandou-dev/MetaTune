package scr;

/**
 * Classe principale de l'application MetaTune.
 * <p>
 * Cette classe constitue le point d'entrée du programme.
 * Elle lance l'application graphique JavaFX via la classe {@link ApplicationGUI}.
 * </p>
 * 
 * @author Nassim
 * @version 1.0
 */
public class Application {

    /**
     * Constructeur par défaut.
     * <p>
     * Non utilisé directement car l'application est lancée via {@link #main(String[])}.
     * </p>
     */
    public Application() {
        // constructeur vide, nécessaire pour Javadoc
    }

    /**
     * Méthode principale de l'application.
     * <p>
     * Lance l'interface graphique JavaFX de MetaTune.
     * </p>
     *
     * @param args arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        javafx.application.Application.launch(ApplicationGUI.class, args);
    }
}

