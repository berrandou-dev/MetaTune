package scr;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

public class ApplicationGUI extends Application {

    private Playlist playListActive;
    private Label labelTitre, labelArtiste, labelAlbum, labelAnnee, labelTemps;
    private ImageView cover;
    private ListView<FichierMp3> listeMorceaux;
    private MediaPlayer mediaPlayer;
    private FichierMp3 morceauActuel;
    private Button btnPause, btnStop, btnOuvrir, btnSuivant, btnVolumePlus, btnMode;
    private Slider sliderTemps, sliderVolume;
    private ScaleTransition pauseAnimation;
    private boolean modeClair = false;

    @Override
    public void start(Stage stage) {
        construireUI(stage);
        stage.setTitle("MetaTune Pro Ultime");
        stage.show();
    }

    private void construireUI(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #141E30, #243B55);");

        VBox mainContainer = new VBox(15);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: rgba(30,30,30,0.6); -fx-background-radius: 25;");

        // Pochette
        cover = new ImageView();
        cover.setFitWidth(200);
        cover.setFitHeight(200);
        cover.setPreserveRatio(true);
        cover.setEffect(new DropShadow(15, Color.BLACK));
        cover.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;");

        // Métadonnées
        labelTitre = creerLabel("Titre : ");
        labelArtiste = creerLabel("Artiste : ");
        labelAlbum = creerLabel("Album : ");
        labelAnnee = creerLabel("Année : ");
        VBox metaBox = new VBox(4, labelTitre, labelArtiste, labelAlbum, labelAnnee);
        metaBox.setAlignment(Pos.CENTER);

        // Liste morceaux avec miniatures
        listeMorceaux = new ListView<>();
        listeMorceaux.setMaxHeight(200);
        listeMorceaux.setStyle("-fx-background-radius: 15; -fx-text-fill: white;");
        listeMorceaux.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) jouerMorceau(selected);
        });
        listeMorceaux.setCellFactory(lv -> new ListCell<>() {
            private final ImageView imgView = new ImageView();
            @Override
            protected void updateItem(FichierMp3 item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    if (item.hasPochette() && item.getMetadonnees().getPochette() != null) {
                        Image img = new Image(new ByteArrayInputStream(item.getMetadonnees().getPochette()), 40, 40, true, true);
                        imgView.setImage(img);
                        setGraphic(imgView);
                    } else setGraphic(null);
                    setStyle(item.equals(morceauActuel) ? "-fx-text-fill: #571d1dff; -fx-font-weight: bold;" : "-fx-text-fill: white;");
                }
            }
        });

        // Drag & Drop
        listeMorceaux.setOnDragOver(event -> {
            if (event.getGestureSource() != listeMorceaux && event.getDragboard().hasFiles())
                event.acceptTransferModes(TransferMode.COPY);
            event.consume();
        });
        listeMorceaux.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                List<File> files = db.getFiles();
                for (File f : files) {
                    if (f.getName().toLowerCase().endsWith(".mp3")) {
                        FichierMp3 mp3 = new FichierMp3(f.getAbsolutePath());
                        listeMorceaux.getItems().add(mp3);
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // Slider temps
        sliderTemps = new Slider(0, 100, 0);
        sliderTemps.setPrefWidth(240);
        labelTemps = new Label("00:00 / 00:00");
        labelTemps.setTextFill(Color.LIGHTGRAY);
        HBox timeBox = new HBox(10, sliderTemps, labelTemps);
        timeBox.setAlignment(Pos.CENTER);

        // Slider volume
        sliderVolume = new Slider(0, 1, 0.5);
        sliderVolume.setPrefWidth(180);
        sliderVolume.valueProperty().addListener((obs, old, val) -> {
            if (mediaPlayer != null) mediaPlayer.setVolume(val.doubleValue());
        });
        btnVolumePlus = createAnimatedButton("🔊+");
        btnVolumePlus.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(Math.min(1.0, mediaPlayer.getVolume() + 0.1));
                sliderVolume.setValue(mediaPlayer.getVolume());
            }
        });
        HBox volumeBox = new HBox(10, new Label("Volume:"), sliderVolume, btnVolumePlus);
        volumeBox.setAlignment(Pos.CENTER);

        // Boutons principaux
        btnPause = createAnimatedButton("⏯"); // Toggle play/pause
        btnStop = createAnimatedButton("⏹");
        btnOuvrir = createAnimatedButton("📁");
        btnSuivant = createAnimatedButton("▶");
        btnMode = createAnimatedButton("🌓"); // Switch mode sombre/clair

        HBox controlBox = new HBox(12, btnPause, btnStop, btnSuivant, btnOuvrir, btnMode);
        controlBox.setAlignment(Pos.CENTER);

        btnPause.setOnAction(e -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) mediaPlayer.pause();
                else mediaPlayer.play();
            }
        });
        btnStop.setOnAction(e -> stopMorceau());
        btnOuvrir.setOnAction(e -> ouvrirRepertoire(stage));
        btnSuivant.setOnAction(e -> jouerSuivant());
        btnMode.setOnAction(e -> switchMode(mainContainer, root));

        // Animation Pause/Play
        pauseAnimation = new ScaleTransition(Duration.millis(500), btnPause);
        pauseAnimation.setFromX(1); pauseAnimation.setFromY(1);
        pauseAnimation.setToX(1.15); pauseAnimation.setToY(1.15);
        pauseAnimation.setCycleCount(ScaleTransition.INDEFINITE);
        pauseAnimation.setAutoReverse(true);

        mainContainer.getChildren().addAll(
                cover,
                metaBox,
                listeMorceaux,
                timeBox,
                volumeBox,
                controlBox
        );

        root.getChildren().add(mainContainer);
        Scene scene = new Scene(root, 420, 780);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
        });
    }

    private void switchMode(VBox main, StackPane root) {
        if (modeClair) {
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #141E30, #243B55);");
            main.setStyle("-fx-background-color: rgba(30,30,30,0.6); -fx-background-radius: 25;");
            modeClair = false;
        } else {
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #FFFFFF, #DDDDDD);");
            main.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 25;");
            modeClair = true;
        }
    }

    private Button createAnimatedButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font(16));
        b.setStyle("-fx-background-color: #7b6cf6; -fx-text-fill: white; -fx-background-radius: 30; -fx-min-width: 50; -fx-min-height: 50;");
        DropShadow shadow = new DropShadow(10, Color.BLACK);
        b.setEffect(shadow);
        b.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> b.setStyle("-fx-background-color: #9b80ff; -fx-text-fill: white; -fx-background-radius: 30; -fx-min-width: 50; -fx-min-height: 50;"));
        b.addEventHandler(MouseEvent.MOUSE_EXITED, e -> b.setStyle("-fx-background-color: #7b6cf6; -fx-text-fill: white; -fx-background-radius: 30; -fx-min-width: 50; -fx-min-height: 50;"));
        return b;
    }

    private void jouerMorceau(FichierMp3 mp3) {
        if (mp3 == null) return;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        try {
            Media media = new Media(new File(mp3.getChemin()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            morceauActuel = mp3;
            mediaPlayer.setVolume(sliderVolume.getValue());
            afficherMetadonnees(mp3);
            listeMorceaux.refresh();
            listeMorceaux.getSelectionModel().select(mp3);

            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                Duration total = mediaPlayer.getTotalDuration();
                if (total != null && !total.isUnknown()) {
                    if (!sliderTemps.isValueChanging())
                        sliderTemps.setValue(newTime.toMillis() / total.toMillis() * 100);
                    labelTemps.setText(formatTime(newTime, total));
                }
            });

            sliderTemps.valueChangingProperty().addListener((obs, old, isChanging) -> {
                if (!isChanging) {
                    Duration total = mediaPlayer.getTotalDuration();
                    if (total != null && !total.isUnknown())
                        mediaPlayer.seek(total.multiply(sliderTemps.getValue() / 100.0));
                }
            });

            mediaPlayer.statusProperty().addListener((obs, old, status) -> {
                if (status == MediaPlayer.Status.PLAYING) pauseAnimation.play();
                else pauseAnimation.stop();
            });

            mediaPlayer.setOnEndOfMedia(this::jouerSuivant);
            mediaPlayer.play();

            // Pochette fade
            if (mp3.hasPochette() && mp3.getMetadonnees().getPochette() != null) {
                Image img = new Image(new ByteArrayInputStream(mp3.getMetadonnees().getPochette()));
                FadeTransition ft = new FadeTransition(Duration.millis(400), cover);
                ft.setFromValue(0.0); ft.setToValue(1.0);
                cover.setImage(img);
                ft.play();
            } else cover.setImage(null);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible de lire : " + mp3.getChemin(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void stopMorceau() {
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    private void jouerSuivant() {
        int i = listeMorceaux.getSelectionModel().getSelectedIndex();
        if (i < listeMorceaux.getItems().size() - 1)
            listeMorceaux.getSelectionModel().select(i + 1);
        else stopMorceau();
    }

    private void ouvrirRepertoire(Stage stage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir un répertoire de musique");
        File dir = chooser.showDialog(stage);
        if (dir == null) return;
        Playlist pl = new Playlist("Playlist");
        ExplorateurRepertoire exp = new ExplorateurRepertoire();
        pl.ajouterPistes(exp.analyser(dir.getAbsolutePath()));
        playListActive = pl;
        listeMorceaux.getItems().setAll(pl.getPistes());
    }

    private Label creerLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        l.setFont(Font.font(14));
        return l;
    }

    private void afficherMetadonnees(FichierMp3 mp3) {
        Metadonnee m = mp3.getMetadonnees();
        if (m == null) return;
        labelTitre.setText("Titre : " + m.getTitre());
        labelArtiste.setText("Artiste : " + m.getArtiste());
        labelAlbum.setText("Album : " + m.getAlbum());
        labelAnnee.setText("Année : " + m.getAnnee());
    }

    private String formatTime(Duration current, Duration total) {
        int currMin = (int) current.toMinutes();
        int currSec = (int) current.toSeconds() % 60;
        int totalMin = (int) total.toMinutes();
        int totalSec = (int) total.toSeconds() % 60;
        return String.format("%02d:%02d / %02d:%02d", currMin, currSec, totalMin, totalSec);
    }

    
}
