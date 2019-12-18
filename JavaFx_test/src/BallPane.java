import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;


public class BallPane extends Pane{
    private Circle[] circle;
    private Timeline animation;
    private int ball_number;
    private static double Largest_Energy = Math.pow(10, 2);
    private static double Infinity = Math.pow(10, 8);
    private Line[] lines;
    private int model;
    private double[][] location_before;
    private double[][] velocity;
    private String function;
    private int evolution_value;

    private double Force(double distance)
    {
        double delta = Math.pow(10, -7);
        // return 1.0 / 2 * ((Energy(distance + delta, this.model) - Energy(distance, this.model)) / delta + (Energy(distance, this.model) - Energy(distance - delta, this.model)) / delta);
        double energy_left = new Energy(distance - delta, this.model, this.function).Calculate_Energy();
        double energy = new Energy(distance, this.model, this.function).Calculate_Energy();
        double energy_right = new Energy(distance + delta, this.model, this.function).Calculate_Energy();
        return 1.0 / 2 * ((energy_right - energy) / delta + (energy - energy_left) / delta);
    }

    BallPane(int ball_number, int model, int evolution_value, String function)
    {
        this.function = function;
        this.evolution_value = evolution_value;
        location_before = new double[ball_number][2];
        velocity = new double[ball_number][2];
        this.ball_number = ball_number;
        this.model = model;
        circle = new Circle[ball_number];
        double radius = 10;
        for(int i = 0; i< ball_number; i++)
        {
            velocity[i][0] = Math.random();
            velocity[i][1] = Math.random();
        }
        if (this.model == 1 || this.model == 4 || this.evolution_value == 1)
        {
            for (int i = 0; i < ball_number; i++)
            {
                circle[i] = new Circle(100 + Math.random() * 500, 50 + Math.random() * 500, radius);
                circle[i].setFill(Color.rgb(255, 0, 0));
                circle[i].setStroke(Color.BLACK);
                circle[i].setOpacity(0.7);
                location_before[i][0] = circle[i].getCenterX();
                location_before[i][1] = circle[i].getCenterY();
            }
        }
        else
        {
            for (int i = 0; i < ball_number; i++)
            {
                circle[i] = new Circle(400, 300, radius);
                circle[i].setFill(Color.rgb(255, 0, 0));
                circle[i].setStroke(Color.BLACK);
                circle[i].setOpacity(0.7);
                location_before[i][0] = circle[i].getCenterX();
                location_before[i][1] = circle[i].getCenterY();
            }
        }
        lines = new Line[(ball_number * (ball_number - 1) / 2)];
        int account = 0;
        for (int i = 0; i < ball_number - 1; i++)
        {
            for(int j = i+1; j < ball_number; j++)
            {
                lines[account] = new Line(circle[i].getCenterX(), circle[i].getCenterY(), circle[j].getCenterX(), circle[j].getCenterY());
                lines[account].setStrokeWidth(2);
                lines[account].setStroke(Color.GREY);
                lines[account].setOpacity(0.5);
                getChildren().add(lines[account]);
                account += 1;
            }
        }
        for (int i = 0; i < ball_number; i++)
        {
            getChildren().add(circle[i]);
        }
        animation=new Timeline(new KeyFrame(Duration.millis(50),e -> moveBall()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void draw_Line(Circle[] circle, Line[] lines, double[] Distance_after)
    {
        int account = 0;
        for (int i = 0; i < circle.length - 1; i++)
        {
            for(int j = i+1; j < circle.length; j++)
            {
                // double force = (Energy_after[account] - Energy_previous[account]) / (Distance_after[account] - Distance_previous[account]);
                double force = Force(Distance_after[account]);
                if(force >= 0)
                {
                    lines[account].setStroke(Color.YELLOW);
                }
                else
                {
                    lines[account].setStroke(Color.RED);
                }
                if (Math.abs(force) > 1)
                {
                    force = 1;
                }
                lines[account].setStrokeWidth(15 * Math.sqrt(Math.abs(force)));
                lines[account].setStartX(circle[i].getCenterX());
                lines[account].setStartY(circle[i].getCenterY());
                lines[account].setEndX(circle[j].getCenterX());
                lines[account].setEndY(circle[j].getCenterY());
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

    private void changeColor(Circle[] ball)
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
                energy_for_one_ball += new Energy(Distance(ball[i], ball[j]), this.model, this.function).Calculate_Energy();
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
            ball[i].setFill(Color.rgb(red, 255 - red, 0));
        }
    }

    private double dot_product(Circle central, double before_x, double before_y, Circle virtual)
    {
        return (before_x - central.getCenterX()) * (virtual.getCenterY() - central.getCenterX()) + (before_y - central.getCenterY()) * (virtual.getCenterY() - central.getCenterY());
    }

    private void moveBall()
    {
        int account;
        double[] distance_after_for_two_pairs = new double[(int) (this.ball_number * (this.ball_number - 1) / 2)];
        if (this.evolution_value == 0) {
            double energy_previous = 0;
            double energy_after = 0;
            Circle[] Virtual_ball = new Circle[ball_number];
            for (int i = 0; i < ball_number - 1; i++) {
                for (int j = i + 1; j < ball_number; j++) {
                    energy_previous += new Energy(Distance(circle[i], circle[j]), this.model, this.function).Calculate_Energy();
                }
            }
            if (energy_previous > Infinity) {
                energy_previous = Infinity;
            }
            for (int i = 0; i < ball_number; i++) {
                location_before[i][0] = circle[i].getCenterX();
                location_before[i][1] = circle[i].getCenterY();
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
                    Virtual_ball[i] = new Circle(circle[i].getCenterX() + delta_x,
                            circle[i].getCenterY() + delta_y,
                            circle[i].getRadius());
                } while (Virtual_ball[i].getCenterX() > getWidth() - Virtual_ball[i].getRadius()
                        || Virtual_ball[i].getCenterX() < Virtual_ball[i].getRadius()
                        || Virtual_ball[i].getCenterY() > getHeight() - Virtual_ball[i].getRadius()
                        || Virtual_ball[i].getCenterY() < Virtual_ball[i].getRadius()
                        || (dot_product(circle[i], location_before[i][0], location_before[i][1], Virtual_ball[i]) >= 0 && (circle[i].getCenterY() != location_before[i][1] || circle[i].getCenterX() != location_before[i][0])));
            }

            account = 0;
            for (int i = 0; i < ball_number - 1; i++) {
                for (int j = i + 1; j < ball_number; j++) {
                    energy_after += new Energy(Distance(Virtual_ball[i], Virtual_ball[j]), this.model, this.function).Calculate_Energy();
                    distance_after_for_two_pairs[account] = Distance(Virtual_ball[i], Virtual_ball[j]);
                    account += 1;
                }
            }
            if (energy_after > Infinity) {
                energy_after = Infinity;
            }
            double Possibility = Math.exp(-energy_after + energy_previous);
            if (Possibility >= 1) {
                update_location(distance_after_for_two_pairs, Virtual_ball);
            } else {
                double MC_Parameter = Math.random();
                if (MC_Parameter < Possibility) {
                    update_location(distance_after_for_two_pairs, Virtual_ball);
                }
            }
        }
        else
        {
            double[][] temp_acceleration = new double[ball_number][2];
            // change the velocity of every ball via force field;
            for (int i = 0; i < ball_number; i++)
            {
                for (int j = 0; j < ball_number; j++)
                {
                    if (j != i)
                    {
                        double relative_distance_x = circle[j].getCenterX() - circle[i].getCenterX();
                        double relative_distance_y = circle[j].getCenterY() - circle[i].getCenterY();
                        double temp_single_force = Force(Distance(circle[i], circle[j]));

                        double mass = 10;
                        double damping_item = 0.001;
                        temp_acceleration[i][0] += relative_distance_x / Distance(circle[i], circle[j]) * temp_single_force / mass - damping_item * velocity[i][0];
                        if (circle[i].getCenterX() > getWidth() - circle[i].getRadius() - 30)
                        {
                            temp_acceleration[i][0] += - 1 / (getWidth() - circle[i].getRadius() - circle[i].getCenterX());
                        }
                        if (circle[i].getCenterX() < circle[i].getRadius() + 30)
                        {
                            temp_acceleration[i][0] += + 1 / (- circle[i].getRadius() + circle[i].getCenterX());
                        }

                        temp_acceleration[i][1] += relative_distance_y / Distance(circle[i], circle[j]) * temp_single_force / mass - damping_item * velocity[i][1];
                        if (circle[i].getCenterY() > getHeight() - circle[i].getRadius() - 30)
                        {
                            temp_acceleration[i][1] += - 1 / (getHeight() - circle[i].getRadius() - circle[i].getCenterY());
                        }
                        if (circle[i].getCenterY() < circle[i].getRadius() + 30)
                        {
                            temp_acceleration[i][1] += + 1 / (- circle[i].getRadius() + circle[i].getCenterY());
                        }
                    }
                }
            }
            for (int i = 0; i < ball_number; i++)
            {
                double delta_t = 0.1;
                double new_x_location = circle[i].getCenterX() + velocity[i][0] * delta_t + 0.5 * temp_acceleration[i][0] * Math.pow(delta_t, 2);
                double new_y_location = circle[i].getCenterY() + velocity[i][1] * delta_t + 0.5 * temp_acceleration[i][1] * Math.pow(delta_t, 2);
                circle[i].setCenterX(new_x_location);
                circle[i].setCenterY(new_y_location);
                velocity[i][0] += temp_acceleration[i][0] * delta_t;
                velocity[i][1] += temp_acceleration[i][1] * delta_t;
                if (circle[i].getCenterX() > getWidth() - circle[i].getRadius() || circle[i].getCenterX() < circle[i].getRadius())
                {
                    velocity[i][0] *= -1;
                }
                if (circle[i].getCenterY() > getHeight() - circle[i].getRadius() || circle[i].getCenterY() < circle[i].getRadius())
                {
                    velocity[i][1] *= -1;
                }
            }
            account = 0;
            for (int i = 0; i < ball_number - 1; i++) {
                for (int j = i + 1; j < ball_number; j++) {
                    distance_after_for_two_pairs[account] = Distance(circle[i], circle[j]);
                    account += 1;
                }
            }
            changeColor(circle);
            draw_Line(circle, lines, distance_after_for_two_pairs);
        }
    }

    private void update_location(double[] distance_after_for_two_pairs, Circle[] virtual_ball) {
        for(int i = 0; i < ball_number; i++)
        {
            location_before[i][0] = circle[i].getCenterX();
            location_before[i][1] = circle[i].getCenterY();
            circle[i].setCenterX(virtual_ball[i].getCenterX());
            circle[i].setCenterY(virtual_ball[i].getCenterY());
        }
        changeColor(circle);
        draw_Line(circle, lines, distance_after_for_two_pairs);
    }
}

