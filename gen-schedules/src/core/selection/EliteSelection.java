package core.selection;

import java.util.Collections;
import java.util.Comparator;

import core.Population;
import core.chromosomes.BaseChromosome;
import core.makespan.MakespanManager;

public class EliteSelection implements SelectionManager {

	private MakespanManager makespanManager;

	Comparator<BaseChromosome> cmp = new Comparator<BaseChromosome>() {

		public int compare(BaseChromosome c1, BaseChromosome c2) {
			return makespanManager.makespan(c1) - makespanManager.makespan(c2);
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
