import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.*;

class MakeRuns{
    public static void main(String args []){
        if(args.length != 2){ //check whether the user input the size of the heap and name of the file
            System.out.println("Usage: java MakeRuns <SIZE> <NAME OF FILE>");
            return;
        }
        int m = 0;
        int run = 0;
        int size = Integer.parseInt(args[0]); //get the size
        String file = args[1]; //get the name of the file
        Heap heap = new Heap(size); //create the heap for storing item
        Heap backUp = new Heap(size); //create the second heap for storing the item that cannot be written out
        int flag = 0;
        String s = ""; //create the string s for storing the item read from file
        String preS = ""; //storing the item that written out before
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
            Scanner sc = new Scanner(new File(file)); //create the scanner for reading data
            while(true){
                if(sc.hasNextLine()){
                    if(heap.length() < size){ //if the number of the item in the heap is less than the size user specified
                        heap.add(sc.nextLine()); //add the data into the heap
                    }else if(heap.length() == size){ //if the number of the item in the heap equal to size
                        if(flag == 0){
                            bw.write("*\t*\n");
                            flag = 1;
                            ;
                        }
                        s = heap.findSmall(); //find the smallest item in the heap
                        if(preS.equals("") || s.compareTo(preS) > 0){ //check if the preS is "" or the current data just read from file is smaller than previous data
                            System.out.println("THE SMALLEST ITEM IS: " + s); //write the smallest data out into file
                            bw.write(s + "\n");
                            heap.replaceSmall(s, sc.nextLine()); //replace the smallest item with new data
                            preS = s; //set the preS to smallest just found
                            
                        }else{ //if the preS is greater than the smallest data just found then need to put the current smallest item into the back up heap
                            heap.replaceSmall(s, null); //remove the smallest item from the heap
                            backUp.add(s);
                        }
                        if(backUp.length() == size){ //if the length of the back up heap equal to the size then it means there is no item in the heap, so need to put all the item from the back up into heap
                            run++;
                            System.out.println("\n");
                            System.out.println("NEW RUN STARTS");
                            bw.write("*\t*\n");
                            for(int i=0; i<backUp.length(); i++){
                                heap.replace(i, backUp.get(i));
                            }
                            backUp = new Heap(size); //init the back up
                            preS = ""; //reset the preS
                        }
                    }
                }else{
                    if(heap.numberOfItems() > 0){
                        s = heap.findSmall();
                        if(s.compareTo(preS) > 0){
                            System.out.println("Clean the heap: " + s);
                            bw.write(s + "\n");
                            heap.replaceSmall(s, null);
                            preS = "";
                        }else{
                            heap.replaceSmall(s, null);
                            backUp.add(s);
                        }
                    }else if(backUp.numberOfItems() > 0){
                        if(m == 0){
                            System.out.println("New Run Starts: ");
                            bw.write("*\t*\n");
                            run++;
                            m = 1;
                        }
                        s = backUp.findSmall();
                        System.out.println("Clean the backUp: " + s);
                        bw.write(s + "\n");
                        backUp.replaceSmall(s, null);
                    }else if(heap.numberOfItems() == 0 && backUp.numberOfItems() == 0){
                        break;
                    }
                }
            }
            bw.close();
            heap.print();
            System.out.println("BackUp heap below: ");
            backUp.print();
            System.out.println("Total runs are: " + (run+1));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
