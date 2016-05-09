package algorithm.genetic.core.crossover;

import algorithm.genetic.core.Population;

public interface CrossoverManager {

	Population crossover(Population p);

	enum CrossoverManagerType {
		RANDOM_CROSSOVER,
		HYBRID_CROSSOVER;
	}
}
