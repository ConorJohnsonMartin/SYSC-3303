package Assignment1;

/**
 * A Chef that is assigned an ingredient and tries to make a sandwich so long as the the table is not empty
 * The Chef implements the Runnable interface
 * 
 * @author Conor Johnson Martin 101106217
 * @version January 29th, 2022
 */
public class Chef implements Runnable{
	private String ingredient;
	private Table table;
	
	/**
	 * Default constructor for Chef class
	 * 
	 * @param ingredient is an String representing the ingredient that is assigned to the Chef
	 * @param table is the table
	 */
	public Chef(String ingredient, Table table) {
		this.ingredient = ingredient;
		this.table = table;
	}
	
	public void run() {
		while(true) {
			table.get(ingredient);
		}
	}
}
