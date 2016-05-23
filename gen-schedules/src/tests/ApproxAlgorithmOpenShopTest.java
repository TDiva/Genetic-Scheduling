package tests;

import algorithm.Solver;
import algorithm.approximate.ApproximateOpenShopCMax;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import problem.Problem;
import problem.Schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TDiva on 5/18/16.
 */
public class ApproxAlgorithmOpenShopTest {

    public static final String[] HEADERS = {
            "JOBS",             // 0
            "MACHINES",         // 1
            "MAX_VALUE",        // 2
            "RESULT",           // 3
            "LOW_BORDER",       // 4
            "ESTIMATE",         // 5
            "TIME(sec)"         // 6
    };

    public static final int NUM_TESTS = 100;
    public static final int[] NUM_JOBS = {3, 5, 10, 20, 50, 100};
    public static final int[] NUM_MACHINES = {3, 5, 10, 20, 50, 100};
    public static final int[] MAX_VALUES = {10000};

    private CSVResponseWriter writer;

    @Test
    public void test() {
        long numTests = NUM_TESTS * MAX_VALUES.length * ((NUM_JOBS.length + 1) * NUM_MACHINES.length) / 2;
        long index = 0;

        try {
            writer = new CSVResponseWriter("results/approx/diagIndexed.csv", HEADERS);
            StopWatch sw = new StopWatch();
            Map<String, String> result = new HashMap<>();
            for (int jobs : NUM_JOBS) {
                result.put(HEADERS[0], String.valueOf(jobs));
                for (int machines : NUM_MACHINES) {
                    if (jobs != machines) continue;
                    result.put(HEADERS[1], String.valueOf(machines));
                    int maxVal = (int) (jobs*Math.sqrt(machines));
                        result.put(HEADERS[2], String.valueOf(maxVal));
                        for (int i = 0; i < NUM_TESTS; i++) {
//                            Problem p = OpenShopPeoblemGenerator.getProblem(jobs, machines, maxVal);
                            Problem p = OpenShopPeoblemGenerator.getDiagonalSpreadedProblem(jobs, maxVal);
//                            System.out.println(p);
//                            System.out.println("******\n");
                            Solver s = new ApproximateOpenShopCMax(p);
                            sw.start();
                            Schedule schedule = s.generateSchedule();
                            sw.stop();

                            long res = schedule.getTime();
                            long est = p.getLowerBorderOfSolution();

                            result.put(HEADERS[3], String.valueOf(res));
                            result.put(HEADERS[4], String.valueOf(est));
                            result.put(HEADERS[5], String.valueOf(((double) res) / est));
                            result.put(HEADERS[6], String.format("%.2f", ((double) sw.getTime()) / 1000));

                            writer.writeLine(result);
                            sw.reset();

                            index++;
                            System.out.print("\rRunning tests: " + index + "/" + numTests);

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

