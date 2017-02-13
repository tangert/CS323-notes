package hw03;

/**
 * Created by tylerangert on 2/12/17.
 */
public class Heaps {

  public static void main(String[] args) {
    System.out.println("Hello heaps!");

    //test cases for min/max heap validation.


  }

  //Heap methods
  public static String toTreeString(String[] x, int n) {
    //recursive format
    //(key (left-subtree) (right-subtree))
    return "";
  }

  public String preOrderTraversal() {
    return "";
  }

  public static void buildMaxHeap(String[] x, int n) {
    int heapSize = x.length;
    for (int i = x.length/2; i >= 0; i--) {
      maxHeapify(x, i);
    }
  }

  public static void maxHeapify(String[] x, int n) {
    int left = getLeft(x, n);
    int right = getRight(x, n);
    int largest;

    if ((left <= x.length) && (compare(x[left],x[n]) > 0)) {
      largest = left;
    } else {
      largest = n;
    }

    if ((right <= x.length) && (compare(x[right],x[largest]) > 0)) {
      largest = right;
    }

    if (largest != n) {
      x[n] = x[largest];
      maxHeapify(x, largest);
    }
  }

  public static void buildMinHeap(String[] x, int n) {
    int heapSize = x.length;
    for(int i = x.length/2; i >= 0; i--) {
      minHeapify(x, i);
    }
  }

  public static void minHeapify(String[] x, int n) {
    int left = getLeft(x, n);
    int right = getRight(x, n);
    int smallest;

    if ((left <= x.length) && (compare(x[left],x[n]) < 0)) {
      smallest = left;
    } else {
      smallest = n;
    }

    if ((left <= x.length) && (compare(x[right],x[smallest]) < 0)) {
      smallest = right;
    }

    if (smallest != n) {
      x[n] = x[smallest];
      minHeapify(x, smallest);
    }
  }

  /*
  takes a string s, an array of strings x representing a valid binary heap,
   and an integer n representing the number of elements actually used in x (assume n ≤ x.length).

   The method automatically detects if x is a max-heap or a min-heap, and adds string s to it.

   If there is not enough data to automatically detect if it is a max-heap or min-heap,
   make it a min-heap by default.

   The method returns true if the insertion was successful,
   or false if there was not enough extra space in the array to insert the new element.
   */
  public static boolean addToHeap(String s, String[] x, int n) {
    //add s to x


    return true;
  }

  //Heap helper methods
  public static int compare(String string1, String string2) {
    return string1.compareTo(string2);
  }

  public static int getLeft(String[] array, int root) {
    if (2*root <= array.length) return 2*root;
    else return -1;
  }

  public static int getRight(String[] array, int root) {
    if (2*root+1 <= array.length) return 2*root+1;
    else return -1;
  }

  public static int getParent(String[] array, int root) {
    if (root == 1) return -1;
    return root/2;
  }

  public static boolean isMinHeap(String[] heap) {
    int left = getLeft(heap, 0);
    return (compare(heap[0], heap[left]) < 0);
  }

  public static boolean isMaxHeap(String[] heap) {
    return !isMinHeap(heap);
  }

  //Sorting algorithms
  public static void heapSort(String[] x, int n, boolean descending) {

  }

  public static void mergeSort1(String[] x, int n, boolean descending) {

  }

  public static void mergeSort2(String[] x, int n, boolean descending) {

  }

  public static void selectionSort(String[] x, int n, boolean descending) {

  }

  //Comparison of sorting algorithms
  public static String[] randomArray(int n, int m) {
    String[] s = new String[5];
    return s;
  }

  public static double measureHeapSort(int n) {
    return 0.0;
  }

  public static double measureMergeSort1(int n) {
    return 0.0;
  }

  public static double measureMergeSort2(int n) {
    return 0.0;
  }

  public static double measureSelectionSort(int n) {
    return 0.0;
  }
}
