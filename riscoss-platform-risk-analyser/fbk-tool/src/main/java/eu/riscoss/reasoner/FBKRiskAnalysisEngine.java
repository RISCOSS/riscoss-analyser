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
import java.util.HashMap;
import java.util.Map;

import eu.riscoss.fbk.io.RiscossLoader;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.lp.FunctionsLibrary;
import eu.riscoss.fbk.lp.JsExtension;
import eu.riscoss.reasoner.AnalysisResponse;
import eu.riscoss.reasoner.Chunk;
import eu.riscoss.reasoner.DataType;
import eu.riscoss.reasoner.Evidence;
import eu.riscoss.reasoner.Field;
import eu.riscoss.reasoner.FieldType;
import eu.riscoss.reasoner.ModelSlice;
import eu.riscoss.reasoner.RiskAnalysisEngine;

public class FBKRiskAnalysisEngine implements RiskAnalysisEngine
{
	static Map<String,String> types;
	
	static {
		types = new HashMap<>();
		
		types.put( "goal", "Goal" );
		types.put( "softgoal", "Goal" );
		types.put( "soft-goal", "Goal" );
		types.put( "soft goal", "Goal" );
		types.put( "hardgoal", "Goal" );
		types.put( "hard-goal", "Goal" );
		types.put( "hard goal", "Goal" );
		types.put( "task", "Goal" );
		types.put( "resource", "Goal" );
		
		types.put( "event", "Risk" );
		
		types.put( "indicator", "Indicator" );
		types.put( "measure", "Indicator" );
	}
	
	Program program = new Program();
	
	RiskEvaluation analysis = null;
	
	RiscossLoader loader = new RiscossLoader();
	
	@Override
	public void loadModel( String xmlModel ) {
		
		loader.load( xmlModel, program );
	}
	
	public RiskEvaluation getEngine() {
		return analysis;
	}
	
	public void setProgram( Program p ) {
		this.program = p;
	}
	
	public Program getProgram() {
		return program;
	}
	
	public Iterable<Chunk> getSlice( String customName ) {
		if( customName.equals( "weights" ) ) {
			ArrayList<Chunk> list = new ArrayList<Chunk>();
			for( Relation r : program.getModel().relations() ) {
				Chunk chunk = new Chunk( r.getId(), "relation" );
				list.add( chunk );
			}
			return list;
		}
		return new ArrayList<Chunk>();
	}
	
	@Override
	public Iterable<Chunk> queryModel( ModelSlice slice ) {
		switch( slice ) {
		case INPUT_DATA: {
			ArrayList<Chunk> list = new ArrayList<Chunk>();
			for( Proposition p : program.getModel().propositions() ) {
				if( p.getProperty( "input", "false" ).equalsIgnoreCase( "false" ) ) continue;
				Chunk chunk = new Chunk( p.getId(), p.getStereotype() );
				
				list.add( chunk );
			}
			return list;
		}
		case OUTPUT_DATA: {
			ArrayList<Chunk> list = new ArrayList<Chunk>();
			for( Proposition p : program.getModel().propositions() ) {
				if( p.getStereotype().equals( "goal" ) == false )
					if( p.getStereotype().equals( "task" ) == false )
						if( p.getProperty( "output", "false" ).equalsIgnoreCase( "false" ) ) continue;
				Chunk chunk = new Chunk( p.getId(), p.getStereotype() );
				list.add( chunk );
			}
			return list;
		}
		case CUSTOM:
			break;
		}
		
		return new ArrayList<Chunk>();
	}
	
