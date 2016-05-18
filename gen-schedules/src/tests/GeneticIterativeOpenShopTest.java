package tests;

import algorithm.Solver;
import algorithm.genetic.GeneticOpenShopCMax;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import problem.Problem;
import problem.Schedule;

import java.util.HashMap;
import java.util.Map;

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
            "ITERATIONS",       // 5
            "RESULT",           // 6
            "LOW_BORDER",       // 7
            "ESTIMATE",         // 8
            "TIME(sec)"         // 9
    };

    public static final int NUM_TESTS = 1;
    public static final int[] NUM_JOBS = {3, 5, 10, 20, 50, 100};
    public static final int[] NUM_MACHINES = {3, 5, 10, 20, 50, 100};
    public static final int[] MAX_VALUES = {10, 100, 1000, 10000};
    public static final int[] POPULATION_SIZE = {10, 50, 100};
    public static final double[] MUTATION = {0, 0.05, 0.1, 0.3};
    public static final int[] ITERATIONS = {1, 10, 50, 100};

    private CSVResponseWriter writer;

    @Test
    public void test() {
        long numTests = NUM_TESTS  * MAX_VALUES.length *
                ((NUM_JOBS.length + 1) * NUM_MACHINES.length) / 2 *
                MUTATION.length * POPULATION_SIZE.length * ITERATIONS.length;
        long index = 0;

        try {
            writer = new CSVResponseWriter("results/genetic/single_test.csv", HEADERS);
            StopWatch sw = new StopWatch();
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

                            for (int popSize: POPULATION_SIZE) {
                                result.put(HEADERS[3], String.valueOf(popSize));
                                for (double mutation: MUTATION) {
                                    result.put(HEADERS[4], String.format("%.1f", mutation));
                                    for (int iterations: ITERATIONS) {
                                        result.put(HEADERS[5], String.valueOf(iterations));

                                        Solver s = new GeneticOpenShopCMax(p, mutation, popSize, iterations);
                                        sw.start();
                                        Schedule schedule = s.generateSchedule();
                                        sw.stop();

                                        long res = schedule.getTime();

                                        result.put(HEADERS[6], String.valueOf(res));
                                        result.put(HEADERS[8], String.valueOf(((double) res) / est));
                                        result.put(HEADERS[9], String.format("%.2f", ((double) sw.getTime()) / 1000));

                                        writer.writeLine(result);
                                        sw.reset();

                                        index ++;
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

