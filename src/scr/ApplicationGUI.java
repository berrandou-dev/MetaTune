package scr;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;

public class ApplicationGUI extends Application {

    private Playlist playListActive;
    private String repertoireCourant;

    private Label labelTitre;
    private Label labelArtiste;
    private Label labelAlbum;
    private Label labelAnnee;
    private ImageView cover;

    private ListView<FichierMp3> listeMorceaux;

    @Override
    public void start(Stage stage) {

        // ----------------- METADATA PANEL -----------------
        labelTitre = new Label("Titre : ");
        labelArtiste = new Label("Artiste : ");
        labelAlbum = new Label("Album : ");
        labelAnnee = new Label("Année : ");

        labelTitre.setFont(Font.font(16));
        labelArtiste.setFont(Font.font(16));
        labelAlbum.setFont(Font.font(16));
        labelAnnee.setFont(Font.font(16));

        VBox metaBox = new VBox(10, labelTitre, labelArtiste, labelAlbum, labelAnnee);
        metaBox.setPadding(new Insets(10));

        cover = new ImageView();
        cover.setFitWidth(150);
        cover.setFitHeight(150);
        cover.setPreserveRatio(true);

        VBox rightPanel = new VBox(20, cover, metaBox);
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: #121212; -fx-text-fill: white;");

        // ----------------- LIST VIEW -----------------
        listeMorceaux = new ListView<>();
        listeMorceaux.setStyle("-fx-background-color: #1e1e1e; -fx-control-inner-background: #1e1e1e; -fx-text-fill: white;");
        listeMorceaux.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                afficherMetadonnees(newVal);
            }
        });

        // ----------------- BUTTONS -----------------
        Button btnOuvrir = new Button("Ouvrir Répertoire");
        btnOuvrir.setStyle("-fx-background-color: #1DB954; -fx-text-fill: white; -fx-font-weight: bold;");
        btnOuvrir.setOnAction(e -> ouvrirRepertoire(stage));

        HBox buttons = new HBox(btnOuvrir);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10));

        // ----------------- MAIN LAYOUT -----------------
        BorderPane root = new BorderPane();
        root.setLeft(listeMorceaux);
        root.setRight(rightPanel);
        root.setBottom(buttons);
        root.setStyle("-fx-background-color: #181818;");

        BorderPane.setMargin(listeMorceaux, new Insets(10));
        BorderPane.setMargin(rightPanel, new Insets(10));

        Scene scene = new Scene(root, 900, 500);
        stage.setTitle("MetaTune - Spotify Style");
        stage.setScene(scene);
        stage.show();
    }

    // ----------------- DISPLAY METADATA -----------------
    private void afficherMetadonnees(FichierMp3 mp3) {
        if (mp3.getMetadonnees() != null) {
            Metadonnee m = mp3.getMetadonnees();
            labelTitre.setText("Titre : " + m.getTitre());
            labelArtiste.setText("Artiste : " + m.getArtiste());
            labelAlbum.setText("Album : " + m.getAlbum());
            labelAnnee.setText("Année : " + m.getAnnee());

            if (mp3.hasPochette() && m.getPochette() != null) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(m.getPochette());
                    Image img = new Image(bis);
                    cover.setImage(img);
                } catch (Exception e) {
                    cover.setImage(null);
                }
            } else {
                cover.setImage(null);
            }
        }
    }

    // ----------------- OPEN DIRECTORY -----------------
    private void ouvrirRepertoire(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir un répertoire de musique");
        File dir = chooser.showDialog(stage);
        if (dir != null) {
            repertoireCourant = dir.getAbsolutePath();

            Playlist pl = new Playlist("Playlist par défaut");
            ExplorateurRepertoire explorateur = new ExplorateurRepertoire();
            pl.ajouterPistes(explorateur.analyser(repertoireCourant));

            playListActive = pl;

            listeMorceaux.getItems().clear();
            listeMorceaux.getItems().addAll(playListActive.getPistes());
        }
    }
}

