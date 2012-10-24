package ps0;

import java.util.*;

/**
 * RandomHello prints a random greeting
 * 
 */
public class RandomHello {
	
	/**
	 * Uses a RandomHello object to contain randomizing code
	 */
	public static void main(String[] argv
			){
		RandomHello randomHello = new RandomHello();
		System.out.println(randomHello.getGreeting());
	}
	
	/**
	 * 
	 * @return a random greeting of 5 choices
	 */
	public String getGreeting(){
		Random fate = new Random();
		int r = fate.nextInt(9);
		if(r == 0){
			return "Nihao!";
		}else if(r <= 2){
			return "G'day mate!";
		}else if(r <= 4){
			return "Top 'o the morning to you!";
		}else if(r <= 7){
			return "Hey there CSE.";
		}else{
			return "May the force be with you.";
		}
	}
}
