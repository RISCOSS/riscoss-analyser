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
}
