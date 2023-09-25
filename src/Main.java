import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
         * This will solve the following linear programming problem:
         *
         * maximize x1 + 2x2 + 3x3
         * subject to x1 + x2 + x3 <= 3
         * 2x1 + 2x2 + x3 <= 5
         * 4x1 + x2 + 2x3 <= 6
         */

        List<Double> c = Arrays.asList(1.0, 2.0, 3.0); // A vector of coefficients of objective function
        List<List<Double>> a = new ArrayList<>(Arrays.asList(
                Arrays.asList(1.0, 1.0, 1.0),
                Arrays.asList(2.0, 2.0, 1.0),
                Arrays.asList(4.0, 1.0, 2.0)
        )); // A matrix of coefficients of constraints
        List<Double> b = new ArrayList<>(Arrays.asList(3.0, 5.0, 6.0)); // A vector of right-hand sides of constraints
        double eps = 1e-6; // The approximation accuracy

        try {
            SimplexResult result = SimplexMethod.solve(c, a, b, eps);
            System.out.println("x* = " + result.getX());
            System.out.println("obj = " + result.getObj());
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
    }
}