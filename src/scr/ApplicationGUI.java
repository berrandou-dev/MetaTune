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
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Pochette
        cover = new ImageView();
        cover.setFitWidth(200);
        cover.setFitHeight(200);
        cover.setPreserveRatio(true);

        // Métadonnées
        labelTitre = creerLabel("Titre : ");
        labelArtiste = creerLabel("Artiste : ");
        labelAlbum = creerLabel("Album : ");
        labelAnnee = creerLabel("Année : ");
        labelDuree = creerLabel("Durée : ");
        labelGenre = creerLabel("Genre : ");
        labelNumeroPiste = creerLabel("Numéro de piste : ");

        VBox metaBox = new VBox(5, labelTitre, labelArtiste, labelAlbum, labelAnnee,
                labelDuree, labelGenre, labelNumeroPiste);
        metaBox.setAlignment(Pos.CENTER);

        // Liste MP3
        listeMorceaux = new ListView<>();
        listeMorceaux.setPrefHeight(250);

        // Afficher le vrai titre au lieu de FichierMp3@xxxx
        listeMorceaux.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(FichierMp3 item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String titre = item.getMetadonnees() != null && item.getMetadonnees().getTitre() != null
                            ? item.getMetadonnees().getTitre()
                            : new File(item.getChemin()).getName();
                    setText(titre);
                }
            }
        });

        listeMorceaux.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> afficherMetadonnees(selected)
        );

        // Drag & Drop MP3
        listeMorceaux.setOnDragOver(e -> {
            if (e.getDragboard().hasFiles())
                e.acceptTransferModes(TransferMode.COPY);
            e.consume();
        });

        listeMorceaux.setOnDragDropped(e -> {
            List<File> files = e.getDragboard().getFiles();
            for (File f : files) {
                if (f.getName().toLowerCase().endsWith(".mp3")) {
                    listeMorceaux.getItems().add(new FichierMp3(f.getAbsolutePath()));
                }
            }
            e.setDropCompleted(true);
            e.consume();
        });

        // Boutons
        Button btnOuvrirRep = new Button("📁 Répertoire");
        Button btnSave = new Button("💾 Sauvegarder playlist");
        Button btnLoad = new Button("📂 Ouvrir playlist");

        btnOuvrirRep.setOnAction(e -> ouvrirRepertoire(stage));
        btnSave.setOnAction(e -> sauvegarderPlaylist(stage));
        btnLoad.setOnAction(e -> chargerPlaylist(stage));

        HBox boutons = new HBox(10, btnOuvrirRep, btnSave, btnLoad);
        boutons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                cover,
                metaBox,
                listeMorceaux,
                boutons
        );

        stage.setScene(new Scene(root, 420, 650));
        stage.show();
    }

    // ---------------- MÉTHODES ----------------

    private void ouvrirRepertoire(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(stage);
        if (dir == null) return;

        ExplorateurRepertoire exp = new ExplorateurRepertoire();
        List<FichierMp3> mp3s = exp.analyser(dir.getAbsolutePath());
        listeMorceaux.getItems().setAll(mp3s);
    }

    private void sauvegarderPlaylist(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Playlist MetaTune (*.playlist)", "*.playlist"),
                new FileChooser.ExtensionFilter("Playlist texte (*.txt)", "*.txt")
        );
        File f = fc.showSaveDialog(stage);
        if (f == null) return;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (FichierMp3 mp3 : listeMorceaux.getItems()) {
                bw.write(mp3.getChemin());
                bw.newLine();
            }
        } catch (IOException e) {
            afficherErreur("Erreur lors de la sauvegarde");
        }
    }

    private void chargerPlaylist(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Playlist MetaTune (*.playlist)", "*.playlist"),
                new FileChooser.ExtensionFilter("Playlist texte (*.txt)", "*.txt")
        );
        File f = fc.showOpenDialog(stage);
        if (f == null) return;

        listeMorceaux.getItems().clear();

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                File mp3 = new File(ligne);
                if (mp3.exists() && mp3.getName().toLowerCase().endsWith(".mp3")) {
                    listeMorceaux.getItems().add(new FichierMp3(ligne));
                }
            }
        } catch (IOException e) {
            afficherErreur("Erreur lors du chargement");
        }
    }

    private void afficherMetadonnees(FichierMp3 mp3) {
        if (mp3 == null) return;
        Metadonnee m = mp3.getMetadonnees();
        if (m == null) return;

        labelTitre.setText("Titre : " + m.getTitre());
        labelArtiste.setText("Artiste : " + m.getArtiste());
        labelAlbum.setText("Album : " + m.getAlbum());
        labelAnnee.setText("Année : " + m.getAnnee());
        labelDuree.setText("Durée : " + m.getDuree());
        labelGenre.setText("Genre : " + m.getGenre());
        labelNumeroPiste.setText("Numéro de piste : " + m.getNumeroPiste());

        if (m.getPochette() != null) {
            cover.setImage(new Image(new ByteArrayInputStream(m.getPochette())));
        } else {
            cover.setImage(null);
        }
    }

    private Label creerLabel(String txt) {
        Label l = new Label(txt);
        l.setTextFill(Color.WHITE);
        l.setFont(Font.font(14));
        return l;
    }

    private void afficherErreur(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}

