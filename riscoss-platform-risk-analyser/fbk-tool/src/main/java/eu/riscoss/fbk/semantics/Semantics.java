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
import java.util.List;

import eu.riscoss.fbk.util.MultiMap;

public class Semantics {
	static final ArrayList<Axiom>	emptyAxiomList	= new ArrayList<Axiom>();
	
	private ArrayList<String> clusters = new ArrayList<>();
	
	private MultiMap<String, Axiom>	axioms			= new MultiMap<String, Axiom>();
	
	public MultiMap<String, Rule>	rules			= new MultiMap<String, Rule>();

	
	public Iterable<Axiom> axioms(String type) {
		if (axioms.get(type) == null)
			return emptyAxiomList;
		
		return axioms.getList(type);
	}
	
	public void putAxiom(String type, String clusterName, Axiom axiom ) {
		axiom.mnemonic = type;
		axioms.put(type, axiom);
		axiom.setCluster( clusterName );
	}
	
	public void addRule( String type, String clusterName, Rule rule ) {
		rules.put( type, rule );
		rule.setCluster( clusterName );
	}
	
	public int getAxiomCount() {
		return axioms.size();
	}
	
	public int getAxiomCount( String type ) {
		return axioms.count( type );
	}
	
	public List<Rule> rules( String type ) {
		return rules.list( type );
	}
	
	public void addCluster( String clusterName ) {
		this.clusters.add( clusterName );
	}
	
	public List<String> clusters() {
		return clusters;
	}
}
