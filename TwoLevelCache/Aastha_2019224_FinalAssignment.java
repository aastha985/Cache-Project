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
            int s = in.nextInt()/2; //cache size
            System.out.println("Enter Number of Cache Lines(CL):");
            int CL = in.nextInt()/2; //no of cache lines
            System.out.println("Enter Block Size:");
            int B = in.nextInt(); //block size
            if(CL*B==s){
                int cache[][] = new int[CL][B]; //initializing cache
                int cache2[][] = new int[2*CL][B];
                String tags[] = new String[CL]; //initializing tags
                String tags2[] = new String[2*CL];
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
                                String pr=direct(blockNoLength, nphy, cache, addre, tags,d,CL,cache2,tags2,B);
                                w.write("Instruction: "+pri+"\n");
                                w.write(pr);
                                //------for writing the cache and tags after each query in output file-----
                                w.write("\nCache 1\n");
                                for(int i=0;i<CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags 1\n");
                                for(int i=0;i<CL;i++){
                                    w.write(tags[i]+"\n");
                                }
                                w.write("Cache 2\n");
                                for(int i=0;i<2*CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache2[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags 2\n");
                                for(int i=0;i<2*CL;i++){
                                    w.write(tags2[i]+"\n");
                                }
                                w.write("\n");
                                
                            }
                            else if(mode == 2){
                                //calls associative function
                                String pr=associative(blockNoLength,nphy,cache,addre,tags,d,CL,cache2,tags2,B);
                                w.write("Instruction: "+pri+"\n");
                                w.write(pr);
                                //------for writing the cache and tags after each query in output file-----
                                w.write("\nCache 1\n");
                                for(int i=0;i<CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags 1\n");
                                for(int i=0;i<CL;i++){
                                    w.write(tags[i]+"\n");
                                }
                                w.write("Cache 2\n");
                                for(int i=0;i<2*CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache2[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags 2\n");
                                for(int i=0;i<2*CL;i++){
                                    w.write(tags2[i]+"\n");
                                }
                                w.write("\n");
                            }
                            else if(mode==3){
                                //calls set associative function
                                String pr=setassociative(N, blockNoLength, nphy, cache, addre, tags, d, CL,cache2,tags2,B);
                                w.write("Instruction: "+pri+"\n");
                                w.write(pr);
                                //------for writing the cache and tags after each query in output file-----
                                w.write("\nCache 1\n");
                                for(int i=0;i<CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags 1\n");
                                for(int i=0;i<CL;i++){
                                    w.write(tags[i]+"\n");
                                }
                                w.write("Cache 2\n");
                                for(int i=0;i<2*CL;i++){
                                    for(int j=0;j<B;j++){
                                        w.write(cache2[i][j]+" ");
                                    }
                                    w.write("\n");
                                }
                                w.write("Tags 2\n");
                                for(int i=0;i<2*CL;i++){
                                    w.write(tags2[i]+"\n");
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
    
    public static String setassociative(int N,int blockNoLength,int nphy,int cache[][],String addre[],String tags[],String d,int CL,int cache2[][],String tags2[],int B){
        //finding block number,block offset,tag,word number values by slicing the address string
        String blockNo = d.substring(0, blockNoLength);
        String blockOffset = d.substring(blockNoLength, nphy);
        int NoofSets = (CL/N);
        int setbits = (int)(Math.log(NoofSets)/Math.log(2));
        int tagLength = blockNoLength-setbits;
        String tag = blockNo.substring(0,tagLength);
        String set = blockNo.substring(tagLength);
        String set2 = blockNo.substring(tagLength-1);
        String tag2 = blockNo.substring(0,tagLength-1);
        int wordno = Integer.parseInt(blockOffset,2);
        String s="";
        //for write
        if(addre.length>1){
            int edit = Integer.parseInt(addre[1]); //value that needs to be written in the address
            int index = -1; //to be used for finding line number to which we need to write
            //loop that checks cache lines of the particular set
            for(int i=Integer.parseInt(set,2)*N;i<N+Integer.parseInt(set,2)*N;i++){
                //if the block is already present in the cache line or the cache line is is empty 
                if(tags[i]!=null && tags[i].equals(tag)){
                    index=i;
                    cache[index][wordno] = edit;
                    s="Cache Hit, Block found at Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")";
                    return s+"\nWriting to Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")";
                }
                else if(tags[i]==null){
                    index=i;
                    tags[index] = tag;
                    cache[index][wordno] = edit;
                    s="Cache Miss, Block Loaded at Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")";
                    return s+"\nWriting to Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")";
                }
            }
            if(index==-1){
                for(int i=Integer.parseInt(set2,2)*N;i<N+Integer.parseInt(set2,2)*N;i++){
                    //if the block is already present in the cache line or the cache line is is empty 
                    if(tags2[i]!=null && tags2[i].equals(tag2)){
                        index=i;
                        cache2[index][wordno] = edit;
                        s="Cache Hit, Block found at Cache 2 line "+index+"(Set "+Integer.parseInt(set2,2)+")";
                        return s+"\nWriting to Cache 2 line "+index+"(Set "+Integer.parseInt(set2,2)+")";
                    }
                    else if(index==-1 && tags2[i]==null){
                        index=i;
                        tags2[index] = tag2;
                        cache2[index][wordno] = edit;
                        s="Cache Miss, Block Loaded at Cache 2 line "+index+"(Set "+Integer.parseInt(set2,2)+")";
                        return s+"\nWriting to Cache 2 line "+index+"(Set "+Integer.parseInt(set2,2)+")";
                    }
                }
            }
            //if the block is not present in the cache and both cache are full
            if(index==-1){
                //random cache replacement technique
                index=(int)(Math.random()*N + Integer.parseInt(set,2)*N);
                s="Cache Miss, Block Loaded at Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")";
            }
            //loading the block to cache
            cache[index] = new int[B];
            tags[index] = tag;
            cache[index][wordno] = edit;
            return s+"\nWriting to Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")";
        }
        //for read
        else{
            int index = -1;
            //loop traverses the cache lines of that set
            for(int i=Integer.parseInt(set,2)*N;i<N+Integer.parseInt(set,2)*N;i++){
                //checks for Cache Hit
                if(tags[i]!=null && tags[i].equals(tag)){
                    index=i;
                    return "Cache Hit, Block present at Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")\nData present in the address:"+cache[index][wordno];
                }
                //if no Hit checks for empty cache line
                else if(tags[i]==null){
                    index=i;
                    tags[index] = tag;
                    s="Cache Miss, Block Loaded at Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")";
                    return s+"\nData present in the address:"+cache[index][wordno];
                }
            }
            if(index==-1){
                for(int i=Integer.parseInt(set2,2)*N;i<N+Integer.parseInt(set2,2)*N;i++){
                    //checks for Cache Hit
                    if(tags2[i]!=null && tags2[i].equals(tag2)){
                        index=i;
                        return "Cache Hit, Block present at Cache 2 line "+index+"(Set "+Integer.parseInt(set2,2)+")\nData present in the address:"+cache2[index][wordno];
                    }
                    //if no Hit checks for empty cache line
                    else if(tags2[i]==null){
                        index=i;
                        tags2[index] = tag2;
                        s="Cache Miss, Block Loaded at Cache 2 line "+index+"(Set "+Integer.parseInt(set2,2)+")";
                        return s+"\nData present in the address:"+cache2[index][wordno];
                    }
                }
            }
            if(index==-1){
                //using random cache replacement technique
                index=(int)(Math.random()*N + Integer.parseInt(set,2)*N);
            }
            //loading the block to cache
            tags[index] = tag;
            cache[index]=new int[B];
            return "Cache Miss, Loaded block at Cache 1 line "+index+"(Set "+Integer.parseInt(set,2)+")\nData present in the address:"+cache[index][wordno];
        }
    }
    public static String associative(int blockNoLength,int nphy,int cache[][],String addre[],String tags[],String d,int CL,int cache2[][],String tags2[],int B){
        //slicing the address string to find block number,block offset, word number
        String blockNo = d.substring(0, blockNoLength);
        String blockOffset = d.substring(blockNoLength, nphy);
        int wordno = Integer.parseInt(blockOffset,2);
        String s="";
        //for write
        if(addre.length>1){
            int edit = Integer.parseInt(addre[1]); //value that needs to be written in the address
            int index = -1; //to be used for finding line number to which we need to write
            //to check for Cache Hit or empty block in the cache
            for(int i=0;i<CL;i++){
                if(tags[i]!=null && tags[i].equals(blockNo)){
                    index=i;
                    cache[index][wordno]=edit;
                    s="Cache Hit, Block found at Cache 1 line "+(index)+"\n";
                    break;
                }
                else if(tags[i]==null){
                    index=i;
                    tags[index]=blockNo;
                    cache[index][wordno]=edit;
                    s="Cache Miss, Block Loaded to Cache 1 line "+index;
                    break;
                }
            }
            if(index==-1){
                for(int i=0;i<2*CL;i++){
                    if(tags2[i]!=null && tags2[i].equals(blockNo)){
                        index=i;
                        cache2[index][wordno]=edit;
                        return "Cache Hit, Block found at Cache 2 line "+(index)+"\n"+"Writing to cache 2 line "+index;
                    }
                    else if(tags2[i]==null){
                        index=i;
                        tags2[index]=blockNo;
                        cache2[index][wordno]=edit;
                        s="Cache Miss, Cache 1 is full, Loaded block at Cache 2 line "+index;
                        return s+"\nWriting to Cache 2 line "+index;
                    }
                }
            }
            //when both are full replace in cache 1 using random cache replacement technique
            if(index==-1){
                index=(int)(Math.random()*CL);
                tags[index]=blockNo;
                cache[index][wordno]=edit;
                s="Cache Miss, Cache 1 and 2 are full, Loaded block at cache 1 line "+index;

            }
            return s+"\nWriting to cache 1 line "+index;
        }
        //for read
        else{
            int index = -1;
            for(int i=0;i<CL;i++){
                if(tags[i]!=null && tags[i].equals(blockNo)){
                    index=i;
                    return "Cache Hit, Block present at cache 1 line "+i+"\nData present in the address:"+cache[i][wordno];
                }
                else if(tags[i]==null){
                    index=i;
                    tags[index]=blockNo;
                    return "Cache Miss, Loaded block at cache 1 line "+index+"\nData present in the address:"+cache[index][wordno];
                }
            }
            if(index==-1){
                for(int i=0;i<2*CL;i++){
                    if(tags2[i]!=null && tags2[i].equals(blockNo)){
                        index=i;
                        return "Cache Hit, Block present at Cache 2 line "+i+"\nData present in the address:"+cache2[i][wordno];
                    }
                    else if(tags2[i]==null){
                        index=i;
                        tags2[index]=blockNo;
                        return "Cache Miss, Cache 1 is full, Loaded block at Cache 2 line "+index+"\nData present in the address:"+cache2[index][wordno];
                    }
                }
            }
            if(index==-1){
                //random cache replacement technique
                index=(int)(Math.random()*CL);
                cache[index]=new int[B];
                tags[index]=blockNo;
                return "Cache Miss, Cache 1 and 2 are full, Loaded block at cache 1 line "+index+"\nData present in the address:"+cache[index][wordno];

            }    
            return "";    
        }
        
    }

    public static String direct(int blockNoLength,int nphy,int cache[][],String addre[],String tags[],String d,int CL,int cache2[][],String tags2[],int B){
        int lineNolength = (int)(Math.log(CL)/Math.log(2)); 
        int tagLength = blockNoLength- lineNolength;
        //slicing the address string to find tag,line number,block number,block offset,word number
        String tag = d.substring(0,tagLength);
        String lineNo = d.substring(tagLength, blockNoLength);
        String blockNo = d.substring(0, blockNoLength);
        String blockOffset = d.substring(blockNoLength, nphy);
        int lineNo1 = Integer.parseInt(lineNo,2);
        int wordno = Integer.parseInt(blockOffset,2);
        int lineNo2=Integer.parseInt(d.substring(tagLength-1, blockNoLength),2);
        // System.out.println(tags[lineNo2]);
        String tag2 = d.substring(0,tagLength-1);
        //for write 
        if(addre.length>1){
            int edit = Integer.parseInt(addre[1]); //value that needs to be written in the address
            if(tags[lineNo1]!=null && tags[lineNo1].equals(tag)){
                cache[lineNo1][wordno]=edit;
                tags[lineNo1] = tag;
                return "Cache Hit, Block found at Cache 1, Writing to Cache 1 line "+lineNo1;
            }
            else if(tags2[lineNo2]!=null && tags2[lineNo2].equals(tag2)){
                cache2[lineNo2][wordno]=edit;
                tags2[lineNo2] = tag2;
                return "Cache Hit, Block found at Cache 2, Writing to Cache2 line "+lineNo2;
            }
            else{
                String s="Cache Miss, Block not found in both cache \n";
                if(tags[lineNo1] != null && tags[lineNo1]!=tag){
                    lineNo2 = Integer.parseInt(tags[lineNo1].substring(tagLength-1)+lineNo,2);
                    tags2[lineNo2] = tags[lineNo1].substring(0,tagLength-1);
                    cache2[lineNo2] = cache[lineNo1];
                    s+="Block which was present at line "+lineNo1+"(Cache 1) moved to line "+lineNo2+"(cache 2)\n";
                }
                cache[lineNo1] = new int[B];
                cache[lineNo1][wordno]=edit;
                tags[lineNo1] = tag;
                return s+"\nWriting to Cache 1 line "+lineNo1;
            }
        }
        //for read
        else{
            if(tags[lineNo1]!=null && tags[lineNo1].equals(tag)){
                tags[lineNo1] = tag;
                return "Cache Hit, Block present in Cache 1 at line "+lineNo1+"\nData in the address: "+cache[lineNo1][wordno];
            }
            else if(tags2[lineNo2]!=null && tags2[lineNo2].equals(tag2)){
                tags2[lineNo2] = tag2;
                return "Cache Hit, Block present in Cache 2 at line "+lineNo2+"\nData in the address: "+cache2[lineNo2][wordno];
            }
            else{
                String s="Cache Miss, Block not found in both cache \n";
                if(tags[lineNo1] != null && tags[lineNo1]!=tag){
                    lineNo2 = Integer.parseInt(tags[lineNo1].substring(tagLength-1)+lineNo,2);
                    tags2[lineNo2] = tags[lineNo1].substring(0,tagLength-1);
                    cache2[lineNo2] = cache[lineNo1];
                    s+="Block which was present at line "+lineNo1+"(Cache 1) moved to line "+lineNo2+"(cache 2)\n";
                }
                cache[lineNo1] = new int[B];
                tags[lineNo1] = tag;
                return s+"Block loaded in cache1 at line "+lineNo1;
            }
        }
    }
}