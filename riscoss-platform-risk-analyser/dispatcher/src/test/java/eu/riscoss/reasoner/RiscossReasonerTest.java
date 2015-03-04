package eu.riscoss.reasoner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RiscossReasonerTest {
	
	public static void main( String[] args ) {
		
		RiskAnalysisEngine e = ReasoningLibrary.get().createRiskAnalysisEngine(); // new CompoundAnalysisEngine();
		
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "Credit.xdsl" ) ) );
		
//		Random r = new Random( 10 );
		
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "LL_acquisition_v1.3.3_output.istarml" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "quality-model-m24.xml" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "LL_impactmodel_legal-m18.xml" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "a.xml" ) ) );
		
//		try {
//			e.loadModel( new String(Files.readAllBytes(Paths.get( new File(
//					"/Users/albertosiena/a.xml").toURI() ) ) ) );
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "acquisition.istarml" ) ) );
//		
//		e.setField( new Chunk( "i1" ), FieldType.INPUT_VALUE, new Field( DataType.EVIDENCE, new Evidence( 1, 1 ) ) );
//		e.setField( new Chunk( "VOperational_Risk" ), FieldType.INPUT_VALUE, new Field( DataType.EVIDENCE, new Evidence( 1, 1 ) ) );
		
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "xwiki/Timeliness.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "xwiki/Activeness.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "xwiki/Community.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "xwiki/Risk.xdsl" ) ) );
		
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "moodbile/Activeness.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "moodbile/Timeliness.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "moodbile/Risk.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "moodbile/Moodbile_M20_v4.istarml" ) ) );
		
		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "ow2/OW2_Activeness.xdsl" ) ) );
		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "ow2/OW2_License.xdsl" ) ) );
		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "ow2/OW2_Quality.xdsl" ) ) );
		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "ow2/RiskTest.xdsl" ) ) );
		
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "community/Activeness.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "community/Community.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "community/Intellectual Property.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "community/Timeliness.xdsl" ) ) );
//		e.loadModel( convertStreamToString( RiscossReasonerTest.class.getResourceAsStream( "community/Risk.xdsl" ) ) );
		
//		Map<String,Chunk> chunks = new HashMap<>();
//		for( Chunk c : e.queryModel( ModelSlice.INPUT_DATA ) ) {
//			chunks.put( c.getId(), c );
//		}
		
//		e.setField(chunk, type, field)
		
//		set( e, "", 0.1 );
		
//		setField( e, chunks, "VBug_fix_time_for_critical___blocker_level_bugs", FieldType.INPUT_VALUE, new double[] { 0.8333333333333334, 0.0, 0.16666666666666666, 0.0, 0.0 } );
//		setField( e, chunks, "VAverage_bug_fix_time__days_", FieldType.INPUT_VALUE, new double[] { 0.8695652173913043, 0.043478260869565216, 0.08695652173913043, 0.0, 0.0 } );
//		setField( e, chunks, "VCommit_frequency___week", FieldType.INPUT_VALUE, new double[] { 0.0, 0.5, 0.5, 0.0, 0.0 } );
//		setField( e, chunks, "VHour__When_the_commit_was_made", FieldType.INPUT_VALUE, new double[] { 0.8076923076923077, 0.11538461538461539, 0.07692307692307693 } );
//		setField( e, chunks, "VWeekday__When_the_commit_was_made", FieldType.INPUT_VALUE, new double[] { 0.8846153846153846, 0.11538461538461539, 0.0 } );
//		setField( e, chunks, "VHoliday__When_the_commit_was_made", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		
//		setField( e, chunks, "Vertices", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VMinimum_Clustering_Coefficient", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VMinimum_Clustering_Coefficient", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VAverage_Clustering_Coefficient", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VMedian_Clustering_Coefficient", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VBusiness_Strategy_dimensions", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VType_of_Project", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VActivities_in_the_project", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VForum_posts_per_day", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0, 0.0, 0.0 } );
//		setField( e, chunks, "VForum_messages_per_thread", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0, 0.0, 0.0 } );
//		setField( e, chunks, "VMail_per_day", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VOverall_community_size", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VNumber_of_testers__individuals_providing_feedback_", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VNumber_of_companies_using_the_software", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
//		setField( e, chunks, "VCompanies_supporting_the_project__adding_to_code_", FieldType.INPUT_VALUE, new double[] { 1.0, 0.0, 0.0 } );
		
//		scenario1( e );
//		System.out.println();
//		for( Chunk c : e.queryModel( ModelSlice.INPUT_DATA ) ) {
//			System.out.println( c.getId() + ": " + e.getField( c, FieldType.INPUT_VALUE ).getValue() );
//		}
		
//		for( Chunk c : e.queryModel( ModelSlice.INPUT_DATA ) ) {
//			System.out.println( e.getField( c, FieldType.MIN ).getValue() + " - " + e.getField( c, FieldType.MAX ).getValue() );
////			e.setField( c, FieldType.INPUT_VALUE, new Field( DataType.DISTRIBUTION, e.getDefaultValue( c ) ) );
//		}
		
		randomize( e, new Random( 10 ) );
		e.runAnalysis(args);
		typedReport(e);
		
