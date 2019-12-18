import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collections;

public class Scatter_Point extends Application {
    private int model = -1;
    private String function_expression;
    Scatter_Point(int model_)
    {
        this.model = model_;
    }

    private double double_min(double[] list)
    {
        if (list.length == 1)
        {
            return list[0];
        }
        double min = list[0];
        for (int i = 1; i < list.length; i++)
        {
            if (list[i] < min)
            {
                min = list[i];
            }
        }
        return min;
    }

    private double double_max(double[] list)
    {
        if (list.length == 1)
        {
            return list[0];
        }
        double max = list[0];
        for (int i = 1; i < list.length; i++)
        {
            if (list[i] > max)
            {
                max = list[i];
            }
        }
        return max;
    }

    Scatter_Point(String function_expression_)
    {
        this.function_expression = function_expression_;
    }

    @Override
    public void start(Stage primarystage)
    {
        NumberAxis x_axis = new NumberAxis(0, 100, 10);
        x_axis.setLabel("distance between two point");
        NumberAxis y_axis = new NumberAxis(-100, 100, 10);
        y_axis.setLabel("Energy");
        ScatterChart<String, Number> scatterChart;
        XYChart.Series series = new XYChart.Series();

        Text function = new Text();
        // System.out.println(ballPane.Energy(10, 2));
        double[] x_list = new double[]{0.1, 0.3, 0.5, 0.8, 1, 3, 8, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        double[] y_list = new double[x_list.length];
        if (this.model != -1)
        {
            function.setText(new Energy(1, this.model).Energy_express());
            for (int i = 0; i < x_list.length; i++)
            {
                y_list[i] = new Energy(x_list[i], this.model).Calculate_Energy();
                series.getData().add(new XYChart.Data(x_list[i], new Energy(x_list[i], this.model).Calculate_Energy()));
            }
            y_axis = new NumberAxis(-10 + double_min(y_list), 10 + double_max(y_list), (double_max(y_list) - double_min(y_list) + 20) / 10.0);
            y_axis.setLabel("Energy");
            scatterChart = new ScatterChart(x_axis, y_axis);
        }
        else
        {
            MathEval math = new MathEval();
            function.setText("Interaction Function: " + this.function_expression);
            for (int i = 0; i < x_list.length; i++)
            {
                math.setVariable("x", x_list[i]);
                y_list[i] = math.evaluate(this.function_expression);
                series.getData().add(new XYChart.Data(x_list[i], math.evaluate(this.function_expression)));
            }
            y_axis = new NumberAxis(-10 + double_min(y_list), 10 + double_max(y_list), (double_max(y_list) - double_min(y_list) + 20) / 10.0);
            y_axis.setLabel("Energy");
            scatterChart = new ScatterChart(x_axis, y_axis);
        }

        scatterChart.getData().addAll(series);

        String family = "Helvetica";

        StackPane root = new StackPane(scatterChart);
        root.getChildren().add(function);
        function.setFill(Color.BLACK);
        function.setFont(Font.font(family, FontPosture.ITALIC, 20));
        function.setTranslateX(200);
        function.setTranslateY(-300);

        //Creating a scene object
        Scene scene = new Scene(root, 900, 700);

        //Setting title to the Stage
        primarystage.setTitle("Scatter Chart");

        //Adding scene to the stage
        primarystage.setScene(scene);

        primarystage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
