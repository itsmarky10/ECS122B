import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class QuickSelect {
    // Edited from http://stackoverflow.com/questions/16027229/reading-from-a-text-file-and-storing-in-a-string
    private String readFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            br.close();
            return sb.toString();
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
        return "Failed";
    }
    
    private int partition(int[] array, int lo, int hi, int pivotIndex) {
        int pivot = array[pivotIndex];
        array[pivotIndex] = array[hi];
        array[hi] = pivot;
        
        int wall = lo;
        int temp;
        
        /* debug
        System.out.println("Begin Sorting Partition " + lo + " - " + hi + " with pivot: " + pivot + " at " + pivotIndex);
        for(int i = lo; i < hi+1; i++) {
            System.out.print(array[i]);
        }
        System.out.println();
        */
        
        for(int i = lo; i < hi; i++) {
            if(array[i] < pivot) {
                // debug
                //System.out.println("Swapping " + array[i] + " at " + i + " and " + array[wall] + " at " + wall);
                
                temp = array[i];
                array[i] = array[wall];
                array[wall] = temp;
                wall++;
            }
        }
        temp = array[wall];
        array[wall] = array[hi];
        array[hi] = temp;
        
        
        // System.out.println("Pivot: " + pivot + " placed at " + wall);
        return wall;
    }
    
    private int qSelect(int [] array, int kOrder, int left, int right) {
        while(true) {
            if(left == right) {
                return array[left];
            }
            
            // Generate a random element to be pivot value between left and right
            Random randomGen = new Random();
            int pivotIndex = randomGen.nextInt(right - left + 1); // Generates a number between 0 and param#
            pivotIndex = left + pivotIndex; // Set pivotIndex between left and right.
            
            // Partition the array
            QuickSelect selectObj = new QuickSelect();
            int pivotPosition = selectObj.partition(array, left, right, pivotIndex);
            
            if(kOrder-1 == pivotPosition) {
                return array[pivotPosition];
            }
            else if(kOrder-1 < pivotPosition) {
                right = pivotPosition-1;
            }
            else {
                left = pivotPosition+1;
            }
        }
    }
    
    public static void main(String[] args) {
        // Simple check for cmd line args
        if(args.length < 2) { // k in second cmd line arg[] should be the order
            System.out.println("Please pass in data filename as the first argument followed by the order k.");
            System.out.println("Tip: The largest element is the set is of order 1.");
            System.exit(1);
        }
        
        QuickSelect selectObj = new QuickSelect();
        
        // Read in data file containing integers
        String fileContents = selectObj.readFile(args[0]); // Read in file as string
        String[] integersAsStrings = fileContents.split("\\W+"); // Split string by white spaces
        int[] array = new int[integersAsStrings.length];
        
        // Convert integers written as strings to integers.
        for(int i = 0; i < integersAsStrings.length; i++) {
            array[i] = Integer.parseInt(integersAsStrings[i]);
        }
        int kOrder = Integer.parseInt(args[1]);
        kOrder = array.length - (kOrder-1); // Change to the k'th LARGEST instead of k'th SMALLEST
        
        // Check if the specified k'th order is within bounds
        if(kOrder <= 0 || kOrder > array.length) {
            System.out.println("ERROR QSelect: kOrder value is " + kOrder + " and array is of size " + array.length);
            System.exit(1);
        }
        
        int result = selectObj.qSelect(array, kOrder, 0, array.length-1);
        
        /* debug
        for(int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
        */
        
        System.out.println(result);
    }

}
