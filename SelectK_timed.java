import java.io.BufferedReader;
import java.io.FileReader;

public class SelectK_timed {
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
        
        SelectK_timed selectObj = new SelectK_timed();
        
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
        
        selectObj.insertsort(array);
        int result = array[kOrder-1];
        //System.out.println(result);
        
    }

}
