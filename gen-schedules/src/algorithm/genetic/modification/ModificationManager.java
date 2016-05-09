package algorithm.genetic.modification;

import algorithm.genetic.core.Population;

public interface ModificationManager {

	 void modify(Population p);

	enum ModificationManagerType {
		LOCAL_SEARCH_MODIFICATION;
	}
}
