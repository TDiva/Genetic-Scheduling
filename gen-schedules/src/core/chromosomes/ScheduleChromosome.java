package core.chromosomes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleChromosome extends BaseChromosome {

	public ScheduleChromosome(List<Integer> genom) {
		super(genom);
	}

	public ScheduleChromosome(int n) {
		List<Integer> genes = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			genes.add(i);
		}
		Collections.shuffle(genes);
		setGenom(genes);
	}
	
	public ScheduleChromosome clone() {
		return new ScheduleChromosome(getGenom());
	}
	
	public ScheduleChromosome clone(List<Integer> genom) {
		return new ScheduleChromosome(genom);
	}

}
