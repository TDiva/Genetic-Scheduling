package algorithm.genetic.core.selection;

import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;
import algorithm.genetic.core.makespan.MakespanManager;

import java.util.Collections;
import java.util.Comparator;

public class EliteSelection implements SelectionManager {

	private MakespanManager makespanManager;

	Comparator<BaseChromosome> cmp = new Comparator<BaseChromosome>() {

		public int compare(BaseChromosome c1, BaseChromosome c2) {
			return Long.valueOf(makespanManager.makespan(c1)).compareTo(makespanManager.makespan(c2));
		}
	};

	public EliteSelection(MakespanManager makespanManager) {
		this.makespanManager = makespanManager;
	}

	@Override
	public Population selection(Population p, int size) {
		Collections.sort(p.getIndividuals(), cmp);

		return new Population(p.getIndividuals().subList(0, size));
	}

}
