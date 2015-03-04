package eu.riscoss.fbk;

import eu.riscoss.fbk.language.Model;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.lp.FunctionsLibrary;
import eu.riscoss.fbk.lp.JsExtension;
import eu.riscoss.fbk.risk.RiskEvaluation;

public class FBKCostAnalysisTest {
	public static void main( String[] args ) {
		Program program = new Program();
		Model model = program.getModel();
		model.addProposition( new Proposition( "situation", "s1" ) );
		model.addProposition( new Proposition( "situation", "s2" ) );
		model.addProposition( new Proposition( "event", "e1" ) );
		model.getProposition( "s1" ).setProperty( "cost", "2" );
		model.getProposition( "s2" ).setProperty( "cost", "3" );
		Relation r;
		r = new Relation( "expose" );
		r.addSource( model.getProposition( "s1" ) );
		r.setTarget( model.getProposition( "e1" ) );
		model.addRelation( r );
		r = new Relation( "increase" );
		r.addSource( model.getProposition( "s2" ) );
		r.setTarget( model.getProposition( "e1" ) );
		model.addRelation( r );
		
		program.getScenario().addConstraint( "s1", "st", "1.0" );
		program.getScenario().addConstraint( "s2", "st", "0.5" );
		
		RiskEvaluation reasoner = new RiskEvaluation();
		JsExtension.get().put( "fx", FunctionsLibrary.get() );
		reasoner.run( program );
		
		double cc = 0;
		for( Proposition p : program.getModel().propositions() ) {
			double cost = Double.parseDouble( p.getProperty( "cost", "1" ) );
			cc += (reasoner.getEvidence( p.getId() ).getSignal() * cost);
			System.out.println( 
					p.getId() + ": " + reasoner.getEvidence( p.getId() ) + " - " +
					reasoner.getEvidence( p.getId() ).getSignal() + " * " + cost + " = " +
					reasoner.getEvidence( p.getId() ).getSignal() * cost );
		}
		System.out.println( "Solution cost: " + cc );
	}
}
