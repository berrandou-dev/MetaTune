package scr;

import java.util.ArrayList;

public class ApplicationCLITest {
public static void main(String[] args) {
    ApplicationCLI app = new ApplicationCLI("MP3 & Playlists", "1.0",new ArrayList<>());
    app.analyserArguments(args);
}
}