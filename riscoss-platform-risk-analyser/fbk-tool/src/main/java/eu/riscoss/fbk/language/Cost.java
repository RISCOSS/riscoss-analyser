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

import java.util.HashSet;
import java.util.Set;

public class Cost
{
	// Name of the cost, e.g. "time", "money" etc
	String				name;
	
	// Predicate subject to cost, e.g. "st", "sf", "vio", ...
	String				variable;
	
	// Cost value
	int					value;
	
	// Variables, e.g. "s1", "s2", "n1" etx
	Set<String> ids = new HashSet<String>();
//	ArrayList<String>	ids	= new ArrayList<String>();
	
	public Cost( String costName )
	{
		this.name = costName;
	}
	
	public Iterable<String> variables()
	{
		return ids;
	}
	
	public String getPredicate()
	{
		return variable;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public String getName()
	{
		return name;
	}
}
