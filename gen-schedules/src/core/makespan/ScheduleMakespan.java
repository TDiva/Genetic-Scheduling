package core.makespan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import problem.Problem;
import problem.Schedule;
import core.Population;
import core.chromosomes.BaseChromosome;
import core.chromosomes.ScheduleChromosome;

public class ScheduleMakespan implements MakespanManager {

	private Problem problem;

	public ScheduleMakespan(Problem p) {
		problem = p;
	}

	private Map<BaseChromosome, Integer> cache = new HashMap<>();

	private Integer getCachedScheduleTime(BaseChromosome ch) {
		if (cache.containsKey(ch)) {
			return cache.get(ch);
		}
		int val = getSchedule(ch).getTime();
		cache.put(ch, val);
		return val;
	}

	@Override
	public int makespan(BaseChromosome ch) {
		return getCachedScheduleTime(ch);
	}

	private Schedule getSchedule(BaseChromosome chromosome) {
		Schedule schedule = new Schedule(problem);
		for (int i = 0; i < chromosome.getLength(); i++) {
			int gen = chromosome.getGen(i);
			int machine = getOperationMachine(gen);
			int job = getOperationJob(gen);
			if (getOperationLength(machine, job) > 0) {
				schedule.schedule(getOperationMachine(gen), getOperationJob(gen));
			}
		}
		return schedule;
	}

	private int getOperationJob(int index) {
		return index / problem.getNumberOfMachines();
	}

	private int getOperationMachine(int index) {
		return index % problem.getNumberOfMachines();
	}

	private int getOperationLength(int machine, int job) {
		return problem.getOperation(job, machine);
	}

	private int getNumberOfOperations() {
		return problem.getNumberOfMachines() * problem.getNumberOfJobs();
	}

	@Override
	public Population createPopulation(int size) {
		List<BaseChromosome> genoms = new ArrayList<>();
		int length = getNumberOfOperations();
		for (int i = 0; i < size; i++) {
			genoms.add(new ScheduleChromosome(length));
		}
		return new Population(genoms);
	}

}
