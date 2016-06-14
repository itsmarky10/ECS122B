import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class DeterministicSelect_timed {
    private void insertsort(int[] array) {
        int[] inSortArr = new int[array.length];
        
        for(int i = 0; i < inSortArr.length; i++) {
            inSortArr[i] = Integer.MAX_VALUE;
        }
        
        inSortArr[0] = array[0];
        int insIndex = 1;
        
        for(int i = 1; i < array.length; i++) {
            inSortArr[insIndex] = array[i];
            for(int j = insIndex; j > 0; j--) {
                if(inSortArr[j-1] > inSortArr[j]) {
                    int temp = inSortArr[j-1];
                    inSortArr[j-1] = inSortArr[j];
                    inSortArr[j] = temp;
                }
                else {
                    break;
                }
            }
            insIndex++;
            
            /* debug
            System.out.print(i + ": ");
            for(int j = 0; j < inSortArr.length; j++) {
                System.out.print(inSortArr[j] + ", ");
            }
            System.out.println();4
            */
            
            
        }
        
        for(int i = 0; i < array.length; i++) {
            array[i] = inSortArr[i];
            
            /* debug
            System.out.print(i-lo + ": ");
            for(int j = 0; j < array.length; j++) {
                System.out.print(array[j] + ", ");
            }
            System.out.println();
            */
        }
    }
    
    private int deterministic_select(int[] array, int elementPos) {
        DeterministicSelect_timed dObj = new DeterministicSelect_timed();
        if(array.length <= 10) {
            dObj.insertsort(array);
            return array[elementPos-1];
        }
        
        // Median of Medians pivot selection
        ArrayList<int[]> groups = new ArrayList<int[]>();
        for(int i = 0; i < array.length/5; i++) { // Generate the groups
            int [] aGroup = new int[5];
            for(int j = i*5; j < (i*5)+5; j++) {
                aGroup[j-(i*5)] = array[j];
            }
            groups.add(aGroup);
        }
        
        //Sort the groups and return the middle element of each group into an array
        int [] mediansOfGroups = new int[groups.size()];
        for(int i = 0; i < groups.size(); i++) {
            dObj.deterministic_select(groups.get(i), 3);
            mediansOfGroups[i] = groups.get(i)[2];
        }
        
        int pivot = dObj.deterministic_select(mediansOfGroups, array.length/10);
        ArrayList<int[]> partitions = new ArrayList<int[]>();
        dObj.det_partition(array, pivot, partitions);
        
        if(elementPos <= partitions.get(0).length) {
            return dObj.deterministic_select(partitions.get(0), elementPos);
        }
        else if(elementPos > partitions.get(0).length + partitions.get(1).length) {
            return dObj.deterministic_select(partitions.get(2), elementPos-(partitions.get(0).length + partitions.get(1).length));
        }
        else {
            return pivot;
        }
    }
    
    private void det_partition(int[] array, int pivot, ArrayList<int[]> partitions) {
        
        ArrayList<Integer> leftPart = new ArrayList<Integer>();
        ArrayList<Integer> midPart = new ArrayList<Integer>();
        ArrayList<Integer> rightPart = new ArrayList<Integer>();
        
        for(int i = 0; i < array.length; i++) {
            if(array[i] < pivot) {
                leftPart.add(array[i]);
            }
            else if(array[i] == pivot) {
                midPart.add(array[i]);
            }
            else {
                rightPart.add(array[i]);
            }
        }
        int [] leftArr = new int[leftPart.size()];
        int [] midArr = new int[midPart.size()];
        int [] rightArr = new int[rightPart.size()];
        
        for(int i = 0; i < leftPart.size(); i++) {
            leftArr[i] = leftPart.get(i);
        }
        for(int i = 0; i < midPart.size(); i++) {
            midArr[i] = midPart.get(i);
        }
        for(int i = 0; i < rightPart.size(); i++) {
            rightArr[i] = rightPart.get(i);
        }
        
        partitions.add(leftArr);
        partitions.add(midArr);
        partitions.add(rightArr);
    }
    
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
    
    public static void main(String[] args) {
        // Simple check for cmd line args
        if(args.length < 2) { // k in second cmd line arg[] should be the order
            System.out.println("Please pass in data filename as the first argument followed by the order k.");
            System.out.println("Tip: The largest element is the set is of order 1.");
            System.exit(1);
        }
        
        DeterministicSelect_timed selectObj = new DeterministicSelect_timed();
        
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
            System.out.println("ERROR: kOrder value is " + kOrder + " and array is of size " + array.length);
            System.exit(1);
        }
        
        int result = selectObj.deterministic_select(array, kOrder);
        
        //System.out.println(result);
    }
}