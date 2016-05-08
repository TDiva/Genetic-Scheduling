package problem;

import java.util.Arrays;

public class Problem {
	private int numberOfMachines;
	private int numberOfJobs;

	private int[][] operations;

	public Problem(int numberOfMachines, int numberOfJobs, int[][] operations) {
		this.numberOfMachines = numberOfMachines;
		this.numberOfJobs = numberOfJobs;
		this.operations = operations;
	}

	public Problem(int numberOfMachines, int numberOfJobs) {
		this.numberOfMachines = numberOfMachines;
		this.numberOfJobs = numberOfJobs;
		this.operations = new int[numberOfJobs][numberOfMachines];
		for (int i = 0; i < numberOfJobs; i++) {
			Arrays.fill(this.operations[i], 0);
		}
	}

	public int getNumberOfMachines() {
		return numberOfMachines;
	}

	public int getNumberOfJobs() {
		return numberOfJobs;
	}

	public int[][] getOperations() {
		return operations;
	}

	public int getOperation(int job, int machine) {
		return operations[job][machine];
	}

	public void setOperation(int job, int machine, int length) {
		operations[job][machine] = length;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("Jobs: %d\n", numberOfJobs));
		sb.append(String.format("Machines: %d\n", numberOfMachines));
		sb.append("operations:\n");
		for (int i = 0; i < numberOfJobs; i++) {
			sb.append(Arrays.toString(operations[i]) + "\n");
		}

		return sb.toString();
	}

}