//		ArrayList<Chunk> ic = new ArrayList<>();
//		
//		for( Chunk c : e.queryModel( ModelSlice.INPUT_DATA ) ) {
//			ic.add( c );
//		}
		
	}
	
	private static void setField(RiskAnalysisEngine e, Map<String, Chunk> chunks, String string, FieldType inputValue, double[] ds) {
		Distribution d = new Distribution();
		for( double v : ds )
			d.values.add( v );
		e.setField( chunks.get( string ), FieldType.INPUT_VALUE, new Field( DataType.DISTRIBUTION, d ) );
	}

	static void scenario1( RiskAnalysisEngine e ) {
		set( e, "percentage-of-files-without-license", 0.9803921568627451 );
		set( e, "percentage-of-files-with-public-domain-license", 0.9803921568627451 );
		set( e, "percentage-of-files-with-permissive-license", 0.9803921568627451 );
		set( e, "copyleft-licenses-with-linking", 0.9803921568627451 );
		set( e, "copyleft-licenses", 0.987012987012987 );
		set( e, "number-of-different-licenses", 0.0 );
		set( e, "files-with-unknown-license", 0.9803921568627451 );
		set( e, "files-with-commercial-license", 0.9803921568627451 );
		set( e, "files-with-ads-required-liceses", 0.9803921568627451 );
	}
	
	static void set( RiskAnalysisEngine e, String id, int value ) {
		for( Chunk c : e.queryModel( ModelSlice.INPUT_DATA ) ) {
			if( id.equals( c.getId() ) ) {
				Field f = e.getField( c, FieldType.INPUT_VALUE );
				if( f.getDataType() == DataType.INTEGER ) {
					f.setValue( value );
					e.setField( c, FieldType.INPUT_VALUE, f );
				}
			}
		}
	}
	
	static void set( RiskAnalysisEngine e, String id, double value ) {
		for( Chunk c : e.queryModel( ModelSlice.INPUT_DATA ) ) {
			if( id.equals( c.getId() ) ) {
				Field f = e.getField( c, FieldType.INPUT_VALUE );
				if( f.getDataType() == DataType.REAL ) {
					f.setValue( value );
					e.setField( c, FieldType.INPUT_VALUE, f );
				}
			}
		}
	}
	
	static void randomize( RiskAnalysisEngine e, Random r ) {
		
		Iterable<Chunk> input = e.queryModel( ModelSlice.INPUT_DATA );
		
		for( Chunk chunk : input ) {
			System.out.println( "INPUT FIELD: " + chunk.getId() );
			
			Field f = e.getField( chunk, FieldType.INPUT_VALUE );
			switch( f.getDataType() ) {
			case INTEGER:
				f.setValue( r.nextInt() );
				e.setField( chunk, FieldType.INPUT_VALUE, f );
				break;
			case REAL:
				f.setValue( r.nextDouble() );
				e.setField( chunk, FieldType.INPUT_VALUE, f );
				break;
			case EVIDENCE:
				f.setValue( new Evidence( r.nextDouble(), r.nextDouble() ) );
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
		}
	}
	
	static void report( RiskAnalysisEngine e ) {
		for( Chunk chunk : e.queryModel( ModelSlice.OUTPUT_DATA ) ) {
			
			Field output = e.getField( chunk, FieldType.OUTPUT_VALUE );
			
//			if( output.getValue() instanceof Evidence )
//				if( ((Evidence)output.getValue()).nonZero() )
			System.out.println( 
					chunk.getId() + ": " + 
					output.getDataType() + " " +
					output.getValue() );
		}
	}
	
	static void sortedReport( RiskAnalysisEngine e ) {
		
		Sorter sorter = ReasoningLibrary.get().createSorter();
		
		for( Chunk chunk : e.queryModel( ModelSlice.OUTPUT_DATA ) ) {
			Field output = e.getField( chunk, FieldType.OUTPUT_VALUE );
			sorter.add( chunk, output );
		}
		
		for( Rank rank : sorter.order() ) {
			System.out.println( 
					rank.rank + ": " +
					rank.chunk.getId() + " - " + 
					rank.field.getDataType() + " " +
					rank.field.getValue() );
		}
	}
	
	static void typedReport( RiskAnalysisEngine e ) {
		Map<String,Sorter> categories = new HashMap<String,Sorter>();
		for( Chunk chunk : e.queryModel( ModelSlice.OUTPUT_DATA ) ) {
			Field output = e.getField( chunk, FieldType.OUTPUT_VALUE );
			String type = e.getField( chunk, FieldType.TYPE ).getValue();
			Sorter sorter = categories.get( type );
			if( sorter == null ) {
				sorter = ReasoningLibrary.get().createSorter();
				categories.put( type, sorter );
			}
			sorter.add( chunk, output );
		}
		String[] types = new String[] { "Goal", "Risk", "Data" };
		for( String type : types ) {
			Sorter sorter = categories.get( type );
			if( sorter != null ) {
				System.out.println( type );
				for( Rank rank : sorter.order() ) {
					String name = rank.chunk.getId();
					try {
						name = e.getField( rank.chunk, FieldType.DESCRIPTION ).getValue(); // + " (" + name + ")";
					} catch( Exception ex ) {}
					System.out.println( 
							rank.rank + ": " +
							name + " - " + 
							rank.field.getDataType() + " " +
							rank.field.getValue() );
				}
			}
		}
	}
	
	@SuppressWarnings("resource")
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	private static double[] randomProbabilityArray(int size,Random rnd) {
		
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
