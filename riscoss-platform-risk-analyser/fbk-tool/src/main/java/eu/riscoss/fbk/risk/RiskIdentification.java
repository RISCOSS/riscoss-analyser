package eu.riscoss.fbk.risk;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import eu.riscoss.fbk.dlv.DlvReasoner;
import eu.riscoss.fbk.language.Analysis;
import eu.riscoss.fbk.language.Cost;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.language.Result;
import eu.riscoss.fbk.language.Scenario.Pair;
import eu.riscoss.fbk.language.Solution;
import eu.riscoss.fbk.semantics.Axiom;
import eu.riscoss.fbk.semantics.Condition;
import eu.riscoss.fbk.semantics.Operator;
import eu.riscoss.fbk.semantics.Rule;
import eu.riscoss.fbk.sysex.WorkingDirectory;
import eu.riscoss.fbk.util.BidiMap;

public class RiskIdentification implements Analysis
{
	DlvReasoner reasoner = new DlvReasoner();
	
	Program sourceProgram;
	BidiMap	bidiMap = new BidiMap();
	int counter = 0;
	
	boolean useRealId = false;
	
	protected String join( Proposition p, String[] values, String separator )
	{
		String joint = "";
		String sep = "";
		for( String val : values )
		{
			joint += sep + val + "(" + p.getId() + ")";
			sep = separator;
		}
		return joint;
	}
	
	public void run( Program program )
	{
		reasoner.setExecutable( program.getOptions().getValue( "dlvExecutable" ) );
		
		reasoner.addEmitter( "program", new DlvReasoner.Emitter() {
			
			@Override
			public String toDatalog( Program program )
			{
				return compile( program );
			}
		});
		
		if( useRealId == false )
		{
			int num = 0;
			bidiMap.clear();
			for( Proposition p : program.getModel().propositions() )
			{
				p.setProperty( "dlvId", "id" + (++num) );
				bidiMap.put( p.getId(), p.getProperty( "dlvId", p.getId() ) );
			}
		}
		
		reasoner.setProgram( program );
		
		reasoner.run( new WorkingDirectory( new File( program.getOptions().getValue( "workingDir" ) ) ) );
	}
	
	String getId( Proposition p )
	{
		if( useRealId ) return p.getId();
		
		return p.getProperty( "dlvId", p.getId() );
		
//		String id = bidiMap.getKey( p.getId() );
//		
//		if( id == null )
//		{
//			id = "id" + (++counter);
//			bidiMap.put( id, p.getId() );
//		}
//		return id;
	}
	
