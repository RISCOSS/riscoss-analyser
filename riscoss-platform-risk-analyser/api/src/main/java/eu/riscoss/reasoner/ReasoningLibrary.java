package eu.riscoss.reasoner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReasoningLibrary
{
	private static final ReasoningLibrary instance = new ReasoningLibrary();
	
	public static ReasoningLibrary get()
	{
		return instance;
	}
	
	Map<String,RiskAnalysisStructure> engines = new HashMap<String,RiskAnalysisStructure>();
	
	List<String> classes = new ArrayList<String>();
	
	private ReasoningLibrary() {
		for( String className : new ClassList( "eu.riscoss.reasoner.impl" ) ) {
			try {
				Class<?> cls = Class.forName( className );
				if( RiskAnalysisEngine.class.isAssignableFrom( cls ) ) {
					classes.add( className );
				}
			}
			catch( Exception ex ) {}
		}
	}
	
	public RiskAnalysisEngine createRiskAnalysisEngine() {
		try {
			return (RiskAnalysisEngine) Class.forName( "eu.riscoss.reasoner.CompoundAnalysisEngine" ).newInstance();
		}
		catch( Exception ex ) {}
		
		return null;
	}
	
	// TODO: Remove?
	public RiskAnalysisStructure createRiskStructure() {
		try {
			return (RiskAnalysisStructure) Class.forName( classes.get( 0 ) ).newInstance();
		}
		catch( Exception ex ) {}
		
		return null;
	}
	
	List<String> getRiskAnalysisEngineList() {
		return classes;
	}
	
	public RiskAnalysisStructure createRiskReasoner( String name ) {
		if( name == null ) return createRiskStructure();
		return engines.get( name );
	}
	
	public RiskReasoner createReasoner(RiskAnalysisStructure riskStructure) {
		if( riskStructure instanceof RiskReasoner )
			return (RiskReasoner)riskStructure;
		
		return null;
	}
	
	public Sorter createSorter() {
		return new DefaultSorter();
	}
}
