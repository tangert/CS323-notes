package hw03;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by tylerangert on 2/12/17.
 */
public class Heaps {

  public static void main(String[] args) {
    System.out.println("Hello heaps!");

    //test cases for min/max heap validation.
    String[] inOrder = {"a", "b", "c", "d", "e", "f", "g", "h"};
    String[] reverseOrder = {"h", "g", "f", "e", "d", "c", "b", "a"};

    /****BASIC HEAP OPERATIONS****/
    //initial array
    System.out.println("\n***BASIC HEAP OPERATIONS***");
    System.out.println("Initial array: " + Arrays.toString(inOrder));

    //builds the max heap using the initial array and prints
    buildMaxHeap(inOrder, inOrder.length);
    System.out.println("Max heap: " + Arrays.toString(inOrder));

    //builds a min heap with the now maxHeapified array, which should return the original.
    buildMinHeap(reverseOrder, reverseOrder.length);
    System.out.println("Min heap: " + Arrays.toString(reverseOrder));

    //testing add to heap methods
    String[] addingHeap = new String[10];
    String alphabet = "abcdefghijklmnop";
    for(int i = 0; i < 6; i++) {
      addingHeap[i] = Character.toString(alphabet.charAt(i));
      dataCount++;
    }

    /****ADDING TO HEAP****/
    System.out.println("\n***ADDING TO HEAP***");
    System.out.println("Original heap for adding: " + Arrays.toString(addingHeap));
    addToHeap("h",addingHeap,dataCount);
    addToHeap("i",addingHeap,dataCount);
    addToHeap("j",addingHeap,dataCount);
    addToHeap("k",addingHeap,dataCount);
    //this will lead to overflow.
    addToHeap("l",addingHeap,dataCount);
    System.out.println(Arrays.toString(addingHeap));


    /*
    IN ORDER TO ENSURE THERE ARE NO NULL POINTER OR ARRAY OUT OF BOUNDS EXCEPTIONS,
    YOU MUST CLEAR THE VARIABLE DATACOUNT WHEN CREATING A NEW ARRAY TO PERFORM ADDING OPERATIONS ON
    SINCE THIS IS NOT OBJECT ORIENTED. WOULD BE A DIFFERENT STORY IF WE CREATED HEAPS WITH CONSTRUCTORS.
    */

    /****SORTING****/
    //HEAP SORT
    System.out.println("\n***SORTING***");
    String[] heapSortArr = {"a","b","c","d","e","f","g", "h"};
    System.out.println("\nORIGINAL Heap sort array: " + Arrays.toString(heapSortArr));
    heapSort(heapSortArr, heapSortArr.length, true);
    System.out.println("Heap sorted array (descending): " + Arrays.toString(heapSortArr));
    heapSort(heapSortArr, heapSortArr.length, false);
    System.out.println("Heap sorted array (ascending): " + Arrays.toString(heapSortArr));


    //TOP DOWN MERGE SORT
    String[] mergeSortArr = {"h", "g", "f", "e", "d", "c", "b", "a"};
    System.out.println("\nORIGINAL Merge sort array: " + Arrays.toString(mergeSortArr));

    mergeSort1(mergeSortArr, mergeSortArr.length, true);
    System.out.println("(Top-down) Merge sorted array (descending): " + Arrays.toString(mergeSortArr));

    mergeSort1(mergeSortArr, mergeSortArr.length, false);
    System.out.println("(Top-down) Merge sorted array (ascending): " + Arrays.toString(mergeSortArr));

    //BOTTOM UP MERGE SORT
    mergeSort2(mergeSortArr, mergeSortArr.length, true);
    System.out.println("(Bottom-up) Merge sorted array (descending):" + Arrays.toString(mergeSortArr));

    mergeSort2(mergeSortArr, mergeSortArr.length, false);
    System.out.println("(Bottom-up) Merge sorted array (ascending): " + Arrays.toString(mergeSortArr));

    //SELECTION SORT
    String[] selectionSortArr = {"a","b","c","d","e","f","g","h","i","j","k","l"};
    selectionSort(selectionSortArr, selectionSortArr.length, true);
    System.out.println("Selection sorted array (descending): " + Arrays.toString(selectionSortArr));
    selectionSort(selectionSortArr, selectionSortArr.length, false);
    System.out.println("Selection sorted array (ascending): " + Arrays.toString(selectionSortArr));

    System.out.println("To tree string: ");
    System.out.println(toTreeString(selectionSortArr, selectionSortArr.length));

    //Sort comparisons
    measureAllAlgs(128, 5);
  }

