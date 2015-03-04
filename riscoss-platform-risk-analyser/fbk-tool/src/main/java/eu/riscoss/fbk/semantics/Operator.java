package eu.riscoss.fbk.semantics;

public abstract class Operator
{
	public static final Operator	Equals	= new Operator() {
		@Override
		public boolean eval(String value, String match) {
			return value.equals( match );
		} };
	
	public static final Operator Not = new Operator() {
		@Override
		public boolean eval(String value, String match) {
			return !value.equals( match );
		}};

	public abstract boolean eval( String value, String match );
}
