import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Murdecai
 */
public class Lyrics {
    
     Stage lyrStage;
     
    public void strt() throws IOException{
            lyrStage = new Stage();
            lyrStage.setTitle("Lyrics");
            
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
            
            lyrStage.setX(50);
            lyrStage.setY(50);
                       
             
             lyrStage.show();
    }
    
    public void closeLyricStage() {
       lyrStage.close(); 
    }
    
}
