package eu.riscoss.fbk.language;

import java.util.HashSet;
import java.util.Set;

public class Cost
{
	// Name of the cost, e.g. "time", "money" etc
	String				name;
	
	// Predicate subject to cost, e.g. "st", "sf", "vio", ...
	String				variable;
	
	// Cost value
	int					value;
	
	// Variables, e.g. "s1", "s2", "n1" etx
	Set<String> ids = new HashSet<String>();
//	ArrayList<String>	ids	= new ArrayList<String>();
	
	public Cost( String costName )
	{
		this.name = costName;
	}
	
	public Iterable<String> variables()
	{
		return ids;
	}
	
	public String getPredicate()
	{
		return variable;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public String getName()
	{
		return name;
	}
}
