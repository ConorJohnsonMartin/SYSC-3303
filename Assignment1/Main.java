package Assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The main class initializes:
 * -the synchronized list where ingredients will be placed
 * -the String ingredients themselves
 * -the 4 threads (1 Agent, 3 Chefs)
 * 
 * And then starts the 4 threads
 * 
 * @author Conor Johnson Martin 101106217
 * @version January 29th, 2022
 */
public class Main {
	
	/**
	 * The main method
	 * 
	 * @param args an array of Strings
	 */
	public static void main(String[] args) {
		
		List<String> ingredients = Collections.synchronizedList(new ArrayList<String>());
		String pb = "Peanut Butter";
		String jam = "Jam";
		String bread = "Bread";
		Table table = new Table();
		
		Thread agent = new Thread(new Agent(pb, jam, bread, ingredients, table), "Agent");
		Thread chef_pb = new Thread(new Chef(pb, table), "Chef PB");
		Thread chef_jam = new Thread(new Chef(jam, table), "Chef Jam");
		Thread chef_bread = new Thread(new Chef(bread, table), "Chef Bread");
		
		agent.start();
		chef_pb.start();
		chef_jam.start();
		chef_bread.start();
		
	}
}