	@Override
	public <T> T getDefaultValue( Chunk chunk ) {
		Proposition p = program.getModel().getProposition( chunk.getId() );
		if( p == null ) return null;
		if( "true".equals( p.getProperty( "required", "true" ) ) ) {
			if( p.getProperty( "default-value", null ) == null ) {
				return null;
			}
		}
		if( p.getProperty( "datatype", "real" ) != null ) {
			DataType dt = DataType.valueOf( p.getProperty( "datatype", "" ).toUpperCase() );
			String def = p.getProperty( "default-value", null );
			if( def == null ) return null;
			switch( dt ) {
			case EVIDENCE:{
				return new Field( DataType.EVIDENCE, new Evidence( 0, 0 ) ).getValue();
			}
			case REAL: {
				try { 
					double pos = 0;
					pos = Double.parseDouble( def );
					return new Field( DataType.REAL, pos ).getValue();
				} catch( Exception ex ) {
					return null;
				}
			}
			case INTEGER: {
				try { 
					int pos = 0;
					pos = Integer.parseInt( def );
					return new Field( DataType.INTEGER, pos ).getValue();
				} catch( Exception ex ) {
					return null;
				}
			}
			case STRING: {
				return new Field( DataType.STRING, def ).getValue();
			}
			case DISTRIBUTION:
			case NaN:
			default:
				// We do not support the DISTRIBUTION case
				throw new RuntimeException( "Unsupported data type: " + dt.toString() );
			}
		}
		else {
			String val = p.getProperty( "default-value", null );
			if( val == null ) return null;
			double pos = 0;
			try { 
				pos = Double.parseDouble( val );
			} catch( Exception ex ) {}
			return new Field( DataType.EVIDENCE, new Evidence( pos, 0 ) ).getValue();
		}
	}
	
	@Override
	public String getLastMessage() {
		return "Ok";
	}
	
	@Override
	public AnalysisResponse runAnalysis( String[] args ) {
		
		analysis = new RiskEvaluation();
		
		JsExtension.get().put( "fx", FunctionsLibrary.get() );
		
		analysis.run(program);
		
		return AnalysisResponse.DONE;
	}
	
	
	
