package tests;

import algorithm.approximate.ApproximateOpenShopCMax;
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

/**
 * Created by TDiva on 5/18/16.
 */
public class ComparisonTest {

    public static final String[] HEADERS = {
            "JOBS",             // 0
            "MACHINES",         // 1
            "LABEL",            // 2
            "MAX_VALUE",        // 3
            "APPROX",           // 4
            "GENETIC SIMPLE",   // 5
            "GENETIC MODIFIED", // 6
    };

    public static final int NUM_TESTS = 20;
    public static final int[] NUM_JOBS = {3, 5, 10, 20, 50, 100};
    public static final int[] NUM_MACHINES = {3, 5, 10, 20, 50, 100};
    public static final int[] MAX_VALUES = {10, 100, 1000, 10000, 1000000};
    public static final int[] POPULATION_SIZE = {20};
    public static final double[] MUTATION = {0.05};
    public static final double[] COEFF = {0};
    public static final int TIMES_TO_REPEAT = 10;

    private CSVResponseWriter writer;

    @Test
    public void test() {
        long numTests = NUM_TESTS * MAX_VALUES.length *
                ((NUM_JOBS.length + 1) * NUM_MACHINES.length) / 2 *
                MUTATION.length * POPULATION_SIZE.length;
        long index = 0;

        try {
            writer = new CSVResponseWriter("results/comparison2.csv", HEADERS, 10);
            StopWatch sw = new StopWatch();
            Map<String, String> result = new HashMap<>();

            for (int jobs : NUM_JOBS) {
                result.put(HEADERS[0], String.valueOf(jobs));
                int machines = jobs;
                result.put(HEADERS[1], String.valueOf(machines));
                result.put(HEADERS[2], jobs + "x" + machines);

                for (int maxVal : MAX_VALUES) {
                    result.put(HEADERS[3], String.valueOf(maxVal));
                    for (int i = 0; i < NUM_TESTS; i++) {
                        Problem p = OpenShopPeoblemGenerator.getProblem(jobs, machines, maxVal);
                        long lBorder = p.getLowerBorderOfSolution();

                        ApproximateOpenShopCMax approx = new ApproximateOpenShopCMax(p);

                        GeneticOpenShopCMax gen1 = new GeneticOpenShopCMax(
                                p,
                                MakespanManager.MakespanManagerType.OPEN_SHOP_SIMPLE,
                                ParentingManager.ParentingManagerType.CROSSOVER_WHEEL,
                                CrossoverManager.CrossoverManagerType.RANDOM_CROSSOVER,
                                MutationManager.MutationManagerType.SWAP_MUTATION,
                                0.05,
                                SelectionManager.SelectionManagerType.ELITE_SELECTION,
                                20,
                                0,
                                0,
                                TIMES_TO_REPEAT);

                        GeneticOpenShopCMax gen2 = new GeneticOpenShopCMax(
                                p,
                                MakespanManager.MakespanManagerType.OPEN_SHOP_MODIFIED,
                                ParentingManager.ParentingManagerType.CROSSOVER_WHEEL,
                                CrossoverManager.CrossoverManagerType.RANDOM_CROSSOVER,
                                MutationManager.MutationManagerType.SWAP_MUTATION,
                                0.05,
                                SelectionManager.SelectionManagerType.ELITE_SELECTION,
                                20,
                                0,
                                0,
                                TIMES_TO_REPEAT);

                        Schedule s1 = approx.generateSchedule();
                        Schedule s2 = gen1.generateSchedule();
                        Schedule s3 = gen2.generateSchedule();

                        long val1 = s1.getTime();
                        long val2 = s2.getTime();
                        long val3 = s3.getTime();

                        long minVal = Math.min(Math.min(val1, val2), val3);

                        result.put(HEADERS[4], String.valueOf(val1 == minVal));
                        result.put(HEADERS[5], String.valueOf(val2 == minVal));
                        result.put(HEADERS[6], String.valueOf(val3 == minVal));

                        writer.writeLine(result);
                    }
                }
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

}

