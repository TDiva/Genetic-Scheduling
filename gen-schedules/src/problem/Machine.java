package problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tatiyana Domanova on 5/20/14.
 */
public class Machine implements Cloneable{

	private int index;
	private Map<Long, Job> schedule;
    private List<Integer> zeroJobs;

	private long time = 0;

	public Machine(int index) {
		this.index = index;
		schedule = new TreeMap<>();
        zeroJobs = new ArrayList<>();
	}

	public long getTime() {
		return time;
	}

	public int getIndex() {
		return index;
	}

	public Map<Long, Job> getSchedule() {
		return schedule;
	}

	public int completedJobs() {
		return schedule.size() + zeroJobs.size();
	}

	public boolean completedJob(Job job) {
		return schedule.containsValue(job) || zeroJobs.contains(job.getIndex());
	}

	public void addJob(Job job) {
		int length = job.getOperationLength(index);
		if (length == 0) {
			zeroJobs.add(job.getIndex());
            job.processZeroOperation(index);
            return;
		}
		long endTime = 0;
		for (long startTime : schedule.keySet()) {
			if ((startTime - endTime) >= length) {
				long time = job.findGap(endTime, startTime, length);
				if (time >= 0) {
					schedule.put(time, job);
					job.processOperation(index, time);
					return;
				}
			}
			endTime = startTime
					+ schedule.get(startTime).getOperationLength(index);
		}
		time = job.findGap(time, length);
		schedule.put(time, job);
		job.processOperation(index, time);
		time += job.getOperationLength(index);
	}

    public void simplyAddJob(Job job) {
        int length = job.getOperationLength(index);
        if (length == 0) {
            zeroJobs.add(job.getIndex());
            job.processZeroOperation(index);
            return;
        }
//        time = job.findGap(time, length);
        time = Math.max(time, job.getFreeTime());
        schedule.put(time, job);
        job.processOperation(index, time);
        time += job.getOperationLength(index);
    }

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (long startTime : schedule.keySet()) {
			sb.append(startTime);
			sb.append(": ");
			sb.append(schedule.get(startTime).getIndex());
			sb.append(";\t");
		}
		return sb.toString();
	}


}
