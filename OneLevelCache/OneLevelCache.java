import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Aastha_2019224_FinalAssignment{
    public static void main(String args[]){
        Scanner in = new Scanner(System.in);
        try{
            System.out.println("Enter Cache Size(s):");
            int s = in.nextInt(); //cache size
            System.out.println("Enter Number of Cache Lines(CL):");
            int CL = in.nextInt(); //no of cache lines
            System.out.println("Enter Block Size:");
            int B = in.nextInt(); //block size
            if(CL*B==s){
                int cache[][] = new int[CL][B]; //initializing cache
                String tags[] = new String[CL]; //initializing tags
                System.out.println("Enter the size of main memory(a multiple of cache size and block size):");
                int n = in.nextInt();  //size of main memory
                int nphysical = (int)(Math.log(n)/Math.log(2));
                System.out.println("FORMAT for Address.txt file");
                System.out.println("1)Address should be "+nphysical+" bit binary string.");
                System.out.println("2)For read:  \"address\"");
                System.out.println("3)For write: \"address data(Integer(32bits))\"");
                System.out.println("4)Each address should be in next line.");
                System.out.println("Ensure this format is followed for correct results.Ignore if your file is already in this format.");
                int nb = n/B; //no of lines in main memory
                int mainMem[][] = new int[nb][B]; //initializing main memory
                System.out.println("\nEnter 1 for Direct Mapping");
                System.out.println("      2 for Associative");
                System.out.println("      3 for n-way Set Associative");
                int mode = in.nextInt();
                int N=2;
                if(mode==3){
                    System.out.println("Enter N for N-way Set Associative(a factor of CL)");
                    N = in.nextInt();
                    if(N==CL) mode=1;
                }
                if(mode!=1 && mode!=2 && mode!=3){
                    System.out.println("Please enter a number between 1 and 3 only");
                }
                try{
                    FileWriter w = new FileWriter("Output.txt");
                    try{
                        if(mode==1){
                            w.write("DIRECT MAPPING\n\n");
                        }
                        else if(mode==2){
                            w.write("ASSOCIATIVE MAPPING\n\n");
                        }
                        else if(mode==3){
                            w.write(N+"-WAY SET ASSOCIATIVE MAPPING\n\n");
                        }
                        File f = new File("Address.txt");
                        Scanner read = new Scanner(f);
                        while (read.hasNextLine()) {
                            String pri = read.nextLine();
                            String addre[] = pri.split(" "); //for reading address and data
                            String d = addre[0]; //address
                            int nphy = (int)(Math.log(n)/Math.log(2)); //no of bits in physical address
                            int blockOffsetLength = (int)(Math.log(B)/Math.log(2));
                            int blockNoLength = nphy - blockOffsetLength;
                            if(mode==1){
                                //calls direct function
                                String pr=direct(blockNoLength, nphy, mainMem, cache, addre, tags,d,CL);
                                w.write("Instruction: "+pri+"\n");
                                w.write(pr);
                                //------for writing the cache and tags after each query in output file-----
                                w.write("\nCache\n");
                                for(int i=0;i<CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags\n");
                                for(int i=0;i<CL;i++){
                                    w.write(tags[i]+"\n");
                                }
                                w.write("\n");
                                
                            }
                            else if(mode == 2){
                                //calls associative function
                                String pr=associative(blockNoLength,nphy,mainMem,cache,addre,tags,d,CL);
                                w.write("Instruction: "+pri+"\n");
                                w.write(pr);
                                //------for writing the cache and tags after each query in output file-----
                                w.write("\nCache\n");
                                for(int i=0;i<CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags\n");
                                for(int i=0;i<CL;i++){
                                    w.write(tags[i]+"\n");
                                }
                                w.write("\n");
                            }
                            else if(mode==3){
                                //calls set associative function
                                String pr=setassociative(N, blockNoLength, nphy, mainMem, cache, addre, tags, d, CL);
                                w.write("Instruction: "+pri+"\n");
                                w.write(pr);
                                //------for writing the cache and tags after each query in output file-----
                                w.write("\nCache\n");
                                for(int i=0;i<CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags\n");
                                for(int i=0;i<CL;i++){
                                    w.write(tags[i]+"\n");
                                }
                                w.write("\n");
                            }
                        }
                        read.close();
                        if(mode ==1){
                            System.out.println("Direct Mapping complete.Check the Output.txt file for results.");
                        }
                        else if(mode ==2){
                            System.out.println("Associative Mapping complete.Check the Output.txt file for results.");
                        }
                        else if(mode ==3){
                            System.out.println(N+"-way Set Associative Mapping complete.Check the Output.txt file for results.");
                        }
                    }
                    catch(FileNotFoundException e){
                        System.out.println("Couldn't find the file you were looking for");
                    }
                    w.close();
                }
                catch(IOException io){
                    System.out.println("couldn't write");
                }
            }
            else{
                System.out.println("Cache size should be equal to block size*no of lines in cache(s=CL*B)");
            }
        }
        catch(InputMismatchException e){
            System.out.println("Please ensure that inputs are of the type integer(2^32)");
        }
        
    }
    
    public static String setassociative(int N,int blockNoLength,int nphy,int mainMem[][],int cache[][],String addre[],String tags[],String d,int CL){
        //finding block number,block offset,tag,word number values by slicing the address string
        String blockNo = d.substring(0, blockNoLength);
        String blockOffset = d.substring(blockNoLength, nphy);
        int NoofSets = (CL/N);
        int setbits = (int)(Math.log(NoofSets)/Math.log(2));
        int tagLength = blockNoLength-setbits;
        String tag = blockNo.substring(0,tagLength);
        String set = blockNo.substring(tagLength);
        int wordno = Integer.parseInt(blockOffset,2);
        String s="";
        //for write
        if(addre.length>1){
            int edit = Integer.parseInt(addre[1]); //value that needs to be written in the address
            mainMem[Integer.parseInt(blockNo,2)][wordno] = edit; //updating main memory
            int index = -1; //to be used for finding line number to which we need to write
            //loop that checks cache lines of the particular set
            for(int i=Integer.parseInt(set,2)*N;i<N+Integer.parseInt(set,2)*N;i++){
                //if the block is already present in the cache line or the cache line is is empty 
                if(tags[i]!=null && tags[i].equals(tag)){
                    index=i;
                    s="Cache Hit, Block found at Cache line "+index+"(Set "+Integer.parseInt(set,2)+")";
                    break;
                }
                else if(tags[i]==null){
                    index=i;
                    s="Cache Miss, Loaded block from main memory line "+ Integer.parseInt(blockNo,2) +" at cache line "+index+"(Set "+Integer.parseInt(set,2)+")";
                    break;
                }
            }
            //if the block is not present in the cache and no cache line is empty
            if(index==-1){
                //random cache replacement technique
                index=(int)(Math.random()*N + Integer.parseInt(set,2)*N);
                if(tags[index]!=null && !(tags[index].equals(blockNo))){
                    s+="Block moved back to main memory line "+Integer.parseInt(tags[index],2)+" from cache line "+index+"(Set "+Integer.parseInt(set,2)+")"+"\n";
                }
                s+="Cache Miss, Loaded block from main memory line "+ Integer.parseInt(blockNo,2) +" at cache line "+index+"(Set "+Integer.parseInt(set,2)+")";
            }
            //loading the block to cache
            tags[index] = tag;
            cache[index] = mainMem[Integer.parseInt(blockNo,2)];
            return s+"\nWriting to cache line "+index+"(Set "+Integer.parseInt(set,2)+")";
        }
        //for read
        else{
            int index = -1;
            //loop traverses the cache lines of that set
            for(int i=Integer.parseInt(set,2)*N;i<N+Integer.parseInt(set,2)*N;i++){
                //checks for Cache Hit
                if(tags[i]!=null && tags[i].equals(tag)){
                    index=i;
                    return "Cache Hit, Block present at cache line "+index+"(Set "+Integer.parseInt(set,2)+")\nData present in the address:"+cache[index][wordno];
                }
                //if no Hit checks for empty cache line
                else if(tags[i]==null){
                    index=i;
                    break;
                }
            }
            if(index==-1){
                //using random cache replacement technique
                index=(int)(Math.random()*N + Integer.parseInt(set,2)*N);
                if(tags[index]!=null && !(tags[index].equals(blockNo))){
                    s+="Block moved back to main memory line "+Integer.parseInt(tags[index],2)+" from cache line "+index+"(Set "+Integer.parseInt(set,2)+")"+"\n";
                }
            }
            //loading the block to cache
            tags[index] = tag;
            cache[index] = mainMem[Integer.parseInt(blockNo,2)];
            return s+"Cache Miss, Loaded block from main memory line "+Integer.parseInt(blockNo,2) +" at cache line "+index+"(Set "+Integer.parseInt(set,2)+")\nData present in the address:"+cache[index][wordno];
        }
    }
    public static String associative(int blockNoLength,int nphy, int mainMem[][],int cache[][],String addre[],String tags2[],String d,int CL){
        //slicing the address string to find block number,block offset, word number
        String blockNo = d.substring(0, blockNoLength);
        String blockOffset = d.substring(blockNoLength, nphy);
        int wordno = Integer.parseInt(blockOffset,2);
        String s="";
        //for write
        if(addre.length>1){
            int edit = Integer.parseInt(addre[1]); //value that needs to be written in the address
            mainMem[Integer.parseInt(blockNo,2)][wordno] = edit; //updating main memory
            int index = -1; //to be used for finding line number to which we need to write
            //to check for Cache Hit or empty block in the cache
            for(int i=0;i<CL;i++){
                if(tags2[i]!=null && (tags2[i].equals(blockNo))){
                    index=i;
                    s="Cache Hit, Block found at cache line "+index;
                    break;
                }
                else if(tags2[i]==null){
                    index=i;
                    s="Cache Miss, Loaded block from main memory line "+ Integer.parseInt(blockNo,2)+" at cache line "+index;
                    break;
                }
            }
            if(index==-1){
                //random cache replacement technique
                s="Cache Miss,";
                index=(int)(Math.random()*CL);
                if(tags2[index]!=null && !(tags2[index].equals(blockNo))){
                    s+=" Block moved back to main memory line "+Integer.parseInt(tags2[index],2)+" from cache line "+index;
                }
                s+="\nLoaded block from main memory line "+ Integer.parseInt(blockNo,2)+" at cache line "+index;
            }
            //loading the block to cache
            tags2[index] = blockNo;
            cache[index] = mainMem[Integer.parseInt(blockNo,2)];
            return s+"\nWriting to cache line "+index;
        }
        //for read
        else{
            int index = -1;
            for(int i=0;i<CL;i++){
                //check for cache hit
                if(tags2[i]!=null && (tags2[i].equals(blockNo))){
                    return "Cache Hit, Block present at cache line "+i+"\nData present in the address:"+cache[i][wordno];
                }
                //check for empty block
                else if(tags2[i]==null){
                    index=i;
                    break;
                }
            }
            s="Cache Miss,";
            if(index==-1){
                //random cache replacement technique
                index=(int)(Math.random()*CL);
                if(tags2[index]!=null && !(tags2[index].equals(blockNo))){
                    s+=" Block moved back to main memory line "+Integer.parseInt(tags2[index],2)+" from cache line "+index;
                }
                
            }
            //loading the block to cache
            tags2[index] = blockNo;
            cache[index] = mainMem[Integer.parseInt(blockNo,2)];
            return s+"\nLoaded block from main memory line "+Integer.parseInt(blockNo,2) +" at cache line "+index+"\nData present in the address:"+cache[index][wordno];
        }
        
    }

    public static String direct(int blockNoLength,int nphy,int mainMem[][],int cache[][],String addre[],String tags[],String d,int CL){
        int lineNolength = (int)(Math.log(CL)/Math.log(2)); 
        int tagLength = blockNoLength- lineNolength;
        //slicing the address string to find tag,line number,block number,block offset,word number
        String tag = d.substring(0,tagLength);
        String lineNo = d.substring(tagLength, blockNoLength);
        String blockNo = d.substring(0, blockNoLength);
        String blockOffset = d.substring(blockNoLength, nphy);
        int lineNo1 = Integer.parseInt(lineNo,2);
        int wordno = Integer.parseInt(blockOffset,2);
        //for write 
        if(addre.length>1){
            String s="";
            if(tags[lineNo1]==null || !(tags[lineNo1].equals(tag))){
                s= "Cache Miss, Loaded block from main memory line "+Integer.parseInt(blockNo,2) +" at cache line "+lineNo1;
                if(tags[lineNo1]!=null && !(tags[lineNo1].equals(tag))){
                    s="Cache Miss,Moved block back to main memory line "+ Integer.parseInt(tags[lineNo1]+lineNo,2)+" from Cache line"+lineNo1+"\nLoaded the block from main memory line "+Integer.parseInt(blockNo,2) +" at cache line "+lineNo1;
                }   
            }
            int edit = Integer.parseInt(addre[1]); //value that needs to be written in the address
            mainMem[Integer.parseInt(blockNo,2)][wordno] = edit; //updating main memory
            cache[lineNo1] = mainMem[Integer.parseInt(blockNo,2)]; //updating cache
            tags[lineNo1] = tag;
            return s+"\nWriting to cache line "+lineNo1;
        }
        //for read
        else{
            //if cache miss(block not present in the cache,then load the block)
            String s="";
            if(tags[lineNo1]==null || !(tags[lineNo1].equals(tag))){
                if(tags[lineNo1]!=null && !(tags[lineNo1].equals(tag))){
                    s="Cache Miss,Moved block back to main memory line "+ Integer.parseInt(tags[lineNo1]+lineNo,2)+" from Cache line"+lineNo1+"\nLoaded the block from main memory line "+Integer.parseInt(blockNo,2) +" at cache line "+lineNo1;
                    s+="\nData in the address: "+cache[lineNo1][wordno];
                }
                else{
                    s="Cache Miss, Loaded block from main memory at cache line "+lineNo1+"\nData in the address:"+cache[lineNo1][wordno];   

                } 
                tags[lineNo1] = tag;
                cache[lineNo1] = mainMem[Integer.parseInt(blockNo,2)];
                return s;
            }
            else{
                return "Cache Hit, Block present in cache at line "+lineNo1+"\nData in the address: "+cache[lineNo1][wordno];
            }
        }
    }
}