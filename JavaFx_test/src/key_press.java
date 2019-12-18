import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Alan
 */
public class key_press extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextField tf = new TextField();
        tf.setOnKeyTyped(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                System.out.println("setOnKeyTyped=");
                System.out.println("    event.isShiftDown()=" + event.isShiftDown());
                System.out.println("    event.getCharacter()=" + event.getCharacter());
                System.out.println("    event.getText()=" + event.getText());
                System.out.println("    event.getCode()=" + event.getCode());
            }
        });
        tf.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                System.out.println("setOnKeyPressed=");
                if (event.getCode() == KeyCode.A) {
                    System.out.println("    You pressed A!");
                }
                System.out.println("    event.getCharacter()=" + event.getCharacter());
                System.out.println("    event.getText()=" + event.getText());
                System.out.println("    event.getCode()=" + event.getCode());
            }
        });
        tf.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                System.out.println("setOnKeyReleased=");
                System.out.println("    event.getCharacter()=" + event.getCharacter());
                System.out.println("    event.getText()=" + event.getText());
                System.out.println("    event.getCode()=" + event.getCode());
            }
        });

        Scene scene = new Scene(tf, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}