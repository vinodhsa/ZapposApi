
// makes selection based on the prices and groups them
// Vinodh Sankaravadivel,USC

package zapposAPI;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class productsGrouping implements Comparable{
	private ArrayList<Product> grpOfProducts;	
	private double sum;						
	private double idealTotal; 				
	private double closeness;					
	private final double acceptableDeviations = Math.pow(10, -6);
	
	public productsGrouping(ArrayList<Product> productsForCombo, double total) {
		grpOfProducts = productsForCombo;
		sum = 0;
		idealTotal = total;
		for(Product x:grpOfProducts) sum += x.getPrice(); 
		closeness = Math.abs(idealTotal - sum);
	}
	
	public double getPrice(int index) {
		return grpOfProducts.get(index).getPrice();
	}
	
	public double getSum() {
		return sum;
	}
	
	public int getProductComboLength() {
		return grpOfProducts.size();
	}
	
	public double getCloseness() {
		return closeness;
	}
	
	public double getTotal() {
		return idealTotal;
	}

	@Override
	public int compareTo(Object o) {
		productsGrouping other = (productsGrouping) o;
		if(this.equals(other)) return 0;
		else if(this.closeness < other.getCloseness()) return -1;
		else return 1;
	}
	
	public boolean equals(productsGrouping other) {
		if(this.grpOfProducts.size() != other.getProductComboLength()) {
			return false;
		}
		if(this.idealTotal != other.getTotal()) {
			return false;
		}
		for(int i = 0; i < grpOfProducts.size(); i++){
			if(Math.abs(this.grpOfProducts.get(i).getPrice() - other.getPrice(i)) > acceptableDeviations) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		String toReturn = "Total Price of the Products Retrieved $" + sum + "\n";
		for(int i = 0; i < grpOfProducts.size(); i ++) {
			toReturn += (i+1) + "==> " + grpOfProducts.get(i).toString() + "\n";
		}
		return toReturn;
	}
	
}
