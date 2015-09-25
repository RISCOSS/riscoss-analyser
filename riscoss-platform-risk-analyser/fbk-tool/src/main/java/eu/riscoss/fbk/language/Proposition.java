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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Proposition {
	
	String						id;
	private ArrayList<Relation>	in	= new ArrayList<Relation>();
	private ArrayList<Relation>	out	= new ArrayList<Relation>();
	HashMap<String,String>		properties = new HashMap<String,String>();
	
	private String classname;
	
	public Proposition( String classname, String id ) {
		this.classname = classname;
		this.id = id;
	}
	
	public Proposition( String id ) {
		classname = this.getClass().getSimpleName().toLowerCase();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String asVar() {
		return "id" + hashCode();
	}
	
	public String toString() {
		return getId();
	}
	
	public Proposition clone() throws CloneNotSupportedException {
		Constructor<?> ctor;
		try
		{
			ctor = getClass().getConstructor( String.class );
			
			Proposition p = (Proposition) ctor.newInstance( getId() );
			
			p.classname = this.classname;
			
			for( String key : this.properties.keySet() ) {
				p.properties.put( key,  this.properties.get( key ) );
			}
			
			return p;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			throw new CloneNotSupportedException();
		}
	}
	
	public Proposition clone( String id ) {
		Constructor<?> ctor;
		try
		{
			ctor = getClass().getConstructor( String.class );
			
			Proposition p = (Proposition) ctor.newInstance( id );
			
			p.classname = this.classname;
			
			for( String key : this.properties.keySet() ) {
				p.properties.put( key,  this.properties.get( key ) );
			}
			
			return p;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			
			return null;
		}
	}
	
	public List<Relation> in() {
		return in;
	}
	
	public List<Relation> out() {
		return out;
	}
	
	public String getStereotype() {
		return classname;
	}
	
	public String getProperty( String name, String def ) {
		String ret = properties.get( name );
		
		if( ret == null ) ret = def;
		
		return ret;
	}
	
	public double getDouble( String name, double def ) {
		String val = properties.get( name );
		
		if( val == null ) return def;
		
		try {
			return Double.parseDouble( val );
		}
		catch( Exception ex ) {
			return def;
		}
	}
	
	public void setProperty( String name, String value ) {
		properties.put( name, value );
	}
	
	public Proposition withProperty( String name, String value ) {
		properties.put( name, value );
		return this;
	}
	
	public void removeProperty( String name ) {
		properties.remove( name );
	}
	
	public Iterable<String> properties() {
		return properties.keySet();
	}
}
