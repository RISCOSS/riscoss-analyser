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
