package algorithm.genetic.core.makespan;

import problem.Schedule;
import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;

public interface MakespanManager {

	int makespan(BaseChromosome ch);

	Schedule translate(BaseChromosome ch);

	Population createPopulation(int size);

	int getOperationJob(int index);

	int getOperationMachine(int index);

}