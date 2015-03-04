package eu.riscoss.fbk.lp;


import java.util.HashMap;

import eu.riscoss.fbk.language.Proposition;

public class Chunk
{
	Proposition				proposition;
	HashMap<String,Node>	predicates = new HashMap<String,Node>();

	public Iterable<String> predicates()
	{
		return predicates.keySet();
	}
	
	public Node getPredicate( String id )
	{
		return predicates.get( id );
	}

	public Proposition getProposition()
	{
		return proposition;
	}
}