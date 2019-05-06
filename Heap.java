class Heap{
    private String [] heap;
    private int size = 0; //store the size of the heap
    private int length = 0; //for keep tracking how many items already in the heap
    
    //Constructor for initialising heap
    public Heap(int size){
        heap = new String[size];
        this.size = size;
    }
    
    //adding things into the heap
    public void add(String s){
        if(heap[0] == null){   //check whether the first place in the heap is null
            heap[0] = s;   //if so put the item at the first place
        }else{
            int index = 0;
            while(index+1 <= size && heap[index+1] != null){  //check whether the next available place is empty and also make sure the next place is within the size of the heap
                index++;  //increment the index by 1
            }
            heap[index+1] = s; //put the item at the available place
        }
        length++; //keep tracking how many items already in the heap
    }
    
    //return how many item currently in the heap
    public int length(){
        return length;
    }
    
    //find the smalleast item in the heap
    public String findSmall(){
        String small = "";
        int flag = 0; //to indicate whether find the first non-null value
        for(int i=0; i<this.length(); i++){ //loop through the heap and find the smalleast item
            if(heap[i] == null){ //if the heap[i] is null then skip it
                continue;
            }else if(flag == 0){ //check whether find the first non-null item in the heap
                small = heap[i]; //if no, then assign the first non-null item to small
                flag = 1; //set the flag to 1
            }else{
                if(small.compareTo(heap[i]) > 0){ //check which item is smaller
                    small = heap[i]; //assign the smaller item to small
                }
            }
        }
        return small; //return the smallest item
    }
    
    //find the specified item in the heap and return the index of it
    public int findIndex(String s){
        int index = 0; //for tracking the index
        for(int i=0; i<this.length(); i++){ //loop through the heap
            if(heap[i] == null){ //if the heap[i] is null then skip it
                index++;
                continue;
            }else if(heap[i].equals(s)){ // check the current item whether is string s
                break; //if so then jump out the loop
            }
            index++; //increment the index by 1
        }
        return index; //return the index
    }
    
    //replace the smalleast item with new item
    public void replaceSmall(String s, String n){
        int index = this.findIndex(s); //get the index of the string needed to replace
        heap[index] = n; //replace the value
    }
    
    //replace the item with new item
    public void replace(int index, String s){
        heap[index] = s; //replace the specific position's value
    }
    
    //return the item at specific index
    public String get(int x){
        return heap[x]; //get the item at specific index
    }
    
    //printing all the items out
    public void print(){
        for(int i=0; i<this.length(); i++){ //loop through the heap
            System.out.println(i + " position is: " + heap[i]); //print out the item
        }
    }
    
    public int numberOfItems(){
        int num = 0;
        for(int i=0; i<this.size; i++){
            if(heap[i] == null){
                continue;
            }
            num++;
        }
        return num;
    }
    
    public void increateLength(){
        this.length++;
    }
}

