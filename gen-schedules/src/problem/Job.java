package problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Job {

	List<Integer> operations;

	/*
	 * Key: time Value: machine (operation) number
	 */
	Map<Integer, Integer> processed;

	private int index;

	public Job(int index, List<Integer> operations) {
		this.index = index;
		this.operations = new ArrayList<Integer>(operations);
		processed = new HashMap<Integer, Integer>();
	}

	public int getIndex() {
		return index;
	}

	public List<Integer> getOperations() {
		return operations;
	}

	public Map<Integer, Integer> getProcessed() {
		return processed;
	}

	public int getOperationLength(int index) {
		return operations.get(index);
	}

	public void processOperation(int index, int startTime) {
		processed.put(startTime, index);
	}

	public int findGap(int startTime, int endTime, int length) {
		if (endTime <= startTime)
			return -1;
		boolean buf[] = new boolean[endTime - startTime];
		Arrays.fill(buf, true);
		for (int left : processed.keySet()) {
			int right = left + operations.get(processed.get(left));
			if (left <= startTime && right >= endTime)
				return -1;
			if (left >= startTime && left < endTime) {
				Arrays.fill(buf, left - startTime, right > endTime ? endTime
						- startTime : right - startTime, false);
			}
			if (right > startTime && right <= endTime) {
				Arrays.fill(buf, left < startTime ? 0 : left - startTime, right
						- startTime, false);
			}
		}
		int sum = 0;
		for (int i = 0; i < buf.length; i++) {
			if (buf[i]) {
				sum++;
			} else {
				if (sum >= length) {
					return i - sum + startTime;
				} else {
					sum = 0;
				}
			}
		}
		if (sum >= length) {
			return buf.length - sum + startTime;
		} else {
			return -1;
		}

	}

	public int findGap(int startTime, int length) {
		int p = startTime;
		for (int time : processed.keySet()) {
			if (time < startTime
					&& time + operations.get(processed.get(time)) > startTime) {
				p = time + operations.get(processed.get(time));
			}
			if (time >= startTime && time < startTime + length) {
				startTime = time + operations.get(processed.get(time));
			}
		}
		return p;
	}

	public int getFreeTime() {
		int lastStart = Collections.max(processed.keySet());
		int index = processed.get(lastStart);
		int length = operations.get(index);
		return lastStart + length;
	}

}
