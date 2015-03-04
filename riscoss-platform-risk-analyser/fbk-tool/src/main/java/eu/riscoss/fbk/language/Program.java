package eu.riscoss.fbk.language;

public class Program
{
	private final Model				model;
	private final Scenario			scenario;
	private final Query				query;
	private final Options			options;
	
	public Program() {
		this( new Model() );
	}
	
	public Program( Model m ) {
		model		= m;
		scenario	= new Scenario( model );
		query		= new Query( model );
		options		= new Options();
	}
	
	public Model getModel() {
		return model;
	}
	
	public Scenario getScenario() {
		return scenario;
	}
	
	public Query getQuery() {
		return query;
	}
	
	public Options getOptions() {
		return options;
	}
}
