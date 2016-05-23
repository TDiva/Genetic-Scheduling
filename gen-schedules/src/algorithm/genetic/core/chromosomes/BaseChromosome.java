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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseChromosome that = (BaseChromosome) o;

        return genom != null ? genom.equals(that.genom) : that.genom == null;

    }

    @Override
    public int hashCode() {
        return genom != null ? genom.hashCode() : 0;
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
