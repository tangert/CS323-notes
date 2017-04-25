package hw08;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by tylerangert on 4/18/17.
 */
public class Dynamic {

    public static void main(String[] args) {
        System.out.println("\n**************************");
        System.out.println("LONGEST COMMON SUBSEQUENCE");
        System.out.println("**************************\n");

        String word1, word2;
        word1 = "cambridge";
        word2 = "elementary";
        System.out.println("LCS between " + word1 + " and " + word2 + ": " +
                longestCommonSubsequence(word1,word2));

        word1 = "hello";
        word2 = "chatter";

        System.out.println("LCS between " + word1 + " and " + word2 + ": " +
                longestCommonSubsequence(word1,word2));

        word1 = "rocketship";
        word2 = "promoter";

        System.out.println("LCS between " + word1 + " and " + word2 + ": " +
                longestCommonSubsequence(word1,word2));

        System.out.println("\n***********");
        System.out.println("COIN CHANGE");
        System.out.println("***********\n");

        ArrayList<Integer> coins = new ArrayList<Integer>();
        coins.add(50);
        coins.add(25);
        coins.add(10);
        coins.add(5);
        coins.add(1);

        System.out.println("Coin denominations: " + coins);
        int change;

        change = 51;
        System.out.println("\n************");
        System.out.println("CHANGE: " + change);
        System.out.println("************");

        System.out.println("Exponential algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange1(change, coins));

        System.out.println("\nBottom up dynamic algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange2(change, coins));


        System.out.println("\nGreedy algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange3(change, coins));

        change = 36;
        System.out.println("\n************");
        System.out.println("CHANGE: " + change);
        System.out.println("************");

        System.out.println("Exponential algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange1(change, coins));

        System.out.println("\nBottom up dynamic algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange2(change, coins));

        System.out.println("\nGreedy algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange3(change, coins));

        change = 19;
        System.out.println("\n************");
        System.out.println("CHANGE: " + change);
        System.out.println("************");

        System.out.println("Exponential algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange1(change, coins));

        System.out.println("\nBottom up dynamic algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange2(change, coins));

        System.out.println("\nGreedy algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange3(change, coins));

        //No solution case:
        change = 0;
        System.out.println("\n************");
        System.out.println("CHANGE: " + change);
        System.out.println("************");

        System.out.println("****THIS TESTS THE NO SOLUTION CASE****");

        System.out.println("Exponential algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange1(change, coins));

        System.out.println("\nBottom up dynamic algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange2(change, coins));

        System.out.println("\nGreedy algorithm:");
        System.out.println("Min number of coins to make " + change + ": " + coinChange3(change, coins));



    }

    //bottom up LCS algorithm.
    public static String longestCommonSubsequence(String x, String y) {

        int[][] lengths = new int[x.length()+1][y.length()+1];

        for (int i = 0; i < x.length(); i++) {
            for (int j = 0; j < y.length(); j++) {
                if (x.charAt(i) == y.charAt(j)) {
                    lengths[i + 1][j + 1] = lengths[i][j] + 1;
                } else {
                    lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j], lengths[i][j + 1]);
                }
            }
        }

        StringBuilder result = new StringBuilder();

        for (int xl = x.length(), yl = y.length(); xl != 0 && yl != 0; ) {

            if (lengths[xl][yl] == lengths[xl-1][yl])
                xl--;
            else if (lengths[xl][yl] == lengths[xl][yl-1])
                yl--;
            else {
                result.append(x.charAt(xl-1));
                xl--;
                yl--;
            }
        }
        return result.reverse().toString();
    }


    //Exponentially creates the list
    public static ArrayList<Integer> coinChange1(int money, ArrayList<Integer> coins) {
        //keeps a list of all possible combinations
        ArrayList<ArrayList<Integer>> allResults = new ArrayList<ArrayList<Integer>>();
        int[] counts = new int[coins.size()];

        //this populates all possible results recursively
        coinChange1Aux(allResults,coins,counts,0,money);

        int min = Integer.MAX_VALUE;
        //this will store the result
        ArrayList<Integer> result = new ArrayList<Integer>();

        //this gets the list with the minimum number of elements
        for(ArrayList<Integer> subList: allResults) {
            if (subList.size() < min) {
                result = subList;
            }
        }

        return result;
    }
    private static void coinChange1Aux(ArrayList<ArrayList<Integer>> allResults, ArrayList<Integer> coins, int[] counts, int start, int total) {
        //base case
        if (start >= coins.size()) {
            //local list for each result
            ArrayList<Integer> subResult = new ArrayList<Integer>();

            for (int i = 0; i < coins.size(); i++) {
                for(int numCoins = 0; numCoins < counts[i]; numCoins++) {
                    //adds the appropriate number of coins
                    subResult.add(coins.get(i));
                }
            }
            allResults.add(subResult);
            return;
        }

        if (start == coins.size() - 1) {
            if (total % coins.get(start) == 0) {
                counts[start] = total / coins.get(start);
                coinChange1Aux(allResults, coins, counts, start + 1, 0);
            }
        } else {
            for (int i = 0; i <= total / coins.get(start); i++) {
                counts[start] = i;
                int newTotal = total - coins.get(start) * i;
                coinChange1Aux(allResults, coins, counts, start + 1, newTotal);
            }
        }

    }

    //Bottom up dynamic programming
    public static ArrayList<Integer> coinChange2(int money, ArrayList<Integer> coins) {

        int input[] = new int[money + 1];
        int results[] = new int[money+1];
        input[0] = 0;

        for(int i=1; i <= money; i++){
            input[i] = Integer.MAX_VALUE;
         }

        for(int j=0; j < coins.size(); j++){
            for(int i=1; i <= money; i++){
                if(i >= coins.get(j)){
                    if (input[i - coins.get(j)] + 1 < input[i]) {
                        input[i] = input[i - coins.get(j)] + 1;
                        results[i] = j;
                    }
                }
            }
        }

        ArrayList<Integer> combo = new ArrayList<Integer>();
        return getResult(results, coins, combo);
    }

    public static ArrayList<Integer> getResult(int[] input, ArrayList<Integer> coins, ArrayList<Integer> combo) {
        int start = input.length-1;
        while (start > 0) {
            int j = input[start];
            combo.add(coins.get(j));
            start -= coins.get(j);
        }
        return combo;
    }

//    //Greedy algorithm
    public static ArrayList<Integer> coinChange3(int money, ArrayList<Integer> coins) {
        ArrayList<Integer> combo = new ArrayList<Integer>();

        for(int i = 0; i < coins.size(); ++i) {
            int c = money / coins.get(i);
            if(c > 0) {
                for(int j = 0; j < c; j++) {
                    combo.add(coins.get(i));
                }
            }
            money %= coins.get(i);
        }

        return combo;
    }

}
