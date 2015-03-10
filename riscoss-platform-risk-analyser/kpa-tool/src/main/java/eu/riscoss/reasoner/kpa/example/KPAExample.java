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

package eu.riscoss.reasoner.kpa.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eu.riscoss.reasoner.AnalysisResponse;
import eu.riscoss.reasoner.Chunk;
import eu.riscoss.reasoner.Distribution;
import eu.riscoss.reasoner.Field;
import eu.riscoss.reasoner.FieldType;
import eu.riscoss.reasoner.ModelSlice;
import eu.riscoss.reasoner.RiskAnalysisEngine;
import eu.riscoss.reasoner.impl.KPARiskAnalysisTool;

public class KPAExample {

	public static void main(String[] args) {

		try {
			
//			System.setProperty( "java.library.path", 
//					System.getProperty( "java.library.path" ) + ":/Users/albertosiena/Downloads/smile_macosx_clang_503_0_38/o" );
			
			// change the path of a folder storing the XDSLs to your likings
//			String basePath = "/Users/albertosiena/Documents/Home/Workspace/RiscossReasonerKPA/src/eu/riscoss/res";
//			String basePath = "D:\\Project Materials\\KPA\\RiskAnalysisTool\\xdsl\\development\\testing";
			String basePath = "D:\\Project Materials\\KPA\\RiskAnalysisTool\\xdsl\\development\\2014-05-20";
			
			String encoding = "UTF-8";
			
			//String xmlModelUnknow = new String(Files.readAllBytes(Paths.get(basePath, "info.txt") ), encoding);
			String xmlModelTimeliness = new String(Files.readAllBytes(Paths.get(basePath, "Timeliness.xdsl") ), encoding);
			String xmlModelActiveness = new String(Files.readAllBytes(Paths.get(basePath, "Activeness.xdsl") ), encoding);
			String xmlModelCommunity = new String(Files.readAllBytes(Paths.get(basePath, "Community.xdsl") ), encoding);
			String xmlModelRisk = new String(Files.readAllBytes(Paths.get(basePath, "Risk.xdsl") ), encoding);
			
			RiskAnalysisEngine engine = new KPARiskAnalysisTool();
			
			//engine.loadModel(xmlModelUnknow);
			engine.loadModel(xmlModelRisk);
			engine.loadModel(xmlModelTimeliness);
			engine.loadModel(xmlModelActiveness);
			engine.loadModel(xmlModelCommunity);
			
			
			// list the required input (with 'default' distribution states)
			Iterable<Chunk> chunks = engine.queryModel(ModelSlice.INPUT_DATA);
			System.out.println("Input slice:");
			for (Chunk chunk : chunks) {
				Field f = engine.getField(chunk, FieldType.INPUT_VALUE);
				Distribution dist = f.getValue();
				System.out.println("\t" + chunk.getStereotype() + 
						(chunk.getStereotype().length() > 4 ? "\t" : "\t\t") + chunk.getId() + "\t" + dist);
			}
			
			// platform sets the new values
			System.out.println("\nGenerating random distributions...");
			for(Chunk chunk : chunks) {
				Field f = engine.getField(chunk, FieldType.INPUT_VALUE);
				switch(f.getDataType())
				{
				case DISTRIBUTION:
					// simulate new probability distributions
					Distribution dist = f.getValue();
					int distSize = dist.getValues().size();
					double[] rndDists = randomProbabilityArray(distSize);
					Distribution newDist = getDistributionFromDoubleArray(rndDists);
					System.out.println("\t" + newDist + " for " + chunk.getId());
					f.setValue(newDist);
					engine.setField(chunk, FieldType.INPUT_VALUE, f);
					break;
				default:
					break;
				}
			}
			
			// expected output
			System.out.println( "\nExpected output: " );
			for(Chunk chunk : engine.queryModel(ModelSlice.OUTPUT_DATA)) {
				System.out.println("\t" + chunk.getStereotype() + "\t" + chunk.getId());
			}
			
			// run the analysis
			AnalysisResponse response = engine.runAnalysis( new String[] {} );
			if(response == AnalysisResponse.DONE)
			{
				System.out.println("\nAnalysis output:");
				for(Chunk chunk : engine.queryModel(ModelSlice.OUTPUT_DATA)) {
					Field output = engine.getField(chunk, FieldType.OUTPUT_VALUE);
					switch(output.getDataType())
					{
					case DISTRIBUTION:
						Distribution dist = output.getValue();
						System.out.println("\t" + chunk.getStereotype() + "\t" + chunk.getId() + "\t" + dist);
						break;
					default:
						break;
					}
				}
				System.out.println("\nAnalysis completed.");
			}
			else {
				System.out.println("\nAnalysis failed!");
				System.out.println(engine.getLastMessage());
			}
			
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
		}
	}

	/**
	 * Method should return an array of double values, with the sum equals to 1.0.
	 * @param size array size
	 * @return random probabilistic array
	 */
	public static double[] randomProbabilityArray(int size) {
		
		Random rnd = new Random();
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
	
	public static Distribution getDistributionFromDoubleArray(double[] array) {
		
		List<Double> values = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			values.add(array[i]);
		}
		Distribution result = new Distribution();
		result.setValues(values);
		return result;
	}
	
	public static String printDistribution(Distribution dist) {
		
		String printDist = "[ ";
		int size = dist.getValues().size();
		if(size == 0)
			return "[ ]";
		if(size == 1)
			return "[ " + dist.getValues().get(0) + " ]";
		Double[] dblArr = new Double[size];
		dist.getValues().toArray(dblArr);
		for (int i = 0; i < size; i++) {
			printDist += dblArr[i];
			if(i < (size - 1)) {
				printDist += ", ";
			}
		}
		printDist += " ]";
		return printDist;
	}
	
}