	@Override
	public Field getField( Chunk chunk, FieldType field ) {
		switch( field ) {
		case INPUT_VALUE: {
			Proposition p = program.getModel().getProposition( chunk.getId() );
			if( p == null ) return null;
			if( p.getProperty( "datatype", null ) != null ) {
				DataType dt = DataType.valueOf( p.getProperty( "datatype", "" ).toUpperCase() );
				switch( dt ) {
				case EVIDENCE:{
					double pos = 0;
					double neg = 0;
					try { 
						pos = Double.parseDouble( program.getScenario().getConstraint( chunk.getId(), "st" ) );
					} catch( Exception ex ) {}
					try { 
						neg = Double.parseDouble( program.getScenario().getConstraint( chunk.getId(), "sf" ) );
					} catch( Exception ex ) {}
					return new Field( DataType.EVIDENCE, new Evidence( pos, neg ) );
				}
				case REAL: {
					double pos = 0;
					try { 
						pos = Double.parseDouble( program.getScenario().getConstraint( chunk.getId(), "st" ) );
					} catch( Exception ex ) {}
					return new Field( DataType.REAL, pos );
				}
				case INTEGER: {
					int pos = 0;
					try { 
						pos = Integer.parseInt( program.getScenario().getConstraint( chunk.getId(), "st" ) );
					} catch( Exception ex ) {}
					return new Field( DataType.INTEGER, pos );
				}
				case STRING: {
					String pos = program.getScenario().getConstraint( chunk.getId(), "st" );
					if( pos == null ) pos = "";
					return new Field( DataType.STRING, pos );
				}
				case DISTRIBUTION:
				case NaN:
				default:
					// We do not support the DISTRIBUTION case
					throw new RuntimeException( "Unsupported data type: " + dt.toString() );
				}
			}
			else {
				double pos = 0;
				double neg = 0;
				try { 
					pos = Double.parseDouble( program.getScenario().getConstraint( chunk.getId(), "st" ) );
				} catch( Exception ex ) {}
				try { 
					neg = Double.parseDouble( program.getScenario().getConstraint( chunk.getId(), "sf" ) );
				} catch( Exception ex ) {}
				return new Field( DataType.EVIDENCE, new Evidence( pos, neg ) );
			}
		}
		case OUTPUT_VALUE:
			return new Field( DataType.EVIDENCE, new Evidence( 
					analysis.getPositiveValue( chunk.getId() ), 
					analysis.getNegativeValue( chunk.getId() ) ) );
		case WEIGHT: {
			Relation r = program.getModel().getRelation( chunk.getId() );
			return new Field( DataType.REAL, r.getWeight() );
		}
		case DESCRIPTION:
			return new Field( DataType.STRING,
					program.getModel().getProposition( chunk.getId() ).getProperty( "label", 
							program.getModel().getProposition( chunk.getId() ).getProperty( "name", chunk.getId() ) ) );
		case QUESTION:
			return new Field( DataType.STRING,
					program.getModel().getProposition( chunk.getId() ).getProperty( "question", "" ) );
		case TYPE:
			return new Field( DataType.STRING, getType( program.getModel().getProposition( chunk.getId() ).getStereotype() ) );
		case MIN: {
			Proposition p = program.getModel().getProposition( chunk.getId() );
			if( p == null ) return null;
			if( p.getProperty( "datatype", null ) != null ) {
				DataType dt = DataType.valueOf( p.getProperty( "datatype", "" ).toUpperCase() );
				switch( dt ) {
				case EVIDENCE:
				case DISTRIBUTION:
					return new Field( DataType.REAL, 0 );
				case REAL: {
					if( p.getProperty( "min", null ) == null )
						return new Field( DataType.NaN, null );
					return new Field( DataType.REAL, Double.parseDouble( p.getProperty( "min", "0.0" ) ) );
				}
				case INTEGER: {
					if( p.getProperty( "min", null ) == null )
						return new Field( DataType.NaN, null );
					return new Field( DataType.REAL, Integer.parseInt( p.getProperty( "min", "0" ) ) );
				}
				case STRING:
					return new Field( DataType.STRING, "" );
				default:
					return new Field( DataType.NaN, null );
				}
			}
		}
		break;
		case MAX: {
			Proposition p = program.getModel().getProposition( chunk.getId() );
			if( p == null ) return null;
			if( p.getProperty( "datatype", null ) != null ) {
				DataType dt = DataType.valueOf( p.getProperty( "datatype", "" ).toUpperCase() );
				switch( dt ) {
				case EVIDENCE:
				case DISTRIBUTION:
					return new Field( DataType.REAL, 1 );
				case REAL: {
					if( p.getProperty( "max", null ) == null )
						return new Field( DataType.NaN, null );
					return new Field( DataType.REAL, Double.parseDouble( p.getProperty( "max", "1.0" ) ) );
				}
				case INTEGER: {
					if( p.getProperty( "max", null ) == null )
						return new Field( DataType.NaN, null );
					return new Field( DataType.REAL, Integer.parseInt( p.getProperty( "max", "1" ) ) );
				}
				case STRING:
					return new Field( DataType.STRING, "" );
				default:
					return new Field( DataType.NaN, null );
				}
			}
		}
		break;
		default:
			break;
		}
		return null;
	}
	
	private String getType( String stereotype ) {
		String ret = types.get( stereotype );
		if( ret == null ) ret = "";
		return ret;
	}
	
	@Override
	public void setField(Chunk chunk, FieldType type, Field f) {
		switch( type ) {
		case INPUT_VALUE:
			switch( f.getDataType() ) {
			case STRING:
				program.getScenario().setConstraint( chunk.getId(), "st", (String)f.getValue() );
				break;
			case REAL:
				program.getScenario().setConstraint( chunk.getId(), "st", "" + (Double)f.getValue() );
				break;
			case INTEGER:
				program.getScenario().removeConstraint( chunk.getId(), "st" );
				program.getScenario().setConstraint( chunk.getId(), "st", "" + (Integer)f.getValue() );
				break;
			case EVIDENCE:
				program.getScenario().setConstraint( chunk.getId(), "st", "" + ((Evidence)f.getValue()).getPositive() );
				program.getScenario().setConstraint( chunk.getId(), "sf", "" + ((Evidence)f.getValue()).getNegative() );
				break;
			default:
				break;
			}
			break;
		case OUTPUT_VALUE:
			break;
		case WEIGHT: {
			try {
				Relation r = program.getModel().getRelation( chunk.getId() );
				r.setWeight( (Float)f.getValue() );
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
		}
		break;
		
		default:break;
		}
	}
}
