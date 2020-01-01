class Energy {
    private double distance;
    private int model;
    private String function;
    private static double Infinity = Math.pow(10, 8);
    Energy(double distance_, int model_)
    {
        this.distance = distance_;
        this.model = model_;
    }

    Energy(double distance_, int model_, String function_)
    {
        this.distance = distance_;
        this.model = model_;
        this.function = function_;
    }

    double Calculate_Energy()
    {
        switch(this.model) {
            case 0:
                if (distance <= Math.pow(10, (-8))) {
                    return Infinity;
                }
                return 2000.0 / Math.pow(distance, 1.2);
            case 1:
                return Math.pow(distance, 0.7);
            case 2: {
                if (distance <= Math.pow(10, (-8))) {
                    return Infinity;
                }
                return 2000.0 / Math.pow(distance, 1.5) - 100.0 / Math.pow(distance, 0.5);
            }
            default:
                MathEval math = new MathEval();
                math.setVariable("x", distance);
                return math.evaluate(this.function);
        }
    }
    String Energy_express()
    {
        switch (this.model) {
            case 0:
                return "2000 / (x ^ 1.2)";
            case 1:
                return "x ^ 0.7";
            default:
                return "2000 / (x ^ 1.5) - 100 / (x ^ 0.5)";
        }
    }
}