	protected String compile( Program program )
	{
		counter = 0;
		
		StringBuilder b = new StringBuilder();
		
		if( useRealId == false )
		{
			b.append( "% Id mapping\n" );
			for( Proposition p : program.getModel().propositions() )
			{
				b.append( "% " + p.getStereotype() + " " + p.getId() + " = " + getId( p ) + "\n" );
			}
			b.append( "\n" );
		}
		
		for( String type : program.getModel().propositionTypes() )
		{
			for( Proposition p : program.getModel().propositions( type ) )
			{
				if( reasoner.semantics.getAxiomCount( p.getStereotype() ) < 1 ) continue;
				
				b.append( "% Axioms for for " + p.getStereotype() + " " + getId( p ) + "\n" );
				for( Axiom axiom : reasoner.semantics.axioms( p.getStereotype() ) )
				{
					b.append( axiom.p_then + "(" + getId( p ) + ") :- " );
					
					String sep = "";
					for( Condition c : axiom.conditions() )
					{
						if( c.isPositive() ) {
							b.append( sep + c.getPredicate() + "(" + getId(p) + ")" );
						}
						else {
							b.append( sep + " not " + c.getPredicate() + "(" + getId(p) + ")" );
						}
						sep = ", ";
					}
					
					b.append( ".\n" );
				}
				b.append( "\n" );
			}
		}
		
		for( String type : program.getModel().relationTypes() )
		{
			List<Rule> list = reasoner.semantics.rules( type );
			
			if( list.size() < 1 ) continue;
			
			for( Relation r : program.getModel().relations( type ) )
			{
				for( Rule rule : list )
				{
					if( r.getOperator() == Relation.AND )
					{
						b.append( "%" + (r.isNegative() ? " NOT" : "") + " and-" + r.getStereotype() + "( "
								+ r.getTarget().getId() + ", " + r.getSources() + " ).\n" );
						
						if( rule.connective == Rule.All )
						{
							b.append( rule.targetPred + "(" + getId( r.getTarget() ) + ") :- " );
							
							String sep = "";
							for( Proposition p : r.getSources() )
							{
								if( rule.getCondition().getOperator() == Operator.Not ) {
									b.append( sep + " not " + rule.getSourcePred() + "(" + getId( p ) + ")" );
								}
								else {
									b.append( sep + rule.getSourcePred() + "(" + getId( p ) + ")" );
								}
								sep = ", ";
							}
							b.append( ".\n" );
						}
						else
						{
							for( Proposition p : r.getSources() )
							{
								b.append( rule.targetPred + "(" + getId( r.getTarget() ) + ") :- " );
								
								if( rule.getCondition().getOperator() == Operator.Not ) {
									b.append( "not " + rule.getSourcePred() + "(" + getId( p ) + ")" );
								}
								else {
									b.append( rule.getSourcePred() + "(" + getId( p ) + ")" );
								}
								b.append( ".\n" );
							}
						}
					}
					
					b.append( "\n" );
				}
			}
		}
		
		b.append( "% Scenario\n" );
		
		for( Proposition s : program.getModel().propositions() ) // "situation" ) )
		{
			ArrayList<Pair> constraints = program.getScenario().constraintsOf( s.getId() );
			
			if( constraints.size() > 0 )
			{
				String sep = "";
				for( Pair p : constraints )
				{
					String value = p.label;
					b.append( sep + value + "(" + getId( s ) + ")" );
					sep = " v ";
				}
				b.append( ".\n" );
			}
		}
		
		b.append( "\n" );
		
		b.append( "% Costs\n" );
		
		// First-come, higher-priority
		int priority = program.getScenario().getCostStructure().getCostTypeCount();
		
		for( String costname : program.getScenario().getCostStructure().costTypes() )
		{
			b.append( "% cost " + priority + ": " + costname + "\n" );
			
			HashMap<String, HashMap<Integer,Cost>> costmap = program.getScenario().getCostStructure()
					.getCosts( costname );
			
			for( String predicate : costmap.keySet() )
			{
				HashMap<Integer,Cost> costs = costmap.get( predicate );
				for( Integer value : costs.keySet() )
				{
					Cost cost = costs.get( value );
					
					for( String id : cost.variables() )
					{
						b.append( ":~ " + cost.getPredicate() + "(" + id + "). [" + cost.getValue()
								+ ":" + priority + "]\n" );
					}
				}
			}
			
			priority--;
		}
		
		b.append( "\n" );
		
		b.append( "% Objectives\n" );
		
		if( program.getQuery().objectivesCount() > 0 )
		{
			String sep = "";
			
			for( String id : program.getQuery().objectives() )
			{
				b.append( sep + program.getQuery().getObjective( id ) + "(" + id + ")" );
				sep = ", ";
			}
			b.append( " ?\n" );
		}
		
		return b.toString();
	}
	
	class RiskIdentificationResult implements Result, Iterable<Solution>, Iterator<Solution>
	{
		Iterator<Solution> it;
		
		@Override
		public String getDescription() {
			return "";
		}

		@Override
		public Iterable<Solution> solutions() {
			return this;
		}

		@Override
		public Iterator<Solution> iterator() {
			try {
				it = reasoner.solutions().iterator();
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<Solution>().iterator();
			}
			return this;
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public Solution next() {
			
			Solution dlvSol = it.next();
			
			Solution riscossSol = new Solution();
			
			for( String dlvVar : dlvSol.variables() )
			{
				String riscossVar = bidiMap.getKey( dlvVar );
				
				for( String field : dlvSol.fields( dlvVar ) )
				{
					for( String value : dlvSol.values( dlvVar, field ) )
					{
						riscossSol.addValue( riscossVar, field, value );
					}
				}
			}
			
			for( String key : dlvSol.properties() )
			{
				riscossSol.setProperty( key, dlvSol.getProperty( key ) );
			}
			
			return riscossSol;
		}

		@Override
		public void remove() {}
	}
	
	@Override
	public Result getResult()
	{
		return new RiskIdentificationResult();
	}	
}
