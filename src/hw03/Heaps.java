package hw03;
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

    //SORT COMPARISONS
    //random array
    //5 values of N to measure Big O
    //256
    //512
    //1024
    //2048
    //4096

    System.out.println("\n***SORT COMPARISONS***");
    System.out.println("\n**HEAP SORT**");
    System.out.println("Elements: 256. Time: " + measureHeapSort(256) + " s");
    System.out.println("Elements: 512. Time: " + measureHeapSort(512) + " s");
    System.out.println("Elements: 1024. Time: " + measureHeapSort(1024) + " s");
    System.out.println("Elements: 2048. Time: " + measureHeapSort(2048) + " s");
    System.out.println("Elements: 4096. Time: " + measureHeapSort(4096) + " s");

    System.out.println("\n**MERGE SORT 1 (TOP-DOWN)**");
    System.out.println("Elements: 256. Time: " + measureMergeSort1(256) + " s");
    System.out.println("Elements: 512. Time: " + measureMergeSort1(512) + " s");
    System.out.println("Elements: 1024. Time: " + measureMergeSort1(1024) + " s");
    System.out.println("Elements: 2048. Time: " + measureMergeSort1(2048) + " s");
    System.out.println("Elements: 4096. Time: " + measureMergeSort1(4096) + " s");

    System.out.println("\n**MERGE SORT 2 (BOTTOM-UP)**");
    System.out.println("Elements: 256. Time: " + measureMergeSort2(256) + " s");
    System.out.println("Elements: 512. Time: " + measureMergeSort2(512) + " s");
    System.out.println("Elements: 1024. Time: " + measureMergeSort2(1024) + " s");
    System.out.println("Elements: 2048. Time: " + measureMergeSort2(2048) + " s");
    System.out.println("Elements: 4096. Time: " + measureMergeSort2(4096) + " s");

    System.out.println("\n**SELECTION SORT**");
    System.out.println("Elements: 256. Time: " + measureSelectionSort(256) + " s");
    System.out.println("Elements: 512. Time: " + measureSelectionSort(512) + " s");
    System.out.println("Elements: 1024. Time: " + measureSelectionSort(1024) + " s");
    System.out.println("Elements: 2048. Time: " + measureSelectionSort(2048) + " s");
    System.out.println("Elements: 4096. Time: " + measureSelectionSort(4096) + " s");
  }

  public static int dataCount = 0;

  //Heap methods
  public static String heapTraversalString = "";
  public static String toTreeString(String[] x, int n) {
        int left = getLeft(n);
        int right = getRight(n);
        heapTraversalString = "(" + x[0];

        toTreeString(x, left);
        toTreeString(x, right);
        return heapTraversalString+=")";

  }

  public static void buildMaxHeap(String[] x, int n) {
    for (int i = n/2; i >= 0; i--) {
      maxHeapify(x, n, i);
    }
  }

  public static int countElements(String[] data) {
    dataCount = 0;
    for(String element: data) {
      if (element != null) {
        dataCount++;
      }
    }
    return dataCount;
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
    return 2*root;
  }

  public static int getRight(int root) {
    return 2*root+1;
  }

  public static int getParent(int root) {
    return root/2;
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
      buildMaxHeap(x, size);
//      for (int i = n-1; i >= 2; i--) {
//        size--;
//        swap(x, 0, i);
//        maxHeapify(x, size, 1);
//      }
    } else if (!descending) {
      buildMinHeap(x, size);
//      for (int i = n-1; i >= 2; i--) {
//        size--;
//        swap(x, 0, i);
//        minHeapify(x, size, 1);
//      }
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
      // Get the index of the element which is in the middle
      int middle = low + (high - low) / 2;
      // Sort the left side of the array
      sort1(toBeSorted, helper, low, middle, descending);
      // Sort the right side of the array
      sort1(toBeSorted, helper,middle + 1, high, descending);
      // Combine them both
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
