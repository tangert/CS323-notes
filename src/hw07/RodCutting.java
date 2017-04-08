package hw07;
import java.util.*;

/**
 * Created by tylerangert on 4/5/17.
 */

public class RodCutting {

    public static void main(String[] args) {
        System.out.println("Hello hw7!");

        RodCutting rodCutter = new RodCutting();

        List<Double> prices = new ArrayList<Double>();
        List<Integer> results = new ArrayList<Integer>();

        //Adding the prices
        prices.add(5.0);
        prices.add(4.0);
        prices.add(3.0);
        prices.add(7.0);
        prices.add(8.0);
        prices.add(3.0);
        prices.add(2.0);
        prices.add(1.0);

        int[] prices2 = new int[prices.size()];

        for (int i = 0; i < prices.size(); i++) {
            prices2[i] = prices.get(i).intValue();
        }

        System.out.println("Rod cut 1: " + rodCutter.rodCut1(prices.size(), prices, results));
        System.out.println("Rod cut 2: " + rodCutter.rodCut2(prices.size(), prices, results));
        System.out.println("Rod cut 3: " + rodCutter.rodCut3(prices.size(), prices, results));

        ArrayList<String> resOps = new ArrayList<String>();
        System.out.println("Cost of changing davide to fossati: " + rodCutter.editDistance("davide","fossati",1,1,1, resOps));

    }

    //direct recursive implementation.
    public double rodCut1(int length, List<Double> prices, List<Integer> resultCuts) {

        if (length == 0) return 0;

        double maxRevenue = Double.NEGATIVE_INFINITY;

        for (int i = 1; i <= length; i++) {
            maxRevenue = Math.max(maxRevenue, prices.get(i - 1) + rodCut1(length - i, prices, resultCuts));
        }

        return maxRevenue;
    }

    //top-down recursion with memoization
    public double rodCut2(int length, List<Double> prices, List<Integer> resultCuts) {
        Double[] r = new Double[length + 1];

        for (int i = 0; i <= length; i++) {
            r[i] = Double.NEGATIVE_INFINITY;
        }

        return rodCut2Aux(length, prices, r);
    }

    public double rodCut2Aux(int length, List<Double> prices, Double[] r) {

        double maxRev;

        if (r[length] >= 0) {
            return r[length];
        }

        if (length == 0) {
            maxRev = 0;

        } else {
            maxRev = Double.NEGATIVE_INFINITY;

            for (int i = 1; i <= length; i++) {
                maxRev = Math.max(maxRev, prices.get(i - 1) + rodCut2Aux(length - i, prices, r));
            }
        }

        r[length] = maxRev;

        return maxRev;
    }

    //bottom-up iteration with memoization.
    public double rodCut3(int length, List<Double> prices, List<Integer> resultCuts) {

        //create an array to store all of the values
        double[] r = new double[length + 1];
        r[0] = 0.0;

        for (int j = 1; j <= length; j++) {

            double q = Double.NEGATIVE_INFINITY;

            for (int i = 1; i <= j; i++) {
                q = Math.max(q, prices.get(i - 1) + r[j - i]);
                System.out.println(q);
            }

            r[j] = q;
        }

        return r[length];
    }

    //helper: min function
    public static int min(int x, int y, int z) {
        int firstMin = Math.min(x,y);
        return Math.min(firstMin,z);
    }

    //helper: compares operation 1 to operation 2 and 3.
    public static boolean operationIsMinimal(int operation1, int operation2, int operation3, int[][] costs, int i, int j) {
        boolean case1 = operation1+costs[i-1][j] < operation2+costs[i][j-1];
        boolean case2 = operation1+costs[i-1][j] < operation3+costs[i][j-1];
        return case1 && case2;
    }

    public static int editDistance(String x,
                                   String y,
                                   int insertCost,
                                   int deleteCost,
                                   int replaceCost,
                                   List<String> resultOperations) {


        int[][] costs = new int[x.length()+1][y.length()+1];
        String[][] operations = new String[x.length()][y.length()];

        int m = x.length();
        int n = y.length();

        //Filling up the horizontal row from 0 -> x.length
        for (int i = 0; i <= m; i++) {
            costs[i][0] = i;
        }

        //Filling up the vertical column from 0 -> y.length
        for (int i = 1; i <= n; i++) {
            costs[0][i] = i;
        }

        //creates a matrix of operations to keep track of costs and operations
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {

                //If they're not the same character...
                if(x.charAt(i - 1) != y.charAt(j - 1)) {

                    //If delete is minimal
                    if (operationIsMinimal(deleteCost, insertCost, replaceCost, costs, i, j)) {
                        operations[i - 1][j - 1] = "remove " + x.charAt(i - 1);
                        costs[i][j] = deleteCost + costs[i - 1][j];

                        //If insert is minimal
                    } else if (operationIsMinimal(insertCost, deleteCost, replaceCost, costs, i, j)) {
                        operations[i - 1][j - 1] = "insert " + y.charAt(j - 1);
                        costs[i][j] = insertCost + costs[i][j - 1];

                        //If replacement is minimal
                    } else {
                        operations[i - 1][j - 1] = "replace " + x.charAt(i - 1) + " with " + y.charAt(j - 1);
                        costs[i][j] = replaceCost + costs[i - 1][j - 1];
                    }

                } else {
                    //No change in the character
                    operations[i - 1][j - 1] = "no change here!";
                    costs[i][j] = costs[i - 1][j - 1];

                }
            }
        }

        //adds the operations to resultOperations
        //starts with the last one
        resultOperations.add(operations[m-1][n-1]);

        //this travels diagonally up the 2D array to find the necessary operations
        while (m > 1 && n > 1) {

            int min = min(costs[m-1][n], costs[m-1][n-1], costs[m][n-1]);

            if (min == costs[m-1][n-1]) {
                resultOperations.add(operations[m-2][n-2]);
                if (m == 2 && n > 2) {
                    n--;
                } else if (n == 2 && m > 2) {
                    m--;
                } else {
                    m--;
                    n--;
                }
            } else if (min == costs[m-1][n]) {
                resultOperations.add(operations[m-2][n-1]);
                m--;
            } else {
                resultOperations.add(operations[m-1][n-2]);
                n--;
            }
        }

        //reverses to place in order
        Collections.reverse(resultOperations);

        for(int[] cost: costs) {
            System.out.println(Arrays.toString(cost) + "\n");
        }

        System.out.println(resultOperations);

        //returns the last element
        return costs[x.length()][y.length()];
    }
}

