package problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatiyana Domanova on 5/20/14.
 */
public class Schedule implements Cloneable {

    private Problem problem;

    private List<Machine> machines;
    private List<Job> jobs;

    public Schedule(Problem problem) {
        this.problem = problem;
        machines = new ArrayList<>();
        jobs = new ArrayList<>();
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

    public void simplySchedule(int op) {
        machines.get(getOperationMachine(op)).simplyAddJob(jobs.get(getOperationJob(op)));
    }

    public boolean isProcessedOperation(int x) {
        Machine machine = machines.get(getOperationMachine(x));
        Job job = jobs.get(getOperationJob(x));
        return machine.completedJob(job);
    }

    public int getOperationJob(int index) {
        return index / problem.getNumberOfMachines();
    }

    public int getOperationMachine(int index) {
        return index % problem.getNumberOfMachines();
    }

    protected int getOperationLength(int machine, int job) {
        return problem.getOperation(job, machine);
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
        sb.append("CMax:\t" + getTime() + "\n");
        return sb.toString();
    }

}
