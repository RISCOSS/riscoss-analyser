package eu.riscoss.fbk.language;


import java.util.HashMap;

public class Query
{
	HashMap<String, String>	objectives	= new HashMap<String, String>();
	Model				model;
	
	public Query( Model model )
	{
		this.model = model;
	}
	
	public String toDatalog()
	{
		StringBuilder b = new StringBuilder();
		
		b.append( "% Objectives\n" );
		
		if( objectives.size() < 1 )
			return b.toString();
		
		String sep = "";
		
		for( String id : objectives.keySet() )
		{
			b.append( sep + objectives.get( id ) + "(" + id + ")" );
			sep = ", ";
		}
		b.append( " ?\n" );
		
		return b.toString();
	}
	
	public void addObjective( String id, String value )
	{
		objectives.put( id, value );
	}

	public int objectivesCount()
	{
		return objectives.size();
	}
	
	public Iterable<String> objectives()
	{
		return objectives.keySet();
	}

	public String getObjective( String id )
	{
		return objectives.get( id );
	}
}
