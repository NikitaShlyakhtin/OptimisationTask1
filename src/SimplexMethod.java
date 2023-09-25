import java.util.ArrayList;
import java.util.List;

public class SimplexMethod {
    private static int n;
    private static int m;
    private static List<List<Double>> d;
    private static List<Integer> basis;
    private static double eps;

    private static void init(List<Double> c, List<List<Double>> a, List<Double> b, double eps) {
        SimplexMethod.n = c.size();
        SimplexMethod.m = b.size();
        SimplexMethod.eps = eps;
        SimplexMethod.d = new ArrayList<>();
        SimplexMethod.basis = new ArrayList<>();
        for (int i = 0; i < m + 1; i ++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < n + m + 1; j ++) {
                row.add(0.0);
            }
            SimplexMethod.d.add(row);
        }

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
    }

    private static int findIteratingIndex() {
        int iteratingIndex = 0;
        for (int j = 1; j < n + m; j++) {
            if (d.get(m).get(j) < d.get(m).get(iteratingIndex)) {
                iteratingIndex = j;
            }
        }
        return iteratingIndex;
    }

    private static int findLeavingIndex(int iteratingIndex) {
        int leavingIndex = -1;
        for (int i = 0; i < m; i++) {
            if (d.get(i).get(iteratingIndex) > eps &&
                    (leavingIndex == -1 || d.get(i).get(n + m) / d.get(i).get(iteratingIndex) < d.get(leavingIndex).get(n + m) / d.get(leavingIndex).get(iteratingIndex))) {
                leavingIndex = i;
            }
        }
        if (leavingIndex == -1) {
            throw new ArithmeticException("Unbounded");
        }
        return leavingIndex;
    }

    private static void pivoting(int iteratingIndex, int leavingIndex) {
        double t = d.get(leavingIndex).get(iteratingIndex);
        for (int j = 0; j <= n + m; j++) {
            d.get(leavingIndex).set(j, d.get(leavingIndex).get(j) / t);
        }
        for (int i = 0; i <= m; i++) {
            if (i != leavingIndex) {
                t = d.get(i).get(iteratingIndex);
                for (int j = 0; j <= n + m; j++) {
                    d.get(i).set(j, d.get(i).get(j) - d.get(leavingIndex).get(j) * t);
                }
            }
        }
        basis.set(leavingIndex, iteratingIndex);
    }

    private static void iteration() {
        while (true) {
            int iteratingIndex = findIteratingIndex();
            if (d.get(m).get(iteratingIndex) >= -eps) {
                break;
            }
            pivoting(iteratingIndex, findLeavingIndex(iteratingIndex));
        }
    }

    private static List<Double> getSolution() {
        List<Double> x = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            x.add(0.0);
        }
        for (int i = 0; i < m; i++) {
            if (basis.get(i) < n) {
                x.set(basis.get(i), d.get(i).get(n + m));
            }
        }
        return x;
    }

    private static Double calcObjectiveValue(List<Double> c, List<Double> x) {
        double obj = 0;
        for (int j = 0; j < n; j++) {
            obj += c.get(j) * x.get(j);
        }
        if (obj < -eps) {
            throw new ArithmeticException("The method is not applicable!");
        }
        return obj;
    }

    public static SimplexResult solve(List<Double> c, List<List<Double>> a, List<Double> b, double eps) {
        init(c, a, b, eps);
        iteration();
        List<Double> x = getSolution();
        double obj = calcObjectiveValue(c, x);
        return new SimplexResult(obj, x);
    }
}
