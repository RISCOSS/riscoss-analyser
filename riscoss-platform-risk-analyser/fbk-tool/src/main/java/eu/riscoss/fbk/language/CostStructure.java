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

import java.util.ArrayList;
import java.util.HashMap;

public class CostStructure
{
	ArrayList<String>						order	= new ArrayList<String>();
	
	/*
	 * E.g.:
	 * costname:"time" => {
	 *   predicate:"st" =>  {
	 *     costvalue:"1" => {
	 *       ids:"s1", "s2"
	 *     }
	 *   }
	 * }
	 */
	HashMap<String, HashMap<String, HashMap<Integer,Cost>>>	costs	= new HashMap<String, HashMap<String, HashMap<Integer,Cost>>>();
	
	public int getCostTypeCount()
	{
		return order.size();
	}
	
	public void addCostType( String costName )
	{
		if( costs.get( costName ) != null )
			return;
		
		order.add( costName );
		
		costs.put( costName, new HashMap<String, HashMap<Integer,Cost>>() );
	}
	
	public void addCost( String costname, String costvalue, String predicate, String[] ids )
	{
		int value = Integer.parseInt( costvalue );
		
		HashMap<String, HashMap<Integer,Cost>> costmap = costs.get( costname );
		
		if( costmap == null ) addCostType( costname );
		
		costmap = costs.get( costname );
		
		HashMap<Integer,Cost> costs = costmap.get( predicate );
		
		if( costs == null )
		{
			costs = new HashMap<Integer,Cost>();
			costmap.put( predicate, costs );
		}
		
		Cost cost = costs.get( value );
		
		if( cost == null )
		{
			cost = new Cost( costname );
			cost.value = value;
			cost.variable = predicate;
			costs.put( value, cost );
		}
		
		for( String id : ids )
		{
			cost.ids.add( id );
		}
	}
	
	public void addCost( String costname, String costvalue, String predicate, String id )
	{
		addCost( costname, costvalue, predicate, new String[] { id } );
		
//		HashMap<String, Cost> costmap = costs.get( costname );
//		
//		if( costmap == null )
//		{
//			addCostType( costname );
//			costmap = costs.get( costname );
//		}
//		
//		Cost cost = costmap.get( predicate );
//		
//		if( cost == null )
//		{
//			cost = new Cost( costname );
//			cost.value = Integer.parseInt( costvalue );
//			cost.variable = predicate;
//			costmap.put( predicate, cost );
//		}
//		
//		cost.ids.add( id );
	}
	
//	public String toDatalog( Model model )
//	{
//		StringBuilder b = new StringBuilder();
//		
//		b.append( "% Costs\n" );
//		
//		// First-come, higher-priority
//		int priority = order.size();
//		
//		for( String costname : order )
//		{
//			b.append( "% cost " + priority + ": " + costname + "\n" );
//			
//			HashMap<String, Cost> costmap = costs.get( costname );
//			
//			for( String value : costmap.keySet() )
//			{
//				Cost cost = costmap.get( value );
//				
//				for( String id : cost.ids )
//				{
//					b.append( ":~ " + cost.variable + "(" + id + "). [" + cost.value + ":"
//							+ priority + "]\n" );
//				}
//			}
//			
//			priority--;
//		}
//		
//		b.append( "\n" );
//		
//		return b.toString();
//	}
	
	public Iterable<String> costTypes()
	{
		return order;
	}
	
	public HashMap<String, HashMap<Integer,Cost>> getCosts( String costname )
	{
		return costs.get( costname );
	}

	public void clear()
	{
		order.clear();
		costs.clear();
	}
}
