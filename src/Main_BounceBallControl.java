import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.collections.FXCollections;
import javafx.scene.input.KeyEvent;


public class Main_BounceBallControl extends Application
{

    private int value_interaction;
    private int value_number;
    private int evolution_value;
    private int dimensional_value;
    @Override
    public void start(Stage primaryStage)
    {
        String family = "Helvetica";
        double size = 50;

        TextFlow textFlow = new TextFlow();
        textFlow.setLayoutX(40);
        textFlow.setLayoutY(40);
        Text text1 = new Text("Weak ");
        text1.setFont(Font.font(family, size));
        text1.setFill(Color.RED);

        Text text2 = new Text("Interaction");
        text2.setFill(Color.ORANGE);
        text2.setFont(Font.font(family, FontWeight.BOLD, 40));


        Text text3 = new Text(" Demonstration");
        text3.setFill(Color.GREEN);
        text3.setFont(Font.font(family, FontPosture.ITALIC, 30));

        textFlow.getChildren().addAll(text1, text2, text3);

        TextFlow self_defined = new TextFlow();

        Text self = new Text("Users-defined");
        self.setFont(Font.font(family, 20));
        self.setFill(Color.WHITE);
        self_defined.getChildren().add(self);
        self_defined.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(5);
        grid.setHgap(5);

        final TextField function = new TextField();
        function.setPromptText("Enter your own Energy Function");
        GridPane.setConstraints(function, 0, 0);
        grid.getChildren().add(function);

        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        VBox left_pane = new VBox(10);
        StackPane right_pane = new StackPane();

        HBox hbox = new HBox();

        submit.setOnAction((ActionEvent e) -> {
            if (
                    (function.getText() != null && !function.getText().isEmpty())
            ) {
                String function_expression = function.getText();
                Scatter_Point scatter_point_self = new Scatter_Point(function_expression);
                scatter_point_self.start(new Stage());
                BounceBallControl open = new BounceBallControl(4, value_number, evolution_value, function_expression);
                right_pane.getChildren().clear();
                open.start(hbox, right_pane);
            } else {
                // take out a warning window!!!
                System.out.println("You have not left a comment.");
            }
        });

        Button btn = new Button("Click to show the interaction animation");

        // combobox for whether it is a 2 dimensional for 3 dimensional graph
        final ComboBox dimensional_type = new ComboBox();
        dimensional_type.setItems(FXCollections.observableArrayList("2D", "3D"));
        dimensional_type.setTooltip(new Tooltip("Select dimension"));
        dimensional_type.setPrefWidth(200);
        dimensional_type.setPromptText("Dimensional type");
        dimensional_type.getSelectionModel().selectedIndexProperty().addListener(
                (ov, value, new_value) -> dimensional_value = new_value.intValue()
        );


        // combobox for the interaction type
        final ComboBox interaction_type = new ComboBox();
        interaction_type.setItems(FXCollections.observableArrayList("repel potential", "attractive potential", "vdW potential"));
        interaction_type.setTooltip(new Tooltip("Please select the interaction type"));
        interaction_type.setPrefWidth(200);
        interaction_type.setPromptText("Interaction type");
        interaction_type.getSelectionModel().selectedIndexProperty().addListener(
                (ov, value, new_value) -> value_interaction = new_value.intValue());

        // combobox for the number of balls
        final ComboBox ball_number = new ComboBox();
        ball_number.setItems(FXCollections.observableArrayList("2", "3", "5", "10", "20"));
        ball_number.setTooltip(new Tooltip("Please select the number of particles"));
        ball_number.setPrefWidth(200);
        ball_number.setPromptText("Ball number");
        String[] circle_number = {"2", "3", "5", "10", "20"};
        ball_number.getSelectionModel().selectedIndexProperty().addListener(
                (ov, value, new_value) -> value_number = Integer.parseInt(
                        circle_number[new_value.intValue()]));

        // combobox for the evolution type (Kinetic Monte Carlo Method or Force Field)
        final ComboBox evolution_type = new ComboBox();
        evolution_type.setItems(FXCollections.observableArrayList("Kinetic Monte Carlo", "Force Field"));
        evolution_type.setPromptText("evolution type");
        evolution_type.setTooltip(new Tooltip("Choose an approach for evolution"));
        evolution_type.setPrefWidth(200);
        evolution_type.getSelectionModel().selectedIndexProperty().addListener(
                (ov, value, new_value) -> evolution_value = new_value.intValue()
        );

        left_pane.setBackground(
                new Background(new BackgroundFill(Color.rgb(25, 27, 32), CornerRadii.EMPTY, Insets.EMPTY)));
        left_pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(1))));

        right_pane.setBackground(
                new Background(new BackgroundFill(Color.rgb(30, 33, 40), CornerRadii.EMPTY, Insets.EMPTY)));

        left_pane.getChildren().addAll(textFlow, interaction_type, ball_number,evolution_type, dimensional_type, btn, self_defined, grid);

        VBox.setVgrow(textFlow, Priority.ALWAYS);
        VBox.setVgrow(interaction_type, Priority.ALWAYS);
        VBox.setVgrow(ball_number, Priority.ALWAYS);
        VBox.setVgrow(evolution_type, Priority.ALWAYS);
        VBox.setVgrow(dimensional_type, Priority.ALWAYS);
        VBox.setVgrow(btn, Priority.ALWAYS);
        VBox.setVgrow(self, Priority.ALWAYS);
        VBox.setVgrow(grid, Priority.ALWAYS);
        left_pane.setPadding(new Insets(10));
        left_pane.setMaxWidth(300);
        left_pane.setMinWidth(300);
        HBox.setHgrow(right_pane, Priority.ALWAYS);
        HBox.setHgrow(left_pane, Priority.ALWAYS);

        right_pane.setMaxWidth(Double.MAX_VALUE);
        hbox.getChildren().addAll(left_pane, right_pane);
        btn.setOnAction((ActionEvent event) ->
        {
            Scatter_Point scatter_point = new Scatter_Point(value_interaction);
            scatter_point.start(new Stage());
            if (dimensional_value == 0)
            {
                BounceBallControl open = new BounceBallControl(value_interaction, value_number, evolution_value);
                right_pane.getChildren().clear();
                open.start(hbox, right_pane);
            }
            else
            {
                Three_Dimensional open = new Three_Dimensional(value_number, value_interaction, evolution_value);
                right_pane.getChildren().clear();
                open.start(new Stage());
            }
        });

        Scene scene = new Scene(hbox, 1000, 600);
        primaryStage.setTitle("Main Pane");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        // Main function
        launch(args);
    }
}

