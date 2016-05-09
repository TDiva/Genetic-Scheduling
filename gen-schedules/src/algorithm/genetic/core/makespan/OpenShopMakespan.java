package algorithm.genetic.core.makespan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algorithm.genetic.core.chromosomes.OpenShopScheduleChromosome;
import problem.Problem;
import problem.Schedule;
import algorithm.genetic.core.Population;
import algorithm.genetic.core.chromosomes.BaseChromosome;

public class OpenShopMakespan implements MakespanManager {

    protected Problem problem;

    public OpenShopMakespan(Problem p) {
        problem = p;
    }

    protected Map<BaseChromosome, Integer> cache = new HashMap<>();

    protected Integer getCachedScheduleTime(BaseChromosome ch) {
        if (cache.containsKey(ch)) {
            return cache.get(ch);
        }
        int val = translate(ch).getTime();
        cache.put(ch, val);
        return val;
    }

    @Override
    public int makespan(BaseChromosome ch) {
        return getCachedScheduleTime(ch);
    }

    public Schedule translate(BaseChromosome chromosome) {
        Schedule schedule = new Schedule(problem);
        for (int i = 0; i < chromosome.getLength(); i++) {
            int gen = chromosome.getGen(i);
            int machine = getOperationMachine(gen);
            int job = getOperationJob(gen);
            schedule.schedule(machine, job);
        }
        return schedule;
    }

    public int getOperationJob(int index) {
        return index / problem.getNumberOfMachines();
    }

    public int getOperationMachine(int index) {
        return index % problem.getNumberOfMachines();
    }

    protected int getOperationLength(int machine, int job) {
        return problem.getOperation(job, machine);
    }

    protected int getNumberOfOperations() {
        return problem.getNumberOfMachines() * problem.getNumberOfJobs();
    }

    @Override
    public Population createPopulation(int size) {
        List<BaseChromosome> genoms = new ArrayList<>();
        int length = getNumberOfOperations();
        for (int i = 0; i < size; i++) {
            genoms.add(new OpenShopScheduleChromosome(length));
        }
        return new Population(genoms);
    }

}
