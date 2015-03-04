package eu.riscoss.fbk.dlv;

import java.util.HashMap;
import java.util.Map;

import eu.riscoss.fbk.language.Model;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Scenario;
import eu.riscoss.fbk.language.Solution;
import eu.riscoss.fbk.risk.RiskSemantics;
import eu.riscoss.fbk.semantics.Semantics;
import eu.riscoss.fbk.sysex.Executable;
import eu.riscoss.fbk.sysex.WorkingDirectory;

public class DlvReasoner
{
	public interface Emitter {
		String toDatalog( Program program );
	}
	
	public static final String DEF_EXECUTABLE_MAC = "dlv.i386-apple-darwin.bin";
	
	public static final String DEF_EXECUTABLE = DEF_EXECUTABLE_MAC;
	
	
	public static final String	INPUTFILE	= "input.dlv";
	
	Program				nomosProgram;
	
	Executable			dlv = null;
	
	String				filename	= DEF_EXECUTABLE;
	
	String				inputFile	= INPUTFILE;
	
	String				outputFile	= "output.txt";
	
	Map<String, Emitter>	emitters	= new HashMap<String, Emitter>();
	public Semantics		semantics = new RiskSemantics();
	
	DlvCompiler			compiler = new DlvCompiler() {
		
		@Override
		public String compile( Program program ) {
			setProgram( program );
			
			StringBuilder b = new StringBuilder();
			
			for( Emitter f : emitters.values() )
			{
				b.append( f.toDatalog( program ) );
				b.append( "\n" );
			}
			return b.toString();
		}
	};
	
	
	public DlvReasoner() {
		nomosProgram = new Program( new Model() );
	}
	
	public void setProgram( Program program ) {
		this.nomosProgram = program;
	}
	
	public void run( WorkingDirectory dir ) {
		if( dlv == null )
		{
			dlv = new Executable( dir.getFile( filename ).getAbsoluteFile() );
		}
		
		dir.rm( inputFile );
		
		if( !nomosProgram.getOptions().isSet( "allow_uncompliance", "false" ) )
		{
			for( Proposition norm : nomosProgram.getModel().propositions( "norm" ) )
			{
				nomosProgram.getScenario().getCostStructure()
				.addCost( "uncompliance", "1", "vio", norm.getId() );
			}
		}
		if( !nomosProgram.getOptions().isSet( "minimize_solution", "false" ) )
		{
			for( Proposition sit : nomosProgram.getModel().propositions( "situation" ) )
			{
				nomosProgram.getScenario().getCostStructure()
				.addCost( "freedom", "1", "st", sit.getId() );
				nomosProgram.getScenario().getCostStructure()
				.addCost( "freedom", "1", "sf", sit.getId() );
			}
		}
		
		dir.write( inputFile, compile( nomosProgram ) );
		
		dlv.setLocking( true );
		dlv.clearArguments();
		dlv.addArgument( dir.getFile( inputFile ).getAbsolutePath() );
		dlv.redirectOutput( dir.getFile( outputFile ) );
		
		dlv.run();
	}
	
	public String compile( Program program ) {
		return compiler.compile( program );
	}
	
	public Executable getExecutable() {
		return dlv;
	}
	
	public Model getModel() {
		return nomosProgram.getModel();
	}
	
	public Scenario getScenario() {
		return nomosProgram.getScenario();
	}
	
	public void addQuery( String id, String value ) {
		this.nomosProgram.getQuery().addObjective( id, value );
	}
	
	public Program getProgram() {
		return nomosProgram;
	}
	
	public void setExecutable( String path ) {
		this.filename = path;
	}
	
	public void setExecutable( Executable exec ) {
		this.dlv = exec;
	}
	
	public Iterable<Solution> solutions() throws Exception {
		return new DlvOutput( getExecutable().getOutputStream() ).solutions();
	}
	
	public void setInputFile( String relativePath ) {
		this.inputFile = relativePath;
	}
	
	public void setOutputFile( String relativePath ) {
		this.outputFile = relativePath;
	}
	
	public String getOutputFile() {
		return outputFile;
	}
	
	public void addEmitter( String stereotype, Emitter emitter ) {
		emitters.put( stereotype, emitter );
	}

	public void setCompiler( DlvCompiler c ) {
		this.compiler = c;
	}
}
