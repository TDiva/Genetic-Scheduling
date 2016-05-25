package problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Job implements Cloneable {

    List<Integer> operations;

    /*
     * Key: time Value: machine (operation) number
     */
    Map<Long, Integer> processed;
    List<Integer> processedZeroJobs;

    private int index;

    private long time = 0;

    public Job(int index, List<Integer> operations) {
        this.index = index;
        this.operations = new ArrayList<>(operations);
        processed = new TreeMap<>();
        processedZeroJobs = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public List<Integer> getOperations() {
        return operations;
    }

    public int getOperationLength(int index) {
        return operations.get(index);
    }

    public boolean isZeroOperation(int index) {
        return operations.get(index) == 0;
    }

    public void processOperation(int index, long startTime) {
        processed.put(startTime, index);
        time = Math.max(time, startTime + operations.get(index));
    }

    public void processZeroOperation(int index) {
        processedZeroJobs.add(index);
    }

    // check whether job is free for the duration in this time gap
    // and return the start time
    public long findGap(long startTime, long endTime, int length) {
        if (endTime <= startTime)
            return -1;

        for (long left : processed.keySet()) {
            long right = left + operations.get(processed.get(left));
            if (left <= startTime && right >= endTime) {
                return -1;
            } else if (left <= startTime && right > startTime) {
                startTime = right;
            } else if (left < endTime && right >= endTime) {
                endTime = left;
            } else if (left >= startTime && right <= endTime) {
                if (left - startTime > length)
                    return startTime;
                startTime = right;
            }
        }
        if (endTime - startTime > length) return startTime;
        return -1;

//		boolean buf[] = new boolean[endTime - startTime];
//		Arrays.fill(buf, true);
//		for (int left : processed.keySet()) {
//			int right = left + operations.get(processed.get(left));
//			// if (left <= startTime && right >= endTime)
//			// return -1;
//			if (left >= startTime && left < endTime) {
//				Arrays.fill(buf, left - startTime, right > endTime ? endTime
//						- startTime : right - startTime, false);
//			}
//			if (right > startTime && right <= endTime) {
//				Arrays.fill(buf, left < startTime ? 0 : left - startTime, right
//						- startTime, false);
//			}
//		}
//		int sum = 0;
//		for (int i = 0; i < buf.length; i++) {
//			if (sum == length) {
//				return i - sum + startTime;
//			}
//			if (buf[i]) {
//				sum++;
//			} else {
//				sum = 0;
//			}
//		}
//		if (sum >= length) {
//			return buf.length - sum + startTime;
//		} else {
//			return -1;
//		}

    }

    public long findGap(long startTime, int length) {
        long p = startTime;
        for (long time : processed.keySet()) {
            if (!(time < p && time + operations.get(processed.get(time)) < p)
                    && !(time > p + length && time
                    + operations.get(processed.get(time)) > p + length)) {
                p = time + operations.get(processed.get(time));
            }
        }
        return p;
    }

    public long getFreeTime() {
        return time;
    }

}
