package algorithm.genetic.core.mutation;

import algorithm.genetic.core.Population;

public interface MutationManager {

	void mutation(Population p);

	enum MutationManagerType {
		SWAP_MUTATION;
	}

}
