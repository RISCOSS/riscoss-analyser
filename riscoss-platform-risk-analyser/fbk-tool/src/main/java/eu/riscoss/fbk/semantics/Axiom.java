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

package eu.riscoss.fbk.semantics;

import java.util.ArrayList;

public class Axiom
{
	public enum TYPE {
		SAT_SAT, SAT_DEN, DEN_SAT, DEN_DEN
	}
	
	public static final int ALL = 0;
	public static final int ATLEAST = 1;
	
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	public String p_then;
	
	int operator = ALL;
	
//	public int fx = 1;
	TYPE type = TYPE.SAT_SAT;
	public String mnemonic = "" + this.hashCode();
	private String clusterName;
	
	public Axiom( String p_then, String ... p_if )
	{
		this.p_then = p_then;
		
		for( String c : p_if )
			conditions.add( new Condition( c, Operator.Equals ) );
	}
	
	public Axiom( String p_then, Condition ... p_if )
	{
		this.p_then = p_then;
		
		for( Condition c : p_if )
			conditions.add( c );
	}
	
//	public Axiom( int fx, String p_then, Condition ... p_if ) {
//		this.p_then = p_then;
//		for( Condition c : p_if )
//			conditions.add( c );
//		this.fx = fx;
//	}
	
	public Axiom( TYPE type, String p_then, Condition ... p_if ) {
		this.p_then = p_then;
		for( Condition c : p_if )
			conditions.add( c );
		this.type = type;
	}
	
	public Axiom( String p_then, int operator, String ... p_if ) {
		this.p_then = p_then;
		for( String c : p_if )
			conditions.add( new Condition( c, Operator.Equals ) );
		this.operator = operator;
	}
	
	public TYPE getPropagationType() {
		return type;
	}
	
	public String toString() {
		return mnemonic;
	}
	
	public String getIf( int i )
	{
		return conditions.get( i ).getPredicate();
		
//		return p_if[i];
	}
	
	public String[] getIf()
	{
		String[] p_if = new String[conditions.size()];
		
		for( int i = 0; i < conditions.size(); i++ )
		{
			p_if[i] = conditions.get( i ).getPredicate();
		}
		
		return p_if;
	}

	public Iterable<Condition> conditions()
	{
		return conditions;
	}

	public void setCluster(String clusterName) {
		this.clusterName = clusterName;
	}
	
	public String getCluster() {
		return this.clusterName;
	}
}