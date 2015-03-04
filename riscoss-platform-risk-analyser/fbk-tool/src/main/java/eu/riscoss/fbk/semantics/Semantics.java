package eu.riscoss.fbk.semantics;

import java.util.ArrayList;
import java.util.List;

import eu.riscoss.fbk.util.MultiMap;

public class Semantics {
	static final ArrayList<Axiom>	emptyAxiomList	= new ArrayList<Axiom>();
	
	private MultiMap<String, Axiom>	axioms			= new MultiMap<String, Axiom>();
	public MultiMap<String, Rule>	rules			= new MultiMap<String, Rule>();
	
	public Iterable<Axiom> axioms(String type) {
		if (axioms.get(type) == null)
			return emptyAxiomList;
		
		return axioms.getList(type);
	}
	
	public void putAxiom(String type, Axiom axiom) {
		axiom.mnemonic = type;
		axioms.put(type, axiom);
	}
	
	public void addRule( String type, Rule rule ) {
		rules.put( type, rule );
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
}