class BounceBallControl{
    private int model;
    private int ball_number;
    private String function;
    private int evolution_value;
    BounceBallControl(int model, int ball_number, int evolution_value)
    {
        this.model = model;
        this.ball_number = ball_number;
        this.evolution_value = evolution_value;
    }
    BounceBallControl(int model, int ball_number, int evolution_value, String function)
    {
        this.model = model;
        this.ball_number = ball_number;
        this.function = function;
        this.evolution_value = evolution_value;
    }
    void start(HBox primaryPane, StackPane ball_pane) {
        BallPane ballPane = new BallPane(this.ball_number, this.model, this.evolution_value, this.function);
        primaryPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.U)
                {
                    ballPane.increaseSpeed();
                }
                if(event.getCode() == KeyCode.D)
                {
                    ballPane.decreaseSpeed();
                }
            }
        });
        // set on mouse show_cutoff_radius
        EventHandler<MouseEvent> mouseEventEventHandler = e -> {
            double x_axis = e.getX();
            double y_axis = e.getY();
            ballPane.change_color_and_opacity(x_axis, y_axis);
        };
        //set on mouse dragged
        EventHandler<MouseEvent> mouseEventEventHandler_dragged = e -> {
            double x_axis = e.getX();
            double y_axis = e.getY();
            ballPane.change_location(x_axis, y_axis);
        };
        ball_pane.addEventHandler(MouseEvent.MOUSE_MOVED, mouseEventEventHandler);
        ball_pane.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEventEventHandler_dragged);
        ball_pane.getChildren().add(ballPane);
    }
}
