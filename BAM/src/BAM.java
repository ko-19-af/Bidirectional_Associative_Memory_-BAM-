import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** This class uses the BAM AI to determine the associated data of some input
 *
 * IDE used: IntelliJ
 *
 * @Author Kevin Olenic
 * ST#: 6814974
 * @Version 1.2
 * @Since 2023-01-22
 */
public class BAM {

    private static final int[][] weightMatrix = new int[5][7];// matrix to hold calculated weights

    public BAM(){
        //Part A train the network by feeding it input data and the expected output
//        train(new int[]{-1,1,1,1,-1}, new int[]{1,1,-1,1});
//        train(new int[]{-1,-1,-1,-1,1}, new int[]{1,-1,-1,-1});
//        train(new int[]{-1,-1,-1,1,1}, new int[]{-1,-1,1,1});
//        train(new int[]{1,1,1,1,1}, new int[]{-1,1,1,-1}); // Part B
//
//        //test the BAM by supplying input and generating output
//        test(new int[]{-1,1,1,1,-1}, false);
//        test(new int[]{-1,-1,-1,-1,1}, false);
//        test(new int[]{-1,-1,-1,1,1}, false);
//        test(new int[]{1,1,1,1,1}, false); //Part B
//
//        //test the BAM by supplying output and generating input
//        test(new int[]{1,1,-1,1}, false);
//        test(new int[]{1,-1,-1,-1}, false);
//        test(new int[]{-1,-1,1,1}, false);
//        test(new int[]{-1,1,1,-1},false); //Part B

        //Part C train the network with the padded output
        int[] yp1 = new int[]{1,1,-1,-1,1,1,1};// 1 1 -1 1
        int[] yp2 = new int[]{1,-1,-1,1,-1,1,-1};// 1 -1 -1 -1
        int[] yp3 = new int[]{-1,-1,1,1,1,1,1};// -1 -1 1 1
        int[] yp4 = new int[]{-1,1,1,-1,-1,-1,-1};// -1 1 1 -1

        // train the B.A.M using the input and padded target data
        train(new int[]{-1,1,1,1,-1},yp1);
        train(new int[]{-1,-1,-1,-1,1},yp2);
        train(new int[]{-1,-1,-1,1,1},yp3);
        train(new int[]{1,1,1,1,1},yp4);

        // test the input target data
//        test(new int[]{-1,1,1,1,-1}, false);
//        test(new int[]{-1,-1,-1,-1,1}, false);
//        test(new int[]{-1,-1,-1,1,1}, false);
//        test(new int[]{1,1,1,1,1}, false);
//
//        // test the padded target data
//        test(yp1, false);
//        test(yp2, false);
//        test(yp3, false);
//        test(yp4, false);

        //Part D test the B.A.M's error correcting ability
        List<int[]> List = new ArrayList<>();// create a list containing all the inputs
        List.add(new int[]{-1,1,1,1,-1});
        List.add(new int[]{-1,-1,-1,-1,1});
        List.add(new int[]{-1,-1,-1,1,1});
        List.add(new int[]{1,1,1,1,1});

        for(int x = 1; x <= 20; x++){// perform twenty test for error correction
            ThreadLocalRandom r = ThreadLocalRandom.current();// generate random
            // randomly choose an array and create a copy to preserve original
            int[] copy = Arrays.copyOf(List.get(r.nextInt(0,List.size())),
                    List.get(r.nextInt(0,List.size())).length);
            System.out.println("Original: " + Arrays.toString(copy));// print original

            for(int m = 0; m < copy.length; m++) {// mutate random amount of the input
                if(r.nextInt(1,100) <= 20){// if mutation occurs (20% chance)
                    copy[m] = copy[m] * (-1);// mutate value
                }
            }
            System.out.println("Mutated: " +Arrays.toString(copy));// print the mutated input
            errorTest(copy);// error test the mutated input
        }
    }// constructor

    public static void main(String[] args) {
        new BAM();
    }//main

    /**This method takes the inputs  (A) and target output (B) to calculate
     * the synaptic weight matrix for the B.A.M network
     *
     * @param input going into the system
     * @param output is the target values of the output
     */
    public void train(int[] input, int[] output){

        for(int y = 0; y < input.length; y++){// travel down the column
            for(int x = 0; x < output.length; x++){// travel to the right of the row
                weightMatrix[y][x] += input[y] * output[x];
            }
        }
}//train

    /**  This method calculates the associated data for an input by calculating the dot product
     * between the input and the previously calculated weight matrix. The method also applies
     * the threshold function ot the result to produce the original data
     *
     * @param input is one of the inputs from the training data that the B.A.M is using to find the associated data
     */
    private int[] test(int[] input, boolean error){
        int[] result;
        int position = 0;

        if(input.length == weightMatrix.length) {// generate target (output)
            result = new int[weightMatrix[0].length];// result matrix has length of x-axis of weight matrix (output)
            for (int x = 0; x < weightMatrix[0].length; x++) {// for each column
                int sum = 0;// reset the sum
                for (int y = 0; y < weightMatrix.length; y++) {//for each row
                    sum +=  weightMatrix[y][x] * input[y];// calculate value of result matrix
                }
                if (sum >= 0) result[position] = 1;// apply threshold function to result
                else result[position] = -1;
                position++;// move to next position in result
            }
        }
        else{// generate (input)
            result = new int[weightMatrix.length];
            for (int[] matrix : weightMatrix) {// for each row
                int sum = 0;// reset the sum
                for (int x = 0; x < weightMatrix[0].length; x++) {// for each column
                    sum += matrix[x] * input[x];
                }
                if (sum >= 0) result[position] = 1;// apply threshold function to result
                else result[position] = -1;
                position++;// move to next position in result
            }
        }

       if(!error){
           System.out.println("Input: " + Arrays.toString(input));
           System.out.println("Output: " + Arrays.toString(result) + "\n");
       }
        return result;// return result of test method
    }//test

    /** This method performs error testing on a mutated input
     *
     * @param input is the mutated input
     */

    private void errorTest(int[] input){
        int[] previnput = test(test(input,true),true);// array holding previous error-corrected input result
        int[] prevoutput = test(previnput,true);// array holding previous error-corrected output result

        while(!Arrays.equals(previnput, test(prevoutput, true)) ){
            previnput = test(prevoutput,true);
            prevoutput = test(test(prevoutput,true),true);
        }// error correct until input/output reach convergence
        System.out.println("Output: " + Arrays.toString(test(test(previnput,true),true))+"\n");// print the error corrected input
    }//errorTest
}//BAM