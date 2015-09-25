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

public class Relation implements Cloneable
{
	private static long num = 0;
	private synchronized static String nextid() {
		num++;
		if( num > (Long.MAX_VALUE -1) ) num = 0;
		return "r:" + num + "@" + System.currentTimeMillis();
	}
	
	
	public static final int	AND			= 0;
	public static final int	OR			= 1;
	
	private String classname;
	
	String id = nextid();
	
	Proposition				target		= null;
	ArrayList<Proposition>	sources		= new ArrayList<Proposition>();
	int						operator	= AND;
	boolean					negative	= false;
	
	HashMap<String,String>		properties = new HashMap<String,String>();
	
	public Relation()
	{
		classname = getClass().getSimpleName().toLowerCase();
	}
	
	public Relation( String stereotype )
	{
		classname = stereotype;
	}
	
	public String getId() {
		return id;
	}
	
	public String getStereotype()
	{
		return classname;
	}
	
	public Relation clone() throws CloneNotSupportedException {
		Constructor<?> ctor;
		try {
			ctor = getClass().getConstructor();
			
			Relation r = (Relation) ctor.newInstance();
			
			r.classname = this.classname;
			r.operator = operator;
			r.negative = negative;
			
			for( String key : properties.keySet() ) {
				r.properties.put( key, properties.get( key ) );
			}
			
			return r;
		}
		catch( Exception e ) {
			e.printStackTrace();
			
			throw new CloneNotSupportedException();
		}
	}
	
	public String getProperty( String name, String def )
	{
		String ret = properties.get( name );
		
		if( ret == null ) ret = def;
		
		return ret;
	}
	
	public void setProperty( String name, String value )
	{
		if( value == null )
			properties.remove( name );
		else if( "".equals( value ) )
			properties.remove( name );
		else {
			if( "operator".equals( name ) ) {
				setOperator( value );
			}
			else {
				properties.put( name, value );
			}
		}
	}

	public void setTarget( Proposition p )
	{
		if( target != null ) {
			target.in().remove( this );
		}
		target = p;
		if( target != null )
			p.in().add( this );
	}
	
	public void addSource( Proposition p )
	{
		p.out().add( this );
		sources.add( p );
	}
	
	public void destroy()
	{
		for( Proposition p : sources )
		{
			p.out().remove( this );
		}
		sources.clear();
		target.in().remove( this );
		target = null;
	}	
	public int getSourceCount()
	{
		return sources.size();
	}
	
	String rule( String lhs, String rhs )
	{
		return lhs + " :- " + rhs + ".\n";
	}
	
	String rule( String lhs, String[] rhs )
	{
		String s = "";
		String sep = "";
		
		for( String chunk : rhs )
		{
			s += sep + chunk;
			sep = ", ";
		}
		
		return lhs + " :- " + s + ".\n";
	}
	
	String st( List<Proposition> props )
	{
		return pred( "st", props );
	}
	
	String sf( List<Proposition> props )
	{
		return pred( "sf", props );
	}
	
	String su( List<Proposition> props )
	{
		return pred( "su", props );
	}
	
	String pred( String p, List<Proposition> props )
	{
		String ret = "";
		String sep = "";
		
		for( Proposition prop : props )
		{
			ret += sep + pred( p, prop );
			sep = ", ";
		}
		
		return ret;
	}
	
	String st( Proposition s )
	{
		return pred( "st", s );
	}
	
	String sf( Proposition s )
	{
		return pred( "sf", s );
	}
	
	String su( Proposition s )
	{
		return pred( "su", s );
	}
	
	String pred( String p, Proposition s )
	{
		return pred( p, s.getId() );
	}
	
	String pred( String p, String var )
	{
		return p + "(" + var + ")";
	}
	
	String at( Proposition s )
	{
		return pred( "at", s );
	}
	
	String af( Proposition s )
	{
		return pred( "af", s );
	}
	
	String au( Proposition s )
	{
		return pred( "au", s );
	}
	
	@Override
	public String toString()
	{
		return getStereotype() + "( " + sources + " -> " + target + " ) ";
	}
	
	@Deprecated
	public void setOperator( int op )
	{
		this.operator = op;
	}
	
	public void setOperator( String op ) {
		if( "and".equalsIgnoreCase( op ) ) {
			setProperty( "function", "fx.join.and(sources)" );
		}
		else if( "or".equalsIgnoreCase( op ) ) {
			setProperty( "function", "fx.join.or(sources)" );
		}
		else if( "avg".equalsIgnoreCase( op ) ) {
			setProperty( "function", "fx.join.avg(sources)" );
		}
	}
	
	@Deprecated
	public int getOperator()
	{
		return this.operator;
	}
	
	public Proposition getTarget()
	{
		return target;
	}
	
	public ArrayList<Proposition> getSources()
	{
		return sources;
	}
	
	public void setSources( ArrayList<Proposition> s )
	{
		sources.clear();
		for( Proposition p : s )
			addSource( p );
	}
	
	public void setNegative( boolean b )
	{
		this.negative = b;
	}
	
	public boolean isNegative()
	{
		return negative;
	}

	public Iterable<String> properties()
	{
		return properties.keySet();
	}
	
	public float getWeight() {
        try {
            return Float.parseFloat( getProperty("weight", "1") );
        } catch (Exception ex) {
        }

        return 1;
	}
	
	public void setWeight( float w ) {
		setProperty( "weight", "" + w );
	}

	public void removeSource( String pid ) {
		for( int i = 0; i < sources.size(); i++ ) {
			if( sources.get( i ).getId().equals( pid ) ) {
				sources.remove( i );
				return;
			}
		}
	}
	
	public void removeSource( Proposition p ) {
		p.out().remove( this );
		for( int i = 0; i < sources.size(); i++ ) {
			if( sources.get( i ).getId().equals( p.getId() ) ) {
				sources.remove( i );
				return;
			}
		}
	}

	public Relation withTarget(Proposition proposition) {
		setTarget( proposition );
		return this;
	}
	
	public Relation withSource( Proposition p ) {
		addSource( p );
		return this;
	}
	
	public Relation withProperty( String key, String value ) {
		setProperty( key, value );
		return this;
	}
}
