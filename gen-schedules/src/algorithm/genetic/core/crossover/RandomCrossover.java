package algorithm.genetic.core.crossover;

import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;
import algorithm.genetic.core.crossover.selection.ParentingManager;
import org.javatuples.Pair;

import java.util.*;

public class RandomCrossover implements CrossoverManager {

	private ParentingManager parentingManager;

	private Random r = new Random(System.currentTimeMillis());

	private int getMask(int length) {
		int mask = 0;
		for (int i = 0; i < length; i++) {
			mask *= 2;
			mask += r.nextInt(2);
		}
		return mask;
	}

	public RandomCrossover(ParentingManager manager) {
		parentingManager = manager;
	}

	protected Pair<BaseChromosome, BaseChromosome> getChildren(
			BaseChromosome p1, BaseChromosome p2, int mask) {

		if (p1.equals(p2)) {
			return new Pair<>(p1.clone(), p2.clone());
		}

		List<Integer> c1 = new ArrayList<Integer>();
		List<Integer> c2 = new ArrayList<Integer>();
		for (int i = 0; i < p1.getLength(); i++) {
			if (mask % 2 == 1) {
				c1.add(p2.getGen(i));
				c2.add(p1.getGen(i));
			} else {
				c1.add(p1.getGen(i));
				c2.add(p2.getGen(i));
			}
			mask /= 2;
		}
		Set<Integer> set1 = new HashSet<>();
		Set<Integer> set2 = new HashSet<>();

		for (int i = 0; i < c1.size(); i++) {
			int c = c1.get(i);

			while (set1.contains(c)) {
				c++;
				c %= c1.size();
			}
			set1.add(c);
			c1.set(i, c);

			c = c2.get(i);

			while (set2.contains(c)) {
				c++;
				c %= c2.size();
			}
			set2.add(c);
			c2.set(i, c);
		}

		// System.out.println(p1 + "\t" + p2);
		// System.out.println(c1 + "\t" + c2);
		return new Pair<>(p1.clone(c1), p2.clone(c2));
	}

	@Override
	public Population crossover(Population p) {
		List<BaseChromosome> newIndividuals = new ArrayList<>();
		parentingManager.assignPopulation(p);
		int iterations = p.getIndividuals().size() / 2;
		for (int i = 0; i < iterations; i++) {

			BaseChromosome p1 = parentingManager.getParent();
			BaseChromosome p2 = parentingManager.getParent();
			int mask = getMask(p.getOneLength());

			Pair<BaseChromosome, BaseChromosome> ch = getChildren(p1, p2, mask);
			newIndividuals.add(ch.getValue0());
			newIndividuals.add(ch.getValue1());
		}

		return new Population(newIndividuals);
	}

}
