package algorithm.genetic.core.chromosomes;

import java.util.ArrayList;
import java.util.List;

public class BaseChromosome implements Cloneable {

	private List<Integer> genom;

	public BaseChromosome(List<Integer> genom) {
		this.genom = new ArrayList<Integer>(genom);
	}

	public BaseChromosome() {
		genom = new ArrayList<>();
	}

	public List<Integer> getGenom() {
		return genom;
	}

	public void setGenom(List<Integer> genes) {
		this.genom = genes;
	}

	public int getLength() {
		return genom.size();
	}

	public int getGen(int index) {
		return genom.get(index);
	}

	public void swapGenes(int g1, int g2) {
		int buf = genom.get(g1);
		genom.set(g1, genom.get(g2));
		genom.set(g2, buf);
	}

	public boolean equals(Object o) {
		if (o instanceof BaseChromosome) {
			return genom.equals(((BaseChromosome) o).genom);
		}
		return false;
	}

	public BaseChromosome clone() {
		return new BaseChromosome(getGenom());
	}

	public BaseChromosome clone(List<Integer> genom) {
		return new BaseChromosome(genom);
	}

	@Override
	public String toString() {
		return "BaseChromosome [genom=" + genom + "]";
	}

}
