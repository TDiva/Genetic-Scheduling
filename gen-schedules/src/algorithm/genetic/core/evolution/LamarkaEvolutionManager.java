package algorithm.genetic.core.evolution;

import algorithm.genetic.core.Population;
import algorithm.genetic.core.crossover.CrossoverManager;
import algorithm.genetic.core.makespan.MakespanManager;
import algorithm.genetic.core.mutation.MutationManager;
import algorithm.genetic.core.selection.SelectionManager;
import algorithm.genetic.modification.ModificationManager;

public class LamarkaEvolutionManager extends EvolutionManager {

	private ModificationManager modificationManager;

	public LamarkaEvolutionManager(CrossoverManager cr, MutationManager mt,
			SelectionManager slt, MakespanManager pr, ModificationManager m) {
		super(cr, mt, slt, pr);
		modificationManager = m;
	}

	@Override
	protected Population evolution(Population p) {
		Population ch = crossoverManager.crossover(p);
		mutationManager.mutation(ch);
		modificationManager.modify(ch);
		ch.getIndividuals().addAll(p.getIndividuals());
		return selectionManager.selection(p, p.getIndividuals().size());
	}

}
