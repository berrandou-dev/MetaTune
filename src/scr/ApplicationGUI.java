package scr;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApplicationGUI extends Application {

    private List<FichierMp3> playlist = new ArrayList<>();
    private int currentIndex = 0;

    private ImageView cover;
    private Label title;
    private Label artist;

    @Override
    public void start(Stage stage) {

        cover = new ImageView();
        cover.setFitWidth(250);
        cover.setFitHeight(250);
        cover.setStyle("-fx-background-radius: 20;");

        title = new Label("No Track");
        title.setFont(Font.font(20));
        title.setTextFill(Color.WHITE);

        artist = new Label("");
        artist.setFont(Font.font(14));
        artist.setTextFill(Color.LIGHTGRAY);

        Button prev = createControlButton("⏮");
        Button next = createControlButton("⏭");
        Button openDir = new Button("📂 Ouvrir dossier");
        openDir.setStyle("-fx-font-size:14;");

        HBox controls = new HBox(10, prev, next);
        controls.setAlignment(Pos.CENTER);

        VBox card = new VBox(15, cover, title, artist, controls, openDir);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: #1e1e1e; -fx-background-radius: 30; -fx-effect: dropshadow(gaussian, black, 20, 0.5, 0, 5);");

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2b5876, #4e4376);");

        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Mini Music Browser");
        stage.setScene(scene);
        stage.show();

        // === Event Handlers ===
        openDir.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choisir un dossier contenant des MP3");
            File dir = chooser.showDialog(stage);
            if (dir != null) {
                loadPlaylist(dir);
            }
        });

        prev.setOnAction(e -> previousTrack());
        next.setOnAction(e -> nextTrack());
    }

    private Button createControlButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: #333;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16;" +
            "-fx-background-radius: 50;" +
            "-fx-min-width: 45;" +
            "-fx-min-height: 45;"
        );
        return btn;
    }

    private void loadPlaylist(File dir) {
        playlist.clear();
        currentIndex = 0;

        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".mp3"));
        if (files != null) {
            for (File f : files) {
                playlist.add(new FichierMp3(f.getAbsolutePath()));
            }
        }

        if (!playlist.isEmpty()) {
            showTrack(0);
        }
    }

    private void showTrack(int index) {
        if (index < 0 || index >= playlist.size()) return;

        currentIndex = index;
        FichierMp3 mp3 = playlist.get(index);

        Metadonnee md = mp3.getMetadonnees();
        title.setText(md.getTitre());
        artist.setText(md.getArtiste());

        if (md.getPochette() != null) {
            Image img = new Image(new java.io.ByteArrayInputStream(md.getPochette()));
            cover.setImage(img);
        } else {
            cover.setImage(null);
        }
    }

    private void nextTrack() {
        if (playlist.isEmpty()) return;
        int nextIndex = (currentIndex + 1) % playlist.size();
        showTrack(nextIndex);
    }

    private void previousTrack() {
        if (playlist.isEmpty()) return;
        int prevIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
        showTrack(prevIndex);
    }

    public static void main(String[] args) {
        launch();
    }
}
