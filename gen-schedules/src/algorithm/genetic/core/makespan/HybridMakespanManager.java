package algorithm.genetic.core.makespan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import problem.Problem;
import problem.Schedule;
import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;
import algorithm.genetic.core.chromosomes.HybridOpenShopScheduleChromosome;

public class HybridMakespanManager extends OpenShopMakespan {

	// for each job: index of machine LPT
	List<Queue<Integer>> temp;

	public class Comp implements Comparator<Integer> {

		private int i;

		public Comp(int i) {
			this.i = i;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			return problem.getOperation(i, o2) - problem.getOperation(i, o1);
		}
	};

	public HybridMakespanManager(Problem p) {
		super(p);

		temp = new ArrayList<>();
		for (int i = 0; i < p.getNumberOfJobs(); i++) {
			LinkedList<Integer> list = new LinkedList<>();
			for (int j = 0; j < p.getNumberOfMachines(); j++) {
				list.add(j);
			}
			Collections.sort(list, new Comp(i));
			temp.add(list);
		}

	}

	@Override
	public Population createPopulation(int size) {
		List<BaseChromosome> genoms = new ArrayList<>();
		int n = problem.getNumberOfJobs();
		int m = problem.getNumberOfMachines();

		for (int i = 0; i < size; i++) {
			genoms.add(new HybridOpenShopScheduleChromosome(n, m));
		}
		return new Population(genoms);
	}

	public Schedule translate(BaseChromosome chromosome) {
//		System.out.println(chromosome);
		Schedule schedule = new Schedule(problem);

		List<Queue<Integer>> p = new ArrayList<>();
		for (int i = 0; i < problem.getNumberOfJobs(); i++) {
			p.add(new LinkedList<>(temp.get(i)));
		}

		for (int i = 0; i < chromosome.getLength(); i++) {
			int gen = chromosome.getGen(i);

			int job = gen;
			while (p.get(job).isEmpty()) {
				job++;
				job %= problem.getNumberOfJobs();
			}
			int machine = p.get(job).poll();

//			System.out.print(String.format("[%d, %d]\t", job, machine));
			if (getOperationLength(machine, job) > 0) {
				schedule.schedule(machine, job);
			}

		}
//		System.out.println();
		return schedule;
	}

}
