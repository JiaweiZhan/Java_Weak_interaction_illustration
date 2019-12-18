import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.html.HTMLDocument;


public class BoxExample extends Application {
    private PerspectiveCamera camera;
    private Timeline animation;
    private Box testBox;
    private Cylinder testCylinderl;
    public Parent createContent() throws Exception {
        // Box
        testBox = new Box(5, 5, 5);
        testCylinderl = new Cylinder(2, 10);
        testBox.setMaterial(new PhongMaterial(Color.GRAY));
        testCylinderl.setMaterial(new PhongMaterial(Color.RED));

        // Create and position camera
        camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (
                new Rotate(-20, Rotate.Y_AXIS),
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(0, 0, -20));

        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().add(camera);
        // root.getChildren().add(testBox);
        root.getChildren().add(testCylinderl);

        // Use a SubScene
        SubScene subScene = new SubScene(root, 1000,900, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);

        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);
        return group;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        Scene scene = new Scene(createContent(), 1000, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
        animation=new Timeline(new KeyFrame(Duration.millis(50), e -> moveBall()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void moveBall()
    {
        testCylinderl.getTransforms().addAll(new Rotate(0.1));
        testCylinderl.setHeight(testCylinderl.getHeight() - 0.01);
        testCylinderl.setRadius(testCylinderl.getRadius() - 0.01);
    }

    public static void main(String[] args) {
        launch(args);
    }
}