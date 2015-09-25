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

package eu.riscoss.fbk.lp;


public class Label implements Comparable<Label>
{
	// override of the tremendous compareTo()
	boolean isLessThan(Label aLabel) {
		if (this.compareTo(aLabel) == -1) // <this> is smaller
			return true;
		else return false;
	}

	public boolean isGreaterThan(Label aLabel) {
		if (this.compareTo(aLabel) == 1) // <this> is greater
			return true;
		else return false;
	}

	boolean isEqualTo(Label aLabel) {
		if (this.compareTo(aLabel) == 0) // <this> is equal
			return true;
		else return false;
	}
	
	
    private final float myValue;      // for easy comparisons between labels

    // static objects ...
    static final Label NO      = new Label(0.0f);
    static final Label PARTIAL = new Label(0.5f);
    static final Label TOTAL   = new Label(1.0f);


    public Label(float aValue) {
    	this( aValue, true );
    }

    public Label(float aValue, boolean limit) {
    	if( aValue > 1 )
    		if( limit )
    			aValue = 1;
        myValue = aValue;
    }

    /**
     * Access method for the value, hides retrieval
     * @return the value as an int
     */
    public float getValue() {
        return myValue;
    }

    /* Comparison method
     */

    public int compareTo(Label aLabel) {
        Label tempLabel = (Label) aLabel; // ????
        // now use the comparison between floats
        float diff = tempLabel.getValue() - this.getValue();
        if (Math.abs(diff) < Float.MIN_VALUE)
            return 0;
        else if (diff > 0.0)
            return -1;
        else return 1;
    }
    
    public String toString() {
    	return "" + myValue;
    }
}
