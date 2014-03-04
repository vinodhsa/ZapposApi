//
// Complete structure of a product
// Vinodh Sankaravadivel,USC
//
package zapposAPI;

import org.json.simple.*;

public class Product {
	private double price;		
	private String id;			
	private String name;		
	private String styleId;		
	private String priceString;	
	
	
	public Product(JSONObject product) 
	{
		
		price = Double.parseDouble(((String) product.get("price")).substring(1));
		id = (String)product.get("productId");
		name = (String)product.get("productName");
		styleId = (String)product.get("styleId");
		priceString = String.format("%.2f", price);
	}
	
	
	public String toString() 
	{
		return name + ", $" + priceString + " (id:" + id + ", styleId:" + styleId + ")";
	}
	
	
	public double getPrice()
	{
		return price;
	}
}
