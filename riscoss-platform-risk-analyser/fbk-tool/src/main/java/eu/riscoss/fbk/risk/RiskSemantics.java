package eu.riscoss.fbk.risk;

import eu.riscoss.fbk.semantics.Axiom;
import eu.riscoss.fbk.semantics.Condition;
import eu.riscoss.fbk.semantics.Operator;
import eu.riscoss.fbk.semantics.Rule;
import eu.riscoss.fbk.semantics.Semantics;

public class RiskSemantics extends Semantics {
	
	public RiskSemantics() {
		
		rules.put( "expose", 
				new Rule( "possible", Rule.TYPE.SAT_SAT,
						new Condition( "st", Operator.Equals ) ) );
		rules.put( "expose", 
				new Rule( "possible", Rule.TYPE.DEN_DEN,
						new Condition( "st", Operator.Equals ) ) );
		
		rules.put( "expose", 
				new Rule( "possible", Rule.TYPE.SAT_SAT,
						new Condition( "threat", Operator.Equals ) ) );
		rules.put( "expose", 
				new Rule( "possible", Rule.TYPE.DEN_DEN,
						new Condition( "threat", Operator.Equals ) ) );
		
		rules.put( "protect", 
				new Rule( "possible", Rule.TYPE.SAT_DEN,
						new Condition( "st", Operator.Equals ) ) );
		rules.put( "protect", 
				new Rule( "possible", Rule.TYPE.DEN_SAT,
						new Condition( "st", Operator.Equals ) ) );
		
		rules.put( "increase", 
				new Rule( "critical", Rule.TYPE.SAT_SAT,
						new Condition( "st", Operator.Equals ) ) );
//		rules.put( "increase", 
//				new Rule( "critical", Rule.TYPE.SAT_DEN,
//						new Condition( "st", Operator.Equals ) ) );
		
		rules.put( "reduce", 
				new Rule( "critical", Rule.TYPE.SAT_DEN,
						new Condition( "st", Operator.Equals ) ) );
		
		rules.put( "indicate", 
				new Rule( "st", 
						new Condition( "st", Operator.Equals ) ) );
		rules.put( "impact", 
				new Rule( "threatened", 
						new Condition( "threat", Operator.Equals ) ) );
		rules.put( "impact", 
				new Rule( "threat", 
						new Condition( "threat", Operator.Equals ) ) );
		rules.put( "satisfy", 
				new Rule( "st", 
						new Condition( "st", Operator.Equals ) ) );
		rules.put( "break", 
				new Rule( "st", Rule.TYPE.SAT_DEN, 
						new Condition( "st", Operator.Equals ) ) );
		
		rules.put( "decomposition", 
				new Rule( Rule.Exists, "threatened", 
						new Condition( "threatened", Operator.Equals ) ) );
		rules.put( "meansEnd", 
				new Rule( "threatened", 
						new Condition( "threatened", Operator.Equals ) ) );
		rules.put( "means-end", 
				new Rule( "threatened", 
						new Condition( "threatened", Operator.Equals ) ) );
		rules.put( "contribution", 
				new Rule( "threatened", 
						new Condition( "threatened", Operator.Equals ) ) );
		rules.put( "depender", 
				new Rule( "threatened", 
						new Condition( "threatened", Operator.Equals ) ) );
		rules.put( "dependee", 
				new Rule( "threatened", 
						new Condition( "threatened", Operator.Equals ) ) );
		
		
		putAxiom( "event", 
				new Axiom( Axiom.TYPE.SAT_SAT, "threat", 
						new Condition( "possible", Operator.Equals ),
						new Condition( "critical", Operator.Equals ) ) );
//		putAxiom( "event", 
//				new Axiom( Axiom.TYPE.DEN_DEN, "threat",
//						new Condition( "possible", Operator.Equals ),
//						new Condition( "critical", Operator.Equals ) ) );
		putAxiom( "event", 
				new Axiom( Axiom.TYPE.SAT_DEN, "threat",
						new Condition( "possible", Operator.Equals ),
						new Condition( "not_critical", Operator.Equals ) ) );
		putAxiom( "event", 
				new Axiom( Axiom.TYPE.DEN_SAT, "not_critical",
						new Condition( "critical", Operator.Equals ) ) );
		
		putAxiom( "event", 
				new Axiom( Axiom.TYPE.DEN_SAT, "not_possible",
						new Condition( "possible", Operator.Equals ) ) );
		putAxiom( "event", 
				new Axiom( Axiom.TYPE.SAT_DEN, "threat",
						new Condition( "not_possible", Operator.Equals ),
						new Condition( "critical", Operator.Equals ) ) );
		
		
		putAxiom( "event",
				new Axiom( Axiom.TYPE.SAT_SAT, "output",
						new Condition( "threat", Operator.Equals ) ) );
		putAxiom( "event",
				new Axiom( Axiom.TYPE.DEN_DEN, "output",
						new Condition( "threat", Operator.Equals ) ) );
		
		putAxiom( "situation",
				new Axiom( Axiom.TYPE.SAT_SAT, "output",
						new Condition( "st", Operator.Equals ) ) );
		putAxiom( "situation",
				new Axiom( Axiom.TYPE.DEN_DEN, "output",
						new Condition( "st", Operator.Equals ) ) );
		putAxiom( "goal",
				new Axiom( "threatened",
						new Condition( "sf", Operator.Equals ) ) );
		putAxiom( "goal",
				new Axiom( "output",
						new Condition( "threatened", Operator.Equals ) ) );
		putAxiom( "goal",
				new Axiom( "output",
						new Condition( "threatened", Operator.Equals ) ) );
		putAxiom( "task",
				new Axiom( "threatened",
						new Condition( "sf", Operator.Equals ) ) );
		putAxiom( "task",
				new Axiom( "output",
						new Condition( "threatened", Operator.Equals ) ) );
		
		
	}
}
