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

package eu.riscoss.fbk.language;

import java.util.HashMap;
import java.util.Map;

import eu.riscoss.fbk.util.MultiMap;

public class Solution
{
	class ValueMap
	{
		MultiMap<String,String> values = new MultiMap<String,String>();
		
		ValueMap()
		{
		}
		
		public boolean contains( String value )
		{
			return values.getList( "" ).contains( value );
		}

		public void put( String field, String value )
		{
			values.put( field, value );
		}
	}
	
	Map<String,ValueMap>			variables = new HashMap<String,ValueMap>();
	
	Map<String, String>				properties	= new HashMap<String, String>();
	
	public boolean hasValue( String var, String val )
	{
		ValueMap values = variables.get( var );
		
		if( values == null ) return false;
		
		return values.contains( val );
	}
	
	public void addValue( String variable, String value )
	{
		ValueMap values = variables.get( variable );
		
		if( values == null )
		{
			values = new ValueMap();
			
			variables.put( variable, values );
		}
		
		values.put( "", value );
	}
	
	public void clear()
	{
		properties.clear();
		variables.clear();
	}
	
	public String getProperty( String prop )
	{
		return properties.get( prop );
	}
	
	public void setProperty( String key, String value )
	{
		properties.put( key, value );
	}
	
	public Iterable<String> variables()
	{
		return this.variables.keySet();
	}
	
	public Iterable<String> values( String var )
	{
		return variables.get( var ).values;
	}

	public void addValue( String var, String field, String value )
	{
		ValueMap values = variables.get( var );
		
		if( values == null )
		{
			values = new ValueMap();
			
			variables.put( var, values );
		}
		
		values.put( field, value );
	}

	public Iterable<String> fields( String var )
	{
		return variables.get( var ).values.keySet();
	}

	public Iterable<String> values( String var, String field )
	{
		return variables.get( var ).values.getList( field );
	}

	public Iterable<String> properties() {
		return properties.keySet();
	}
}
