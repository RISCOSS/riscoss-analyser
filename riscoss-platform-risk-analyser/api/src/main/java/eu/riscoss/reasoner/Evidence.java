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

public class Evidence {
	
	double p, m;
	
	public Evidence( double plus, double minus ) {
		p = round( plus );
		m = round( minus );
	}
	
	public Evidence( Distribution d ) {
		int count = d.getValues().size();
		double value = 0;
		for( Double val : d.getValues() ) {
			p += val.doubleValue() * value;
			value += (1.0 / count);
		}
		value = 1;
		for( Double val : d.getValues() ) {
			m += val.doubleValue() * value;
			value -= (1.0 / count);
		}
	}
	
	private double round( double d ) {
		return ((double)((int)(d * 10000))) / 10000;
	}
	
	public double getPositive() {
		return p;
	}
	
	public double getNegative() {
		return m;
	}
	
	public String toString() {
		return "[+" + p + "," + "-" + m + "]";
	}
	
	public void set( double p, double n ) {
		this.p = p;
		this.m = n;
	}

	public boolean nonZero() {
		return (m != 0) | (p != 0);
	}

	public double getDirection() {
		return (p - m);
	}

	public double getConflict() {
		return Math.max(p, m) - Math.abs(p - m);
	}

	public double getStrength() {
		return Math.abs( p + m - p * m );
//		return (p + m) /2;
	}
	
	public double getSignal() {
		return (1 + getDirection()) /2;
	}

	public static Evidence unpack(String value) {
		if( value == null ) return new Evidence( 0, 0 );
		String[] parts = value.split( "[;]" );
		if( parts.length != 2 ) return new Evidence( 0, 0 );
		try {
			double p = Double.parseDouble( parts[0] );
			double m = Double.parseDouble( parts[1] );
			return new Evidence( p, m );
		}
		catch( Exception ex ) {
			return new Evidence( 0, 0 );
		}
//		return null;
	}
	
	public String pack() {
		return this.p + ";" + this.m;
	}
}
