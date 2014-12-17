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

	private Population evolution(Population p, int size) {
		Population ch = crossoverManager.crossover(p);
		mutationManager.mutation(ch);
		p.getIndividuals().addAll(ch.getIndividuals());
		return selectionManager.selection(p, size);
	}

	public Schedule generateSchedule(int iterations, int size) {
		this.iterations = iterations;
		Population p = makespanManager.createPopulation(size);

		for (int i = 0; i < iterations; i++) {
			p = evolution(p, size);
		}

		BaseChromosome best = Collections.min(p.getIndividuals(), chComp);

		return makespanManager.translate(best);
	}

	public Schedule generateSchedule(double coef, int size) {
		int it = 0;
		Population p = makespanManager.createPopulation(size);
		BaseChromosome bestc = null, worstc = null;
		int best = 0, worst = 0;
		do {
			it++;
			p = evolution(p, size);
			bestc = Collections.min(p.getIndividuals(), chComp);
			worstc = Collections.max(p.getIndividuals(), chComp);
			best = makespanManager.makespan(bestc);
			worst = makespanManager.makespan(worstc);
		} while (((double) (worst - best)) / worst > coef);

		this.iterations = it;
		return makespanManager.translate(Collections.min(p.getIndividuals(),
				chComp));

	}
}
