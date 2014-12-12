package problem;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tatiyana Domanova on 5/20/14.
 */
public class Machine {

	private int index;
	private Map<Integer, Job> schedule;

	private int time = 0;

	public Machine(int index) {
		this.index = index;
		schedule = new TreeMap<Integer, Job>();
	}

	public int getTime() {
		return time;
	}

	public int getIndex() {
		return index;
	}

	public Map<Integer, Job> getSchedule() {
		return schedule;
	}
	
	public int completedJobs() {
		return schedule.size();
	}

	public boolean completedJob(Job job) {
		return schedule.containsValue(job);
	}

	public void addJob(Job job) {
		int length = job.getOperationLength(index);
		if (length == 0) {
			job.processOperation(index, 0);
		}
		int endTime = 0;
		for (int startTime : schedule.keySet()) {
			if ((startTime - endTime) > length) {
				int time = job.findGap(endTime, startTime, length);
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

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Integer startTime : schedule.keySet()) {
			sb.append(startTime);
			sb.append(": ");
			sb.append(schedule.get(sb).getIndex());
			sb.append("; ");
		}
		return sb.toString();
	}
}
