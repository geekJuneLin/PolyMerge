import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.*;

class PolyMerge {
    static int[] run;
    static int[] result;
    static Heap heap;
    static Heap fileHeap;
    static BufferedWriter mergeFile;
    static ArrayList<Name> nameList;
    public static void main (String args []){
        if (args.length!=2){
            System.out.println ("Usage: java PolyMerge <Num of Runs> <Num of Files to use>");
            return;
        }
        
        int counter = 0;
        //Get the number of the Runs
        int numOfRuns = Integer.parseInt(args[0]);
        //How many files can be used to distribute the file
        int numOfFiles = Integer.parseInt(args[1]);
        //Initialise the heap object
        heap = new Heap (numOfFiles);
        Scanner [] scArray = new Scanner[numOfFiles];
        String name = "";
        String [] fileArray = new String[numOfFiles];
        nameList = new ArrayList<Name>(numOfFiles);
        int flag = 0;
        int level = distributeFile(numOfFiles, numOfRuns); //call the distribute function
        try{
            //Write the files into the different files
            Scanner sc = new Scanner(new File("output.txt"));
            BufferedWriter bw;
            String s = "";
            for(int i=0; i<(numOfFiles - 1); i++){
                flag = 0;
                counter = 0;
                name = String.valueOf(i) + ".txt";
                bw = new BufferedWriter(new FileWriter(name));
                //System.out.println(i);
                scArray[i] = new Scanner(new File(name));
                while(counter <= run[i]){
                    if(run[i] == 0){
                        counter++;
                        break;
                    }
                    if(flag == 0){
                        bw.write("*\t*\n");
                        counter++;
                        flag = 1;
                        s = sc.nextLine();
                        System.out.println("Counter: " + counter);
                    }
                    if(sc.hasNextLine()){
                        s = sc.nextLine();
                        if(counter < run[i]){
                            if(s.contains("*\t*")){
                                bw.write("*\t*\n");
                                counter++;
                            }else{
                                bw.write(s + "\n");
                            }
                        }else{
                            if(s.contains("*\t*")){
                                counter++;
                            }else{
                                bw.write(s + "\n");
                            }
                        }
                    }else{
                        if(counter < run[i])
                            bw.write("*\t*\n");
                        counter++;
                    }
                }
                bw.close();
            }
            
            //Merge files each time into one file
            System.out.println("Level number: " + level);
            int index = FindOutput();  //Get which file is empty
            //System.out.println("Empty file name: " + index);
            //    BufferedWriter mergeFile = new BufferedWriter(new FileWriter(String.valueOf(index) + ".txt"));
            int numOutput = GetnumOutput (); // Get the minimum runs
            int runNum = 0;
            int fileRunNum = 0;
            int [] fileIndex = new int[numOfFiles - 1];
            fileHeap = new Heap(numOfFiles - 1);
            String emptyName = String.valueOf(numOfFiles - 1) + ".txt";
            String smallName = "";
            String small;
            int readIndex = 0;
            //System.out.println("Smalleast run file: " + numOutput);
            for(int i=0; i<level; i++){ // loop through each level
                System.out.println("At level: " + i);
                numOutput = GetnumOutput(); //get the num of mininum runs
                System.out.println("Smalleast run file: " + numOutput);
                System.out.println("Empty file name: " + FindOutput());
                if(i == 0)
                    break;
                runNum = 0;
                index = FindOutput();
                smallName = String.valueOf(getSmallName()) + ".txt";
                System.out.println("The smalleast file is: " + smallName);
                mergeFile = new BufferedWriter(new FileWriter(String.valueOf(index) + ".txt"));
                while(runNum < numOutput){ // check how many runs have been read
                    System.out.println("Reading the " + runNum + " run");
                    fileRunNum = 0;
                    for(int z=0; z<numOfFiles - 1; z++){
                        fileIndex[z] = 0;
                    }
                    for(int x=0; x<numOfFiles; x++){ // loop through each file
                        //System.out.println("FILENUM: " + x);
                        //if current file is the output file then skip it
                        if(x == FindOutput()){
                            System.out.println("Skip the empty file");
                            continue;
                        }
                        //if current file has been read once then skip it
                        if(fileIndex[x] == -1){
                            System.out.println("Current file has been read one run " + x);
                            continue;
                        }
                        //check whether the current file has nextLine element
                        if(scArray[x].hasNextLine()){
                            //if the number of elements in fileHeap is less than the number of the file that has data, then read data and add into heap
                            if(fileHeap.length() < (numOfFiles - 1)){
                                //read the data
                                s = scArray[x].nextLine();
                                //check the data whether has the delimit mark
                                if(s.contains("*\t*")){
                                    if(!scArray[x].hasNextLine()){
                                        break;
                                    }else{
                                        //set the current file to be the status of read already
                                        fileIndex[x] = -1;
                                        //increment the number of read runs
                                        fileRunNum++;
                                        //increate the length of the heap
                                        fileHeap.add(null);
                                        //mergeFile.write("*\t*\n");
                                    }
                                }else{
                                    //add the data into heap directly
                                    fileHeap.add(s);
                                    nameList.add(new Name(s, x));  //////*************
                                }
                            }else{
                                //find smalleast item in the heap
                                small = fileHeap.findSmall();
                                //print out the data
                                fileHeap.print();
                                if(small.equals("")){
                                    System.out.println("FIND EMPTY SMALL ITEM");
                                    //initialize the heap
                                    fileHeap = new Heap(numOfFiles - 1);
                                    //put a new delimit mark into output file
                                    mergeFile.write("*\t*\n");
                                    //increment the runs
                                    runNum++;
                                    System.out.println("All " + (numOfFiles - 1) + " have been read once");
                                    //break the loop
                                    break;
                                }
                                System.out.println("Writing out: " + small);
                                //write the smalleast item into output file
                                mergeFile.write(small + "\n");
                                //find which file is the smalleast item from
                                readIndex = findName(small);
                                System.out.println("Find small: " + readIndex);
                                //check whether that file has been read once
                                if(fileIndex[readIndex] == -1){
                                    continue;
                                    //read the next line from that file
                                }else if(scArray[readIndex].hasNextLine()){
                                    s = scArray[readIndex].nextLine();
                                    //check whether it contains the delimit mark
                                    if(s.contains("*\t*")){
                                        //replace the smalleast item with null
                                        fileHeap.replaceSmall(small, null);
                                        removeName(readIndex);
                                        //set that file to be the status of reading once
                                        fileIndex[readIndex] = -1;
                                        //increment the number of read runs
                                        fileRunNum++;
                                        //mergeFile.write("*\t*\n");
                                        System.out.println("readIndex: " + readIndex);
                                        //continue;
                                    }else{
                                        removeName(readIndex);
                                        //replace the smalleast item with the data just read from
                                        fileHeap.replaceSmall(small, s);
                                        nameList.add(new Name(s, readIndex));
                                    }
                                }else{
                                    
                                    System.out.println("***BUG***:" + runNum);
                                    //mergeFile.write();
                                    //runNum++;
                                    mergeFile.close();
                                    return;
                                }
                            }
                        }
                    }
                }
                mergeFile.close();
                moveContent(emptyName, smallName);
                updateRun();
                checkRun();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    //calculate the good distribution and distribute runs into files
    private static int distributeFile(int numOfFiles, int numOfRuns){
        //Track how many levels are there do get the distribution;
        int level = 1;
        int totalRun = 0;
        run = new int[(numOfFiles)]; // How many runs are there in each file
        result = new int[(numOfFiles)];
        run [0] =1; //The first item is 1
        run [(numOfFiles-1)] = 0; //The last item is 0
        for(int i = 1; i<(numOfFiles-1);i++){
            run[i] =0;
        }
        while (totalRun<numOfRuns){
            totalRun = 0;
            for (int i=0;i<(numOfFiles);i++){
                result[i] = run[i];
            }
            for(int i=(numOfFiles-3);i>=0;i--){
                run[i] = result[i+1]+result[0];
                totalRun += run[i];
            }
            run [(numOfFiles -2)] = result [0];
            totalRun += run[(numOfFiles -2)];
            level ++;
        }
        for(int i=0;i<numOfFiles;i++){
            System.out.println("File[" + i +"]: "+run[i]);
        }
        System.out.println("Total runs are: " + totalRun);
        return level;
    }
    
    //Find which is the output file
    private static int FindOutput(){
        int index = 0;
        for(int i = 0;i<run.length;i++){
            if (run[i]==0){
                index = i;
            }
        }
        return index;
    }
    
    //how many runs merged into the output file
    private static int GetnumOutput(){
        int smallest = Integer.MAX_VALUE;
        for (int i = 0;i<run.length;i++){
            if(run[i] !=0 && run[i] < smallest){
                smallest = run [i];
            }
        }
        return smallest;
    }
    
    private static int getSmallName(){
        int x = GetnumOutput();
        int index = 0;
        for(int i=0; i<run.length; i++){
            if(run[i] == x){
                index = i;
                break;
            }
        }
        return index;
    }
    
    private static void updateRun(){
        int x = GetnumOutput();
        for(int i=0; i<run.length; i++){
            if(run[i] != 0 && run[i] != x){
                run[i] -= x;
            }
        }
    }
    
    private static void checkRun(){
        for(int i=0; i<run.length; i++){
            System.out.println(run[i]);
        }
    }
    
    private static void moveContent(String output, String smallFile){
        System.out.println("Empty: " + output + " , Small: " + smallFile);
        Scanner sc;// = new Scanner(new File(output));
        BufferedWriter bw;// = new BufferedWriter(new FileWriter(smallFile));
        try{
            sc = new Scanner(new File(output));
            bw = new BufferedWriter(new FileWriter(smallFile));
            while(sc.hasNextLine()){
                bw.write(sc.nextLine() + "\n");
            }
            sc.close();
            bw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static int findName(String data){
        int index = 0;
        Name item = new Name("", 0);
        for(int x=0; x<nameList.size(); x++){
            item = nameList.get(x);
            if(item.getData().equals(data)){
                index = item.getFileName();
                break;
            }
        }
        return index;
    }
    
    private static void removeName(int index){
        Name item = new Name("", 0);
        for(int x=0; x<nameList.size(); x++){
            item = nameList.get(x);
            if(item.getFileName() == index){
                nameList.remove(x);
            }
        }
    }
}