  public static void measureAllAlgs(int baseValue, int numberOfRepeats) {
    System.out.println("\n***SORT COMPARISONS***");
    for(int repeats = 0; repeats < numberOfRepeats; repeats++) {
      System.out.println("COMPARISON RUN: " + repeats);
      int i;
      int original = baseValue;
      System.out.println("\n**HEAP SORT**");
      for (i = 0; i < 5; i++) {
        System.out.println("Elements: " + (baseValue) + " Time: " + measureHeapSort(baseValue) + " s");
        baseValue *= 2;
      }
      baseValue = original;

      System.out.println("\n**MERGE SORT 1 (TOP-DOWN)**");
      for (i = 0; i < 5; i++) {
        System.out.println("Elements: " + (baseValue) + " Time: " + measureMergeSort1(baseValue) + " s");
        baseValue *= 2;
      }
      baseValue = original;

      System.out.println("\n**MERGE SORT 2 (BOTTOM-UP)**");
      for (i = 0; i < 5; i++) {
        System.out.println("Elements: " + (baseValue) + " Time: " + measureMergeSort2(baseValue) + " s");
        baseValue *= 2;
      }
      baseValue = original;

      System.out.println("\n**SELECTION SORT**");
      for (i = 0; i < 5; i++) {
        System.out.println("Elements: " + (baseValue) + " Time: " + measureSelectionSort(baseValue) + " s");
        baseValue *= 2;
      }
      baseValue = original;
    }
  }

  public static int dataCount = 0;

  //Heap methods
  public static String toTreeString(String[] x, int n) {
    if (n <= 1) {
      return x[0];
    }
    return recursiveTreePrint(x, n,0);
  }

  public static String recursiveTreePrint(String[] rootTree, int numElements, int startingPos) {
    if(startingPos >= numElements) {
      return "";
    }

    int left = startingPos*2+1;
    int right = startingPos*2+2;

    System.out.println(rootTree[startingPos]);

    String leftSubTree = recursiveTreePrint(rootTree, numElements, left);
    String rightSubTree = recursiveTreePrint(rootTree, numElements, right);
    return "("+ rootTree[startingPos] + leftSubTree + rightSubTree + ")";
  }

  public static int countElements(String[] data) {
    int dataCount = 0;
    for(String element: data) {
      if (element != null) {
        dataCount++;
      }
    }
    return dataCount;
  }


  public static void buildMaxHeap(String[] x, int n) {
    for (int i = n/2; i >= 0; i--) {
      maxHeapify(x, n, i);
    }
  }

  public static void maxHeapify(String[] x, int n, int i) {
    int left = getLeft(i);
    int right = getRight(i);
    int largest;

    if (left < n && compare(x[left],x[i]) > 0) {
      largest = left;
    } else {
      largest = i;
    }

    if (right < n && compare(x[right],x[largest]) > 0) {
      largest = right;
    }

    if (largest != i) {
      swap(x, i, largest);
      maxHeapify(x, n, largest);
    }
  }

  public static void buildMinHeap(String[] x, int n) {
    for(int i = n/2; i >= 0; i--) {
      minHeapify(x, n, i);
    }
  }

  public static void minHeapify(String[] x, int n, int i) {
    int left = getLeft(i);
    int right = getRight(i);
    int smallest;

    if (left < n && compare(x[left],x[i]) < 0) {
      smallest = left;
    } else {
      smallest = i;
    }

    if (right < n && compare(x[right],x[smallest]) < 0) {
      smallest = right;
    }

    if (smallest != i) {
      swap(x, i, smallest);
      minHeapify(x, n, smallest);
    }
  }

  //Add to heap
  public static boolean addToHeap(String s, String[] x, int n) {
    System.out.println("\nDatacount before adding: " + dataCount);
    if (x.length == n) {
      System.out.println("Whoops. Not enough room.");
      return false;
    }

    if (isMaxHeap(x, n-1)) {
      //since we are passing in data count that is already subtracted 1
      //we pass in n
      x[n] = s;
      maxHeapify(x, n, n-1);
    } else if (isMinHeap(x, n-1)) {
      x[n] = s;
      minHeapify(x, n, n-1);
    } else {
      buildMinHeap(x, n);
      x[n] = s;
    }
      dataCount++;
      System.out.println(Arrays.toString(x));
      System.out.println("Datacount after adding: " + dataCount);
      return true;
    }

