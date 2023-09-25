import java.util.List;

public class SimplexResult {
    private final double obj;
    private final List<Double> x;

    public SimplexResult(double obj, List<Double> x) {
        this.obj = obj;
        this.x = x;
    }

    public double getObj() {
        return obj;
    }

    public List<Double> getX() {
        return x;
    }

    public String toString() {
        return "x* = " + x + "\nobj = " + obj;
    }
}