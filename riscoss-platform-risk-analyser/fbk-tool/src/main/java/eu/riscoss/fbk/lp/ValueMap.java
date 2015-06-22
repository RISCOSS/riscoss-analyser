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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.riscoss.fbk.language.Proposition;

public class ValueMap implements Iterable<String> {
	
	static final List<String> emptyList = new ArrayList<String>();
	
	Map<String,Chunk> map = new HashMap<String,Chunk>();
	
	public Chunk store( Proposition p ) {
		
		Chunk c = map.get( p.getId() );
		
		if( c != null ) return c;
		
		c = new Chunk();
		
		c.proposition = p;
		
		map.put( p.getId(), c );
		
		return c;
	}
	
	public Node store( Proposition p, String predicate ) {
		
		Chunk c = store( p );
		
		Node node = c.predicates.get( predicate );
		
		if( node == null ) {
			node = new Node( new Label( 0f ), new Label( 0f ) );
			c.predicates.put( predicate, node );
		}
		
		return node;
	}
	
	public Chunk getChunk( String id ) {
		return map.get( id );
	}
	
	public Proposition getProposition( String id ) {
		
		Chunk c = map.get( id );
		
		if( c == null ) return null;
		
		return c.proposition;
	}
	
	public Iterable<String> predicates( String id ) {
		
		Chunk c = map.get( id );
		
		if( c == null ) return emptyList;
		
		return c.predicates.keySet();
	}
	
	public Node getNode( String id, String predicate ) {
		
		Chunk c = map.get( id );
		
		if( c == null ) return null;
		
		return c.predicates.get( predicate );
	}
	
	@Override
	public Iterator<String> iterator() {
		return map.keySet().iterator();
	}
}