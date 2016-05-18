package tests;

import problem.Problem;

import java.util.Random;

/**
 * Created by TDiva on 5/18/16.
 */
public class OpenShopPeoblemGenerator {

    private static final Random r = new Random(System.currentTimeMillis());

    public static Problem getProblem(int numJobs, int numMachines, int maxLenght) {
        int[][] op = new int[numJobs][numMachines];
        for (int i = 0; i < numJobs; i++) {
            for (int j = 0; j < numMachines; j++) {
                op[i][j] = r.nextInt(maxLenght);
            }
        }
        return new Problem(numMachines, numJobs, op);
    }

    public static  Problem getProblem(int[][] operations) {
        int nJobs = operations.length;
        if (nJobs == 0) {
            throw new IllegalArgumentException("Problem should have at least 1 job!");
        }
        int nMachines = operations[0].length;
        if (nMachines == 0) {
            throw new IllegalArgumentException("Problem should have at least 1 machine!");
        }
        for (int i = 0; i<nJobs; i++) {
            if (operations[i].length != nMachines) {
                throw new IllegalArgumentException("Problem should have at equal line width!");
            }
        }
        return new Problem(nMachines, nJobs, operations);
    }

}
