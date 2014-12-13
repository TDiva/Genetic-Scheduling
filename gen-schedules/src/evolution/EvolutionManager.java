package evolution;

import java.util.Collections;
import java.util.Comparator;

import problem.Schedule;
import core.Population;
import core.chromosomes.BaseChromosome;
import core.crossover.CrossoverManager;
import core.makespan.MakespanManager;
import core.mutation.MutationManager;
import core.selection.SelectionManager;

public class EvolutionManager {
	
	public int iterations = 0;

	private CrossoverManager crossoverManager;
	private MutationManager mutationManager;
	private SelectionManager selectionManager;
	private MakespanManager makespanManager;

	public final Comparator<BaseChromosome> chComp = new Comparator<BaseChromosome>() {

		@Override
		public int compare(BaseChromosome c1, BaseChromosome c2) {
			return makespanManager.makespan(c1) - makespanManager.makespan(c2);
		}
	};

	public EvolutionManager(CrossoverManager cr, MutationManager mt,
			SelectionManager slt, MakespanManager pr) {
		crossoverManager = cr;
		mutationManager = mt;
		selectionManager = slt;
		makespanManager = pr;
	}

	public Schedule generateSchedule(int iterations, int size) {
		this.iterations = iterations;
		Population p = makespanManager.createPopulation(size);

		for (int i = 0; i < iterations; i++) {
			p = crossoverManager.crossover(p);
			mutationManager.mutation(p);
			p = selectionManager.selection(p, size);
		}

		BaseChromosome best = Collections.max(p.getIndividuals(), chComp);

		return makespanManager.translate(best);
	}

	public Schedule generateSchedule(int size) {
		int it = 0;
		Population p = makespanManager.createPopulation(size);
		BaseChromosome best = Collections.max(p.getIndividuals(), chComp);
		BaseChromosome oldBest;
		do {
			it++;
			oldBest = best;
			p = crossoverManager.crossover(p);
			mutationManager.mutation(p);
			p = selectionManager.selection(p, size);
			best = Collections.max(p.getIndividuals(), chComp);
		} while (!oldBest.equals(best));

		this.iterations = it;
		return makespanManager.translate(best);

	}
}
