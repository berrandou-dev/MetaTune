package scr;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class ApplicationGUI extends Application {

    private ListView<FichierMp3> listeMorceaux;
    private ImageView cover;
    private Label labelTitre, labelArtiste, labelAlbum, labelAnnee;
    private Label labelDuree, labelGenre, labelNumeroPiste;

    @Override
    public void start(Stage stage) {

        stage.setTitle("MetaTune – Gestion de playlists MP3");

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #121212;");

        /* =======================
           Pochette + Métadonnées
           ======================= */
        cover = new ImageView();
        cover.setFitWidth(200);
        cover.setFitHeight(200);
        cover.setPreserveRatio(true);

        labelTitre = creerLabel("Titre : ");
        labelArtiste = creerLabel("Artiste : ");
        labelAlbum = creerLabel("Album : ");
        labelAnnee = creerLabel("Année : ");
        labelDuree = creerLabel("Durée : ");
        labelGenre = creerLabel("Genre : ");
        labelNumeroPiste = creerLabel("Numéro de piste : ");

        VBox metaBox = new VBox(
            5,
            labelTitre,
            labelArtiste,
            labelAlbum,
            labelAnnee,
            labelDuree,
            labelGenre,
            labelNumeroPiste
        );
        metaBox.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, cover, metaBox);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("""
            -fx-background-color: #181818;
            -fx-background-radius: 12;
        """);

        /* =======================
           Liste des musiques
           ======================= */
        listeMorceaux = new ListView<>();
        listeMorceaux.setPrefHeight(260);
        listeMorceaux.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        listeMorceaux.setTooltip(
            new Tooltip("Ctrl + clic : sélection multiple | Shift + clic : plage continue")
        );

        listeMorceaux.setStyle("""
            -fx-background-color: #181818;
            -fx-control-inner-background: #181818;
        """);

        listeMorceaux.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(FichierMp3 item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    String titre = item.getMetadonnees() != null
                        && item.getMetadonnees().getTitre() != null
                        ? item.getMetadonnees().getTitre()
                        : new File(item.getChemin()).getName();

                    setText(titre);
                    setStyle("""
                        -fx-text-fill: white;
                        -fx-padding: 8;
                    """);
                }
            }
        });

        listeMorceaux.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, selected) -> afficherMetadonnees(selected)
        );

        /* =======================
           Drag & Drop
           ======================= */
        listeMorceaux.setOnDragOver(e -> {
            if (e.getDragboard().hasFiles())
                e.acceptTransferModes(TransferMode.COPY);
            e.consume();
        });

        listeMorceaux.setOnDragDropped(e -> {
            for (File f : e.getDragboard().getFiles()) {
                if (f.getName().toLowerCase().endsWith(".mp3")) {
                    listeMorceaux.getItems().add(new FichierMp3(f.getAbsolutePath()));
                }
            }
            e.setDropCompleted(true);
            e.consume();
        });

        /* =======================
           Boutons
           ======================= */
        Button btnOuvrirRep = creerBouton("Ouvrir un répertoire");
        Button btnSave = creerBouton("Sauvegarder la playlist");
        Button btnLoad = creerBouton("Ouvrir une playlist");

        btnSave.setTooltip(
            new Tooltip("Sélectionnez des musiques ou rien pour tout sauvegarder")
        );

        btnOuvrirRep.setOnAction(e -> ouvrirRepertoire(stage));
        btnSave.setOnAction(e -> sauvegarderPlaylist(stage));
        btnLoad.setOnAction(e -> chargerPlaylist(stage));

        HBox boutons = new HBox(12, btnOuvrirRep, btnSave, btnLoad);
        boutons.setAlignment(Pos.CENTER);

        /* =======================
           Assemblage final
           ======================= */
        root.getChildren().addAll(card, listeMorceaux, boutons);

        stage.setScene(new Scene(root, 600, 750));
        stage.show();
    }

    /* =======================
       Méthodes utilitaires
       ======================= */
    private Button creerBouton(String texte) {
        Button b = new Button(texte);
        b.setMinWidth(Region.USE_PREF_SIZE);
        b.setMaxWidth(Region.USE_PREF_SIZE);
        b.setPrefHeight(40);
        b.setStyle("""
            -fx-background-color: #1DB954;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
        """);

        b.setOnMouseEntered(e -> b.setStyle("""
            -fx-background-color: #1ed760;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
        """));

        b.setOnMouseExited(e -> b.setStyle("""
            -fx-background-color: #1DB954;
            -fx-text-fill: black;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-cursor: hand;
        """));

        return b;
    }

    private Label creerLabel(String txt) {
        Label l = new Label(txt);
        l.setTextFill(Color.web("#E0E0E0"));
        l.setFont(Font.font("Arial", 15));
        return l;
    }

    private void ouvrirRepertoire(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(stage);
        if (dir == null) return;

        ExplorateurRepertoire exp = new ExplorateurRepertoire();
        listeMorceaux.getItems().setAll(exp.analyser(dir.getAbsolutePath()));
    }

    private ExporteurPlaylist getExporteur(File f) {
        String nom = f.getName().toLowerCase();

        if (nom.endsWith(".m3u8")) return new ExporteurM3u8();
        if (nom.endsWith(".xspf")) return new ExporteurXspf();
        if (nom.endsWith(".jspf")) return new ExporteurJspf();

        return null;
    }

    private void sauvegarderPlaylist(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("M3U8 (*.m3u8)", "*.m3u8"),
            new FileChooser.ExtensionFilter("XSPF (*.xspf)", "*.xspf"),
            new FileChooser.ExtensionFilter("JSPF (*.jspf)", "*.jspf")
        );

        File f = fc.showSaveDialog(stage);
        if (f == null) return;

        ExporteurPlaylist exporteur = getExporteur(f);
        if (exporteur == null) {
            afficherErreur("Format non supporté");
            return;
        }

        Playlist playlist = new Playlist("Ma playlist");
        List<FichierMp3> selection = listeMorceaux.getSelectionModel().getSelectedItems();

        if (!selection.isEmpty())
            playlist.getPistes().addAll(selection);
        else
            playlist.getPistes().addAll(listeMorceaux.getItems());

        exporteur.exporter(playlist, f.getAbsolutePath());
    }

    private void chargerPlaylist(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                "Playlists (*.m3u8, *.xspf, *.jspf)",
                "*.m3u8", "*.xspf", "*.jspf"
            )
        );

        File f = fc.showOpenDialog(stage);
        if (f == null) return;

        listeMorceaux.getItems().clear();

        String nom = f.getName().toLowerCase();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {

            String ligne;
            StringBuilder contenu = new StringBuilder();

            while ((ligne = br.readLine()) != null)
                contenu.append(ligne).append("\n");

            String texte = contenu.toString();

            /* ========= M3U8 ========= */
            if (nom.endsWith(".m3u8")) {

                for (String l : texte.split("\n")) {

                    if (l.startsWith("#") || l.isBlank())
                        continue;

                    File mp3 = l.startsWith("file:/")
                        ? new File(new java.net.URI(l))
                        : new File(l);

                    if (mp3.exists() && mp3.getName().toLowerCase().endsWith(".mp3"))
                        listeMorceaux.getItems().add(new FichierMp3(mp3.getAbsolutePath()));
                }
            }

            /* ========= XSPF ========= */
            else if (nom.endsWith(".xspf")) {

                String[] locations = texte.split("<location>");

                for (int i = 1; i < locations.length; i++) {
                    String path = locations[i].split("</location>")[0].trim();

                    File mp3 = path.startsWith("file:/")
                        ? new File(new java.net.URI(path))
                        : new File(path);

                    if (mp3.exists() && mp3.getName().toLowerCase().endsWith(".mp3"))
                        listeMorceaux.getItems().add(new FichierMp3(mp3.getAbsolutePath()));
                }
            }

            /* ========= JSPF ========= */
            else if (nom.endsWith(".jspf")) {

                String[] locations = texte.split("\"location\"\\s*:\\s*\"");

                for (int i = 1; i < locations.length; i++) {
                    String path = locations[i].split("\"")[0];

                    File mp3 = path.startsWith("file:/")
                        ? new File(new java.net.URI(path))
                        : new File(path);

                    if (mp3.exists() && mp3.getName().toLowerCase().endsWith(".mp3"))
                        listeMorceaux.getItems().add(new FichierMp3(mp3.getAbsolutePath()));
                }
            }

            else {
                afficherErreur("Format non supporté");
            }

        } catch (Exception e) {
            afficherErreur("Erreur chargement playlist");
        }
    }

    private void afficherMetadonnees(FichierMp3 mp3) {
        if (mp3 == null || mp3.getMetadonnees() == null) return;

        Metadonnee m = mp3.getMetadonnees();

        labelTitre.setText("Titre : " + m.getTitre());
        labelArtiste.setText("Artiste : " + m.getArtiste());
        labelAlbum.setText("Album : " + m.getAlbum());
        labelAnnee.setText("Année : " + m.getAnnee());
        labelDuree.setText("Durée : " + m.getDuree());
        labelGenre.setText("Genre : " + m.getGenre());
        labelNumeroPiste.setText("Numéro de piste : " + m.getNumeroPiste());

        if (m.getPochette() != null)
            cover.setImage(new Image(new ByteArrayInputStream(m.getPochette())));
        else
            cover.setImage(null);
    }

    private void afficherErreur(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}

