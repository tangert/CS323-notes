public static void main(String[] args) {

	System.out.println(leader({98, 23, 54, 12, 20, 7, 27 }));

}

//given array of integers, find a maximum
//sum of non adjacent elements.

public int findMaxSum(int[] array) {

	// for i in array {

	// }
}


//buy and sell stock at proper time


//calculate frequencies

public HashMap<Int,Int> frequencies(int[] array) {

	// HashMap<Int,Int> freqDictionary = new HashMap<Int,Int>;

	// for i in array {
	// 	if freqDictionary.contains(i){
	// 		freqDictionary.i.value ++;
	// 	} else {
	// 		freqDictionary.put(i);
	// 	}
	// }
}

//find all permutations of a string 
//strategy: 
//2^n amount of strings
//oh boy lol
public String[] permutations(String string) {
	// if string.length() == 0 {
	// 	return "";
	// }

	// else {

	// }
}

//leaders in the array
//element which is larger than elements in array to the right
//start from end of array
//keep another array of "leaders"
//if current number is greater than current max, add to leader array.

public int[] leader(int[] numbers) {
	//create leaders array, max length is input length

	int[] leaders = new int[numbers.length()];
	int relativeMax = 0;
	//starting from end of array
	for(i = numbers.length()-1; i >= 0; i--) {
		if numbers[i] > relativeMax {
			numbers[i] = relativeMax;
			leaders[(numbers.length()-1)- i] = numbers[i];
		}
	}

	System.out.println(Arrays.toString(leaders));
	return leaders;
}
