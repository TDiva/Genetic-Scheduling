package tests;

import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import problem.Problem;
import problem.Schedule;
import approximate.ApproximateOpenShopCMax;
import core.crossover.CrossoverManager;
import core.crossover.RandomCrossover;
import core.crossover.selection.CrossoverWheel;
import core.makespan.MakespanManager;
import core.makespan.OpenShopMakespan;
import core.mutation.MutationManager;
import core.mutation.SwapMutation;
import core.selection.EliteSelection;
import core.selection.SelectionManager;
import evolution.EvolutionManager;

public class OpenShopTests {

	public static final Random r = new Random(System.currentTimeMillis());
	public static final int MAX_LENGTH = 100;

	public static final int JOBS = 4;
	public static final int MACHINES = 4;

	public static final double MUTATION = 0.5d;
	public static final int EVOLUTION_ITERATIONS = 100;
	public static final double EVOLUTION_COEFFICIENT = 0.001d;

	public static final int SIZE_OF_POPULATION = 100;

	public Problem getProblem() {
		int[][] op = new int[JOBS][MACHINES];
		for (int i = 0; i < JOBS; i++) {
			for (int j = 0; j < MACHINES; j++) {
				op[i][j] = r.nextInt(MAX_LENGTH);
			}
		}
		return new Problem(MACHINES, JOBS, op);
	}

	@Ignore
	@Test
	public void testCMax() {
		Problem p = getProblem();
		System.out.println(String.format(
				"Open Shop: m = %d, n = %d, max = %d\n", MACHINES, JOBS,
				MAX_LENGTH));
		ApproximateOpenShopCMax approximate = new ApproximateOpenShopCMax();

		long startTime = System.currentTimeMillis();
		Schedule c1 = approximate.generateSchedule(p);
		long endTime = System.currentTimeMillis();
		System.out.println(String.format("Approximate: %d \n\t time: %d mls ",
				c1.getTime(), endTime - startTime));

		MakespanManager mk = new OpenShopMakespan(p);
		CrossoverManager cr = new RandomCrossover(new CrossoverWheel());
		MutationManager mt = new SwapMutation(MUTATION);
		SelectionManager sl = new EliteSelection(mk);
		EvolutionManager evolution = new EvolutionManager(cr, mt, sl, mk);

		startTime = System.currentTimeMillis();
		Schedule c2 = evolution.generateSchedule(EVOLUTION_ITERATIONS,
				SIZE_OF_POPULATION);
		endTime = System.currentTimeMillis();
		System.out
				.println(String
						.format("Iterative evolution: %d \n\t size: %d, mutation: %.2f, iterations: %d, time: %d mls",
								c2.getTime(), SIZE_OF_POPULATION, MUTATION,
								EVOLUTION_ITERATIONS, endTime - startTime));

		startTime = System.currentTimeMillis();
		Schedule c3 = evolution.generateSchedule(EVOLUTION_COEFFICIENT,
				SIZE_OF_POPULATION);
		endTime = System.currentTimeMillis();
		System.out
				.println(String
						.format("Stohastic evolution: %d \n\t size: %d, mutation: %.2f, iterations: %d, time: %d mls",
								c3.getTime(), SIZE_OF_POPULATION, MUTATION,
								evolution.iterations, endTime - startTime));

		System.out.println(String.format(
				"Quality: \n\t iterative: %d%% \n\t stohastic: %d%%",
				c2.getTime() * 100 / c1.getTime(),
				c3.getTime() * 100 / c1.getTime()));
	}

	public static final int NUMBER_OF_TESTS = 5;

	@Test
	public void manyTests() {
		for (int JOBS = 4; JOBS <= 10; JOBS++) {
			for (int MACHINES = JOBS; MACHINES <= 10; MACHINES++) {
				System.out.println(String.format(
						"Open Shop: m = %d, n = %d, max = %d\n", MACHINES,
						JOBS, MAX_LENGTH));

				long aveTime1 = 0l;
				long aveTime2 = 0l;
				long aveTime3 = 0l;
				long evo1 = 0l;
				long evo2 = 0l;
				long it = 0l;

				for (int i = 0; i < NUMBER_OF_TESTS; i++) {
					Problem p = getProblem();

					ApproximateOpenShopCMax approximate = new ApproximateOpenShopCMax();

					long startTime = System.currentTimeMillis();
					Schedule c1 = approximate.generateSchedule(p);
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

					evo1 += (c2.getTime() * 100) / c1.getTime();
					evo2 += (c3.getTime() * 100) / c1.getTime();
					it += evolution.iterations;
					System.out.print("!");
				}

				System.out.println();
				System.out.println(String.format("Approximate: time = %d",
						aveTime1 / NUMBER_OF_TESTS));
				System.out.println(String.format(
						"Iterative evo: eff = %d%%, time = %d, it = %d ", evo1
								/ NUMBER_OF_TESTS, aveTime2 / NUMBER_OF_TESTS,
						EVOLUTION_ITERATIONS));
				System.out
						.println(String
								.format("Stohastic evo: eff = %d%%, time = %d, it = %d , cief = %.4f",
										evo2 / NUMBER_OF_TESTS, aveTime3
												/ NUMBER_OF_TESTS, it
												/ NUMBER_OF_TESTS,
										EVOLUTION_COEFFICIENT));
			}
		}
	}
}
