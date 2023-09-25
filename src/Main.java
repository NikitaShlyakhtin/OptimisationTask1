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

        List<Double> c = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0)); // A vector of coefficients of objective function
        List<List<Double>> a = new ArrayList<>(Arrays.asList(
                Arrays.asList(1.0, 1.0, 1.0),
                Arrays.asList(2.0, 2.0, 1.0),
                Arrays.asList(4.0, 1.0, 2.0)
        )); // A matrix of coefficients of constraints
        List<Double> b = new ArrayList<>(Arrays.asList(3.0, 5.0, 6.0)); // A vector of right-hand sides of constraints
        double eps = 1e-6; // The approximation accuracy

        try {
            SimplexResult result = SimplexMethod.solve(c, a, b, eps);

            System.out.print("x* = [");
            List<Double> xValues = result.getX();
            int lastIndex = xValues.size() - 1;
            for (int i = 0; i < xValues.size(); i++) {
                System.out.print(xValues.get(i));
                if (i != lastIndex) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");


            System.out.println("obj = " + result.getObj());
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
    }
}

class SimplexResult {
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
}

class SimplexMethod {
    public static SimplexResult solve(List<Double> c, List<List<Double>> a, List<Double> b, double eps) {
        int n = c.size();
        int m = b.size();

        List<List<Double>> d = new ArrayList<>();
        for (int i = 0; i < m + 1; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < n + m + 1; j++) {
                row.add(0.0);
            }
            d.add(row);
        }

        List<Integer> basis = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                d.get(i).set(j, a.get(i).get(j));
            }
            d.get(i).set(n + i, 1.0);
            basis.add(n + i);
            d.get(i).set(n + m, b.get(i));
        }

        for (int j = 0; j < n; j++) {
            d.get(m).set(j, -c.get(j));
        }

        while (true) {
            int k = 0;
            for (int j = 1; j < n + m; j++) {
                if (d.get(m).get(j) < d.get(m).get(k)) {
                    k = j;
                }
            }
            if (d.get(m).get(k) >= -eps) {
                break;
            }

            int l = -1;
            for (int i = 0; i < m; i++) {
                if (d.get(i).get(k) > eps && (l == -1 || d.get(i).get(n + m) / d.get(i).get(k) < d.get(l).get(n + m) / d.get(l).get(k))) {
                    l = i;
                }
            }
            if (l == -1) {
                throw new ArithmeticException("Unbounded");
            }

            double t = d.get(l).get(k);
            for (int j = 0; j <= n + m; j++) {
                d.get(l).set(j, d.get(l).get(j) / t);
            }
            for (int i = 0; i <= m; i++) {
                if (i != l) {
                    t = d.get(i).get(k);
                    for (int j = 0; j <= n + m; j++) {
                        d.get(i).set(j, d.get(i).get(j) - d.get(l).get(j) * t);
                    }
                }
            }
            basis.set(l, k);
        }

        List<Double> x = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            x.add(0.0);
        }
        for (int i = 0; i < m; i++) {
            if (basis.get(i) < n) {
                x.set(basis.get(i), d.get(i).get(n + m));
            }
        }

        double obj = 0;
        for (int j = 0; j < n; j++) {
            obj += c.get(j) * x.get(j);
        }

        if (obj < -eps) {
            throw new ArithmeticException("The method is not applicable!");
        }

        return new SimplexResult(obj, x);
    }
}
