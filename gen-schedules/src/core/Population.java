package core;

import java.util.ArrayList;
import java.util.List;

import core.chromosomes.BaseChromosome;

public class Population implements Cloneable {

	private int size;
	private List<BaseChromosome> individuals;

	private int oneLength;

	public Population(List<BaseChromosome> population) {
		this.individuals = new ArrayList<BaseChromosome>(population);
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

}
