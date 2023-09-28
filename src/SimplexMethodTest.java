import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * The type Simplex method test.
 * Test cases for the problem.
 */
public class SimplexMethodTest {
    /**
     * Test of two variable problem.
     * If the optimum is 42 and values of x1 = 6.0 x2 = 4.0, the test is passed.
     */
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
        Assert.assertEquals(42.0, result.getObj(), eps); // comparing expected and received results
        Assert.assertEquals(Arrays.asList(6.0, 4.0), result.getX()); // comparing expected and received results
    }

    /**
     * Test of three variable problem.
     * If the optimum is 9 and values of x1 = 0.0 x2 = 0.0 x3 = 3.0, the test is passed.
     */
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
        Assert.assertEquals(9.0, result.getObj(), eps); // comparing expected and received results
        Assert.assertEquals(Arrays.asList(0.0, 0.0, 3.0), result.getX()); // comparing expected and received results
    }

    /**
     * Test of unbound.
     * If there is no optimum, the test is passed.
     */
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
        Assert.assertEquals("Result is unbounded", ex.getMessage()); // comparing expected and received results
    }

}
