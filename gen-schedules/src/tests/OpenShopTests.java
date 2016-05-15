package tests;

import algorithm.approximate.ApproximateOpenShopCMax;
import algorithm.genetic.GeneticOpenShopCMax;
import algorithm.genetic.core.crossover.CrossoverManager;
import algorithm.genetic.core.crossover.HybridCrossover;
import algorithm.genetic.core.crossover.RandomCrossover;
import algorithm.genetic.core.crossover.selection.CrossoverWheel;
import algorithm.genetic.core.evolution.EvolutionManager;
import algorithm.genetic.core.evolution.LamarkaEvolutionManager;
import algorithm.genetic.core.makespan.HybridMakespanManager;
import algorithm.genetic.core.makespan.MakespanManager;
import algorithm.genetic.core.makespan.OpenShopMakespan;
import algorithm.genetic.core.mutation.MutationManager;
import algorithm.genetic.core.mutation.SwapMutation;
import algorithm.genetic.core.selection.EliteSelection;
import algorithm.genetic.core.selection.SelectionManager;
import algorithm.genetic.modification.LocalSearchModificationManager;
import algorithm.genetic.modification.ModificationManager;
import org.junit.Ignore;
import org.junit.Test;
import problem.Problem;
import problem.Schedule;

import java.util.Random;

public class OpenShopTests {

	public static final Random r = new Random(System.currentTimeMillis());
	public static final int MAX_LENGTH = 10;

	public static final int JOBS = 5;
	public static final int MACHINES = 3;

	public static final double MUTATION = 0.5d;
	public static final int EVOLUTION_ITERATIONS = 30;
	public static final double EVOLUTION_COEFFICIENT = 1e-3;

	public static final int NUMBER_OF_TESTS = 100;

	public static final int SIZE_OF_POPULATION = 100;

	public static Problem getProblem() {
		int[][] op = new int[JOBS][MACHINES];
		for (int i = 0; i < JOBS; i++) {
			for (int j = 0; j < MACHINES; j++) {
				op[i][j] = r.nextInt(MAX_LENGTH);
			}
		}
		return new Problem(MACHINES, JOBS, op);
	}

    public Problem getProblemFix() {
        int[][] op = new int[][]{
                {2,3,1,0,1},
                {5,0,5,6,4},
                {9,1,5,7,5},
                {8,8,2,5,6},
                {1,2,0,0,8}
        };
        return new Problem(MACHINES, JOBS, op);
    }

