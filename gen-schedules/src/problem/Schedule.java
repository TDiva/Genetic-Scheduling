package problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatiyana Domanova on 5/20/14.
 */
public class Schedule {

	private List<Machine> machines;
	private List<Job> jobs;

	public Schedule(Problem problem) {
		machines = new ArrayList<Machine>();
		jobs = new ArrayList<Job>();
		for (int i = 0; i < problem.getNumberOfMachines(); i++) {
			machines.add(new Machine(i));
		}
		for (int i = 0; i < problem.getNumberOfJobs(); i++) {
			List<Integer> job = new ArrayList<Integer>();
			for (int j = 0; j < problem.getNumberOfMachines(); j++) {
				job.add(problem.getOperation(i, j));
			}
			jobs.add(new Job(i, job));
		}
	}

	public List<Machine> getMachines() {
		return machines;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void schedule(int machine, int job) {
		machines.get(machine).addJob(jobs.get(job));
	}

    public void simplySchedule(int machine, int job) {
        machines.get(machine).simplyAddJob(jobs.get(job));
    }

	public long getTime() {
		long time = 0;
		for (Machine machine : machines) {
			if (time < machine.getTime()) {
				time = machine.getTime();
			}
		}
		return time;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Machine m : machines) {
			sb.append(m.getIndex());
			sb.append(": {");
			sb.append(m.toString());
			sb.append("}\n");
		}
        sb.append("CMax:\t" + getTime());
		return sb.toString();
	}
}
