package algorithm.genetic.core.evolution;

import java.util.Collections;
import java.util.Comparator;

import problem.Schedule;
import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;
import algorithm.genetic.core.crossover.CrossoverManager;
import algorithm.genetic.core.makespan.MakespanManager;
import algorithm.genetic.core.mutation.MutationManager;
import algorithm.genetic.core.selection.SelectionManager;

public class EvolutionManager {

	public int iterations = 0;

	protected CrossoverManager crossoverManager;
	protected MutationManager mutationManager;
	protected SelectionManager selectionManager;
	protected MakespanManager makespanManager;

	protected final Comparator<BaseChromosome> chComp = new Comparator<BaseChromosome>() {

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

	protected Population evolution(Population p, int size) {
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
		// System.out.println("\nBest\t" + best);

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
		BaseChromosome bestC = Collections.min(p.getIndividuals(), chComp);
		// System.out.println("\nBest\t" + bestC);
		return makespanManager.translate(bestC);

	}
}
