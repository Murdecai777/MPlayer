import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MPlayer extends Application{
    private static Stage primalStage;
    private static MPlayer instance;

    static protected String choosedList = "Default list", radioURL = "http://85.114.140.64:8000/666";
    static protected Boolean listWasChanged = false;
    String listName;
    static Thread download;
    ComboBox<String> cbList = new ComboBox<String>();
    
    public MPlayer() {
        instance=this;
    }
        
    public static MPlayer getInstance(){
        return instance;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();  
        root = FXMLLoader.load(getClass().getResource("PlayerView.fxml"));
        root.setStyle("-fx-border-color: black, transparent; -fx-border-width: 1px, 5px; -fx-border-insets: 0px, 1px"); 
        
    root.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent me) {
            primalStage.setX(me.getScreenX());
            primalStage.setY(me.getScreenY());
        }
    });
               
    
    
        cbList.getItems().addAll("|Add new|", "Default list", "TrackList"/*, "Internet"*/);
        
        try (
            FileReader fr = new FileReader("UserPlaylists.txt")) {
            BufferedReader br = new BufferedReader(fr);
            String content;
            while ((content = br.readLine()) != null) {
                System.out.println(content);
                cbList.getItems().add(content);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
        cbList.setValue("Default list");
        choosedList = cbList.getValue();
        cbList.setLayoutX(385);
        cbList.setLayoutY(65);
        cbList.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if(!"|Add new|".equals(t1) && !"Internet".equals(t1)){
                    choosedList = t1;
                    listWasChanged = true;
                    onDestroy(true);
                }
                else if("Internet".equals(t1)){
               /*     download = new Thread(new Runnable()
                    {
                            @Override
                            public void run() //Этот метод будет выполняться в побочном потоке
                            {       downloadFiles(radioURL);
                            }
                    });
                  // download.start();
                   choosedList = t1;
                   listWasChanged = true; */
                }
                else
                    addNewList();
      }    
  });
    
    root.getChildren().add(cbList);
         primaryStage.initStyle(StageStyle.UNDECORATED);  
        
        primalStage = primaryStage;  
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                this.getClass().getClassLoader().getResource("res/combobox.css").toString());
        primalStage.setScene(scene);  
        primalStage.setTitle("MPlayer");
        primalStage.show();  
    }

    private void addNewList(){
        
        final Stage addListInputStage = new Stage();
        addListInputStage.setTitle("List setting");
        AnchorPane r = new AnchorPane();  
        r.setStyle("-fx-border-color: black, transparent; -fx-border-width: 1px, 5px; -fx-border-insets: 0px, 1px"); 
        addListInputStage.initStyle(StageStyle.UNDECORATED);
        Scene listScene = new Scene(r);
        
        final TextArea listNameArea = new TextArea();
 
        addListInputStage.setScene(listScene);
        
        listNameArea.setPrefHeight(28);
        listNameArea.setPrefWidth(100);
        listNameArea.setMinHeight(27);
        listNameArea.setLayoutX(10);
        listNameArea.setLayoutY(10);
        listNameArea.setMaxWidth(TextArea.USE_PREF_SIZE);
        
        
        Button btnSetList = new Button();
        btnSetList.setLayoutX(120);
        btnSetList.setLayoutY(42);
        btnSetList.setText("Ok");
        btnSetList.setStyle("-fx-base: white;");
        btnSetList.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                if(listNameArea.getText() != null){
                    setListName(listNameArea.getText());
                    addListInputStage.close();
                    saveUserList(listName);
                    cbList.getItems().add(listName);
                    cbList.setValue(listName);
                    choosedList = cbList.getValue();
                }
            }
        });
        
        
        r.getChildren().addAll(listNameArea, btnSetList);
        addListInputStage.show();
    }
    
    private void setListName(String listName){
        this.listName = listName;
    }
    
    
    private void saveUserList(String listName){
         
        PrintStream out = null;
        try {
            out = new PrintStream(

              new BufferedOutputStream(

               new FileOutputStream("UserPlaylists.txt", true)));

     out.println(listName);

        } catch (Exception e) {
            System.err.println(e);
        }finally {

            if (out != null) {

              out.close();

            }

        }
    }
    
    public static void downloadFiles(String strURL) {
        String strPath = "D:\\Users\\Murdecai\\Documents\\NetBeansProjects\\MP0.91\\song.mp3";
        try {
            URL connection = new URL(strURL);
            HttpURLConnection urlconn;
            urlconn = (HttpURLConnection) connection.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.connect();
            InputStream in = null;
            in = urlconn.getInputStream();
            OutputStream writer = new FileOutputStream(strPath);
            byte buffer[] = new byte[50];
            int c = in.read(buffer);
            while (c > 0 && !strURL.equals("")) {
                writer.write(buffer, 0, c);
                c = in.read(buffer);
            }
            writer.flush();
            writer.close();
            in.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
     
    private void onDestroy(Boolean destroy){
        if(download != null){
                MPlayer.downloadFiles("");
                if(destroy == true && download.isAlive())
                           download.interrupt();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }  
}