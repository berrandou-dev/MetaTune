package scr ;
public interface ExporteurPlaylist {
	void exporter(Playlist playlist, String cheminSortie);
	String formaterChemin(FichierMp3 mp3);
}