	@Test
	public void testGenetic() {
		Problem p = getProblem();
		System.out.println(String.format(
				"Open Shop: m = %d, n = %d, max = %d\n", MACHINES, JOBS,
				MAX_LENGTH));
        System.out.println(p);
        System.out.println("**********");
		ApproximateOpenShopCMax approximate = new ApproximateOpenShopCMax(p);

		long startTime = System.currentTimeMillis();
		Schedule c1 = approximate.generateSchedule();
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Approximate: %d \n\t time: %d mls ",
				c1.getTime(), endTime - startTime));
        System.out.println(c1);
        System.out.println("**********");

		GeneticOpenShopCMax geneticIterative = new GeneticOpenShopCMax(p,
				MUTATION,
				SIZE_OF_POPULATION,
                EVOLUTION_ITERATIONS);

		startTime = System.currentTimeMillis();
		Schedule c2 = geneticIterative.generateSchedule();
		endTime = System.currentTimeMillis();
		System.out
				.println(String
						.format("Iterative evo: %d \n\t size: %d, mutation: %.2f, iterations: %d, time: %d mls",
								c2.getTime(), SIZE_OF_POPULATION, MUTATION,
								EVOLUTION_ITERATIONS, endTime - startTime));
        System.out.println(c2);
        System.out.println("**********");

		GeneticOpenShopCMax geneticStohastic = new GeneticOpenShopCMax(p,
				MUTATION,
				SIZE_OF_POPULATION,
                EVOLUTION_COEFFICIENT);

		startTime = System.currentTimeMillis();
		Schedule c3 = geneticStohastic.generateSchedule();
		endTime = System.currentTimeMillis();
		System.out
				.println(String
						.format("Stohastic evo: %d \n\t size: %d, mutation: %.2f, iterations: %d, time: %d mls",
								c3.getTime(), SIZE_OF_POPULATION, MUTATION,
								geneticStohastic.getNumberOfIterations(), endTime - startTime));
        System.out.println(c3);
        System.out.println("**********");

        int bord = p.getLowerBorderOfSolution();

		System.out.println(String.format(
				"Quality (from lower border = %d): \n\t approx: %d%%\n\t iterative: %d%% \n\t stohastic: %d%%",
				bord,
                c1.getTime() * 100 / bord,
                (c2.getTime()) * 100 / bord,
				(c3.getTime()) * 100 / bord));
	}

	@Test
	public void testHybrid() {
		Problem p = getProblem();
		System.out.println(String.format(
				"Open Shop: m = %d, n = %d, max = %d\n", MACHINES, JOBS,
				MAX_LENGTH));
		System.out.println(p.toString());
		ApproximateOpenShopCMax approximate = new ApproximateOpenShopCMax(p);

		long startTime = System.currentTimeMillis();
		Schedule c1 = approximate.generateSchedule();
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Approximate: %d \n\t time: %d mls ",
				c1.getTime(), endTime - startTime));
		System.out.println(c1.toString());

		MakespanManager mk = new OpenShopMakespan(p);
		CrossoverManager cr = new RandomCrossover(new CrossoverWheel());
		MutationManager mt = new SwapMutation(MUTATION);
		SelectionManager sl = new EliteSelection(mk);
		ModificationManager mod = new LocalSearchModificationManager(mk);
		EvolutionManager evolution = new LamarkaEvolutionManager(cr, mt, sl,
				mk, mod);

		startTime = System.currentTimeMillis();
		Schedule c2 = evolution.generateSchedule(EVOLUTION_ITERATIONS,
				SIZE_OF_POPULATION);
		endTime = System.currentTimeMillis();
		System.out
				.println(String
						.format("Iterative hybrid: %d \n\t size: %d, mutation: %.2f, iterations: %d, time: %d mls",
								c2.getTime(), SIZE_OF_POPULATION, MUTATION,
								EVOLUTION_ITERATIONS, endTime - startTime));
		System.out.println(c2.toString());

		startTime = System.currentTimeMillis();
		Schedule c3 = evolution.generateSchedule(EVOLUTION_COEFFICIENT,
				SIZE_OF_POPULATION);
		endTime = System.currentTimeMillis();
		System.out
				.println(String
						.format("Stohastic hybrid: %d \n\t size: %d, mutation: %.2f, iterations: %d, time: %d mls",
								c3.getTime(), SIZE_OF_POPULATION, MUTATION,
								evolution.iterations, endTime - startTime));

		System.out.println(c3.toString());
		System.out.println(String.format(
				"Quality: \n\t iterative: %d%% \n\t stohastic: %d%%",
				(c1.getTime() - c2.getTime()) * 100 / c1.getTime(),
				(c1.getTime() - c3.getTime()) * 100 / c1.getTime()));

	}

	@Test
	public void testFitnessFunctionHybrid() {
		Problem p = getProblem();
		System.out.println(p);
		System.out.println(String.format(
				"Open Shop: m = %d, n = %d, max = %d\n", MACHINES, JOBS,
				MAX_LENGTH));
		ApproximateOpenShopCMax approximate = new ApproximateOpenShopCMax(p);

		long startTime = System.currentTimeMillis();
		Schedule c1 = approximate.generateSchedule();
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Approximate: %d \n\t time: %d mls ",
				c1.getTime(), endTime - startTime));

		MakespanManager mk = new HybridMakespanManager(p);
		CrossoverManager cr = new HybridCrossover(new CrossoverWheel());
		MutationManager mt = new SwapMutation(MUTATION);
		SelectionManager sl = new EliteSelection(mk);
		EvolutionManager evolution = new EvolutionManager(cr, mt, sl, mk);

		startTime = System.currentTimeMillis();
		Schedule c2 = evolution.generateSchedule(EVOLUTION_ITERATIONS,
				SIZE_OF_POPULATION);
		endTime = System.currentTimeMillis();
		System.out
				.println(String
						.format("Iterative fitness-function hybrid: %d \n\t size: %d, mutation: %.2f, iterations: %d, time: %d mls",
								c2.getTime(), SIZE_OF_POPULATION, MUTATION,
								EVOLUTION_ITERATIONS, endTime - startTime));
		System.out.println(c2);

		startTime = System.currentTimeMillis();
		Schedule c3 = evolution.generateSchedule(EVOLUTION_COEFFICIENT,
				SIZE_OF_POPULATION);
		endTime = System.currentTimeMillis();
		System.out
				.println(String
						.format("Stohastic fitness-function hybrid: %d \n\t size: %d, mutation: %.2f, iterations: %d, time: %d mls",
								c3.getTime(), SIZE_OF_POPULATION, MUTATION,
								evolution.iterations, endTime - startTime));
		System.out.println(c3);
		System.out.println(String.format(
				"Quality: \n\t iterative: %d%% \n\t stohastic: %d%%",
				(c1.getTime() - c2.getTime()) * 100 / c1.getTime(),
				(c1.getTime() - c3.getTime()) * 100 / c1.getTime()));
	}

	@Ignore
	@Test
	public void manyTests() {
		for (int JOBS = 10; JOBS <= 100; JOBS += 10) {
			for (int MACHINES = JOBS; MACHINES <= JOBS; MACHINES += 2) {
				System.out.println(String.format(
						"Open Shop: m = %d, n = %d, max = %d", MACHINES, JOBS,
						MAX_LENGTH));

				long aveTime1 = 0l;
				long aveTime2 = 0l;
				long aveTime3 = 0l;
				long aveTime4 = 0l;
				long aveTime5 = 0l;
				long aveTime6 = 0l;
				long aveTime7 = 0l;
				long evo1 = 0l;
				long evo2 = 0l;
				long hybr1 = 0l;
				long hybr2 = 0l;
				long f_hybr1 = 0l;
				long f_hybr2 = 0l;
				long it = 0l;
				long hyb_it = 0l;
				long f_hyb_it = 0l;

				for (int i = 0; i < NUMBER_OF_TESTS; i++) {
					Problem p = getProblem();

					ApproximateOpenShopCMax approximate = new ApproximateOpenShopCMax(p);

					long startTime = System.currentTimeMillis();
					Schedule c1 = approximate.generateSchedule();
					long endTime = System.currentTimeMillis();

					MakespanManager mk = new OpenShopMakespan(p);
					CrossoverManager cr = new RandomCrossover(
							new CrossoverWheel());
					MutationManager mt = new SwapMutation(MUTATION);
					SelectionManager sl = new EliteSelection(mk);
					EvolutionManager evolution = new EvolutionManager(cr, mt,
							sl, mk);
					aveTime1 += endTime - startTime;

					startTime = System.currentTimeMillis();
					Schedule c2 = evolution.generateSchedule(
							EVOLUTION_ITERATIONS, SIZE_OF_POPULATION);
					endTime = System.currentTimeMillis();
					aveTime2 += endTime - startTime;

					startTime = System.currentTimeMillis();
					Schedule c3 = evolution.generateSchedule(
							EVOLUTION_COEFFICIENT, SIZE_OF_POPULATION);
					endTime = System.currentTimeMillis();
					aveTime3 += endTime - startTime;

					ModificationManager mod = new LocalSearchModificationManager(
							mk);
					EvolutionManager hybrid = new LamarkaEvolutionManager(cr,
							mt, sl, mk, mod);
					startTime = System.currentTimeMillis();
					Schedule c4 = hybrid.generateSchedule(EVOLUTION_ITERATIONS,
							SIZE_OF_POPULATION);
					endTime = System.currentTimeMillis();
					aveTime4 += endTime - startTime;

					startTime = System.currentTimeMillis();
					Schedule c5 = hybrid.generateSchedule(
							EVOLUTION_COEFFICIENT, SIZE_OF_POPULATION);
					endTime = System.currentTimeMillis();
					aveTime5 += endTime - startTime;

					MakespanManager mk1 = new HybridMakespanManager(p);
					CrossoverManager cr1 = new HybridCrossover(
							new CrossoverWheel());
					MutationManager mt1 = new SwapMutation(MUTATION);
					SelectionManager sl1 = new EliteSelection(mk1);
					EvolutionManager evolution1 = new EvolutionManager(cr1,
							mt1, sl1, mk1);

					startTime = System.currentTimeMillis();
					Schedule c6 = evolution1.generateSchedule(
							EVOLUTION_ITERATIONS, SIZE_OF_POPULATION);
					endTime = System.currentTimeMillis();
					aveTime6 += endTime - startTime;

					startTime = System.currentTimeMillis();
					Schedule c7 = evolution1.generateSchedule(
							EVOLUTION_COEFFICIENT, SIZE_OF_POPULATION);
					endTime = System.currentTimeMillis();
					aveTime7 += endTime - startTime;

					evo1 += ((c1.getTime() - c2.getTime()) * 100)
							/ c1.getTime();
					evo2 += ((c1.getTime() - c3.getTime()) * 100)
							/ c1.getTime();

					hybr1 += ((c1.getTime() - c4.getTime()) * 100)
							/ c1.getTime();
					hybr2 += ((c1.getTime() - c5.getTime()) * 100)
							/ c1.getTime();

					f_hybr1 += ((c1.getTime() - c6.getTime()) * 100)
							/ c1.getTime();
					f_hybr2 += ((c1.getTime() - c7.getTime()) * 100)
							/ c1.getTime();

					it += evolution.iterations;
					hyb_it += hybrid.iterations;
					f_hyb_it += evolution1.iterations;
					System.out.print(".");
				}

				System.out.println(String.format("Approximate: time = %d mls",
						aveTime1 / NUMBER_OF_TESTS));
				// System.out.println(String.format(
				// "Iterative evo: eff = %d%%, time = %d mls, it = %d ",
				// evo1 / NUMBER_OF_TESTS, aveTime2 / NUMBER_OF_TESTS,
				// EVOLUTION_ITERATIONS));
				// System.out
				// .println(String
				// .format("Stohastic evo: eff = %d%%, time = %d mls, it = %d , cief = %f",
				// evo2 / NUMBER_OF_TESTS, aveTime3
				// / NUMBER_OF_TESTS, it
				// / NUMBER_OF_TESTS,
				// EVOLUTION_COEFFICIENT));
				//
				// System.out
				// .println(String
				// .format("Iterative hybrid: eff = %d%%, time = %d mls, it = %d ",
				// hybr1 / NUMBER_OF_TESTS, aveTime4
				// / NUMBER_OF_TESTS,
				// EVOLUTION_ITERATIONS));
				// System.out
				// .println(String
				// .format("Stohastic hybrid: eff = %d%%, time = %d mls, it = %d , cief = %f",
				// hybr2 / NUMBER_OF_TESTS, aveTime5
				// / NUMBER_OF_TESTS, hyb_it
				// / NUMBER_OF_TESTS,
				// EVOLUTION_COEFFICIENT));
				//
				// System.out
				// .println(String
				// .format("Iterative fitness-function hybrid: eff = %d%%, time = %d mls, it = %d ",
				// f_hybr1 / NUMBER_OF_TESTS, aveTime6
				// / NUMBER_OF_TESTS,
				// EVOLUTION_ITERATIONS));
				// System.out
				// .println(String
				// .format("Stohastic fitness-function hybrid: eff = %d%%, time = %d mls, it = %d , cief = %f",
				// f_hybr2 / NUMBER_OF_TESTS, aveTime7
				// / NUMBER_OF_TESTS, f_hyb_it
				// / NUMBER_OF_TESTS,
				// EVOLUTION_COEFFICIENT));
				//
				System.out.println();
				System.out.println("******");
				System.out.print(String.format("%d\t%d%%\t", aveTime2
						/ NUMBER_OF_TESTS, evo1 / NUMBER_OF_TESTS));
				System.out.print(String.format("%d\t%d%%\t", aveTime3
						/ NUMBER_OF_TESTS, evo2 / NUMBER_OF_TESTS));
				System.out.print(String.format("%d\t%d%%\t", aveTime4
						/ NUMBER_OF_TESTS, hybr1 / NUMBER_OF_TESTS));
				System.out.print(String.format("%d\t%d%%\t", aveTime5
						/ NUMBER_OF_TESTS, hybr2 / NUMBER_OF_TESTS));
				System.out.print(String.format("%d\t%d%%\t", aveTime6
						/ NUMBER_OF_TESTS, f_hybr1 / NUMBER_OF_TESTS));
				System.out.print(String.format("%d\t%d%%\t", aveTime7
						/ NUMBER_OF_TESTS, f_hybr2 / NUMBER_OF_TESTS));
				System.out.println();
				System.out.println("******");
				System.out.println();
				System.out.println();
			}
		}
	}
}
