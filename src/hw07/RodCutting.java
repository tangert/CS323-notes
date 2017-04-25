package hw07;
import java.util.*;

/**
 * Created by tylerangert on 4/5/17.
 */

public class RodCutting {

    public static void main(String[] args) {
        System.out.println("Hello hw7!");

        RodCutting rodCutter = new RodCutting();

        System.out.println("\n*******************");
        System.out.println("***EDIT DISTANCE***");
        System.out.println("*******************\n");

        ArrayList<String> resOps = new ArrayList<String>();

        String str1 = "hello";
        String str2 = "mellow";
        System.out.println("Cost of changing " + str1 + " to " + str2 + ": " +
                rodCutter.editDistance(str1,str2,1,1,1, resOps));

        for(int i = 0; i < resOps.size(); i++) {
            System.out.println("Step " + (i+1) + ": " + resOps.get(i));
        }

        resOps.clear();

        System.out.println();

        str1 = "tyler";
        str2 = "bobby";

        System.out.println("Cost of changing " + str1 + " to " + str2 + ": " +
                rodCutter.editDistance(str1,str2,1,1,1, resOps));

        for(int i = 0; i < resOps.size(); i++) {
            System.out.println("Step " + (i+1) + ": " + resOps.get(i));
        }

        resOps.clear();

        System.out.println();

        str1 = "hamburger";
        str2 = "cranberry";

        System.out.println("Cost of changing " + str1 + " to " + str2 + ": " +
                rodCutter.editDistance(str1,str2,1,1,1, resOps));

        for(int i = 0; i < resOps.size(); i++) {
            System.out.println("Step " + (i+1) + ": " + resOps.get(i));
        }


    }

    //direct recursive implementation.
    public double rodCut1(int length, List<Double> prices, List<Integer> resultCuts) {

        if (length == 0) return 0;

        ArrayList<Integer> helperCuts = new ArrayList<Integer>(length);
        double maxRev = rodCut1Aux(length, prices, helperCuts);

        int i = length;
        while (i > 0) {
            resultCuts.add(helperCuts.get(i));
            i-= helperCuts.get(i).intValue();
        }

        return maxRev;
    }

    public double rodCut1Aux(int length, List<Double> prices, List<Integer> resultCuts) {

        if (length == 0) return 0;

        double maxRev = Double.NEGATIVE_INFINITY;

        for (int i = 1; i <= length; i++) {
            maxRev = Math.max(maxRev, prices.get(i - 1) + rodCut1Aux(length - i, prices, resultCuts));
            resultCuts.add(i);
        }

        return maxRev;
    }

    //top-down recursion with memoization
    public double rodCut2(int length, List<Double> prices, List<Integer> resultCuts) {

        Double[] r = new Double[length + 1];

        for (int i = 0; i <= length; i++) {
            r[i] = Double.NEGATIVE_INFINITY;
        }

        ArrayList<Integer> helperCuts = new ArrayList<Integer>(length);
        double maxRevenue = rodCut2Aux(length, prices, r, helperCuts);

        int i = length;
        while (i > 0) {
            resultCuts.add(helperCuts.get(i));
            i-= helperCuts.get(i).intValue();
        }

        return maxRevenue;
    }

    public double rodCut2Aux(int length, List<Double> prices, Double[] r, List<Integer> helperCuts) {

        double maxRev;

        if (r[length] >= 0) {
            return r[length];
        }

        if (length == 0) {
            maxRev = 0;

        } else {
            maxRev = Double.NEGATIVE_INFINITY;

            for (int i = 1; i <= length; i++) {
                maxRev = Math.max(maxRev, prices.get(i - 1) + rodCut2Aux(length - i, prices, r, helperCuts));
                helperCuts.add(i);
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


        ArrayList<Integer> helperCuts = new ArrayList<Integer>(length);

        for (int j = 1; j <= length; j++) {

            double maxRev = Double.NEGATIVE_INFINITY;

            for (int i = 1; i <= j; i++) {
                maxRev = Math.max(maxRev, prices.get(i - 1) + r[j - i]);
                helperCuts.add(i);
            }

            r[j] = maxRev;
        }

        int i = length;
        while (i > 0) {
            resultCuts.add(helperCuts.get(i));
            i-= helperCuts.get(i).intValue();
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
                    } else if (operationIsMinimal(insertCost, replaceCost, deleteCost, costs, i, j)) {
                        operations[i - 1][j - 1] = "insert " + y.charAt(j - 1);
                        costs[i][j] = insertCost + costs[i][j - 1];

                        //replacement is minimal
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

            int above = costs[m-1][n];
            int diagonal = costs[m-1][n-1];
            int left = costs[m][n-1];
            int minOp = min(above, diagonal, left);

            if (minOp == diagonal) {

                resultOperations.add(operations[m-2][n-2]);

                //handles edge cases for unequal m x n
                if (m == 2 && n > 2) {
                    n--;
                } else if (n == 2 && m > 2) {
                    m--;
                } else {
                    m--;
                    n--;
                }

            } else if (minOp == above) {
                resultOperations.add(operations[m-2][n-1]);
                m--;
            } else {
                resultOperations.add(operations[m-1][n-2]);
                n--;
            }
        }

        //reverses to place in order
        Collections.reverse(resultOperations);

        //returns the last element
        return costs[x.length()][y.length()];
    }
}