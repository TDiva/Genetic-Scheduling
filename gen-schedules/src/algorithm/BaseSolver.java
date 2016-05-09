package algorithm;

import problem.Problem;
import problem.Schedule;

/**
 * Created by TDiva on 5/9/16.
 */
public abstract class BaseSolver implements Solver{

    protected Problem problem;

    public BaseSolver(Problem p) {
        this.problem = p;
    }
}
