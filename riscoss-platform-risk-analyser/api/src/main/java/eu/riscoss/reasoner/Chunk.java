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

package eu.riscoss.reasoner;

import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Generic fragment of information to be passed to and from the platform;
 * can be for example a node of a graph, or a relation, or a weight, or a record, or whatever else
 * 
 * @author albertosiena
 *
 */
public class Chunk {
	
	Address		address = null;
	
	String		id;
	
	/*
	 *  Additional qualifier that represents the "type" of information this chunk is representing
	 *  if not needed, can be ignored by the engine
	 */
	String		stereotype;
	
	public Chunk() {}
	
	public Chunk( String id ) {
		this.id = id;
	}
	
	public Chunk(String id, String stereotype ) {
		this( id );
		this.stereotype = stereotype;
	}

	public Chunk( Address addr, String id, String stereotype ) {
		this( id );
		this.stereotype = stereotype;
		this.address = addr;
	}

	public String getId() {
		return this.id;
	}
	
	public String getStereotype() {
		return stereotype;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public String toUrl() {
		return "addr=" + address + "&id=" + id + "&stereotype=" + stereotype;
	}
	
	public String toString() {
		return toUrl();
	}
	
	public static Chunk fromUrl( String url ) {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		try {
			String[] pairs = url.split("&");
			for (String pair : pairs) {
				int idx = pair.indexOf("=");
				if( idx < 0 ) continue;
				query_pairs.put(
						URLDecoder.decode(pair.substring(0, idx), "UTF-8"), 
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8") );
			}
			Chunk c = new Chunk( query_pairs.get( "id" ) );
			if( query_pairs.get( "stereotype" ) != null )
				c.stereotype = query_pairs.get( "stereotype" );
			if( query_pairs.get( "addr" ) != null )
				c.address = new Address( query_pairs.get( "addr" ) );
			return c;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			return null;
		}
	}
}
