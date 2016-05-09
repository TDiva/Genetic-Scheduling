package algorithm.genetic.core.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algorithm.genetic.core.chromosomes.BaseChromosome;
import algorithm.genetic.core.crossover.selection.ParentingManager;
import org.javatuples.Pair;

public class HybridCrossover extends RandomCrossover implements
		CrossoverManager {

	private Random r = new Random(System.currentTimeMillis());

	public HybridCrossover(ParentingManager manager) {
		super(manager);
	}

	@Override
	protected Pair<BaseChromosome, BaseChromosome> getChildren(
			BaseChromosome p1, BaseChromosome p2, int mask) {

		if (p1.equals(p2)) {
			return new Pair<>(p1, p2);
		}

		List<Integer> c1 = new ArrayList<Integer>(p1.getGenom());
		List<Integer> c2 = new ArrayList<Integer>(p2.getGenom());

		int start = r.nextInt(p1.getLength() - 1);
		int end = start + r.nextInt(p1.getLength() - start);

		for (int i = start; i <= end; i++) {
			c1.set(i, p2.getGen(i));
			c2.set(i, p1.getGen(i));
		}

		return new Pair<>(p1.clone(c1), p2.clone(c2));
	}

}
