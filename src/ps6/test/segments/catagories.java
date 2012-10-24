package ps6.test.segments;

/*
  program to iterate over a Tiger database txt file and catagorize the
most numerous street names found in the database.  takes in three arguments
#1 the input database #2 the output file
 */

import java.io.*;
import java.util.*;

public class catagories{
    public static void main(String[] args) throws FileNotFoundException{
	if(args.length != 2){
	    System.out.println("MUST SPECIFY #1 INPUT FILE #2 OUTPUT FILE");
	}else{
	    Scanner read = new Scanner(new File(args[0]));
	    PrintStream write = new PrintStream(new File(args[1]));
	    
	    zipDiffs(read, write);

	}
    }

    public static void zipDiffs(Scanner read, PrintStream write){
	while(read.hasNextLine()){
	    String line = read.nextLine();
	    String[] fields = line.split("\t");

	    if(fields.length > 4 && !fields[4].equals("") && !fields[5].equals("") && !fields[4].equals(fields[5])){
		write.println(line);
	    }
	    
	}

    }

    public static void nameCounts(Scanner read, PrintStream write){
	    
	Map<String, Integer> counts = new HashMap<String, Integer>();
	
	String max = "";
	while(read.hasNextLine()){
	    String[] fields = read.nextLine().split("\t");
	    String street = fields[0];
	    if(!street.equals("(unnamed street)")){
		Integer i = counts.get(street);
		counts.put(street,  i == null ? 1 : i+1);
		max = street;
	    }
	}
	
	List<String> bigOnes = new LinkedList<String>();
	for(String key : counts.keySet()){
	    if(counts.get(key) != null && counts.get(key) > 50){
		bigOnes.add(key);
	    }
	}
	
    
	for(String street : bigOnes){
	    write.println(counts.get(street)+"\t"+street);
	}
	
    }
}