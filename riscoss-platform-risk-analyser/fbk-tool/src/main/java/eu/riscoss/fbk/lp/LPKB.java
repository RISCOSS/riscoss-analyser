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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.riscoss.fbk.language.Proposition;


public class LPKB
{
	ValueMap	index = new ValueMap();
	Set<Node>	nodes = new HashSet<Node>();
	List<String> layers = new ArrayList<>();
	
	Map<String,ArrayList<Edge>> edges = new HashMap<String,ArrayList<Edge>>();
	
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
	
	public void addLayer( String name ) {
		layers.add( name );
		edges.put( name, new ArrayList<Edge>() );
	}
	
	public void addRelation( Edge rel, String clusterName ) {
		ArrayList<Edge> cluster = edges.get( clusterName );
		if( cluster == null ) {
			throw new RuntimeException( "Undefined layer: " + clusterName );
		}
		cluster.add( rel );
	}
	
	public Node getNode( String id, String val ) {
		return index.getNode( id, val );
	}
	
	public ValueMap index()
	{
		return index;
	}

	public Collection<Node> nodes() {
		return nodes;
	}

	public List<Edge> edges( String layerName ) {
		ArrayList<Edge> layer = edges.get( layerName );
		if( layer == null ) return new ArrayList<Edge>();
		return layer;
	}
	
	public Collection<String> layers() {
		return layers;
	}
}
