package Assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Table that is shared between an Agent and Chefs to produce and eat sandwiches
 * 
 * @author Conor Johnson Martin 101106217
 * @version January 29th, 2022
 */
public class Table {
	
	private List<String> contents = Collections.synchronizedList(new ArrayList<String>()); //where ingredients are put and taken
    private boolean empty = true; //means that the table is empty
    
    /**
     * A method that an Agent uses to put a List of items on the table if the table is empty
     * 
     * @param items is a List of ingredients to be put on the table
     */
    public synchronized void put(List<String> items) {
        while (!empty) {
            try {
            	System.out.println("The Agent is waiting");
                wait();
            } catch (InterruptedException e) {
            	System.out.println("Problem with Agent: ");
				e.printStackTrace();
            }
        }
        contents.addAll(items);
        System.out.println("The Agent put " + contents.toString() + "on the table");
        empty = false;
        notifyAll();
    }
    
    /**
     * A method that a Chef uses to remove items from the table if the table is not empty or
     * if the Chef's proprietary ingredient is not one of the two ingredients placed on the table by the Agent
     *  
     * @param ingredient is the Chef's proprietary ingredient
     */
    public synchronized void get(String ingredient) {
    	while (empty || contents.contains(ingredient)) {
            try {
            	System.out.println("The Chef " + ingredient.toString() + " is waiting");
                wait();
            } catch (InterruptedException e) {
            	System.out.println("Problem with Chef: ");
				e.printStackTrace();
            }
        }
        contents.clear();
        System.out.println("The Chef " + ingredient.toString() + " has eaten a sandwich");
        empty = true;
        notifyAll();
    }
    
   
}