package evolution;

import core.Population;
import core.crossover.CrossoverManager;
import core.makespan.MakespanManager;
import core.mutation.MutationManager;
import core.selection.SelectionManager;

public class EvolutionManager {

	private CrossoverManager crossoverManager;
	private MutationManager mutationManager;
	private SelectionManager selectionManager;
	private MakespanManager makespanManager;

	public EvolutionManager(CrossoverManager cr, MutationManager mt,
			SelectionManager slt, MakespanManager pr) {
		crossoverManager = cr;
		mutationManager = mt;
		selectionManager = slt;
		makespanManager = pr;
	}

	public Population evolution(int iterations, int size) {
		Population p = makespanManager.createPopulation(size);

		for (int i = 0; i < iterations; i++) {
			p = crossoverManager.crossover(p);
			mutationManager.mutation(p);
			p = selectionManager.selection(p, size);
		}
		return p;
	}
}
