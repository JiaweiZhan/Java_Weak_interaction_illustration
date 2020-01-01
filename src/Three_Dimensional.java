import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;

class Three_Dimensional extends Application {
    private Timeline animation;
    private Sphere[] new_sphere;
    private int ball_number;
    private static double Largest_Energy = - 3 * Math.pow(10, 2);
    private static double Infinity = Math.pow(10, 8);
    private Cylinder[] lines_cylinder;
    private int model;
    private double[][] location_before;
    private double[][] velocity;
    private int evolution_value;

    private final Group root = new Group();
    private final XformWorld world = new XformWorld();
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final XformCamera cameraXform = new XformCamera();
    private static final double CAMERA_INITIAL_DISTANCE = -1000;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;
    private double mouseFactorX, mouseFactorY;

    Three_Dimensional(int ball_number, int model, int evolution_value)
    {
        location_before = new double[ball_number][3];
        velocity = new double[ball_number][3];
        this.ball_number = ball_number;
        this.model = model;
        this.evolution_value = evolution_value;
        this.lines_cylinder = new Cylinder[this.ball_number * (this.ball_number - 1) / 2];
    }

    @Override
    public void start(Stage primaryStage) {
        for(int i = 0; i< ball_number; i++)
        {
            velocity[i][0] = Math.random();
            velocity[i][1] = Math.random();
            velocity[i][2] = Math.random();
        }
        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
        new_sphere = new Sphere[ball_number];
        if (this.model == 1 || this.model == 4 || this.evolution_value == 1)
        {
            for (int i = 0; i < ball_number; i++)
            {
                new_sphere[i] = new Sphere(10);
                new_sphere[i].setTranslateX(Math.random() * 400 - 200);
                new_sphere[i].setTranslateY(Math.random() * 300 - 150);
                new_sphere[i].setTranslateZ(Math.random() * 150 - 75);
                new_sphere[i].setMaterial(redMaterial);
                location_before[i][0] = new_sphere[i].getTranslateX();
                location_before[i][1] = new_sphere[i].getTranslateY();
                location_before[i][2] = new_sphere[i].getTranslateZ();
            }
        }
        else
        {
            for (int i = 0; i < ball_number; i++)
            {

                new_sphere[i] = new Sphere(10);
                new_sphere[i].setTranslateX(Math.random() * 400 - 200);
                new_sphere[i].setTranslateY(Math.random() * 300 - 150);
                new_sphere[i].setTranslateZ(Math.random() * 50 - 25);
                new_sphere[i].setMaterial(redMaterial);
                location_before[i][0] = new_sphere[i].getTranslateX();
                location_before[i][1] = new_sphere[i].getTranslateY();
                location_before[i][2] = new_sphere[i].getTranslateZ();
            }
        }

        int count = 0;
        for(int i = 0; i < ball_number - 1; i++)
        {
            for(int j = i + 1; j < ball_number; j++)
            {
                double force = Force(Distance(new_sphere[i], new_sphere[j]));
                lines_cylinder[count] = createConnection(new Point3D(new_sphere[i].getTranslateX(), new_sphere[i].getTranslateY(), new_sphere[i].getTranslateZ()), new Point3D(new_sphere[j].getTranslateX(), new_sphere[j].getTranslateY(), new_sphere[j].getTranslateZ()), force);
                root.getChildren().add(lines_cylinder[count]);
                count += 1;
            }
        }
        PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        for (int i = 0; i < ball_number; i++)
        {
            root.getChildren().add(new_sphere[i]);
        }

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        buildCamera();
        buildBodySystem();
        Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(Color.GRAY);
        handleMouse(scene);
        primaryStage.setTitle("Three_Dimensional");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setCamera(camera);
        mouseFactorX = 180.0/scene.getWidth();
        mouseFactorY = 180.0/scene.getHeight();
        animation=new Timeline(new KeyFrame(Duration.millis(50), e -> moveBall()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.U)
                {
                    animation.setRate(animation.getRate()+0.1);
                }
                if(event.getCode() == KeyCode.D)
                {
                    animation.setRate(animation.getRate()>0 ? animation.getRate()-0.1 : 0);
                }
            }
        });
    }


    private void createConnection(int count, Point3D origin, Point3D target, double force) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
        PhongMaterial newMaterial = new PhongMaterial();
        if(force >= 0)
        {
            newMaterial.setDiffuseColor(Color.YELLOWGREEN);
            newMaterial.setSpecularColor(Color.YELLOW);
        }
        else
        {
            newMaterial.setDiffuseColor(Color.DARKRED);
            newMaterial.setSpecularColor(Color.RED);
        }
        if (Math.abs(force) > 1)
        {
            force = 1;
        }
        final List<Transform> transforms = lines_cylinder[count].getTransforms();
        lines_cylinder[count].getTransforms().removeAll(transforms);
        lines_cylinder[count].setRadius(5 * Math.sqrt(Math.abs(force)));
        lines_cylinder[count].setHeight(height);
        lines_cylinder[count].setMaterial(newMaterial);
        lines_cylinder[count].getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
    }

    private Cylinder createConnection(Point3D origin, Point3D target, double force) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
        PhongMaterial newMaterial = new PhongMaterial();
        if(force >= 0)
        {
            newMaterial.setDiffuseColor(Color.YELLOWGREEN);
            newMaterial.setSpecularColor(Color.YELLOW);
        }
        else
        {
            newMaterial.setDiffuseColor(Color.DARKRED);
            newMaterial.setSpecularColor(Color.RED);
        }
        if (Math.abs(force) > 1)
        {
            force = 1;
        }
        Cylinder line = new Cylinder(5 * Math.sqrt(Math.abs(force)), height);
        line.setMaterial(newMaterial);
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
        return line;
    }

    private void changeColor(Sphere[] ball)
    {
        for (int i = 0; i < ball.length; i++)
        {
            double energy_for_one_ball = 0;
            for(int j = 0; j < ball.length; j++)
            {
                if (j == i)
                {
                    continue;
                }
                energy_for_one_ball += new Energy(Distance(ball[i], ball[j]), this.model).Calculate_Energy();
            }
            int red = (int)(255 * energy_for_one_ball / Largest_Energy);
            if (red >= 255)
            {
                red = 255;
            }
            else if(red <= 0)
            {
                red = 0;
            }
            PhongMaterial newMaterial = new PhongMaterial();
            newMaterial.setDiffuseColor(Color.rgb(red, 255 - red, 0));
            newMaterial.setSpecularColor(Color.WHITESMOKE);
            ball[i].setMaterial(newMaterial);
        }
    }

    private double Force(double distance)
    {
        double delta = Math.pow(10, -7);
        double energy_left = new Energy(distance - delta, this.model).Calculate_Energy();
        double energy = new Energy(distance, this.model).Calculate_Energy();
        double energy_right = new Energy(distance + delta, this.model).Calculate_Energy();
        return 1.0 / 2 * ((energy_right - energy) / delta + (energy - energy_left) / delta);
    }

    private double Distance(Sphere a, Sphere b)
    {
        double x1 = a.getTranslateX();
        double y1 = a.getTranslateY();
        double z1 = a.getTranslateZ();
        double x2 = b.getTranslateX();
        double y2 = b.getTranslateY();
        double z2 = b.getTranslateZ();
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) + Math.pow((z1 - z2), 2));
    }

    private void moveBall()
    {
        int account;
        double[] distance_after_for_two_pairs = new double[(int) (this.ball_number * (this.ball_number - 1) / 2)];
        double[][] temp_acceleration = new double[ball_number][3];
        // change the velocity of every ball via force field;
        for (int i = 0; i < ball_number; i++)
        {
            for (int j = 0; j < ball_number; j++)
            {
                if (j != i)
                {
                    double relative_distance_x = new_sphere[j].getTranslateX() - new_sphere[i].getTranslateX();
                    double relative_distance_y = new_sphere[j].getTranslateY() - new_sphere[i].getTranslateY();
                    double relative_distance_z = new_sphere[j].getTranslateZ() - new_sphere[i].getTranslateZ();
                    double temp_single_force = Force(Distance(new_sphere[i], new_sphere[j]));

                    // mass is just the mass of every ball;
                    double mass = 10;

                    // damping means the rate for the velocity to decrease or avoid teh velocity from being too big and would fly anywhere.
                    // generally, 0.0001 is a good choice since it could take more time to become stable. However, once you want to get a stable status quickly,
                    // you should set the damping_item to 0.001
                    double damping_item = 0.0005;

                    // calculating the change of acceleration for every ball based on their force, F = ma;
                    temp_acceleration[i][0] += relative_distance_x / Distance(new_sphere[i], new_sphere[j]) * temp_single_force / mass - damping_item * velocity[i][0];
                    if (new_sphere[i].getTranslateX() > 300 - new_sphere[i].getRadius() - 10)
                    {
                        temp_acceleration[i][0] += - 1 / (300 - new_sphere[i].getRadius() - new_sphere[i].getTranslateX());
                    }
                    if (new_sphere[i].getTranslateX() < -300 + new_sphere[i].getRadius() + 10)
                    {
                        temp_acceleration[i][0] += + 1 / (- new_sphere[i].getRadius() + new_sphere[i].getTranslateX() + 300);
                    }

                    temp_acceleration[i][1] += relative_distance_y / Distance(new_sphere[i], new_sphere[j]) * temp_single_force / mass - damping_item * velocity[i][1];
                    if (new_sphere[i].getTranslateY() > 200 - new_sphere[i].getRadius() - 10)
                    {
                        temp_acceleration[i][1] += - 1 / (200 - new_sphere[i].getRadius() - new_sphere[i].getTranslateY());
                    }
                    if (new_sphere[i].getTranslateY() < -200 + new_sphere[i].getRadius() + 10)
                    {
                        temp_acceleration[i][1] += + 1 / (- new_sphere[i].getRadius() + new_sphere[i].getTranslateY() + 200);
                    }

                    temp_acceleration[i][2] += relative_distance_z / Distance(new_sphere[i], new_sphere[j]) * temp_single_force / mass - damping_item * velocity[i][2];
                    if (new_sphere[i].getTranslateZ() > 100 - new_sphere[i].getRadius() - 10)
                    {
                        temp_acceleration[i][2] += - 1 / (100 - new_sphere[i].getRadius() - new_sphere[i].getTranslateZ());
                    }
                    if (new_sphere[i].getTranslateZ() < -100 + new_sphere[i].getRadius() + 10)
                    {
                        temp_acceleration[i][2] += + 1 / (- new_sphere[i].getRadius() + new_sphere[i].getTranslateZ() + 100);
                    }
                }
            }
        }

        // calculate the change of velocity of every ball based on delta_V = a * delta_T
        // Also calculate the new location of every ball by delta_S = VT + 1/2 * a * T^2
        for (int i = 0; i < ball_number; i++)
        {
            double delta_t = 0.1;
            double new_x_location = new_sphere[i].getTranslateX() + velocity[i][0] * delta_t + 0.5 * temp_acceleration[i][0] * Math.pow(delta_t, 2);
            double new_y_location = new_sphere[i].getTranslateY() + velocity[i][1] * delta_t + 0.5 * temp_acceleration[i][1] * Math.pow(delta_t, 2);
            double new_z_location = new_sphere[i].getTranslateZ() + velocity[i][2] * delta_t + 0.5 * temp_acceleration[i][2] * Math.pow(delta_t, 2);
            new_sphere[i].setTranslateX(new_x_location);
            new_sphere[i].setTranslateY(new_y_location);
            new_sphere[i].setTranslateZ(new_z_location);
            velocity[i][0] += temp_acceleration[i][0] * delta_t;
            velocity[i][1] += temp_acceleration[i][1] * delta_t;
            velocity[i][2] += temp_acceleration[i][2] * delta_t;
        }
        account = 0;
        for (int i = 0; i < ball_number - 1; i++) {
            for (int j = i + 1; j < ball_number; j++) {
                distance_after_for_two_pairs[account] = Distance(new_sphere[i], new_sphere[j]);
                account += 1;
            }
        }
        changeColor(new_sphere);
        draw_Line(distance_after_for_two_pairs);
    }

    private void draw_Line(double[] Distance_after)
    {
        int count = 0;
        for(int i  = 0; i < ball_number - 1; i++)
        {
            for (int j = i + 1; j < ball_number; j++)
            {
                double force = Force(Distance_after[count]);
                createConnection(count, new Point3D(new_sphere[i].getTranslateX(), new_sphere[i].getTranslateY(), new_sphere[i].getTranslateZ()), new Point3D(new_sphere[j].getTranslateX(), new_sphere[j].getTranslateY(), new_sphere[j].getTranslateZ()), force);
                count += 1;
            }
        }
    }

    private void buildCamera() {
        // build camera function is used to change the view of user
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(camera);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
    }

    private void buildBodySystem() {
        // buildBodySystem is used to build the box, in which every ball could only be restricted
        // to move.
        PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);
        Box box = new Box(600, 400, 200);
        box.setMaterial(whiteMaterial);
        box.setDrawMode(DrawMode.LINE);
        world.getChildren().addAll(box);
    }

    private void handleMouse(Scene scene) {
        // handleMouse function is used to change the view based on the movement of mouse
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry(mouseDeltaX * 180.0/scene.getWidth());
                cameraXform.rx(-mouseDeltaY * 180.0/scene.getHeight());
            } else if (me.isSecondaryButtonDown()) {
                camera.setTranslateZ(camera.getTranslateZ() + mouseDeltaY);
            }
        });
    }

    public static void main(String[] args) {
        // Main function
        launch(args);
    }

}

class XformWorld extends Group {
    final Translate t = new Translate(0.0, 0.0, 0.0);
    final Rotate rx = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);

    public XformWorld() {
        super();
        this.getTransforms().addAll(t, rx, ry, rz);
    }
}

class XformCamera extends Group {
    Point3D px = new Point3D(1.0, 0.0, 0.0);
    Point3D py = new Point3D(0.0, 1.0, 0.0);
    Rotate r;
    private Transform t = new Rotate();

    XformCamera() {
        super();
    }

    public void rx(double angle) {
        r = new Rotate(angle, px);
        this.t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    public void ry(double angle) {
        r = new Rotate(angle, py);
        this.t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

}
