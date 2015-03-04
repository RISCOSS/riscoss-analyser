package eu.riscoss.fbk.lp;


import java.util.LinkedList;

public class Graph
{
	LinkedList<Node> nodes;
	
	LinkedList<Relation> relations;
	
	Graph()
	{
		nodes     = new LinkedList<Node>();
		relations = new LinkedList<Relation>();
	}
	
	public Iterable<Relation> relations()
	{
		return relations;
	}
	
	public Iterable<Node> nodes()
	{
		return nodes;
	}
	
	int getNodeCount()
	{
		return nodes.size();
	}
	
	
	public void addNode( Node n )
	{
		nodes.add( n );
	}
	
	public void addRelation( Relation r )
	{
		relations.add( r );
	}
	
	public int propagate() 
	{
		int repetitions = 0;
		
		boolean graphChanged;
		
		do {
			++repetitions;
			
			for( Node node : nodes )
				node.syncLabels();
			
			graphChanged = false;
			
			for( Node currentNode : nodes ) {
				
				if (currentNode.getIncomingRelations().isEmpty()) continue;
				
				for( Relation currentRelation : currentNode.getIncomingRelations() ) {
					currentRelation.propagate();
				}
				if (currentNode.hasChanged()) graphChanged = true;
			}
			
		}
		while (graphChanged == true);
		return repetitions;
	}
}
