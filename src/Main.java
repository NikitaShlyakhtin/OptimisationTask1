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
            System.out.println("x* = " + Arrays.toString(result.getX()));
            System.out.println("obj = " + result.getObj());
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }
    }
}

class SimplexResult {
    private final double obj;
    private final double[] x;

    public SimplexResult(double obj, double[] x) {
        this.obj = obj;
        this.x = x;
    }

    public double getObj() {
        return obj;
    }

    public double[] getX() {
        return x;
    }
}

class SimplexMethod {
    public static SimplexResult solve(List<Double> c, List<List<Double>> a, List<Double> b, double eps) {
        int n = c.size();
        int m = b.size();
        double[][] d = new double[m + 1][n + m + 1];
        int[] basis = new int[m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                d[i][j] = a.get(i).get(j);
            }
            d[i][n + i] = 1;
            basis[i] = n + i;
            d[i][n + m] = b.get(i);
        }

        for (int j = 0; j < n; j++) {
            d[m][j] = -c.get(j);
        }

        while (true) {
            int k = 0;
            for (int j = 1; j < n + m; j++) {
                if (d[m][j] < d[m][k]) {
                    k = j;
                }
            }
            if (d[m][k] >= -eps) {
                break;
            }

            int l = -1;
            for (int i = 0; i < m; i++) {
                if (d[i][k] > eps && (l == -1 || d[i][n + m] / d[i][k] < d[l][n + m] / d[l][k])) {
                    l = i;
                }
            }
            if (l == -1) {
                throw new ArithmeticException("Unbounded");
            }

            double t = d[l][k];
            for (int j = 0; j <= n + m; j++) {
                d[l][j] /= t;
            }
            for (int i = 0; i <= m; i++) {
                if (i != l) {
                    t = d[i][k];
                    for (int j = 0; j <= n + m; j++) {
                        d[i][j] -= d[l][j] * t;
                    }
                }
            }
            basis[l] = k;
        }

        double[] x = new double[n];
        for (int i = 0; i < m; i++) {
            if (basis[i] < n) {
                x[basis[i]] = d[i][n + m];
            }
        }

        double obj = 0;
        for (int j = 0; j < n; j++) {
            obj += c.get(j) * x[j];
        }

        if (obj < -eps) {
            throw new ArithmeticException("The method is not applicable!");
        }

        return new SimplexResult(obj, x);
    }
}
