package eu.riscoss.reasoner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.riscoss.reasoner.impl.FBKRiskAnalysisEngine;
import eu.riscoss.reasoner.impl.KPARiskAnalysisTool;

public class CompoundAnalysisEngine implements RiskAnalysisEngine {
	
	public static void main( String[] args ) {}
	
	RiskAnalysisEngine				first = null;
	RiskAnalysisEngine				last = null;
	
	Map<String,RiskAnalysisEngine>	engines = new HashMap<String,RiskAnalysisEngine>();
	
	List<Junction>					junctions = new ArrayList<Junction>();
	
	public CompoundAnalysisEngine() {
		this( true );
	}
	
	public CompoundAnalysisEngine( boolean useDefaultEngineSet ) {
		if( useDefaultEngineSet ) {
			addEngine( new KPARiskAnalysisTool() );
			addEngine( new FBKRiskAnalysisEngine() );
		}
		else {
		}
	}

	public void addEngine( RiskAnalysisEngine engine ) {
		
		engines.put( "" + engine.hashCode(), engine );
		
		if( first == null ) {
			first = engine;
		}
		else {
			Junction j = new Junction();
			j.source = last;
			j.target = engine;
			junctions.add( j );
		}
		
		last = engine;
	}
	
	@Override
	public void loadModel(String xmlModel) {
		for( RiskAnalysisEngine engine : engines.values() ) {
			try {
				engine.loadModel(xmlModel);
			}
			catch( Exception ex ) {
			
			}
		}
	}
	
	@Override
	public Iterable<Chunk> queryModel(ModelSlice slice) {
		// TODO: strip away matching output/input?
		List<Chunk> chunks = new ArrayList<Chunk>();
		
		for( RiskAnalysisEngine engine : engines.values() ) {
			for( Chunk chunk : engine.queryModel(slice) ) {
				chunks.add( mkChunk( engine,chunk ) );
			}
		}
		
		return chunks;
	}
	
	Chunk mkChunk( RiskAnalysisEngine engine, Chunk c ) {
		return new Chunk( new Address( "" + engine.hashCode() ), c.getId(), c.getStereotype() );
//		return new Chunk( c.getId() + "@" + engine.hashCode(), c.getStereotype() );
	}
	
	RiskAnalysisEngine getEngine( Chunk chunk ) throws Exception {
		if( chunk.getAddress() == null ) return null; //throw new Exception( "Engine NULL not found" );
		RiskAnalysisEngine engine = engines.get( chunk.getAddress().toString() );
		if( engine == null ) throw new Exception( "Engine not found" );
		return engine;
//		int n = chunk.getId().lastIndexOf( "@" );
//		if( n == -1 ) throw new Exception( "Engine identifier not found in chunk id '" + chunk.getId() + "'" );
//		String ename = chunk.getId().substring( n+1 );
//		RiskAnalysisEngine engine = engines.get( ename );
//		if( engine == null ) throw new Exception( "Engine not found" );
//		return engine;
	}
	
	Chunk getNestedChunk( Chunk chunk ) throws Exception {
		return chunk;
//		int n = chunk.getId().lastIndexOf( "@" );
//		if( n == -1 ) throw new Exception( "Chunk identifier not found in chunk id '" + chunk.getId() + "'" );
//		String cname = chunk.getId().substring( 0, n );
//		Chunk c2 = new Chunk( cname, chunk.getStereotype() );
//		return c2;
	}
	
	@Override
	public Field getField(Chunk chunk, FieldType field) {
		Field ret = null;
		try {
			RiskAnalysisEngine engine = getEngine(chunk);
			if( engine != null ) {
				Chunk c = getNestedChunk(chunk);
				ret = engine.getField( c, field );
			}
			else {
				for( RiskAnalysisEngine e: engines.values() ) {
					Chunk c = getNestedChunk(chunk);
					Field f = e.getField( c, field );
					if( f != null ) {
						ret = f;
						break;
					}
				}
//				return null;
			}
		}
		catch( Exception ex ) {
			if( field == FieldType.TYPE ) {
				return new Field( DataType.STRING, "Risk" );
			}
			ex.printStackTrace();
			return null;
		}
		if( ret == null ) {
			if( field == FieldType.TYPE ) {
				ret = new Field( DataType.STRING, "Risk" );
			}
		}
		return ret;
	}
	
	@Override
	public void setField(Chunk chunk, FieldType type, Field field) {
		try {
			RiskAnalysisEngine engine = getEngine(chunk);
			Chunk c = getNestedChunk(chunk);
			engine.setField(c, type, field);
		}
		catch( Exception ex ) {
			for( RiskAnalysisEngine e : engines.values() ) {
				e.setField(chunk, type, field);
			}
		}
	}
	
	@Override
	public <T> T getDefaultValue(Chunk chunk) {
		try {
			RiskAnalysisEngine engine = getEngine(chunk);
			Chunk c = getNestedChunk(chunk);
			return engine.getDefaultValue(c);
		}
		catch( Exception ex ) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getLastMessage() {
		return "";
	}
	
	@Override
	public AnalysisResponse runAnalysis(String[] args) {
		
		for( Junction j : junctions ) {
			j.source.runAnalysis(args);
			if( j.target != null )
				j.apply();
		}
		if( junctions.size() > 0 )
			junctions.get( junctions.size() -1 ).target.runAnalysis(args);
		else
			if( first != null )
				first.runAnalysis( args );
		
		return AnalysisResponse.DONE;
	}
	
}
