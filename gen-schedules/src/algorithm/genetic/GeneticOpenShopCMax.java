package algorithm.genetic;

import algorithm.BaseSolver;
import algorithm.Solver;
import algorithm.genetic.core.crossover.CrossoverManager;
import algorithm.genetic.core.crossover.HybridCrossover;
import algorithm.genetic.core.crossover.RandomCrossover;
import algorithm.genetic.core.crossover.selection.CrossoverWheel;
import algorithm.genetic.core.crossover.selection.ParentingManager;
import algorithm.genetic.core.evolution.EvolutionManager;
import algorithm.genetic.core.makespan.MakespanManager;
import algorithm.genetic.core.makespan.OpenShopMakespan;
import algorithm.genetic.core.mutation.MutationManager;
import algorithm.genetic.core.mutation.SwapMutation;
import algorithm.genetic.core.selection.EliteSelection;
import algorithm.genetic.core.selection.SelectionManager;
import problem.Problem;
import problem.Schedule;

/**
 * Created by TDiva on 5/9/16.
 */
public class GeneticOpenShopCMax extends BaseSolver implements Solver {

    private MakespanManager makespanManager;
    private ParentingManager parentingManager;
    private CrossoverManager crossoverManager;
    private MutationManager mutationManager;
    private SelectionManager selectionManager;

    private EvolutionManager evolutionManager;

    private int sizeOfPopulation;
    private int maxIterations;
    private double equalityCoeff;

    public GeneticOpenShopCMax(Problem p,
                               ParentingManager.ParentingManagerType parentingManagerType,
                               CrossoverManager.CrossoverManagerType crossoverManagerType,
                               MutationManager.MutationManagerType mutationManagerType,
                               double mutation,
                               SelectionManager.SelectionManagerType selectionManagerType,
                               int sizeOfPopulation,
                               int iterations,
                               double equalityCoeff
                               ) {
        super(p);
        makespanManager = new OpenShopMakespan(p);
        switch (parentingManagerType) {
            case CROSSOVER_WHEEL:
                this.parentingManager = new CrossoverWheel();
                break;
            default: throw new UnsupportedOperationException("Parenting manager type " + parentingManagerType.name() + " is not supported");
        }
        switch (crossoverManagerType) {
            case RANDOM_CROSSOVER:
                this.crossoverManager = new RandomCrossover(parentingManager);
                break;
            case HYBRID_CROSSOVER:
                this.crossoverManager = new HybridCrossover(parentingManager);
                break;
            default: throw new UnsupportedOperationException("Crossover manager type " + crossoverManagerType.name() + " is not supported");
        }
        switch (mutationManagerType) {
            case SWAP_MUTATION:
                this.mutationManager = new SwapMutation(mutation);
                break;
            default: throw new UnsupportedOperationException("Selection manager type " + selectionManagerType.name() + " is not supported");
        }
        switch (selectionManagerType) {
            case ELITE_SELECTION:
                this.selectionManager = new EliteSelection(makespanManager);
                break;
            default: throw new UnsupportedOperationException("Selection manager type " + selectionManagerType.name() + " is not supported");
        }
        evolutionManager = new EvolutionManager(crossoverManager, mutationManager, selectionManager, makespanManager);

        this.sizeOfPopulation = sizeOfPopulation;
        this.maxIterations = iterations;
        this.equalityCoeff = equalityCoeff;
    }

    public int getNumberOfIterations() {
        return evolutionManager.iterations;
    }

    @Override
    public Schedule generateSchedule() {
        if (maxIterations > 0) {
            return evolutionManager.generateSchedule(maxIterations, sizeOfPopulation);
        } else {
            return evolutionManager.generateSchedule(equalityCoeff, sizeOfPopulation);
        }
    }
}
