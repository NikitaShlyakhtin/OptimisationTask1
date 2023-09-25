import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SimplexMethodTest {
    @Test
    public void testTwoVariableProblem() {
        List<Double> c = Arrays.asList(5.0, 3.0);
        List<List<Double>> a = Arrays.asList(
                Arrays.asList(1.0, 2.0),
                Arrays.asList(-3.0, 1.0),
                Arrays.asList(1.0, -1.0)
        );
        List<Double> b = Arrays.asList(14.0, 0.0, 2.0);
        double eps = 1e-6;
        SimplexResult result = SimplexMethod.solve(c, a, b, eps);
        Assert.assertEquals(42.0, result.getObj(), eps);
        Assert.assertEquals(Arrays.asList(6.0, 4.0), result.getX());
    }

    @Test
    public void testThreeVariableProblem() {
        List<Double> c = Arrays.asList(1.0, 2.0, 3.0);
        List<List<Double>> a = Arrays.asList(
                Arrays.asList(1.0, 1.0, 1.0),
                Arrays.asList(2.0, 2.0, 1.0),
                Arrays.asList(4.0, 1.0, 2.0)
        );
        List<Double> b = Arrays.asList(3.0, 5.0, 6.0);
        double eps = 1e-6;
        SimplexResult result = SimplexMethod.solve(c, a, b, eps);
        Assert.assertEquals(9.0, result.getObj(), eps);
        Assert.assertEquals(Arrays.asList(0.0, 0.0, 3.0), result.getX());
    }

    @Test
    public void testUnbound() {
        List<Double> c = Arrays.asList(0.0, 2.0, 1.0);
        List<List<Double>> a = Arrays.asList(
                Arrays.asList(1.0, -1.0, 1.0),
                Arrays.asList(-2.0, 1.0, 0.0),
                Arrays.asList(0.0, 1.0, -2.0)
        );
        List<Double> b = Arrays.asList(5.0, 3.0, 5.0);
        double eps = 1e-6;
        Exception ex = Assert.assertThrows(ArithmeticException.class, () -> SimplexMethod.solve(c, a, b, eps));
        Assert.assertEquals("Unbounded", ex.getMessage());
    }

}
