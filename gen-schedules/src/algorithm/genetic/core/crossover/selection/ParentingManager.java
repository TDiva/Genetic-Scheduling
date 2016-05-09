package algorithm.genetic.core.crossover.selection;

import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;

public interface ParentingManager {

	void assignPopulation(Population p);

	BaseChromosome getParent();

	enum ParentingManagerType {
		CROSSOVER_WHEEL;
	}
}
