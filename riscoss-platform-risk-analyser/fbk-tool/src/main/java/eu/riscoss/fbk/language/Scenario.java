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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Scenario
{
	public static class Pair {
		public String	label;
		public String	value;
		
		public Pair( String k, String v ) {
			label = k;
			value = v;
		}
		
		public String toString() {
			return "[" + label + "=" + value + "]";
		}
	}
	
	public static class ConstraintSet implements Collection<Pair> {
		
		class ConstraintSetIterator implements Iterator<Pair> {
			
			Iterator<String> keyIt = constraints.keySet().iterator();
			Iterator<String> valueIt = null;
			String label;
			
			ConstraintSetIterator() {
				if( keyIt.hasNext() ) {
					label = keyIt.next();
					valueIt = constraints.get( label ).iterator();
				}
			}
			
			@Override
			public boolean hasNext() {
				return valueIt.hasNext();
			}
			
			@Override
			public Pair next() {
				String val = valueIt.next();
				Pair pair = new Pair( label, val );
				if( !valueIt.hasNext() ) {
					if( keyIt.hasNext() ) {
						label = keyIt.next();
						valueIt = constraints.get( label ).iterator();
					}
				}
				return pair;
			}
			
			@Override
			public void remove() {
				throw new RuntimeException( "Unimplemented" );
			}
			
		}
		
		Map<String,ArrayList<String>> constraints = new HashMap<String,ArrayList<String>>();
		
		public Pair getPair( String key ) {
			ArrayList<String> list = constraints.get( key );
			if( list == null ) return null;
			if( list.size() < 1 ) return null;
			return new Pair( key, list.get( 0 ) );
		}
		
		public void add( String label, String value ) {
			ArrayList<String> list = constraints.get( label );
			if( list == null ) {
				list = new ArrayList<String>();
				constraints.put( label, list );
			}
			list.add( value );
		}
		
		public void clear() {
			constraints.clear();
		}
		
		public String getValue( String label ) {
			ArrayList<String> list = constraints.get( label );
			if( list == null ) return null;
			if( list.size() < 1 ) return null;
			return list.get( 0 );
		}
		
		public void remove( String label ) {
			constraints.remove( label );
		}
		
		@Override
		public int size() {
			return constraints.size();
		}
		
		@Override
		public boolean isEmpty() {
			return constraints.isEmpty();
		}
		
		@Override
		public boolean contains( Object o ) {
			if( !(o instanceof Pair) ) return false;
			
			return constraints.containsKey( ((Pair)o).label );
		}
		
		@Override
		public Iterator<Pair> iterator() {
			return new ConstraintSetIterator();
		}
		
		@Override
		public Object[] toArray() {
			throw new RuntimeException("Unimplemented");
		}
		
		@Override
		public <T> T[] toArray( T[] a ) {
			throw new RuntimeException("Unimplemented");
		}
		
		@Override
		public boolean add( Pair e ) {
			throw new RuntimeException("Unimplemented");
		}
		
		@Override
		public boolean remove( Object o ) {
			throw new RuntimeException("Unimplemented");
		}
		
		@Override
		public boolean containsAll( Collection<?> c ) {
			throw new RuntimeException("Unimplemented");
		}
		
		@Override
		public boolean addAll( Collection<? extends Pair> c ) {
			throw new RuntimeException("Unimplemented");
		}
		
		@Override
		public boolean removeAll( Collection<?> c ) {
			throw new RuntimeException("Unimplemented");
		}
		
		@Override
		public boolean retainAll( Collection<?> c ) {
			throw new RuntimeException("Unimplemented");
		}

		public String getValue( String label, int index ) {
			ArrayList<String> list = constraints.get( label );
			if( list == null ) return null;
			if( list.size() <= index ) return null;
			return list.get( index );
		}

		public void set( String label, String value ) {
			ArrayList<String> list = new ArrayList<String>();
			constraints.put( label, list );
			list.add( value );
		}
	}
	
	public static class ConstraintMap {
		
		HashMap<String, ConstraintSet>	constraints	= new HashMap<String, ConstraintSet>();
		
		public void add( String id, String label, String value ) {
			ConstraintSet set = constraints.get( id );
			if( set == null ) {
				set = new ConstraintSet();
				constraints.put( id, set );
			}
			set.add( label, value );
		}
		
		public void set( String id, String label, String value ) {
			ConstraintSet set = constraints.get( id );
			if( set == null ) {
				set = new ConstraintSet();
				constraints.put( id, set );
			}
			set.set( label, value );
		}
		
		public Collection<ConstraintSet> values() {
			return constraints.values();
		}
		
		public void clear() {
			
			for( ConstraintSet set : constraints.values() ) {
				set.clear();
			}
			
			constraints.clear();
		}
		
		public Iterable<String> keySet() {
			return constraints.keySet();
		}
		
		public String getConstraint( String id, String label ) {
			
			ConstraintSet set = constraints.get( id );
			
			if( set == null ) return null;
			
			return set.getValue( label );
		}
		
		public void remove( String id, String label ) {
			
			ConstraintSet set = constraints.get( id );
			
			if( set == null ) return;
			
			set.remove( label );
			
		}
		
		public Collection<Pair> constraintsOf( String id ) {
			
			ConstraintSet set = constraints.get( id );
			
			if( set == null ) return emptylist;
			
			return set;
		}

		public List<String> constraintsOf( String id, String label ) {
			
			ConstraintSet set = constraints.get( id );
			
			if( set == null ) return new ArrayList<String>();
			
			List<String> list = set.constraints.get( label );
			
			if( list == null ) list = new ArrayList<String>();
			
			return list;
		}

		public String getConstraint( String id, String label, int index ) {
			ConstraintSet set = constraints.get( id );
			if( set == null ) return null;
			return set.getValue( label, index );
		}
		
	}
	
	ConstraintMap						constraints	= new ConstraintMap();
	
	//	HashMap<String, ArrayList<Pair>>	constraints	= new HashMap<String, ArrayList<Pair>>();
	
	Model								model;
	
	CostStructure						costs		= new CostStructure();
	
	static final ArrayList<Pair>		emptylist	= new ArrayList<Pair>();
	
	public Scenario( Model model ) {
		this.model = model;
	}
	
	public void addConstraint( String id, String label ) {
		
		constraints.add( id, label, null );
		
		//		ArrayList<Pair> list = constraints.get( id );
		//		if( list == null ) {
		//			list = new ArrayList<Pair>();
		//			constraints.put( id, list );
		//		}
		//		list.add( new Pair( label, null ) );
	}
	
	public void addConstraint( String id, String label, String value ) {
		
		constraints.add( id, label, value );
		
		//		ArrayList<Pair> list = constraints.get( id );
		//		if( list == null ) {
		//			list = new ArrayList<Pair>();
		//			constraints.put( id, list );
		//		}
		//		list.add( new Pair( label, value ) );
	}
	
	public void setConstraint( String id, String label, String value ) {
		
		constraints.set( id, label, value );
		
		//		ArrayList<Pair> list = constraints.get( id );
		//		if( list == null ) {
		//			list = new ArrayList<Pair>();
		//			constraints.put( id, list );
		//		}
		//		else {
		//			list.clear();
		//		}
		//		list.add( new Pair( label, value ) );
	}
	
	public void removeConstraints() {
		constraints.clear();
		
		//		for( String key : constraints.keySet() ) {
		//			constraints.get( key ).clear();
		//		}
		//		constraints.clear();
	}
	
	public Collection<Pair> constraintsOf( String id ) {
		
		return constraints.constraintsOf( id );
		
		//		ArrayList<Pair> ret = constraints.get( id );
		//		
		//		if( ret == null ) {
		//			ret = emptylist;
		//		}
		//		
		//		return ret;
	}
	
	public List<String> constraintsOf( String id, String label ) {
		return constraints.constraintsOf( id, label );
	}
	
	public CostStructure getCostStructure() {
		return this.costs;
	}
	
	public Iterable<String> keys() {
		return constraints.keySet();
	}
	
	public String getConstraint( String id, String label ) {
		
		return constraints.getConstraint( id, label );
		
		//		ArrayList<Pair> list = constraints.get( id );
		//		if( list == null ) return "";
		//		
		//		for( Pair pair : list )
		//		{
		//			if( pair.label.equals( label ) )
		//				return pair.value;
		//		}
		//		
		//		return "";
	}
	
	public void removeConstraint( String id, String label ) {
		
		constraints.remove( id, label );
		
		//		ArrayList<Pair> list = constraints.get( id );
		//		if( list == null ) return;
		//		
		//		ArrayList<Pair> rem = new ArrayList<Pair>();
		//		
		//		for( Pair pair : list )
		//			if( pair.label.equals( label ) )
		//				rem.add( pair );
		//		
		//		for( Pair pair : rem )
		//			list.remove( pair );
	}
	
	public void clear() {
		constraints.clear();
		costs.clear();
	}

	public String getConstraint( String id, String label, int index ) {
		return constraints.getConstraint( id, label, index );
	}
}
