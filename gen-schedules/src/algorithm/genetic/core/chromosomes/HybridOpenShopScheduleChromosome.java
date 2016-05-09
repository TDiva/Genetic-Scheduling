package algorithm.genetic.core.chromosomes;

import java.util.List;

public class HybridOpenShopScheduleChromosome extends
		OpenShopScheduleChromosome {

	private int n;
	private int m;

	// gen = index of job
	public HybridOpenShopScheduleChromosome(int n, int m) {
		super(n * m);
		this.n = n;
		this.m = m;
		for (int i = 0; i < n * m; i++) {
			getGenom().set(i, getGenom().get(i) % n);
		}
	}

	public HybridOpenShopScheduleChromosome(List<Integer> genom) {
		super(genom);
	}

	@Override
	public HybridOpenShopScheduleChromosome clone() {
		return new HybridOpenShopScheduleChromosome(n, m);
	}

	@Override
	public HybridOpenShopScheduleChromosome clone(List<Integer> genom) {
		// TODO Auto-generated method stub
		return new HybridOpenShopScheduleChromosome(genom);
	}

}
