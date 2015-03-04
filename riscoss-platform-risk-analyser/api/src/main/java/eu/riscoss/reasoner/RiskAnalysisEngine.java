package eu.riscoss.reasoner;

/**
 * Any subclass of RiskAnalysisEngine must be located in the package 
 * 'eu.riscoss.reasoner.impl' in order to be found by the ReasoningLibrary
 * 
 * @author albertosiena
 *
 */
public interface RiskAnalysisEngine extends RiskAnalysisStructure, RiskReasoner {
	
}
