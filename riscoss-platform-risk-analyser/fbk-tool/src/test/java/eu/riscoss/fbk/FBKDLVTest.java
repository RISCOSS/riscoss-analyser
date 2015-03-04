package eu.riscoss.fbk;

import java.io.File;
import java.util.Date;

import eu.riscoss.fbk.dlv.DlvReasoner;
import eu.riscoss.fbk.dlv.TemplateCompiler;
import eu.riscoss.fbk.language.Model;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.sysex.WorkingDirectory;
import eu.riscoss.fbk.util.TimeDiff;

public class FBKDLVTest {
	public static void main( String[] args ) {
		Program program = new Program();
		Model model = program.getModel();
		model.addProposition( new Proposition( "goal", "g1" ) );
		model.addProposition( new Proposition( "goal", "g1_1" ) );
		model.addProposition( new Proposition( "goal", "g1_2" ) );
		model.addProposition( new Proposition( "goal", "g1_2_1" ) );
		model.addProposition( new Proposition( "goal", "g1_2_2" ) );
		model.addProposition( new Proposition( "situation", "s1" ) );
		model.addProposition( new Proposition( "situation", "s2" ) );
		model.addProposition( new Proposition( "event", "e1" ) );
		Relation r;
		r = new Relation( "expose" );
		r.addSource( model.getProposition( "s1" ) );
		r.setTarget( model.getProposition( "e1" ) );
		model.addRelation( r );
		r = new Relation( "increase" );
		r.addSource( model.getProposition( "s2" ) );
		r.setTarget( model.getProposition( "e1" ) );
		model.addRelation( r );
		r = new Relation( "impact" );
		r.addSource( model.getProposition( "e1" ) );
		r.setTarget( model.getProposition( "g1_1" ) );
		model.addRelation( r );
//		r = new Relation( "impact" );
//		r.addSource( model.getProposition( "e1" ) );
//		r.setTarget( model.getProposition( "g1_2" ) );
//		model.addRelation( r );
		r = new Relation( "decomposition" );
		r.addSource( model.getProposition( "g1_1" ) );
		r.addSource( model.getProposition( "g1_2" ) );
		r.setTarget( model.getProposition( "g1" ) );
		r.setOperator( Relation.OR );
		model.addRelation( r );
		r = new Relation( "decomposition" );
		r.addSource( model.getProposition( "g1_2_1" ) );
		r.addSource( model.getProposition( "g1_2_2" ) );
		r.setTarget( model.getProposition( "g1_2" ) );
		r.setOperator( Relation.AND );
		model.addRelation( r );
		
		program.getScenario().addConstraint( "s1", "sat", "true" );
		program.getScenario().addConstraint( "s2", "sat", "true" );
		
//		program.getScenario().addConstraint( "g1", "safe", "true" );
		program.getQuery().addObjective( "g1", "safe" );
		
		DlvReasoner reasoner = new DlvReasoner();
		reasoner.setProgram( program );
		
		WorkingDirectory dir = new WorkingDirectory( new File( "/Users/albertosiena/Desktop/old/nrtool" ) );
		
//		if( tool.getCommandLine().getArgument( "t", null ) != null ) {
			File file = dir.getFile( "riscoss-template.txt" );
//			if( file.exists() ) {
				TemplateCompiler c = new TemplateCompiler();
				c.setTemplateFile( file );
				reasoner.setCompiler( c );
//			}
//		}
		
		
		reasoner.setExecutable( "dlv.i386-apple-darwin.bin" );
		
		System.out.println( "Running engine..." );
		long startTime = System.currentTimeMillis();
		reasoner.run( dir );
		long stopTime = System.currentTimeMillis();
		System.out.println( "Done" );
		System.out.println( "Approximate execution time: "
				+ new TimeDiff( new Date( startTime ), new Date( stopTime ) ) );
		
		for( String s : dir.inputFiles() )
			System.out.print( s + " " );
		System.out.println( "" );
		
		for( String s : dir.outputFiles() )
			System.out.print( s + " " );
		System.out.println( "" );
		
		System.out.println( "Gathering output..." );
		
	}
}
