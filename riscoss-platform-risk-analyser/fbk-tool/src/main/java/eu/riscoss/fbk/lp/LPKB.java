package eu.riscoss.fbk.lp;

import eu.riscoss.fbk.language.Proposition;


public class LPKB
{
	ValueMap	index = new ValueMap();
	Graph		graph = new Graph();
	
	public Node		TRUE = new Node( new Label( 1f ), new Label( 0f ) );
	public Node		FALSE = new Node( new Label( 0f ), new Label( 1f ) );
	
	public LPKB()
	{
		graph.addNode( TRUE );
		graph.addNode( FALSE );
	}
	
	public Node store( Proposition p, String label )
	{
		Node node = null;
		
		if( index.getNode( p.getId(), label ) == null )
		{
			node = index.store( p, label );
			
			graph.addNode( node );
		}
		else
			node = index.store( p, label );
		
		return node;
	}
	
	public void addRelation( eu.riscoss.fbk.lp.Relation rel )
	{
		graph.addRelation( rel );
	}
	
	public Node getNode( String id, String val )
	{
		return index.getNode( id, val );
	}

	public Graph getGraph()
	{
		return graph;
	}
	
	public ValueMap index()
	{
		return index;
	}
}
