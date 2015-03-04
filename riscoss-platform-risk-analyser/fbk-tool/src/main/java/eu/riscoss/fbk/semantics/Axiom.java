package eu.riscoss.fbk.semantics;

import java.util.ArrayList;

public class Axiom
{
	public enum TYPE {
		SAT_SAT, SAT_DEN, DEN_SAT, DEN_DEN
	}
	
	public static final int ALL = 0;
	public static final int ATLEAST = 1;
	
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	public String p_then;
	
	int operator = ALL;
	
//	public int fx = 1;
	TYPE type = TYPE.SAT_SAT;
	public String mnemonic = "" + this.hashCode();
	
	public Axiom( String p_then, String ... p_if )
	{
		this.p_then = p_then;
		
		for( String c : p_if )
			conditions.add( new Condition( c, Operator.Equals ) );
	}
	
	public Axiom( String p_then, Condition ... p_if )
	{
		this.p_then = p_then;
		
		for( Condition c : p_if )
			conditions.add( c );
	}
	
//	public Axiom( int fx, String p_then, Condition ... p_if ) {
//		this.p_then = p_then;
//		for( Condition c : p_if )
//			conditions.add( c );
//		this.fx = fx;
//	}
	
	public Axiom( TYPE type, String p_then, Condition ... p_if ) {
		this.p_then = p_then;
		for( Condition c : p_if )
			conditions.add( c );
		this.type = type;
	}
	
	public Axiom( String p_then, int operator, String ... p_if ) {
		this.p_then = p_then;
		for( String c : p_if )
			conditions.add( new Condition( c, Operator.Equals ) );
		this.operator = operator;
	}
	
	public TYPE getPropagationType() {
		return type;
	}
	
	public String toString() {
		return mnemonic;
	}
	
	public String getIf( int i )
	{
		return conditions.get( i ).getPredicate();
		
//		return p_if[i];
	}
	
	public String[] getIf()
	{
		String[] p_if = new String[conditions.size()];
		
		for( int i = 0; i < conditions.size(); i++ )
		{
			p_if[i] = conditions.get( i ).getPredicate();
		}
		
		return p_if;
	}

	public Iterable<Condition> conditions()
	{
		return conditions;
	}
}