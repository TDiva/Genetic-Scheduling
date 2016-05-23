package tests;

import algorithm.genetic.GeneticOpenShopCMax;
import algorithm.genetic.core.crossover.CrossoverManager;
import algorithm.genetic.core.crossover.selection.ParentingManager;
import algorithm.genetic.core.makespan.MakespanManager;
import algorithm.genetic.core.mutation.MutationManager;
import algorithm.genetic.core.selection.SelectionManager;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import problem.Problem;
import problem.Schedule;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;

/**
 * Created by TDiva on 5/18/16.
 */
public class GeneticIterativeOpenShopTest {

    public static final String[] HEADERS = {
            "JOBS",             // 0
            "MACHINES",         // 1
            "MAX_VALUE",        // 2
            "POPULATION_SIZE",  // 3
            "MUTATION",         // 4
            "ESTIMATE 1",         // 5
            "ESTIMATE 10",         // 6
            "ESTIMATE 100",         // 7
            "TIME(sec)"         // 8
    };

    public static final int NUM_TESTS = 1000;
    public static final int[] NUM_JOBS = {3};
    public static final int[] NUM_MACHINES = {3};
    public static final int[] MAX_VALUES = {10, 100, 1000};
    public static final int[] POPULATION_SIZE = {20};
    public static final double[] MUTATION = {0.05};
    public static final int[] ITERATIONS = {5};

    private CSVResponseWriter writer;

    @Test
    public void test() {
        long numTests = NUM_TESTS * MAX_VALUES.length *
                ((NUM_JOBS.length + 1) * NUM_MACHINES.length) / 2 *
                MUTATION.length * POPULATION_SIZE.length * ITERATIONS.length;
        long index = 0;

        try {
            writer = new CSVResponseWriter("results/genetic/first-test-simple.csv", HEADERS, 10);
            StopWatch sw = new StopWatch();
            int iterations = ITERATIONS[0];
            Map<String, String> result = new HashMap<>();
            for (int jobs : NUM_JOBS) {
                result.put(HEADERS[0], String.valueOf(jobs));
                for (int machines : NUM_MACHINES) {
                    if (jobs > machines) continue;
                    result.put(HEADERS[1], String.valueOf(machines));
                    for (int maxVal : MAX_VALUES) {
                        result.put(HEADERS[2], String.valueOf(maxVal));
                        for (int i = 0; i < NUM_TESTS; i++) {
                            Problem p = OpenShopPeoblemGenerator.getProblem(jobs, machines, maxVal);
                            long est = p.getLowerBorderOfSolution();
                            result.put(HEADERS[7], String.valueOf(est));

                            for (int popSize : POPULATION_SIZE) {
                                result.put(HEADERS[3], String.valueOf(popSize));
                                for (double mutation : MUTATION) {
                                    result.put(HEADERS[4], String.format("%.2f", mutation));
                                    GeneticOpenShopCMax s = new GeneticOpenShopCMax(
                                            p,
                                            MakespanManager.MakespanManagerType.OPEN_SHOP_SIMPLE,
                                            ParentingManager.ParentingManagerType.CROSSOVER_WHEEL,
                                            CrossoverManager.CrossoverManagerType.RANDOM_CROSSOVER,
                                            MutationManager.MutationManagerType.SWAP_MUTATION,
                                            mutation,
                                            SelectionManager.SelectionManagerType.ELITE_SELECTION,
                                            popSize,
                                            iterations,
                                            0);
                                    sw.start();
                                    Schedule schedule = s.generateSchedule();
                                    sw.stop();

                                    long res1 = s.getBestAtIteration(1).getTime();
                                    long res10 = s.getBestAtIteration(2).getTime();
                                    long res100 = schedule.getTime();


                                    result.put(HEADERS[5], String.format("%.10f", ((double) res1) / est));
                                    result.put(HEADERS[6], String.format("%.10f", ((double) res10) / est));
                                    result.put(HEADERS[7], String.format("%.10f", ((double) res100) / est));

                                    result.put(HEADERS[8], String.format("%.2f", ((double) sw.getTime()) / 1000));
                                    writer.writeLine(result);
                                    sw.reset();

                                    index++;
                                    System.out.print("\rRunning tests: " + index + "/" + numTests);
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    @Test
    public void test1() {
        int[][] op = {
                {7,8,1},
                {5,8,8},
                {1,3,9}
        };
        Problem p = OpenShopPeoblemGenerator.getProblem(op);
        for (int i = 0; i< 1000; i++) {
            GeneticOpenShopCMax s = new GeneticOpenShopCMax(
                    p,
                    MakespanManager.MakespanManagerType.OPEN_SHOP_SIMPLE,
                    ParentingManager.ParentingManagerType.CROSSOVER_WHEEL,
                    CrossoverManager.CrossoverManagerType.RANDOM_CROSSOVER,
                    MutationManager.MutationManagerType.SWAP_MUTATION,
                    0.05,
                    SelectionManager.SelectionManagerType.ELITE_SELECTION,
                    20,
                    3,
                    0);
            System.out.println("test " + i + ":");
            Schedule sc = s.generateSchedule();
            long res1 = s.getBestAtIteration(1).getTime();
            long res2 = s.getBestAtIteration(2).getTime();
//            System.out.println(s.getBestAtIteration(1));
//            System.out.println(s.getBestAtIteration(2));
            assertFalse("test " + i + "\t" + res1 + ">=" + res2, res1 < res2);
            System.out.println("****");
        }
    }

}

