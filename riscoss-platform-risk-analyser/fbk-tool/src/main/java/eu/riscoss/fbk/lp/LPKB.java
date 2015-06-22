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

import java.util.ArrayList;
import java.util.List;

import eu.riscoss.fbk.language.Proposition;


public class LPKB
{
	ValueMap	index = new ValueMap();
	List<Node>	nodes = new ArrayList<Node>();
	
	public Node		TRUE = new Node( new Label( 1f ), new Label( 0f ) );
	public Node		FALSE = new Node( new Label( 0f ), new Label( 1f ) );
	
	public LPKB() {
		nodes.add( TRUE );
		nodes.add( FALSE );
	}
	
	public Node store( Proposition p, String label ) {
		
		Node node = null;
		
		if( index.getNode( p.getId(), label ) == null ) {
			node = index.store( p, label );
			nodes.add( node );
		}
		else
			node = index.store( p, label );
		
		node.setName( p.getId() );
		
		return node;
	}
	
	public void addRelation( Edge rel ) {
		// FIXME required?
//		graph.addRelation( rel );
	}
	
	public Node getNode( String id, String val ) {
		return index.getNode( id, val );
	}
	
	public ValueMap index()
	{
		return index;
	}

	public List<Node> nodes() {
		return nodes;
	}
}
