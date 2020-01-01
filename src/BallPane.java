import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import java.util.Vector;


public class BallPane extends Pane{
    private Vector<Circle> circle_vec = new Vector<Circle>();
    private Timeline animation;
    private static double Largest_Energy = Math.pow(10, 2);
    private static double Infinity = Math.pow(10, 8);
    private Vector<Line> lines_vec = new Vector<Line>();
    private int model;
    private Vector<Double> location_before_vec_x = new Vector<Double>();
    private Vector<Double> location_before_vec_y = new Vector<Double>();
    private Vector<Double> velocity_vec_x = new Vector<Double>();
    private Vector<Double> velocity_vec_y = new Vector<Double>();
    private String function;
    private int evolution_value;

    private double Force(double distance)
    {
        double delta = Math.pow(10, -7);
        double energy_left = new Energy(distance - delta, this.model, this.function).Calculate_Energy();
        double energy = new Energy(distance, this.model, this.function).Calculate_Energy();
        double energy_right = new Energy(distance + delta, this.model, this.function).Calculate_Energy();
        return 1.0 / 2 * ((energy_right - energy) / delta + (energy - energy_left) / delta);
    }

    BallPane(int ball_number, int model, int evolution_value, String function)
    {
        this.function = function;
        this.evolution_value = evolution_value;
        this.model = model;
        double radius = 10;
        for(int i = 0; i< ball_number; i++)
        {
            velocity_vec_x.addElement(Math.random());
            velocity_vec_y.addElement(Math.random());
        }
        if (this.model == 1 || this.model == 4 || this.evolution_value == 1)
        {
            for (int i = 0; i < ball_number; i++)
            {
                Circle new_circle = new Circle(100 + Math.random() * 500, 50 + Math.random() * 500, radius);
                new_circle.setFill(Color.rgb(255, 0, 0));
                new_circle.setStroke(Color.BLACK);
                new_circle.setOpacity(0.7);
                circle_vec.add(new_circle);
                location_before_vec_x.add(new_circle.getCenterX());
                location_before_vec_y.add(new_circle.getCenterY());
            }
        }
        else
        {
            for (int i = 0; i < ball_number; i++)
            {
                Circle new_circle = new Circle(400, 300, radius);
                new_circle.setFill(Color.rgb(255, 0, 0));
                new_circle.setStroke(Color.BLACK);
                new_circle.setOpacity(0.7);
                circle_vec.add(new_circle);
                location_before_vec_x.add(new_circle.getCenterX());
                location_before_vec_y.add(new_circle.getCenterY());
            }
        }
        for (int i = 0; i < circle_vec.size() - 1; i++)
        {
            for(int j = i+1; j < circle_vec.size(); j++)
            {
                Line new_line = new Line(circle_vec.get(i).getCenterX(), circle_vec.get(i).getCenterY(), circle_vec.get(j).getCenterX(), circle_vec.get(j).getCenterY());
                new_line.setStrokeWidth(1);
                new_line.setStroke(Color.GREY);
                new_line.setOpacity(0.5);
                lines_vec.add(new_line);
                getChildren().add(lines_vec.lastElement());
            }
        }
        for (int i = 0; i < circle_vec.size(); i++)
        {
            getChildren().add(circle_vec.get(i));
        }
        animation=new Timeline(new KeyFrame(Duration.millis(50),e -> moveBall()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    void change_location(double mouse_x_axis, double mouse_y_axis) {
        change_color_and_opacity(mouse_x_axis, mouse_y_axis);
        int index_of_circle = -1;
        for (int i = 0; i < circle_vec.size(); i++) {
            if (Distance(circle_vec.get(i), mouse_x_axis, mouse_y_axis) <= circle_vec.get(i).getRadius()) {
                index_of_circle = i;
                break;
            }
        }
        if (index_of_circle != -1)
        {
            velocity_vec_y.setElementAt(0.0, index_of_circle);
            velocity_vec_x.setElementAt(0.0, index_of_circle);
            circle_vec.get(index_of_circle).setCenterX(mouse_x_axis);
            circle_vec.get(index_of_circle).setCenterY(mouse_y_axis);
            change_color_and_opacity(index_of_circle);
        }
    }

    private void change_color_and_opacity(int index_of_circle)
    {
        for(int i = 0; i < circle_vec.size(); i++)
        {
            if (Distance(circle_vec.get(index_of_circle), circle_vec.get(i)) > 300)
            {
                circle_vec.get(i).setOpacity(0.1);
            }
            else
            {
                circle_vec.get(i).setOpacity(0.7);
            }
        }
        int acount = 0;
        for(int k = 0; k < circle_vec.size() - 1; k++)
        {
            for(int m = k + 1; m < circle_vec.size(); m++)
            {
                if ((k != index_of_circle && m != index_of_circle) || Distance(circle_vec.get(k), circle_vec.get(m)) > 300)
                {
                    lines_vec.get(acount).setOpacity(0.1);
                }
                else
                {
                    lines_vec.get(acount).setOpacity(0.5);
                }
                acount += 1;
            }
        }
    }

    void change_color_and_opacity(double mouse_x_axis, double mouse_y_axis)
    {
        int index_of_circle;
        boolean In_Ball = false;
        for (int i = 0; i < circle_vec.size(); i++)
        {
            if (Distance(circle_vec.get(i), mouse_x_axis, mouse_y_axis) <= circle_vec.get(i).getRadius())
            {
                index_of_circle = i;
                In_Ball = true;
                for (int j = 0; j < circle_vec.size(); j++)
                {
                    if ( index_of_circle == j)
                    {
                        continue;
                    }
                    if (Distance(circle_vec.get(i), circle_vec.get(j)) > 300)
                    {
                        circle_vec.get(j).setOpacity(0.1);
                    }
                }
                int acount = 0;
                for(int k = 0; k < circle_vec.size() - 1; k++)
                {
                    for(int m = k + 1; m < circle_vec.size(); m++)
                    {
                        if ((k != index_of_circle && m != index_of_circle) || Distance(circle_vec.get(k), circle_vec.get(m)) > 300)
                        {
                            lines_vec.get(acount).setOpacity(0.1);
                        }
                        acount += 1;
                    }
                }
                break;
            }
        }
        if (!In_Ball)
        {
            for(int i = 0; i < circle_vec.size(); i++)
            {
                circle_vec.get(i).setOpacity(0.7);
            }
            int acount = 0;
            for(int i = 0; i < circle_vec.size() - 1; i++)
            {
                for(int j = i + 1; j < circle_vec.size(); j++)
                {
                    lines_vec.get(acount).setOpacity(0.5);
                    acount += 1;
                }
            }
        }
    }

    private void draw_Line(Vector<Circle> circle, Vector<Line> lines, Vector<Double> Distance_after)
    {
        int account = 0;
        for (int i = 0; i < circle.size() - 1; i++)
        {
            for(int j = i+1; j < circle.size(); j++)
            {
                double force = Force(Distance_after.get(account));
                if(force >= 0)
                {
                    lines.get(account).setStroke(Color.YELLOW);
                }
                else
                {
                    lines.get(account).setStroke(Color.RED);
                }
                if (Math.abs(force) > 1)
                {
                    force = 1;
                }
                if (this.model == 2)
                {
                    lines.get(account).setStrokeWidth(15 * Math.sqrt(Math.abs(force)));
                }
                lines.get(account).setStartX(circle.get(i).getCenterX());
                lines.get(account).setStartY(circle.get(i).getCenterY());
                lines.get(account).setEndX(circle.get(j).getCenterX());
                lines.get(account).setEndY(circle.get(j).getCenterY());
                account += 1;
            }
        }
    }

    void play()
    {
        animation.play();
        animation.setRate(1);
    }

    void pause()
    {
        animation.pause();
    }

    void increaseSpeed()
    {
        animation.setRate(animation.getRate()+0.1);
    }

    void decreaseSpeed()
    {
        animation.setRate(animation.getRate()>0 ? animation.getRate()-0.1 : 0);
    }

    public DoubleProperty rateProperty()
    {
        return animation.rateProperty();
    }

    private double Distance(Circle a, Circle b)
    {
        double x1 = a.getCenterX();
        double y1 = a.getCenterY();
        double x2 = b.getCenterX();
        double y2 = b.getCenterY();
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    private double Distance(Circle a, double x_axis, double y_axis)
    {
        return Math.sqrt(Math.pow((a.getCenterX() - x_axis), 2) + Math.pow((a.getCenterY() - y_axis), 2));
    }

    private void changeColor(Vector<Circle> ball)
    {
        for (int i = 0; i < ball.size(); i++)
        {
            double energy_for_one_ball = 0;
            for(int j = 0; j < ball.size(); j++)
            {
                if (j == i)
                {
                    continue;
                }
                energy_for_one_ball += new Energy(Distance(ball.get(i), ball.get(j)), this.model, this.function).Calculate_Energy();
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
            ball.get(i).setFill(Color.rgb(red, 255 - red, 0));
        }
    }

    private double dot_product(Circle central, double before_x, double before_y, Circle virtual)
    {
        return (before_x - central.getCenterX()) * (virtual.getCenterY() - central.getCenterX()) + (before_y - central.getCenterY()) * (virtual.getCenterY() - central.getCenterY());
    }

    private void moveBall()
    {
        Vector<Double> distance_after_for_two_pairs_vec = new Vector<Double>();
        if (this.evolution_value == 0) {
            double energy_previous = 0;
            double energy_after = 0;
            Vector<Circle> Virtual_ball_vec = new Vector<Circle>();
            for (int i = 0; i < circle_vec.size() - 1; i++) {
                for (int j = i + 1; j < circle_vec.size(); j++) {
                    energy_previous += new Energy(Distance(circle_vec.get(i), circle_vec.get(j)), this.model, this.function).Calculate_Energy();
                }
            }
            if (energy_previous > Infinity) {
                energy_previous = Infinity;
            }
            for (int i = 0; i < circle_vec.size(); i++) {
                location_before_vec_x.setElementAt(circle_vec.get(i).getCenterX(), i);
                location_before_vec_y.setElementAt(circle_vec.get(i).getCenterY(), i);
                Virtual_ball_vec.add(new Circle());
                do {
                    double random_u;
                    double random_v;
                    do {
                        random_u = 2 * Math.random() - 1;
                        random_v = 2 * Math.random() - 1;
                    } while (Math.pow(random_u, 2) + Math.pow(random_v, 2) > 1);
                    double r = Math.sqrt(Math.pow(random_u, 2) + Math.pow(random_v, 2));
                    double tmp = Math.sqrt(-2 * Math.log(Math.pow(r, 2)));
                    double delta_x = random_u / r * tmp;
                    double delta_y = random_v / r * tmp;
                    Virtual_ball_vec.setElementAt(new Circle(circle_vec.get(i).getCenterX() + delta_x,
                            circle_vec.get(i).getCenterY() + delta_y,
                            circle_vec.get(i).getRadius()), i);
                } while (Virtual_ball_vec.lastElement().getCenterX() > getWidth() - Virtual_ball_vec.lastElement().getRadius()
                        || Virtual_ball_vec.lastElement().getCenterX() < Virtual_ball_vec.lastElement().getRadius()
                        || Virtual_ball_vec.lastElement().getCenterY() > getHeight() - Virtual_ball_vec.lastElement().getRadius()
                        || Virtual_ball_vec.lastElement().getCenterY() < Virtual_ball_vec.lastElement().getRadius()
                        || (dot_product(circle_vec.get(i), location_before_vec_x.get(i), location_before_vec_y.get(i), Virtual_ball_vec.lastElement()) >= 0 && (circle_vec.get(i).getCenterY() != location_before_vec_y.get(i) || circle_vec.get(i).getCenterX() != location_before_vec_x.get(i))));
            }

            for (int i = 0; i < circle_vec.size() - 1; i++) {
                for (int j = i + 1; j < circle_vec.size(); j++) {
                    energy_after += new Energy(Distance(Virtual_ball_vec.get(i), Virtual_ball_vec.get(j)), this.model, this.function).Calculate_Energy();
                    distance_after_for_two_pairs_vec.add(Distance(Virtual_ball_vec.get(i), Virtual_ball_vec.get(j)));
                }
            }
            if (energy_after > Infinity) {
                energy_after = Infinity;
            }
            double Possibility = Math.exp(-energy_after + energy_previous);
            if (Possibility >= 1) {
                update_location(distance_after_for_two_pairs_vec, Virtual_ball_vec);
            } else {
                double MC_Parameter = Math.random();
                if (MC_Parameter < Possibility) {
                    update_location(distance_after_for_two_pairs_vec, Virtual_ball_vec);
                }
            }
        }
        else
        {
            Vector<Double> temp_acceleration_x = new Vector<Double>();
            Vector<Double> temp_acceleration_y = new Vector<Double>();
            for (int i = 0; i < circle_vec.size(); i++)
            {
                temp_acceleration_x.add(0.0);
                temp_acceleration_y.add(0.0);
                for (int j = 0; j < circle_vec.size(); j++)
                {
                    if (j != i)
                    {
                        double relative_distance_x = circle_vec.get(j).getCenterX() - circle_vec.get(i).getCenterX();
                        double relative_distance_y = circle_vec.get(j).getCenterY() - circle_vec.get(i).getCenterY();
                        double temp_single_force = Force(Distance(circle_vec.get(i), circle_vec.get(j)));

                        double mass = 10;
                        double damping_item = 0.001;
                        double new_temp_x = temp_acceleration_x.get(i) + relative_distance_x / Distance(circle_vec.get(i), circle_vec.get(j)) * temp_single_force / mass - damping_item * velocity_vec_x.get(i);
                        temp_acceleration_x.setElementAt(new_temp_x, i);
                        // if (circle[i].getCenterX() > getWidth() - circle[i].getRadius() - 30)
                        // {
                        //     temp_acceleration[i][0] += - 1 / (getWidth() - circle[i].getRadius() - circle[i].getCenterX());
                        // }
                        // if (circle[i].getCenterX() < circle[i].getRadius() + 30)
                        // {
                        //     temp_acceleration[i][0] += + 1 / (- circle[i].getRadius() + circle[i].getCenterX());
                        // }

                         double new_temp_y = temp_acceleration_y.get(i) + relative_distance_y / Distance(circle_vec.get(i), circle_vec.get(j)) * temp_single_force / mass - damping_item * velocity_vec_y.get(i);
                         temp_acceleration_y.setElementAt(new_temp_y, i);
                        // if (circle[i].getCenterY() > getHeight() - circle[i].getRadius() - 30)
                        // {
                        //     temp_acceleration[i][1] += - 1 / (getHeight() - circle[i].getRadius() - circle[i].getCenterY());
                        // }
                        // if (circle[i].getCenterY() < circle[i].getRadius() + 30)
                        // {
                        //     temp_acceleration[i][1] += + 1 / (- circle[i].getRadius() + circle[i].getCenterY());
                        // }
                    }
                }
            }
            for (int i = 0; i < circle_vec.size(); i++)
            {
                double delta_t = 0.1;
                double new_x_location = circle_vec.get(i).getCenterX() + velocity_vec_x.get(i) * delta_t + 0.5 * temp_acceleration_x.get(i) * Math.pow(delta_t, 2);
                double new_y_location = circle_vec.get(i).getCenterY() + velocity_vec_y.get(i) * delta_t + 0.5 * temp_acceleration_y.get(i) * Math.pow(delta_t, 2);
                circle_vec.get(i).setCenterX(new_x_location);
                circle_vec.get(i).setCenterY(new_y_location);
                double new_velocity_x = velocity_vec_x.get(i) + temp_acceleration_x.get(i) * delta_t;
                velocity_vec_x.setElementAt(new_velocity_x, i);
                double new_velocity_y = velocity_vec_y.get(i) + temp_acceleration_y.get(i) * delta_t;
                velocity_vec_y.setElementAt(new_velocity_y, i);
                if (circle_vec.get(i).getCenterX() > getWidth() - circle_vec.get(i).getRadius() || circle_vec.get(i).getCenterX() < circle_vec.get(i).getRadius())
                {
                    double new_velocity_x_new = velocity_vec_x.get(i) * (-1);
                    velocity_vec_x.setElementAt(new_velocity_x_new, i);
                }
                if (circle_vec.get(i).getCenterY() > getHeight() - circle_vec.get(i).getRadius() || circle_vec.get(i).getCenterY() < circle_vec.get(i).getRadius())
                {
                    double new_velocity_y_new = velocity_vec_y.get(i) * (-1);
                    velocity_vec_y.setElementAt(new_velocity_y_new, i);
                }
            }
            for (int i = 0; i < circle_vec.size() - 1; i++) {
                for (int j = i + 1; j < circle_vec.size(); j++) {
                    distance_after_for_two_pairs_vec.add(Distance(circle_vec.get(i), circle_vec.get(j)));
                }
            }
            changeColor(circle_vec);
            draw_Line(circle_vec, lines_vec, distance_after_for_two_pairs_vec);
        }
    }

    private void update_location(Vector<Double> distance_after_for_two_pairs_vec, Vector<Circle> virtual_ball_vec) {
        for(int i = 0; i < circle_vec.size(); i++)
        {
            location_before_vec_x.setElementAt(circle_vec.get(i).getCenterX(), i);
            location_before_vec_y.setElementAt(circle_vec.get(i).getCenterY(), i);
            circle_vec.get(i).setCenterX(virtual_ball_vec.get(i).getCenterX());
            circle_vec.get(i).setCenterY(virtual_ball_vec.get(i).getCenterY());
        }
        changeColor(circle_vec);
        draw_Line(circle_vec, lines_vec, distance_after_for_two_pairs_vec);
    }
}

