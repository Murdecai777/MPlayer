import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Murdecai
 */
public class Lyrics {
    
     Stage lyrStage;
     String songText;
     String songName;
     
     public void strt() throws IOException{

       String textPath = "D:\\Users\\Murdecai\\Documents\\NetBeansProjects\\MP0.91\\"+songName + ".txt";
       
       File file = new File(textPath);
       if(file.exists() && file.isFile()){
           readLyrics();
       }
       else if(!file.exists()){
           setLyrics();
       }
     }
     
    public void setLyrics() throws IOException{
                
            lyrStage = new Stage();
            lyrStage.setTitle("Lyrics setting");
            lyrStage.setHeight(420);
        AnchorPane root = new AnchorPane();  
        root.setStyle("-fx-border-color: black, transparent; -fx-border-width: 1px, 5px; -fx-border-insets: 0px, 1px"); 
        
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                lyrStage.setX(me.getScreenX());
                lyrStage.setY(me.getScreenY());
            }
        });
        
        lyrStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        
        final TextArea lyricArea = new TextArea();
 
        lyrStage.setScene(scene);
        
        lyricArea.setPrefHeight(100);
        lyricArea.setPrefWidth(190);
        lyricArea.setMinHeight(100);
        lyricArea.setLayoutY(315);
        lyricArea.setLayoutX(7);
        lyricArea.setMaxWidth(TextField.USE_PREF_SIZE);

        final Label label = new Label(); 
/*
        final ScrollPane sp = new ScrollPane();
        sp.setPrefSize(200, 200);
        sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        sp.setPannable(true);
        sp.setContent(label);
        sp.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    label.setLayoutY(-new_val.doubleValue());
                    label.setLayoutX(-new_val.doubleValue());
            }
        });
*/

        
        label.textProperty().bind(new StringBinding() {
            {
                bind(lyricArea.textProperty());
            }            
            @Override protected String computeValue() {

                    String text = lyricArea.getText();

                    return "Lyric preview: \n\n " + songName +"\n\n" + text;
            }
        });

        final VBox vBox = new VBox();
        vBox.setPrefHeight(800);  //////////////////////////////////////////
        vBox.setPrefWidth(240);
        vBox.setPadding(new Insets(18));
        vBox.getChildren().addAll(label);
        
        final VBox vBox2 = new VBox();
        vBox2.setPrefHeight(340);
        vBox2.setPrefWidth(240);
        vBox2.setAlignment(Pos.BOTTOM_RIGHT);
        
                
        Button btnText = new Button();
        btnText.setText("Ok");
        btnText.setStyle("-fx-base: white;");
        btnText.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
               String txt = lyricArea.getText();
               textSet(txt);
               lyrStage.close();
            }
        });
        
        vBox2.getChildren().add(btnText);
            
       ScrollBar sc = new ScrollBar();
        sc.setMin(0);
        sc.setMax(500);
        sc.setValue(30);
        sc.setPrefWidth(3);
        sc.setPrefHeight(160);
        sc.setVisibleAmount(10);
        sc.setOrientation(Orientation.VERTICAL);
        
        sc.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                       label.setLayoutY(-new_val.doubleValue());
                }
        });
        
        root.getChildren().addAll(vBox,lyricArea,vBox2,sc);
        
            lyrStage.setX(50);
            lyrStage.setY(50);
                       
             
             lyrStage.show();
    }
    
    public void closeLyricStage() {
       lyrStage.close();  
       lyrStage = null;
    }
    
    public void textSet(String s) {
       songText = s;
        if(!songText.isEmpty()){
            String filename = songName+".txt";
             PrintStream out = null;
             try {
                 out = new PrintStream(

                   new BufferedOutputStream(

                    new FileOutputStream(filename, true)));

                  out.println(songName);
                  out.println(songText);

             } catch (Exception e) {
                 System.err.println(e);
             }finally {

                 if (out != null) {

                   out.close();

                 }

             }
        }
    }
 
    public void readLyrics() throws IOException{
            
           String filename = songName + ".txt";
            
            lyrStage = new Stage();
            lyrStage.setTitle("Lyrics");
            lyrStage.setHeight(420);
        AnchorPane root = new AnchorPane();  
        root.setStyle("-fx-border-color: black, transparent; -fx-border-width: 1px, 5px; -fx-border-insets: 0px, 1px"); 
        
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                lyrStage.setX(me.getScreenX());
                lyrStage.setY(me.getScreenY());
            }
        });
        
        lyrStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        
 
        lyrStage.setScene(scene);
        
        Boolean songTitle = true;
        final Label label = new Label();
        
        ScrollBar sc = new ScrollBar();
        sc.setMin(0);
        sc.setValue(0);
        sc.setPrefWidth(3);
        sc.setPrefHeight(140);
        sc.setVisibleAmount(10);
        sc.setOrientation(Orientation.VERTICAL);
        
        sc.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                       label.setLayoutY(-new_val.doubleValue());
                }
        });
        
        
        int row = 0;
               try (
                 FileReader re = new FileReader(filename)) {
                     BufferedReader br = new BufferedReader(re);
                     String content;
                     while ((content = br.readLine()) != null) {
                        if(songTitle == true){
                            content = content.toUpperCase();
                            content = content.substring(0, content.length() - 4);
                            songText = "    ";
                            songTitle = false;
                        }  
                        songText += (content + "\n    ");
                        row++;
                     }
                } catch (IOException ex) {
                    System.out.println("Cant read but why?!!!");
                }
        
        sc.setMax(row*10);       
               
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setMinSize(50, 80);
        label.setText(songText);
        //label.boundsInParentProperty();
        root.getChildren().addAll(label,sc);
                    
        lyrStage.setX(50);
        lyrStage.setY(50);
                          
        lyrStage.show();
    }
}