  //Heap helper methods
  public static int compare(String string1, String string2) {
    return string1.compareTo(string2);
  }

  public static void swap(String[] data, int index1, int index2) {
    String temp = data[index1];
    data[index1] = data[index2];
    data[index2] = temp;
  }

  public static int getLeft(int root) {
    return 2*root+0;
  }

  public static int getRight(int root) {
    return 2*root+1;
  }

  public static int getParent(int root) {
    return (root)/2;
  }

  public static boolean isMinHeap(String[] heap, int dataCount) {
    //this assumes the array you're providing is already a valid heap.
    //it therefore suffices to check if the left most value to the root is less than 0.
    return compare(heap[0], heap[dataCount-1]) < 0;
  }

  public static boolean isMaxHeap(String[] heap, int dataCount) {
    return !isMinHeap(heap, dataCount);
  }

  //SORTING ALGORITHMS
  //HEAP SORT
  public static void heapSort(String[] x, int n, boolean descending) {
    int size = n;
    if (descending) {
      buildMinHeap(x,n);
      for(int i = n-1; i >= 1; i--) {
        swap(x,0,i);
        size--;
        minHeapify(x,size,0);
      }
    } else if (!descending) {
      buildMaxHeap(x,n);
      for(int i = n-1; i >= 1; i--) {
        swap(x,0,i);
        size--;
        maxHeapify(x,size,0);
      }
    }
  }

  //TOP DOWN MERGE SORT
  public static void mergeSort1(String[] x, int n, boolean descending) {

    String[] helper;
    String[] input;
    int inputLength;

      input = x;
      inputLength = input.length;
      helper = new String[inputLength];
      sort1(input, helper, 0, inputLength-1, descending);
  }

  public static void sort1(String[] toBeSorted, String[] helper, int low, int high, boolean descending) {
    if (low < high) {
      int middle = low + (high - low) / 2;
      sort1(toBeSorted, helper, low, middle, descending);
      sort1(toBeSorted, helper,middle + 1, high, descending);
      merge1(toBeSorted, helper, low, middle, high, descending);
    }
  }

  //BOTTOM UP MERGE SORT
  public static void mergeSort2(String[] x, int n, boolean descending) {
    String[] helper;
    String[] input;
    int inputLength;

    input = x;
    inputLength = input.length;
    helper = new String[inputLength];
    sort2(input, helper, descending);
  }

  public static void sort2(String[] toBeSorted, String[] helper, boolean descending) {
    int width;
    for ( width = 1; width < toBeSorted.length; width = 2*width ) {
      // Combine sections of array a of width "width"
      int i;
      for ( i = 0; i < toBeSorted.length; i+=(2*width) ) {
        int left;
        int middle;
        int right;

        left = i;
        middle = i + width;
        right  = i + 2*width;
        merge2(toBeSorted, helper, left, middle, right, descending);
      }
    }
  }

  //MERGE HELPER ALGORITHM 2

