package algorithm.genetic.core.selection;

import algorithm.genetic.core.Population;

public interface SelectionManager {

	Population selection(Population p, int size);

	enum SelectionManagerType {
		ELITE_SELECTION;
	}
}
