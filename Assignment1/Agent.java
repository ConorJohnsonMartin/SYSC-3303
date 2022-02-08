package Assignment1;

import java.util.ArrayList;
import java.util.List;

/**
 * An Agent that randomly selects two ingredients and puts them on an empty table
 * The Agent implements the Runnable interface
 * 
 * @author Conor Johnson Martin 101106217
 * @version January 29th, 2022
 */
public class Agent implements Runnable{
	
	private String pb;
	private String jam;
	private String bread;
	private List<String> ingredients = new ArrayList<String>();
	private Table table;

	/**
	 * Default constructor for Agent class
	 * 
	 * @param pb is an String representing peanut butter
	 * @param jam is an String representing jam
	 * @param bread is an String representing bread
	 * @param ingredients is a synchronized List of ingredients
	 * @param table is the table
	 */
	public Agent(String pb, String jam, String bread, List<String> ingredients, Table table) {
		this.pb = pb;
		this.jam = jam;
		this.bread = bread;
		this.ingredients = ingredients;
		this.table = table;
	}
	
	/**
	 * A method that is part of the Runnable interface, when Agent thread is started, run() is executed
	 */
	public void run() {
		while(true) {
			if(ingredients.size() >= 1) {
				ingredients.clear();
			}
			if(ingredients.size() == 0) {
				int temp = randomNumGenerator();
				if(temp <= 30) {
					ingredients.add(pb);
					ingredients.add(jam);
				}else if(temp > 30 && temp <= 60) {
					ingredients.add(pb);
					ingredients.add(bread);
				}else{
					ingredients.add(jam);
					ingredients.add(bread);
				}
				table.put(ingredients);
			}
		}
	}
	
	/**
	 * A method that randomly generates a number within range 1 to 90
	 * This range was chosen to add a greater span of random numbers that can be generated as opposed to just 1 to 3
	 * 
	 * @return a randomly generated integer between 1 and 90
	 */
	public Integer randomNumGenerator() {
		int range = 90;
		return (int)(Math.random()*range) + 1;
	}
}