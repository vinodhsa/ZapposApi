// Uses REST api to do the calling
// Vinodh Sankaravadivel,USC
package zapposAPI;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class searchForOptions {
	private int numItems;			
	private double totalPrice;		
	private double maxPrice;		
	private int moveToNext;			
	private JSONArray products;		
	private ArrayList<Product> productObjects; 
	private ArrayList<productsGrouping> productCombos;
	private final double deviations = Math.pow(10, -7); 
	private int limit=200;
	private int threshold = 10;
		
	
	public searchForOptions(int num, double total)
	{
		numItems = num;
		totalPrice = total;
		maxPrice = Integer.MAX_VALUE; 	
		moveToNext = 1;	
		products = new JSONArray();
		productObjects = new ArrayList<Product>();
		productCombos = new ArrayList<productsGrouping>();
	}
	
	
	private Double getPrice(Object item)
	{
		return Double.parseDouble(((String) ((JSONObject) item).get("price")).substring(1));
	}
	
	@SuppressWarnings("unchecked")
	private void setProductsInRange() throws IOException, ParseException {
		
		if(totalPrice < 50 )
		{
			limit=100;
		}
		else if(totalPrice > 100)
		{
			limit=300;
		}
		else if (totalPrice > 140 ) 
		{
			limit=400;
		}
		
		String reply = helperClass.httpGet(helperClass.BASEURL + "&term=&limit="+limit+"&sort={\"price\":\"asc\"}");
		JSONObject replyObject = helperClass.parseReply(reply);
		JSONArray resultArray = helperClass.getResults(replyObject);
		
		double firstPrice = getPrice(resultArray.get(0));
		
		if( (firstPrice * numItems) > totalPrice) {
			products = null;
			return;
		}
		
		maxPrice = totalPrice - (numItems - 1)*(firstPrice);
		
		//moving to the next page
		moveToNext++;
		
		Double lastPrice = getPrice(resultArray.get(resultArray.size() - 1));
		
		while(lastPrice < maxPrice)
		{ 
			
			String completeURL = helperClass.BASEURL + "&term=&limit=200&sort={\"price\":\"asc\"}&page=" + moveToNext;
			String nextPage = helperClass.httpGet(completeURL);
			JSONObject nextObject = helperClass.parseReply(nextPage);
			JSONArray nextArray = helperClass.getResults(nextObject);
			
			resultArray.addAll(nextArray);
			lastPrice = getPrice(nextArray.get(nextArray.size() - 1));
			moveToNext++;
		}

	products = resultArray;
	}
	
	private void setSearchableProducts() {
		try{
			productObjects.add(new Product((JSONObject)products.get(0)));	
		}
		catch(NullPointerException e)
		 {
			System.out.println("No objects found for the price !!! Please increase your price range \n");
			System.exit(1);
		}
		
		
		//count how many times a price has already shown up
		int already = 1;
		int numPrices = 1;
		//go through the whole 
		for(int i = 1; i < products.size() && getPrice(products.get(i)) < maxPrice; i++) {
			double currentPrice = getPrice(products.get(i));
			if( currentPrice > productObjects.get(numPrices-1).getPrice()) {
				productObjects.add(new Product((JSONObject)products.get(i)));
				numPrices++;
				already = 1;
			} else if(Math.abs(currentPrice - productObjects.get(numPrices-1).getPrice()) < deviations && already < numItems){
				productObjects.add(new Product((JSONObject)products.get(i)));
				numPrices++;
				already++;
			} else {
				while(i < products.size() && Math.abs(currentPrice - productObjects.get(numPrices-1).getPrice()) < deviations) {
					i++;
					currentPrice = getPrice(products.get(i));
				}
				i++;
				already = 0;
			}
		}
	}

	/**
	 * Recursively finds the product combinations of numItems items within $1 of the totalPrice
	 */
	private void setProductCombos() {
		setProductCombosRecursive(productObjects, totalPrice, new ArrayList<Product>());
	}
	
	private void setProductCombosRecursive(ArrayList<Product> productList, double target, ArrayList<Product> partial) {
		
		//if partial size > numItems, you already have too many items, so stop
		if(partial.size() > numItems) { return; }
		
		double sum = 0;
		for(Product x : partial) sum += x.getPrice();
		
		if(Math.abs(sum - target) < 1 && partial.size() == numItems && productCombos.size() < threshold)
		{
			if(productCombos.size() == 0) 
			{	
				productCombos.add(new productsGrouping(partial, totalPrice));
			}
			else
			{
				productsGrouping testerCombo = productCombos.get(productCombos.size() -1);
				productsGrouping partialCombo = new productsGrouping(partial, totalPrice);
				if(!partialCombo.equals(testerCombo))
				{
					productCombos.add(partialCombo);
				}
			}
		}
		else if (productCombos.size() >= 10)
		{
			return;
		}
		else
		{
		
			if(sum >= target + 1 )
			{
				return;
			}
			
			
			for(int i = 0; i < productList.size() && !(partial.size() == numItems && sum < target) && productCombos.size() < 10; i++)
			{
				ArrayList<Product> remaining = new ArrayList<Product>();
				Product n = productList.get(i);
				for(int j=i+1; j < productList.size(); j++) {remaining.add(productList.get(j)); }
				ArrayList<Product> partial_rec = new ArrayList<Product>(partial);
				partial_rec.add(n);
				setProductCombosRecursive(remaining, target, partial_rec);
				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void sortProductCombos() {
		Collections.sort(productCombos);
	}
	
	
	public String getGiftCombos() throws IOException, ParseException {
		//get products from API
		System.out.println("We are searching to give you the best Zappos experience !!!");
		this.setProductsInRange();
		
		System.out.println("Working on your request !!!We will be right back !!");
		
		this.setSearchableProducts();
		
		this.setProductCombos();
	
		this.sortProductCombos();
		
		
		if(productCombos.size() != 0) {
			String toPrint = "\nThere you go for shopping!!!\n";
			for(productsGrouping x:productCombos) {
				toPrint += x.toString() + "\n";
			}
		
			return toPrint;
		}
		else {
			return "Sorry we could not make it!!.Please refine your search quesry.";
		}
	}
	
}