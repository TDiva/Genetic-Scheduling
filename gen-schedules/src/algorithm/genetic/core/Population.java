package algorithm.genetic.core;

import algorithm.genetic.core.chromosomes.BaseChromosome;
import algorithm.genetic.core.makespan.MakespanManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Population implements Cloneable {

	private int size;
	private List<BaseChromosome> individuals;

	private int oneLength;

	public Population(List<BaseChromosome> population) {
		this.individuals = new ArrayList<>(population);
		this.size = population.size();
		if (this.size > 0) {
			this.oneLength = population.get(0).getLength();
		} else {
			this.oneLength = 0;
		}
	}

	public int getOneLength() {
		return oneLength;
	}

	public List<BaseChromosome> getIndividuals() {
		return individuals;
	}

    public BaseChromosome getBest(MakespanManager makespanManager) {
        return Collections.min(individuals, (a,b) -> Long.valueOf(makespanManager.makespan(a)).compareTo(makespanManager.makespan(b)));
    }

    public long getBestValue(MakespanManager makespanManager) {
        return individuals.stream().map(i -> makespanManager.makespan(i)).min((a,b) -> a.compareTo(b)).get();
    }

}
