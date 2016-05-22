package algorithm.brute;

import algorithm.BaseSolver;
import algorithm.Solver;
import problem.Problem;
import problem.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDiva on 5/22/16.
 */
public class BruteOpenShop extends BaseSolver implements Solver {

    private final int maxSteps;

    public BruteOpenShop(Problem p) {
        super(p);
        maxSteps = p.getNumberOfJobs() * p.getNumberOfMachines();
    }

    private Long upperBound;
    private Schedule result;

    private void placeNext(List<Integer> list) throws CloneNotSupportedException {
        Schedule parent = getSchedule(list, problem);
        long time = parent.getTime();
        if (time>= upperBound) return;
        if (list.size() >= maxSteps) {
            if (time < upperBound) {
                upperBound = time;
                result = parent;
            }
            return;
        }

        for (int i = 0; i < maxSteps; i++) {
            if (!list.contains(i)) {
                List childList = new ArrayList<>(list);
                childList.add(i);
                placeNext(childList);
            }
        }
    }

    private Schedule getSchedule(List<Integer> s, Problem p) {
        Schedule res = new Schedule(p);
        for (Integer op : s) {
            res.simplySchedule(op);
        }
        return res;
    }

    @Override
    public Schedule generateSchedule() {
        upperBound = Long.MAX_VALUE;
        result = null;

        try {
            placeNext(new ArrayList<>());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
