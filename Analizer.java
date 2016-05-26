import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Murdecai
 */
public class Analizer {
        
    private XYChart.Data<String, Number>[] series1Data,series2Data ;
    private AudioSpectrumListener audioSpectrumListener;
    Stage stage;
    
    MediaPlayer audioMediaPlayer;
    
    Analizer(){
        
    }
    
    
    public void strt() throws IOException{
        
            stage = new Stage();
            stage.setTitle("Spectrum Analizer");
            
            AnchorPane root = new AnchorPane();  
            root.setStyle("-fx-border-color: black, transparent; -fx-border-width: 1px, 5px; -fx-border-insets: 0px, 1px"); 
            

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                stage.setX(me.getScreenX());
                stage.setY(me.getScreenY());
            }
        });
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root);
            stage.setScene(scene);  

            scene.getStylesheets().add(
                this.getClass().getClassLoader().getResource("res/Spec_style.css").toString());
            
            stage.setX(750);
            stage.setY(50);
            
            root.getChildren().add(createChart());
            
            analize();
             
             stage.show();
    }
    
    public void analize(){
    
        audioMediaPlayer.setAudioSpectrumListener(new AudioSpectrumListener() {
                @Override
                public void spectrumDataUpdate(double timestamp, double duration,
                        float[] magnitudes, float[] phases) {
                            for (int i = 0; i < series1Data.length; i++) {
                               series1Data[i].setYValue(magnitudes[i] + 60);
                             }
                        }
            });
        
    }
    
    protected BarChart<String, Number> createChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0,50,10);
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        bc.setId("barAudioDemo");
        bc.setLegendVisible(false);
        bc.setAnimated(false);
        bc.setBarGap(0);
        bc.setCategoryGap(1);
        bc.setVerticalGridLinesVisible(false);
        bc.setHorizontalGridLinesVisible(false);
        // setup chart
        bc.setTitle("Spectrum Analizer");
        xAxis.setLabel("Frequency bands");
       // xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(herz)));
        yAxis.setLabel("Magnitudes");
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,null,"dB"));
        // add starting data
        XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
        
         XYChart.Series<String,Number> series2 = new XYChart.Series<String,Number>();
        series2.setName("Data Series 2");
        //noinspection unchecked
        series1Data = new XYChart.Data[120];
        series2Data = new XYChart.Data[15];
        String[] categories = new String[120];
        for (int i=0; i<series1Data.length; i++) {
            categories[i] = Integer.toString(i+1);
            series1Data[i] = new XYChart.Data<String,Number>(categories[i],50);
            series1.getData().add(series1Data[i]);
        }
        for (int j=0; j<series2Data.length; j++) {
                    series2.getData().add(series1Data[j*8]);
        }
        bc.getData().add(series2);
        return bc;
    }
    
    public void closeAnilizer() {
       stage.close(); 
    }
}
