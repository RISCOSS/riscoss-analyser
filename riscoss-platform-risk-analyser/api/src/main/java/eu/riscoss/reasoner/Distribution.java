/*
   (C) Copyright 2013-2016 The RISCOSS Project Consortium
   
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package eu.riscoss.reasoner;

import java.util.ArrayList;
import java.util.Arrays;
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
		
		return pack();
		
//		String printDist = "[ ";
//		int size = values.size();
//		if(size == 0)
//			return "[ ]";
//		if(size == 1)
//			return "[ " + values.get(0) + " ]";
//		Double[] dblArr = new Double[size];
//		values.toArray(dblArr);
//		for (int i = 0; i < size; i++) {
//			printDist += dblArr[i];
//			if(i < (size - 1)) {
//				printDist += ", ";
//			}
//		}
//		printDist += " ]";
//		return printDist;
	}
	
	public String pack() {
		String str = "";
		String sep = "";
		for( double val : values ) {
			str += sep + val;
			sep = ";";
		}
		return str;
	}
	
	public static Distribution unpack( String value ) {
		try {
			String[] parts = value.split( "[;]" );
			Double[] d = new Double[parts.length];
			for( int i = 0; i < parts.length; i++ ) {
				d[i] = Double.parseDouble( parts[i] );
			}
			Distribution dist = new Distribution();
			dist.setValues( Arrays.asList( d ) );
			return dist;
		}
		catch( Exception ex ) {
			return null;
		}
	}
}
