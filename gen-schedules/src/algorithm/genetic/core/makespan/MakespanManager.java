package algorithm.genetic.core.makespan;

import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;
import problem.Schedule;

public interface MakespanManager {

	long makespan(BaseChromosome ch);

	Schedule translate(BaseChromosome ch);

	Population createPopulation(int size);

	int getOperationJob(int index);

	int getOperationMachine(int index);

    enum MakespanManagerType {
        OPEN_SHOP_SIMPLE,
        OPEN_SHOP_MODIFIED
    }

}
