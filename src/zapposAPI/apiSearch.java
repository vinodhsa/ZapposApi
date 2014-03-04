// Front end: to get the input from User
// Vinodh Sankaravadivel., USC
package zapposAPI;

import java.io.*;
import org.json.simple.parser.*;

public class apiSearch {

	
	public static void main(String[] args) throws ParseException {

		// ---------------------------------------------------------------------
		// get inputs from user
		// ---------------------------------------------------------------------
		boolean inputFlag = true;
		int numItems = 0;
		double totalPrice = 0;

		while (inputFlag) {
			String numItemsRequest = "Please Enter the number of items that you would like to Buy? ";
			String numItemsString = helperClass.prompt(numItemsRequest);
			String totalPriceRequest = "Your Price for us to work on (in dollars)? $";
			String totalPriceString = helperClass.prompt(totalPriceRequest);

			boolean errors = false;

			try {
				numItems = Integer.parseInt(numItemsString);
			} catch (NumberFormatException e) {
				System.err
						.println("Number of items must be an integer greater than 0. Restarting: ");
				errors = true;
			}

			// check to make sure total price is a double
			try {
				totalPrice = Double.parseDouble(totalPriceString);
			} catch (NumberFormatException e) {
				System.err
						.println("Total price must be a number greater than 0. Restarting: ");
				errors = true;
			}

			if (numItems < 1 && !errors) {
				System.out
						.println("Number of items must be greater than 0. Restarting:");
			}
			else if (totalPrice <= 0 && !errors) {
				System.out
						.println("Total price must be greater than 0. Restarting:");
			}
			else if (!errors) {
				inputFlag = false;
			}
		}
		try {
			searchForOptions searcher = new searchForOptions(numItems, totalPrice);
			System.out.println(searcher.getGiftCombos());

		} catch (IOException e) {
			System.err
					.println("Something went wrong!! we are sorry !!.");
			e.printStackTrace();
		}
	}
}
