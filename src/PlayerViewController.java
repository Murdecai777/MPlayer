import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javax.swing.JFileChooser;


public class PlayerViewController implements Initializable {

    @FXML
    private ListView musicList;
    @FXML
    private Label progress, total, musicName;
    @FXML
    private Slider volume;
    
    Boolean CREATED_EQ = false;
    Analizer analizer;
    
    private MediaPlayer mediaPlayer;
    private int musicId = -1;
    
    private void getAllMusic(File song) {
        if (song.isDirectory()) {
            File[] listFiles = song.listFiles();
            for (File file : listFiles) {
                getAllMusic(file);
            }
        } else {
            if (song.getName().toLowerCase().endsWith(".mp3") || song.getName().toLowerCase().endsWith(".wav")
                    || song.getName().toLowerCase().endsWith(".m4a")|| song.getName().toLowerCase().endsWith(".aif")) {
                
                Music music = new Music();
                music.setName(song.getName());
                music.setFilePath(song.getAbsolutePath());
                
                musicList.getItems().add(music);
            }
        }
    }
    
    public void chooseMusic() {
        JFileChooser jf = new JFileChooser();
        jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int i = jf.showSaveDialog(null);
        if (i == JFileChooser.APPROVE_OPTION) {
            getAllMusic(jf.getSelectedFile());
            saveDirectory(jf.getSelectedFile().getAbsolutePath());
        }
    }
    
    public void refreshProgress() {
        
        try {
            ChangeListener<Duration> changeListener = new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> ov, Duration t, Duration t1) {
                    final Duration currentTime = mediaPlayer.getCurrentTime();
                    progress.setText( formatDuration(currentTime));
                    
                            final Duration totalTime = mediaPlayer.getTotalDuration();
                            total.setText( formatDuration(totalTime));
                }
            };
            mediaPlayer.currentTimeProperty().addListener(changeListener);
        } catch (Exception e) {
            System.out.println("Refresh progress error " + e);
        }
        
    }
    
    public void playClick(Music music) {
                                    
        String source = new File(music.getFilePath()).toURI().toString();
        if (musicId != -1) {
            mediaPlayer.dispose();
        }
        Media media = new Media(source);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume.getValue());
        musicName.setText(music.getName());
        refreshProgress();
        if(CREATED_EQ){
            analizer.audioMediaPlayer = mediaPlayer;
            analizer.analize();
        }
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                nextMusic();
            }
        });
        
        
        mediaPlayer.play();
        
    }
    
    public void mClickByDouble(MouseEvent evt) {
        if (evt.getButton().equals(MouseButton.PRIMARY)) {
            if (evt.getClickCount() == 2) {
                playClick((Music) musicList.getFocusModel().getFocusedItem());
                musicId = musicList.getFocusModel().getFocusedIndex();
            }
        }
    }
    
    public void previousMusic() {
        if (musicId > 0) {
            musicId--;
            playClick((Music) musicList.getItems().get(musicId));
            musicList.getFocusModel().focus(musicId);
            musicList.getSelectionModel().select(musicId);
        }
    }
    
    public void playPause() {
        if(mediaPlayer.getStatus().equals(mediaPlayer.getStatus().PAUSED) || mediaPlayer.getStatus().equals(mediaPlayer.getStatus().STOPPED))
            mediaPlayer.play();
        else if(mediaPlayer.getStatus().equals(mediaPlayer.getStatus().PLAYING))
            mediaPlayer.pause();
    }
        
    public void stopMusic() {
        mediaPlayer.stop();
    }
    
    public void nextMusic() {
        if (musicId < musicList.getItems().size() - 1) {
            musicId++;
            playClick((Music) musicList.getItems().get(musicId));
            musicList.getFocusModel().focus(musicId);
            musicList.getSelectionModel().select(musicId);
        }
    }
    
    private void loadDirectory() {
        try (
            FileReader fr = new FileReader("TrackList.txt")) {
            BufferedReader br = new BufferedReader(fr);
            String content;
            while ((content = br.readLine()) != null) {
                System.out.println(content);
                getAllMusic(new File(content));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void saveDirectory(String directory) {
        
        PrintStream out = null;
        PrintStream p;
        try {
            out = new PrintStream(

              new BufferedOutputStream(

               new FileOutputStream("TrackList.txt", true)));

     out.println(directory);

        } catch (Exception e) {
            System.err.println(e);
        }finally {

       if (out != null) {

         out.close();

       }

   }
    }
    
    public void closeApp() {
       Platform.exit(); 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDirectory();
        volume.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                System.out.println(volume.getValue());
                mediaPlayer.setVolume(volume.getValue());
            }
        });
        

    }
    
    private String formatDuration(Duration duration) {
    double millis = duration.toMillis();
    int seconds = (int) (millis / 1000) % 60;
    int minutes = (int) (millis / (1000 * 60));
    return String.format("%02d:%02d", minutes, seconds);
  }


    public void openAnalizer() throws IOException{
        if(CREATED_EQ == false){
            analizer = new Analizer();
            CREATED_EQ = true;
            analizer.audioMediaPlayer = mediaPlayer;
            analizer.strt();
        }
        else{ 
            analizer.closeAnilizer();
            CREATED_EQ = false;
        }

    }

}
