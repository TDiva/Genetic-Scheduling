package core.makespan;

import problem.Schedule;
import core.Population;
import core.chromosomes.BaseChromosome;

public interface MakespanManager {

	int makespan(BaseChromosome ch);
	
	Schedule translate(BaseChromosome ch);
	
	Population createPopulation(int size);
}
