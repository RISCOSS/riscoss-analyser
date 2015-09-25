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

package eu.riscoss.reasoner;

import eu.riscoss.fbk.semantics.Axiom;
import eu.riscoss.fbk.semantics.Condition;
import eu.riscoss.fbk.semantics.Operator;
import eu.riscoss.fbk.semantics.Rule;
import eu.riscoss.fbk.semantics.Semantics;


public class RiskSemantics extends Semantics {
	
	public RiskSemantics() {
		
		addCluster( "Indicators" );
		addCluster( "Evidences" );
		
		addRule( "expose", "Evidences", 
				new Rule( "possible", Rule.TYPE.SAT_SAT,
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "expose", "Evidences", 
				new Rule( "possible", Rule.TYPE.DEN_DEN,
						new Condition( "sat", Operator.Equals ) ) );
		
		addRule( "expose", "Evidences", 
				new Rule( "possible", Rule.TYPE.SAT_SAT,
						new Condition( "threat", Operator.Equals ) ) );
		addRule( "expose", "Evidences", 
				new Rule( "possible", Rule.TYPE.DEN_DEN,
						new Condition( "threat", Operator.Equals ) ) );
		
		addRule( "protect", "Evidences", 
				new Rule( "possible", Rule.TYPE.SAT_DEN,
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "protect", "Evidences", 
				new Rule( "possible", Rule.TYPE.DEN_SAT,
						new Condition( "sat", Operator.Equals ) ) );
		
		addRule( "increase", "Evidences", 
				new Rule( "critical", Rule.TYPE.SAT_SAT,
						new Condition( "sat", Operator.Equals ) ) );
		
		addRule( "reduce", "Evidences", 
				new Rule( "critical", Rule.TYPE.SAT_DEN,
						new Condition( "sat", Operator.Equals ) ) );
		
		addRule( "indicate", "Indicators", 
				new Rule( "st", 
						new Condition( "st", Operator.Equals ) ) );
//		addRule( "indicate", 
//				new Rule( "sat", 
//						new Condition( "st", Operator.Equals ) ) );
		addRule( "impact", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_DEN,
						new Condition( "threat", Operator.Equals ) ) );
		addRule( "impact", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_SAT,
						new Condition( "threat", Operator.Equals ) ) );
		addRule( "satisfy", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_SAT, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "satisfy", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_DEN, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "break", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_DEN, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "break", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_SAT, 
						new Condition( "sat", Operator.Equals ) ) );
		
		addRule( "decomposition", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_SAT, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "decomposition", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_DEN, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "meansEnd", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_SAT, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "meansEnd", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_DEN, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "means-end", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_SAT, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "means-end", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_DEN, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "contribution", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_SAT, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "contribution", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_DEN, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "depender", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_SAT, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "depender", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_DEN, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "dependee", "Evidences", 
				new Rule( "sat", Rule.TYPE.SAT_SAT, 
						new Condition( "sat", Operator.Equals ) ) );
		addRule( "dependee", "Evidences", 
				new Rule( "sat", Rule.TYPE.DEN_DEN, 
						new Condition( "sat", Operator.Equals ) ) );
		
		
		putAxiom( "event",  "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "threat", 
						new Condition( "possible", Operator.Equals ),
						new Condition( "critical", Operator.Equals ) ) );
		putAxiom( "event", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "threat",
						new Condition( "possible", Operator.Equals ),
						new Condition( "not_critical", Operator.Equals ) ) );
		putAxiom( "event", "Evidences", 
				new Axiom( Axiom.TYPE.DEN_SAT, "not_critical",
						new Condition( "critical", Operator.Equals ) ) );
		
		putAxiom( "event",  "Evidences", 
				new Axiom( Axiom.TYPE.DEN_SAT, "not_possible",
						new Condition( "possible", Operator.Equals ) ) );
		putAxiom( "event",  "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "threat",
						new Condition( "not_possible", Operator.Equals ),
						new Condition( "critical", Operator.Equals ) ) );
		
		
		// Conversion between input st/sf to sat
		putAxiom( "goal", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "sat",
						new Condition( "st", Operator.Equals ) ) );
		putAxiom( "goal", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "sat",
						new Condition( "sf", Operator.Equals ) ) );
		putAxiom( "task", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "sat",
						new Condition( "st", Operator.Equals ) ) );
		putAxiom( "task", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "sat",
						new Condition( "sf", Operator.Equals ) ) );
		putAxiom( "softgoal", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "sat",
						new Condition( "st", Operator.Equals ) ) );
		putAxiom( "softgoal", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "sat",
						new Condition( "sf", Operator.Equals ) ) );
		putAxiom( "resource", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "sat",
						new Condition( "st", Operator.Equals ) ) );
		putAxiom( "resource", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "sat",
						new Condition( "sf", Operator.Equals ) ) );
		putAxiom( "situation", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "sat",
						new Condition( "st", Operator.Equals ) ) );
		putAxiom( "situation", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "sat",
						new Condition( "sf", Operator.Equals ) ) );
		putAxiom( "proposition", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "sat",
						new Condition( "st", Operator.Equals ) ) );
		putAxiom( "proposition", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "sat",
						new Condition( "sf", Operator.Equals ) ) );
		
		// predicates to be reported as output
		putAxiom( "event", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "output",
						new Condition( "threat", Operator.Equals ) ) );
		putAxiom( "event", "Evidences", 
				new Axiom( Axiom.TYPE.DEN_DEN, "output",
						new Condition( "threat", Operator.Equals ) ) );
		putAxiom( "situation", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_SAT, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "situation", "Evidences", 
				new Axiom( Axiom.TYPE.DEN_DEN, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "indicator", "Indicators", 
				new Axiom( Axiom.TYPE.SAT_SAT, "output",
						new Condition( "st", Operator.Equals ) ) );
		
		putAxiom( "goal", "Evidences", 
				new Axiom( Axiom.TYPE.DEN_SAT, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "goal", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "softgoal", "Evidences", 
				new Axiom( Axiom.TYPE.DEN_SAT, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "softgoal", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "task", "Evidences", 
				new Axiom( Axiom.TYPE.DEN_SAT, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "task", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "resource", "Evidences", 
				new Axiom( Axiom.TYPE.DEN_SAT, "output",
						new Condition( "sat", Operator.Equals ) ) );
		putAxiom( "resource", "Evidences", 
				new Axiom( Axiom.TYPE.SAT_DEN, "output",
						new Condition( "sat", Operator.Equals ) ) );
		
	}
}
