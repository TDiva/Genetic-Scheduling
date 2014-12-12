package core.crossover.selection;

import core.Population;
import core.chromosomes.BaseChromosome;

public interface ParentingManager {
	
	void assignPopulation(Population p);
	BaseChromosome getParent(); 

}
