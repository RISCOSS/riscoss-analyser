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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			addEngine( "eu.riscoss.reasoner.impl.KPARiskAnalysisTool" );
			addEngine( "eu.riscoss.reasoner.KPARiskAnalysisTool" );
			addEngine( "eu.riscoss.reasoner.FBKRiskAnalysisEngine" );
		}
		else {
		}
	}
	
	public void addEngine( String clsName ) {
		try {
			Class<?> cls = Class.forName( clsName );
			if( cls != null ) {
				RiskAnalysisEngine rae = (RiskAnalysisEngine)cls.newInstance();
				if( rae != null ) {
					addEngine( rae );
				}
			}
		} catch (ClassNotFoundException e) {
		} catch( NoClassDefFoundError e ) {
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		catch( Throwable t ) {
			t.printStackTrace();
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
	
	public Collection<RiskAnalysisEngine> listEngines() {
		return this.engines.values();
	}
	
	@Override
	public void loadModel(String xmlModel) {
		for( RiskAnalysisEngine engine : engines.values() ) {
			try {
				engine.loadModel(xmlModel);
			}
			catch( Exception ex ) {
			
			}
			catch( Error err ) {
				System.out.println( "Error catch" );
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

	@Override
	public void resetFields() {
		for( RiskAnalysisEngine engine : engines.values() ) {
			try {
				engine.resetFields();
			}
			catch( Exception ex ) {
			
			}
			catch( Error err ) {
				System.out.println( "Error catch" );
			}
		}
	}
	
}
