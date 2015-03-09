package eu.riscoss.reasoner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReasoningLibrary
{
	private static final ReasoningLibrary instance = new ReasoningLibrary();
	
	public static ReasoningLibrary get() {
		return instance;
	}
	
	Map<String,RiskAnalysisEngine> engines = new HashMap<>();
	
	List<String> classes = new ArrayList<String>();
	
	private ReasoningLibrary() {
		classes.add( "eu.riscoss.reasoner.CompoundAnalysisEngine" );
		classes.add( "eu.riscoss.reasoner.FBKRiskAnalysisEngine" );
	}
	
	public RiskAnalysisEngine createRiskAnalysisEngine() {
		for( String clsName : classes ) {
			try {
				return (RiskAnalysisEngine) Class.forName( clsName ).newInstance();
			}
			catch( Exception ex ) {}
		}
		
		return null;
	}
	
	public Sorter createSorter() {
		return new DefaultSorter();
	}
}
