package eu.riscoss.fbk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eu.riscoss.reasoner.Chunk;
import eu.riscoss.reasoner.Distribution;
import eu.riscoss.reasoner.Evidence;
import eu.riscoss.reasoner.Field;
import eu.riscoss.reasoner.FieldType;
import eu.riscoss.reasoner.ModelSlice;
import eu.riscoss.reasoner.RiskAnalysisEngine;
import eu.riscoss.reasoner.impl.FBKRiskAnalysisEngine;

public class FBKTest {
	public static void main( String[] args ) {
		
		Random r = new Random( 10 );
		
		FBKRiskAnalysisEngine fbk = new FBKRiskAnalysisEngine();
		
		fbk.loadModel( convertStreamToString( FBKTest.class.getResourceAsStream( "jstest.xml" ) ) );
//		fbk.loadModel( convertStreamToString( FBKTest.class.getResourceAsStream( "riskmodel_legal-m18.xml" ) ) );
		
		
//		fbk.setField( new Chunk( "i" ), FieldType.INPUT_VALUE, new Field( DataType.INTEGER, 1250 ) );
		
		
		randomize( fbk, r );
		fbk.runAnalysis(args);
		report(fbk);
	}
	
	static void randomize( RiskAnalysisEngine e, Random r ) {
		
		Iterable<Chunk> input = e.queryModel( ModelSlice.INPUT_DATA );
		
		for( Chunk chunk : input ) {
			
			Field f = e.getField( chunk, FieldType.INPUT_VALUE );
			
			System.out.println( f.getDataType() );
			
			switch( f.getDataType() ) {
			case EVIDENCE:
				f.setValue( new Evidence( 1.0, 1.0 ) ); //r.nextDouble(), r.nextDouble() ) );
				e.setField( chunk, FieldType.INPUT_VALUE, f );
				break;
			case INTEGER:
				f.setValue( 50 ); //r.nextInt() );
				e.setField( chunk, FieldType.INPUT_VALUE, f );
				break;
			case REAL:
				f.setValue( 0.5 ); //r.nextDouble() );
				e.setField( chunk, FieldType.INPUT_VALUE, f );
				break;
			case STRING:
				f.setValue( "ABC" );
				e.setField( chunk, FieldType.INPUT_VALUE, f );
				break;
			case DISTRIBUTION: {
				Distribution dist = f.getValue();
				int distSize = dist.getValues().size();
				double[] rndDists = randomProbabilityArray(distSize,r);
				f.setValue(getDistributionFromDoubleArray(rndDists));
				e.setField( chunk, FieldType.INPUT_VALUE, f );
			}
				break;
				default:
					break;
			}
			
			System.out.println( "=> " + f.getValue().toString() );
		}
	}
	
	static void report( RiskAnalysisEngine e ) {
		for( Chunk chunk : e.queryModel( ModelSlice.OUTPUT_DATA ) ) {
			
			Field output = e.getField( chunk, FieldType.OUTPUT_VALUE );
			
			System.out.println( 
					chunk.getId() + ": " + 
					output.getDataType() + " " +
					output.getValue() );
		}
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	private static double[] randomProbabilityArray(int size,Random rnd) {
		
//		Random rnd = new Random();
		int limit = size > 1 ? (100 / (size - 1)) : 100;
		int partialSum = 0; 
		int[] intArr = new int[size];
		for (int i = 0; i < size - 1; i++) {
			int random = rnd.nextInt(limit);
			intArr[i] = random;
			partialSum += random;
		}
		intArr[size-1] = 100 - partialSum;
		double[] resArray = new double[size];
		for (int i = 0; i < resArray.length; i++) {
			resArray[i] = (double)intArr[i] / 100;
		}
		return resArray;
	}
	
	private static Distribution getDistributionFromDoubleArray(double[] array) {
		
		List<Double> values = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			values.add(array[i]);
		}
		Distribution result = new Distribution();
		result.setValues(values);
		return result;
	}
}
