package algorithm.genetic.core.crossover.selection;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;

public class CrossoverWheel implements ParentingManager {

	/*
	 * Key: Right side of range Value: BaseChromosome parent
	 */
	private Map<Integer, BaseChromosome> wheel;
	private int size;

	private Random random = new Random();

	public CrossoverWheel() {
		size = 0;
		wheel = new TreeMap<>();
	}

	public void assignPopulation(Population population) {
		size = 0;
		wheel = new TreeMap<>();
		for (BaseChromosome chromosome : population.getIndividuals()) {
			size++;
			// TODO: add criteria of quality
			// size+=BaseChromosome.getValue();
			wheel.put(size, chromosome);
		}
	}

	@Override
	public BaseChromosome getParent() {
		if (size == 0)
			return null;
		int key = random.nextInt(size);
		for (Integer sector : wheel.keySet()) {
			if (sector >= key) {
				return wheel.get(sector);
			}
		}
		return null;
	}

}
