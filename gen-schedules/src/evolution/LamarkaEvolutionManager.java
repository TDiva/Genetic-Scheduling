package evolution;

import modification.ModificationManager;
import core.Population;
import core.crossover.CrossoverManager;
import core.makespan.MakespanManager;
import core.mutation.MutationManager;
import core.selection.SelectionManager;

public class LamarkaEvolutionManager extends EvolutionManager {

	private ModificationManager modificationManager;

	public LamarkaEvolutionManager(CrossoverManager cr, MutationManager mt,
			SelectionManager slt, MakespanManager pr, ModificationManager m) {
		super(cr, mt, slt, pr);
		modificationManager = m;
	}

	@Override
	protected Population evolution(Population p, int size) {
		Population ch = crossoverManager.crossover(p);
		mutationManager.mutation(ch);
		modificationManager.modify(ch);
		p.getIndividuals().addAll(ch.getIndividuals());
		return selectionManager.selection(p, size);
	}

}
