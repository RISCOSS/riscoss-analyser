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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.riscoss.fbk.io.RiscossLoader;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.lp.JsExtension;

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
	
	public void loadModel( File file ) {
		loader.load( file, program );
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
//				if( p.getStereotype().equals( "goal" ) == false )
//					if( p.getStereotype().equals( "task" ) == false )
//						if( p.getProperty( "output", "false" ).equalsIgnoreCase( "false" ) ) continue;
				if( isOutput( p ) ) {
//				if( "true".equalsIgnoreCase( p.getProperty( "output", "false" ) ) ) {
					Chunk chunk = new Chunk( p.getId(), p.getStereotype() );
					list.add( chunk );
				}
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
		
		analysis.setProgram( program );
//		analysis.initCode();
		
		program.getScenario().setConstraint( "always", "st", "1.0" );
		
		for( Proposition p : program.getModel().propositions() ) {
			
			if( p.getProperty( "default-value", null ) != null ) {
				try {
					double d = Double.parseDouble( p.getProperty( "default-value", null ) );
					if( program.getScenario().getConstraint( p.getId(), "st" ) == null ) {
						program.getScenario().setConstraint( p.getId(), "st", "" + d );
					}
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
			
			if( p.getProperty( "input", "false" ).equalsIgnoreCase( "false" ) ) continue;
			
			List<String> list = program.getScenario().constraintsOf( p.getId(), "st" );
			if( list == null ) continue;
			
			if( list.size() < 2 ) continue;
			
			String datatype = p.getProperty( "datatype", null );
			if( datatype == null ) datatype = DataType.EVIDENCE.name();
			if( datatype != null ) {
				DataType dt = DataType.valueOf( datatype.toUpperCase() );
				switch( dt ) {
				case EVIDENCE:{
					
					ArrayList<Evidence> values = new ArrayList<>();
					for( int i = 0; i < list.size(); i++ ) {
						double pos = 0;
						double neg = 0;
						try { 
							pos = Double.parseDouble( program.getScenario().getConstraint( p.getId(), "st", i ) );
						} catch( Exception ex ) {}
						try { 
							neg = Double.parseDouble( program.getScenario().getConstraint( p.getId(), "sf", i ) );
						} catch( Exception ex ) {}
						Evidence e = new Evidence( pos, neg );
						values.add( e );
					}
					
					
					if( p.getProperty( "unifier", null ) == null ) {
						// calculate a default unification
						
						double pos = 0, neg = 0;
						
						for( Evidence e : values ) {
							pos += e.getPositive();
							neg += e.getNegative();
						}
						
						pos = pos / values.size();
						neg = neg / values.size();
						
						program.getScenario().removeConstraint( p.getId(), "st" );
						program.getScenario().removeConstraint( p.getId(), "sf" );
						program.getScenario().setConstraint( p.getId(), "st", "" + pos );
						program.getScenario().setConstraint( p.getId(), "sf", "" + neg );
						
					}
					else {
						try {
							JsExtension.get().put( "values", values );
							
							String code = "x=" + p.getProperty( "unifier", "x" ) + ";";
							
							Evidence e = (Evidence)JsExtension.get().eval(code);
							
							program.getScenario().removeConstraint( p.getId(), "st" );
							program.getScenario().removeConstraint( p.getId(), "sf" );
							program.getScenario().setConstraint( p.getId(), "st", "" + e.getPositive() );
							program.getScenario().setConstraint( p.getId(), "sf", "" + e.getNegative() );
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				break;
				case REAL: {
					
					ArrayList<Double> values = new ArrayList<>();
					for( int i = 0; i < list.size(); i++ ) {
						double pos = 0;
						try { 
							pos = Double.parseDouble( program.getScenario().getConstraint( p.getId(), "st", i ) );
						} catch( Exception ex ) {}
						values.add( pos );
					}
					
					
					if( p.getProperty( "unifier", null ) == null ) {
						// calculate a default unification
						
						double pos = 0;
						
						for( Double e : values ) {
							pos += e;
						}
						
						pos = pos / values.size();
						
						program.getScenario().removeConstraint( p.getId(), "st" );
						program.getScenario().setConstraint( p.getId(), "st", "" + pos );
						
					}
					else {
						try {
							JsExtension.get().put( "values", values );
							
							String code = "x=" + p.getProperty( "unifier", "x" ) + ";";
							
							Double e = (Double)JsExtension.get().eval(code);
							
							program.getScenario().removeConstraint( p.getId(), "st" );
							program.getScenario().setConstraint( p.getId(), "st", "" + e );
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				break;
				case INTEGER: {
					
					ArrayList<Integer> values = new ArrayList<>();
					for( int i = 0; i < list.size(); i++ ) {
						int pos = 0;
						try { 
							pos = (int)Double.parseDouble( program.getScenario().getConstraint( p.getId(), "st", i ) );
						} catch( Exception ex ) {}
						values.add( pos );
					}
					
					
					if( p.getProperty( "unifier", null ) == null ) {
						// calculate a default unification
						
						double pos = 0;
						
						for( Integer e : values ) {
							pos += e;
						}
						
						program.getScenario().removeConstraint( p.getId(), "st" );
						program.getScenario().setConstraint( p.getId(), "st", "" + pos );
						
					}
					else {
						try {
							JsExtension.get().put( "values", values );
							
							String code = "x=" + p.getProperty( "unifier", "x" ) + ";";
							
							Integer e = (Integer)JsExtension.get().eval(code);
							
							program.getScenario().removeConstraint( p.getId(), "st" );
							program.getScenario().setConstraint( p.getId(), "st", "" + e );
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				break;
				case STRING:
//				{
//					String pos = program.getScenario().getConstraint( chunk.getId(), "st" );
//					if( pos == null ) pos = "";
//					return new Field( DataType.STRING, pos );
//				}
				case DISTRIBUTION:
				case NaN:
				default:
					// We do not support the DISTRIBUTION case
//					throw new RuntimeException( "Unsupported data type: " + dt.toString() );
				}
			}
			else {
//				double pos = 0;
//				double neg = 0;
//				try { 
//					pos = Double.parseDouble( program.getScenario().getConstraint( chunk.getId(), "st" ) );
//				} catch( Exception ex ) {}
//				try { 
//					neg = Double.parseDouble( program.getScenario().getConstraint( chunk.getId(), "sf" ) );
//				} catch( Exception ex ) {}
//				return new Field( DataType.EVIDENCE, new Evidence( pos, neg ) );
			}
			
//			Collection<Pair> pairs = program.getScenario().constraintsOf( p.getId() );
//			if( pairs == null ) continue;
//			if( pairs.size() < 2 ) continue;
//			if( p.getProperty( "unifier", null ) == null ) {
//				// calculate a default unification
//			}
//			else {
//				try {
//					ArrayList<String> values = new ArrayList<String>();
//					for( Pair pair : pairs ) values.add( pair.value );
//					JsExtension.get().put( "values", values );
//					String code = "x=" + p.getProperty( "unifier", "x" ) + ";";
//					
//					String unified = JsExtension.get().eval(code).toString();
//					
//					program.getScenario().removeConstraint( p.getId(), "st" );
//					program.getScenario().setConstraint( p.getId(), "st", unified );
//					
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
		}
		
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
			if( analysis == null ) {
				return new Field( DataType.EVIDENCE, new Evidence( 0, 0 ) );
			}
			{
			Proposition p = program.getModel().getProposition( chunk.getId() );
			if( p == null ) return null;
				if( p.getProperty( "datatype", DataType.EVIDENCE.name() ) != null ) {
					DataType dt = DataType.valueOf( p.getProperty( "datatype", DataType.EVIDENCE.name() ).toUpperCase() );
					switch( dt ) {
					case EVIDENCE:
						return new Field( DataType.EVIDENCE, new Evidence( 
								analysis.getPositiveValue( chunk.getId() ), 
								analysis.getNegativeValue( chunk.getId() ) ) );
					case INTEGER:
						return new Field( DataType.INTEGER, 
								(int) analysis.getPositiveValue( chunk.getId() ) );
					case REAL:
						return new Field( DataType.REAL, 
								(double) analysis.getPositiveValue( chunk.getId() ) );
						default:
							return null;
					}
				}
			}
			return new Field( DataType.EVIDENCE, new Evidence( 
					analysis.getPositiveValue( chunk.getId() ), 
					analysis.getNegativeValue( chunk.getId() ) ) );
		case WEIGHT: {
			Relation r = program.getModel().getRelation( chunk.getId() );
			return new Field( DataType.REAL, r.getWeight() );
		}
		case LABEL:
			return new Field( DataType.STRING,
					program.getModel().getProposition( chunk.getId() ).getProperty( "label", 
							program.getModel().getProposition( chunk.getId() ).getProperty( "name", chunk.getId() ) ) );
		case DESCRIPTION:
			return new Field( DataType.STRING,
					program.getModel().getProposition( chunk.getId() ).getProperty( "description", 
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
				// single->multi
//				program.getScenario().setConstraint( chunk.getId(), "st", "" + (Double)f.getValue() );
				program.getScenario().addConstraint( chunk.getId(), "st", "" + (Double)f.getValue() );
				break;
			case INTEGER:
				// single->multi
//				program.getScenario().removeConstraint( chunk.getId(), "st" );
//				program.getScenario().setConstraint( chunk.getId(), "st", "" + (Integer)f.getValue() );
				program.getScenario().addConstraint( chunk.getId(), "st", "" + (Integer)f.getValue() );
				break;
			case EVIDENCE:
				// single->multi
//				program.getScenario().setConstraint( chunk.getId(), "st", "" + ((Evidence)f.getValue()).getPositive() );
//				program.getScenario().addConstraint( chunk.getId(), "sf", "" + ((Evidence)f.getValue()).getNegative() );
				program.getScenario().addConstraint( chunk.getId(), "st", "" + ((Evidence)f.getValue()).getPositive() );
				program.getScenario().addConstraint( chunk.getId(), "sf", "" + ((Evidence)f.getValue()).getNegative() );
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

	@Override
	public void resetFields() {
		program.getScenario().clear();
	}

	public boolean isOutput( Proposition p ) {
		return "true".equalsIgnoreCase( p.getProperty( "output", "false" ) );
	}
}
