package core.selection;

import core.Population;

public interface SelectionManager {

	public Population selection(Population p, int size);
}