  public static void merge2(String[] toBeSorted, String[] helper, int left, int middle, int right, boolean descending) {
    int i = left;
    int j = middle;
    int k = left;

    if (descending) {
      while (i < middle || j < right) {
        if (i < middle && j < right) {
          if (compare(toBeSorted[i], toBeSorted[j]) > 0) {
            helper[k++] = toBeSorted[i++];
          } else {
            helper[k++] = toBeSorted[j++];
          }
        } else if (i == middle) {
          helper[k++] = toBeSorted[j++];
        } else if (j == right) {
          helper[k++] = toBeSorted[i++];
        }
      }
    } else if (!descending) {
      while (i < middle || j < right) {
        if (i < middle && j < right) {
          if (compare(toBeSorted[i], toBeSorted[j]) < 0) {
            helper[k++] = toBeSorted[i++];
          } else {
            helper[k++] = toBeSorted[j++];
          }
        } else if (i == middle) {
          helper[k++] = toBeSorted[j++];
        } else if (j == right) {
          helper[k++] = toBeSorted[i++];
        }
      }
    }

    for (i = left; i < right; i++) {
      toBeSorted[i] = helper[i];
    }
  }
  //MERGE HELPER ALGORITHM 1
  public static void merge1(String[] toBeSorted, String[] helper, int low, int middle, int high, boolean descending) {
    for (int i = low; i <= high; i++) helper[i] = toBeSorted[i];

    int i = low;
    int j = middle + 1;
    int k = low;

    //copy values
    if (descending) {
      while (i <= middle && j <= high) {
        if (compare(helper[i], helper[j]) > 0) {
          toBeSorted[k] = helper[i];
          i++;
        } else {
          toBeSorted[k] = helper[j];
          j++;
        }
        k++;
      }
    } else if (!descending) {
      while (i <= middle && j <= high) {
        if (compare(helper[i], helper[j]) <= 0) {
          toBeSorted[k] = helper[i];
          i++;
        } else {
          toBeSorted[k] = helper[j];
          j++;
        }
        k++;
      }
    }
    // Copy the rest of the left side of the array into the target array
    while (i <= middle) {
      toBeSorted[k] = helper[i];
      k++;
      i++;
    }
  }

  //SELECTION SORT
  public static void selectionSort(String[] x, int n, boolean descending) {
    if (descending) {
      for (int i = 0; i < n-1; i++)  {
        int index = i;
        for (int j = i+1; j<n; j++) {
          if (compare(x[j], x[index]) > 0)
            index = j;
        }
        String smallest = x[index];
        x[index] = x[i];
        x[i] = smallest;
      }
    }
    else if (!descending) {
      for (int i = 0; i < n-1; i++)  {
        int index = i;
        for (int j = i + 1; j < n; j++) {
          if (compare(x[j], x[index]) < 0)
            index = j;
        }
        String smallest = x[index];
        x[index] = x[i];
        x[i] = smallest;
      }
    }
  }

  //Comparison of sorting algorithms
  public static String[] randomArray(int n, int m) {
    String capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lowers = "abcdefghijklmnopqrstuvwxyz";
    String numbers = "0123456789";
    String alphaNumeric = capitals + lowers + numbers;

    int randomPosition;
    String[] array = new String[n];
    for(int i = 0; i < array.length; i++) {
      array[i] = new String();
      for(int j = 0; j < m; j++) {
        randomPosition = (int)(Math.random()*alphaNumeric.length());
        array[i] += alphaNumeric.charAt(randomPosition);
      }
    }
    return array;
  }

  public static double measureHeapSort(int n) {
    double totalTime = 0;
    for(int i = 0; i < 1000; i++) {
      String[] arr = randomArray(n, 5);
      double startTime = System.nanoTime();
      heapSort(arr, arr.length, true);
      double endTime = System.nanoTime();
      double duration = endTime - startTime;
      totalTime+=duration;
    }
    //converts nano seconds to seconds.
    return totalTime/1000000000.0;
  }

  public static double measureMergeSort1(int n) {
    double totalTime = 0;
    for(int i = 0; i < 1000; i++) {
      String[] arr = randomArray(n, 5);
      double startTime = System.nanoTime();
      mergeSort1(arr, arr.length, true);
      double endTime = System.nanoTime();
      double duration = endTime - startTime;
      totalTime+=duration;
    }
    //converts nano seconds to seconds.
    return totalTime/1000000000.0;
  }

  public static double measureMergeSort2(int n) {
    double totalTime = 0;
    for(int i = 0; i < 1000; i++) {
      String[] arr = randomArray(n, 5);
      double startTime = System.nanoTime();
      mergeSort2(arr, arr.length, true);
      double endTime = System.nanoTime();
      double duration = endTime - startTime;
      totalTime+=duration;
    }
    //converts nano seconds to seconds.
    return totalTime/1000000000.0;
  }

  public static double measureSelectionSort(int n) {
    double totalTime = 0;
    for(int i = 0; i < 1000; i++) {
      String[] arr = randomArray(n, 5);
      double startTime = System.nanoTime();
      selectionSort(arr, arr.length, true);
      double endTime = System.nanoTime();
      double duration = endTime - startTime;
      totalTime+=duration;
    }
    //converts nano seconds to seconds.
    return totalTime/1000000000.0;
  }
}
