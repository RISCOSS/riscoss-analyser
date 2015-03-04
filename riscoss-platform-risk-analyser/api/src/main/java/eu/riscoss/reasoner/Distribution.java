package eu.riscoss.reasoner;

import java.util.ArrayList;
import java.util.List;

/**
 * More fields/methods needed?
 * 
 * @author albertosiena
 *
 */
public class Distribution {
	List<Double> values = new ArrayList<Double>();
	
	public void setValues( List<Double> v ) {
		values.clear();
		for( Double d : v )
			values.add( d );
	}
	
	public List<Double> getValues() {
		return values;
	}
	
	public String toString() {
		
		String printDist = "[ ";
		int size = values.size();
		if(size == 0)
			return "[ ]";
		if(size == 1)
			return "[ " + values.get(0) + " ]";
		Double[] dblArr = new Double[size];
		values.toArray(dblArr);
		for (int i = 0; i < size; i++) {
			printDist += dblArr[i];
			if(i < (size - 1)) {
				printDist += ", ";
			}
		}
		printDist += " ]";
		return printDist;
	}
}
