package algorithm.genetic.core.chromosomes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenShopScheduleChromosome extends BaseChromosome {

	public OpenShopScheduleChromosome(List<Integer> genom) {
		super(genom);
	}

	public OpenShopScheduleChromosome(int n) {
		List<Integer> genes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			genes.add(i);
		}
		Collections.shuffle(genes);
		setGenom(genes);
	}

	public OpenShopScheduleChromosome clone() {
		return new OpenShopScheduleChromosome(getGenom());
	}

	public OpenShopScheduleChromosome clone(List<Integer> genom) {
		return new OpenShopScheduleChromosome(genom);
	}

}
