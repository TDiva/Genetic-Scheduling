package algorithm.approximate;

import algorithm.BaseSolver;
import algorithm.Solver;
import problem.Job;
import problem.Machine;
import problem.Problem;
import problem.Schedule;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ApproximateOpenShopCMax extends BaseSolver implements Solver {

	public ApproximateOpenShopCMax(Problem p) {
		super(p);
	}

	private boolean hasFinished(Machine m) {
		return m.completedJobs() == problem.getNumberOfJobs();
	}

	public Schedule generateSchedule() {
		Schedule schedule = new Schedule(problem);

		PriorityQueue<Machine> queue = new PriorityQueue<Machine>(
				problem.getNumberOfMachines(), new Comparator<Machine>() {

					@Override
					public int compare(Machine arg0, Machine arg1) {
						int res = Long.valueOf(arg0.getTime()).compareTo(arg1.getTime());
                        if (res == 0) {
                            return arg0.getIndex() - arg1.getIndex();
                        }
                        return res;
					}

				});

		queue.addAll(schedule.getMachines());

		while (!queue.isEmpty()) {
			Machine m = queue.remove();
			if (!hasFinished(m)) {
				Job j = findFreeJob(schedule, m);
				if (j != null) {
                    // without heuristic
					schedule.simplySchedule(m.getIndex(), j.getIndex());
				} else {
					System.out.println("!!!");
				}
				queue.add(m);
			}
		}

		return schedule;
	}

	private Job findFreeJob(Schedule sc, Machine m) {
		Job minJob = null;
		long minTime = Long.MAX_VALUE;
		for (Job j : sc.getJobs()) {
			long ft = j.getFreeTime();
			if (ft < minTime && !m.completedJob(j)) {
				minTime = ft;
				minJob = j;
			}
		}
		return minJob;
	}
}
