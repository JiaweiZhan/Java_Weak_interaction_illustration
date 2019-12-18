import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Created by wzj on 2018/3/29.
 */
public class AutoSizeButton extends Application
{
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Button button1 = new Button("Button1");
        button1.setPadding(new Insets(10));
        Button button2 = new Button("Button2");

        button1.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        button2.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

        Rectangle rect1 = new Rectangle(60,20);
        rect1.setFill(Color.TRANSPARENT);

        Rectangle rect2 = new Rectangle(60,20);
        rect2.setFill(Color.TRANSPARENT);

        VBox hBox = new VBox(10,rect1,button1,button2,rect2);
        hBox.setAlignment(Pos.CENTER);

        VBox.setVgrow(button1, Priority.ALWAYS);
        VBox.setVgrow(button2, Priority.ALWAYS);

        Button button3 = new Button("Button3");
        Button button4 = new Button("Button4");

        button3.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        button4.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);

        Rectangle rect3 = new Rectangle(60,20);
        rect3.setFill(Color.TRANSPARENT);

        Rectangle rect4 = new Rectangle(60,20);
        rect4.setFill(Color.TRANSPARENT);

        VBox hBox_2 = new VBox(10,rect3,button4,button3,rect4);
        hBox_2.setAlignment(Pos.CENTER);

        VBox.setVgrow(button1, Priority.ALWAYS);
        VBox.setVgrow(button2, Priority.ALWAYS);
        hBox.setMaxWidth(10);

        // BorderPane root = new BorderPane();
        // root.setBottom(hBox_2);
        HBox hbox = new HBox(hBox, hBox_2);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        HBox.setHgrow(hBox_2, Priority.ALWAYS);

        Scene scene = new Scene(hbox,300,250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}