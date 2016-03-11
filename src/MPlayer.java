import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MPlayer extends Application {
    private static Stage primalStage;
    private static MPlayer instance;
 //   double initX; double initY;
    
    public MPlayer() {
        instance=this;
    }
        
    public static MPlayer getInstance(){
        return instance;
    }
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();  
        root = FXMLLoader.load(getClass().getResource("PlayerView.fxml"));
        root.setStyle("-fx-border-color: black, transparent; -fx-border-width: 1px, 5px; -fx-border-insets: 0px, 1px"); 
        
      //  initX = primalStage.getX();
      //  initY = primalStage.getY();
        
    root.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent me) {
            primalStage.setX(me.getScreenX());
            primalStage.setY(me.getScreenY());
        //    initX = primalStage.getX();
        //    initY = primalStage.getY();
        }
    });
               
         primaryStage.initStyle(StageStyle.UNDECORATED);  
        
        primalStage = primaryStage;  
        Scene scene = new Scene(root);
        primalStage.setScene(scene);  
        primalStage.setTitle("MPlayer");
        primalStage.show();  
    }

    public static void main(String[] args) {
        launch(args);
    }  
}