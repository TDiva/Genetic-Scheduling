package core.crossover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.javatuples.Pair;

import core.Population;
import core.chromosomes.BaseChromosome;
import core.crossover.selection.ParentingManager;

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
			return new Pair<>(p1, p2);
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
		Set<Integer> gens1 = new HashSet<Integer>();
		Set<Integer> gens2 = new HashSet<Integer>();
		for (int i = 0; i < p1.getLength(); i++) {
			if (gens1.contains(c1.get(i))) {
				int item = (c1.get(i) + 1) % p1.getLength();
				while (gens1.contains(item)) {
					item++;
					item %= p1.getLength();
				}
				c1.set(i, item);
				gens1.add(item);
			} else {
				gens1.add(c1.get(i));
			}
			if (gens2.contains(c1.get(i))) {
				int item = (c2.get(i) + 1) % p1.getLength();
				while (gens2.contains(item)) {
					item++;
					item %= p2.getLength();
				}
				c2.set(i, item);
				gens2.add(item);
			} else {
				gens2.add(c2.get(i));
			}
		}
		return new Pair<>(p1.clone(c1), p2.clone(c2));
	}

	@Override
	public Population crossover(Population p) {
		List<BaseChromosome> newIndividuals = new ArrayList<>(
				p.getIndividuals());
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
