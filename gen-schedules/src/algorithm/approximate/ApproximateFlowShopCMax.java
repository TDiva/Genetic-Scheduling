package algorithm.approximate;

import java.util.List;

import problem.Problem;
import problem.Schedule;

@Deprecated
public class ApproximateFlowShopCMax {

	private Problem problem;
	
	private Schedule generateForTwo(Problem p1) {
//		List<Integer>
		return new Schedule(p1);
	}
	
	private Schedule join(List<Schedule> list) {
		return new Schedule(problem);
	}

	public Schedule generateSchedule(Problem p) {
		problem = p;
		if (p.getNumberOfMachines() % 2 == 1) {
			int m = p.getNumberOfMachines();
			int n = p.getNumberOfJobs();
			int[][] op = p.getOperations();
			m = m + 1;
			int[][] op1 = new int[n][m];
			// TODO: check!
			for (int i = 0; i < n; i++) {
				System.arraycopy(op[i], 0, op1[i], 0, m - 1);
				op1[i][m - 1] = 0;
			}
			problem = new Problem(m, n, op1);
		}
		Schedule schedule = new Schedule(p);
		
		

		return schedule;
	}

}
