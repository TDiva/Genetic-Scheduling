package core.makespan;

import core.Population;
import core.chromosomes.BaseChromosome;

public interface MakespanManager {

	int makespan(BaseChromosome ch);
	
	Population createPopulation(int size);
}
