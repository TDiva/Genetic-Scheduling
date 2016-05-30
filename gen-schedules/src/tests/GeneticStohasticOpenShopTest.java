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

/**
 * Created by TDiva on 5/18/16.
 */
public class GeneticStohasticOpenShopTest {

    public static final String[] HEADERS = {
            "JOBS",             // 0
            "MACHINES",         // 1
            "LABEL",            // 2
            "MAX_VALUE",        // 3
            "POPULATION_SIZE",  // 4
            "MUTATION",         // 5
            "COEFFICIENT",      // 6
            "ITERATIONS",       // 7
            "ESTIMATE",         // 8
            "TIME(sec)"         // 9
    };

    public static final int NUM_TESTS = 10;
    public static final int[] NUM_JOBS = {3, 5, 10, 20, 50, 100};
    public static final int[] NUM_MACHINES = {3, 5, 10, 20, 50, 100};
    public static final int[] MAX_VALUES = {10, 100, 1000};
    public static final int[] POPULATION_SIZE = {20};
    public static final double[] MUTATION = {0.05};
    public static final double[] COEFF = {0};
    public static final int TIMES_TO_REPEAT = 5;

    private CSVResponseWriter writer;

    @Test
    public void test() {
        long numTests = NUM_TESTS * MAX_VALUES.length *
                ((NUM_JOBS.length + 1) * NUM_MACHINES.length) / 2 *
                MUTATION.length * POPULATION_SIZE.length;
        long index = 0;

        try {
            writer = new CSVResponseWriter("results/genetic/stohastic-modified.csv", HEADERS, 10);
            StopWatch sw = new StopWatch();
            Map<String, String> result = new HashMap<>();

            for (int jobs : NUM_JOBS) {
                result.put(HEADERS[0], String.valueOf(jobs));
                for (int machines : NUM_MACHINES) {
                    if (jobs > machines) continue;
                    result.put(HEADERS[1], String.valueOf(machines));

                    for (int maxVal : MAX_VALUES) {
                        result.put(HEADERS[3], String.valueOf(maxVal));
                        for (int i = 0; i < NUM_TESTS; i++) {
                            Problem p = OpenShopPeoblemGenerator.getProblem(jobs, machines, maxVal);
                            long est = p.getLowerBorderOfSolution();

                            for (double coeff : COEFF) {
                                result.put(HEADERS[6], String.format("%.2f", (1d - coeff) * 100));
                                result.put(HEADERS[2], String.format("%dx%d", jobs, machines));

                                for (int popSize : POPULATION_SIZE) {
                                    result.put(HEADERS[4], String.valueOf(popSize));
                                    for (double mutation : MUTATION) {
                                        result.put(HEADERS[5], String.format("%.2f", mutation));
                                        GeneticOpenShopCMax s = new GeneticOpenShopCMax(
                                                p,
                                                MakespanManager.MakespanManagerType.OPEN_SHOP_MODIFIED,
                                                ParentingManager.ParentingManagerType.CROSSOVER_WHEEL,
                                                CrossoverManager.CrossoverManagerType.RANDOM_CROSSOVER,
                                                MutationManager.MutationManagerType.SWAP_MUTATION,
                                                mutation,
                                                SelectionManager.SelectionManagerType.ELITE_SELECTION,
                                                popSize,
                                                0,
                                                coeff,
                                                TIMES_TO_REPEAT);
                                        sw.start();
                                        Schedule schedule = s.generateSchedule();
                                        sw.stop();

                                        long res100 = schedule.getTime();
                                        int iter = s.getNumberOfIterations();


                                        result.put(HEADERS[8], String.format("%.10f", ((double) res100) / est));
                                        result.put(HEADERS[7], String.format("%d", iter));

                                        result.put(HEADERS[9], String.format("%.2f", ((double) sw.getTime()) / 1000));
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
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

}

