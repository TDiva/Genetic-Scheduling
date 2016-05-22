package tests;

import algorithm.brute.BruteOpenShop;
import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;
import problem.Problem;
import problem.Schedule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TDiva on 5/18/16.
 */
public class BruteOpenShopTest {

    public static final String[] HEADERS = {
            "JOBS",             // 0
            "MACHINES",         // 1
            "OPERATIONS",       // 2
            "RESULT",           // 3
            "LOW_BORDER",       // 4
            "ESTIMATE",         // 5
            "TIME(sec)"         // 6
    };

    private CSVResponseWriter writer;

    private int MAX_TIME = 60*1000;

    private int MAX_VALUE = 10;
    private int MIN_PARAM = 1;

    @Test
    public void test() {
        try {
            writer = new CSVResponseWriter("results/brute/test10min.csv", HEADERS, 1);
            StopWatch sw = new StopWatch();
            Map<String, String> result = new HashMap<>();
            double time = 0;
            int job = MIN_PARAM;
            while (time < MAX_TIME) {
                job++;
                result.put(HEADERS[0], String.valueOf(job));
                int machine = MIN_PARAM;
                while (time < MAX_TIME && machine <= job) {
                    System.out.print("\rRunning tests: " + job + "-" + machine);
                    machine++;
                    result.put(HEADERS[1], String.valueOf(machine));
                    result.put(HEADERS[2], String.valueOf(machine*job));

                    Problem p = OpenShopPeoblemGenerator.getProblem(job, machine, MAX_VALUE);
                    BruteOpenShop solver = new BruteOpenShop(p);
                    sw.start();
                    Schedule schedule = solver.generateSchedule();
                    sw.stop();
                    time = sw.getTime();

                    result.put(HEADERS[3], String.valueOf(schedule.getTime()));
                    result.put(HEADERS[4], String.valueOf(p.getLowerBorderOfSolution()));
                    result.put(HEADERS[5], String.format("%.2f", ((double) schedule.getTime())/ p.getLowerBorderOfSolution()));
                    result.put(HEADERS[6], String.format("%.2f", time / 1000));
                    sw.reset();
                    writer.writeLine(result);
                }
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

}

