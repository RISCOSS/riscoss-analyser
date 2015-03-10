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
