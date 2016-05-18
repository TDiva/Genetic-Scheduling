package algorithm.genetic.modification;

import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;
import algorithm.genetic.core.makespan.MakespanManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocalSearchModificationManager implements ModificationManager {

	protected final Comparator<BaseChromosome> chComp = new Comparator<BaseChromosome>() {

		@Override
		public int compare(BaseChromosome c1, BaseChromosome c2) {
			return Long.valueOf(makespanManager.makespan(c1)).compareTo(makespanManager.makespan(c2));
		}
	};

	public static class Pair {
		public int i;
		public int j;
	}

	private MakespanManager makespanManager;

	public LocalSearchModificationManager(MakespanManager makespanManager) {
		super();
		this.makespanManager = makespanManager;
	}

	public void modify(Population p) {
		for (BaseChromosome chromosome : p.getIndividuals()) {
			chromosome.setGenom(modify(chromosome).getGenom());
		}
	}

	private BaseChromosome modify(BaseChromosome ch) {
		while (true) {
			List<BaseChromosome> nb = findNeighbours(ch);
			BaseChromosome best = Collections.min(nb, chComp);
			if (makespanManager.makespan(best) >= makespanManager.makespan(ch)) {
				return best;
			}
			ch = best;
		}
	}

	private int findPM(BaseChromosome ch, int index) {
		int machine = makespanManager.getOperationMachine(ch.getGen(index));
		index--;
		while (index >= 0
				&& makespanManager.getOperationMachine(ch.getGen(index)) != machine) {
			index--;
		}
		return index;
	}

	private int findSM(BaseChromosome ch, int index) {
		int machine = makespanManager.getOperationMachine(ch.getGen(index));
		index++;
		while (index < ch.getLength()
				&& makespanManager.getOperationMachine(ch.getGen(index)) != machine) {
			index++;
		}
		return index;
	}

	private int findPJ(BaseChromosome ch, int index) {
		int job = makespanManager.getOperationJob(ch.getGen(index));
		index--;
		while (index >= 0
				&& makespanManager.getOperationJob(ch.getGen(index)) != job) {
			index--;
		}
		return index;
	}

	private int findSJ(BaseChromosome ch, int index) {
		int job = makespanManager.getOperationJob(ch.getGen(index));
		index++;
		while (index < ch.getLength()
				&& makespanManager.getOperationJob(ch.getGen(index)) != job) {
			index++;
		}
		return index;
	}

	private List<BaseChromosome> reverseM(BaseChromosome ch, int i, int j) {
		List<BaseChromosome> res = new ArrayList<>();

		BaseChromosome ch1 = ch.clone();
		ch1.swapGenes(i, j);
		res.add(ch1);

		BaseChromosome ch2 = ch.clone();
		ch2.swapGenes(i, j);
		ch2.swapGenes(findPM(ch, j), j);
		res.add(ch1);

		BaseChromosome ch3 = ch.clone();
		ch3.swapGenes(i, j);
		ch3.swapGenes(i, findSM(ch, i));
		res.add(ch3);

		BaseChromosome ch4 = ch.clone();
		ch4.swapGenes(i, j);
		ch2.swapGenes(findPM(ch, j), j);
		ch3.swapGenes(i, findSM(ch, i));
		res.add(ch4);

		return res;
	}

	private List<BaseChromosome> reverseJ(BaseChromosome ch, int i, int j) {
		List<BaseChromosome> res = new ArrayList<>();

		BaseChromosome ch1 = ch.clone();
		ch1.swapGenes(i, j);
		res.add(ch1);

		BaseChromosome ch2 = ch.clone();
		ch2.swapGenes(i, j);
		ch2.swapGenes(findPJ(ch, j), j);
		res.add(ch1);

		BaseChromosome ch3 = ch.clone();
		ch3.swapGenes(i, j);
		ch3.swapGenes(i, findSJ(ch, i));
		res.add(ch3);

		BaseChromosome ch4 = ch.clone();
		ch4.swapGenes(i, j);
		ch2.swapGenes(findPJ(ch, j), j);
		ch3.swapGenes(i, findSJ(ch, i));
		res.add(ch4);

		return res;
	}

	private List<BaseChromosome> findNeighbours(BaseChromosome chromosome) {
		List<BaseChromosome> res = new ArrayList<>();

		// Find blocks of machine operations
		for (int i = 0; i < chromosome.getLength(); i++) {
			int machine = makespanManager.getOperationMachine(chromosome
					.getGen(i));
			int pMachine = i == 0 ? -1 : makespanManager
					.getOperationMachine(chromosome.getGen(i - 1));
			int sMachine = i == chromosome.getLength() - 1 ? -1
					: makespanManager.getOperationMachine(chromosome.getGen(i));
			if (pMachine != machine && sMachine == machine) {
				for (int j = i + 1; j < chromosome.getLength(); j++) {
					if (makespanManager.getOperationMachine(chromosome
							.getGen(j)) == machine)
						res.addAll(reverseM(chromosome, i, j));
				}
			}
		}

		for (int i = 0; i < chromosome.getLength(); i++) {
			int job = makespanManager.getOperationJob(chromosome.getGen(i));
			int pJob = i == 0 ? -1 : makespanManager.getOperationJob(chromosome
					.getGen(i - 1));
			int sJob = i == chromosome.getLength() - 1 ? -1 : makespanManager
					.getOperationJob(chromosome.getGen(i));
			if (pJob == job && sJob != job) {
				for (int j = i + 1; j < chromosome.getLength(); j++) {
					if (makespanManager.getOperationJob(chromosome.getGen(j)) == job)
						res.addAll(reverseJ(chromosome, i, j));
				}
			}
		}

		return res;
	}

}
