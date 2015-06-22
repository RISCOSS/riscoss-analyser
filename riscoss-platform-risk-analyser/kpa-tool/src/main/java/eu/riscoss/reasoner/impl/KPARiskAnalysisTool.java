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

package eu.riscoss.reasoner.impl;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import smile.Network;
import smile.SMILEException;
import smile.UserProperty;
import eu.riscoss.kpa.util.NetworkElementType;
import eu.riscoss.kpa.util.UserPropertyCheckResult;
import eu.riscoss.reasoner.ReasonerException;
import eu.riscoss.reasoner.AnalysisResponse;
import eu.riscoss.reasoner.Chunk;
import eu.riscoss.reasoner.DataType;
import eu.riscoss.reasoner.Distribution;
import eu.riscoss.reasoner.Field;
import eu.riscoss.reasoner.FieldType;
import eu.riscoss.reasoner.ModelSlice;
import eu.riscoss.reasoner.RiskAnalysisEngine;

public class KPARiskAnalysisTool implements RiskAnalysisEngine {

	// network properties
	public static final String BN_TYPE_PROP_NAME = "Type";
	public static final String BN_TYPE_PROP_VALUE_INDICATOR = "INDICATOR";
	public static final String BN_TYPE_PROP_VALUE_RISK = "RISK";
	
	// predefined node properties
	public static final String NODE_TYPE_PROP_NAME = "Type";
	public static final String NODE_TYPE_PROP_VALUE_INPUT = "INPUT";
	public static final String NODE_TYPE_PROP_VALUE_INDICATOR = "INDICATOR";
	public static final String NODE_TYPE_PROP_VALUE_RISK = "RISK";
	
	// dynamic node properties
	public static final String NODE_SET_PROP_NAME = "Set";
	public static final String NODE_SET_PROP_VAL = "Y";
	
	public static final double DIST_SUM_LIMIT_LOW = 0.999;
	public static final double DIST_SUM_LIMIT_HI = 1.001;

	// store BN references in this map
	private final Map<String, Network> networkMap;
	private String lastMessage = "";
	
	/**
	 * Default constructor.
	 */
	public KPARiskAnalysisTool() {

		this.networkMap = new HashMap<>();
	}
	
	/**
	 * Gets the user property value in upper-case. Internal checks are case insensitive.
	 * @param props properties
	 * @param propName property name
	 * @return property value, or <code>null</code> if there's no such property, 
	 * its value is null, or properties supplied are empty/null.
	 */
	private String getUserPropertyValue(UserProperty[] props, String propName) {
		
		if(props == null)
			return null;
		for(UserProperty up : props) {
			if(up.name != null && propName.toUpperCase().equals(up.name.toUpperCase()) && up.value != null) {
				return up.value.toUpperCase();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private void setUserPropertyValue(UserProperty[] props, String propName, String propVal) {
		
		if(props == null)
			return;
		for(UserProperty up : props) {
			if(up.name != null && propName.toUpperCase().equals(up.name.toUpperCase())) {
				up.value = NODE_SET_PROP_VAL.toUpperCase();
				break;
			}
		}
	}
	
	/**
	 * Get chunk list for specific node type in the given BN.
	 * @param bn BayesiaN network
	 * @param nodeType node type
	 * @return
	 */
	private Collection<Chunk> getChunksForNodeType(Network bn, String nodeType) {
		
		ArrayList<Chunk> chunks = new ArrayList<>();
		for(int hNode : bn.getAllNodes()) {
			String propVal = getUserPropertyValue(
					bn.getNodeUserProperties(hNode), NODE_TYPE_PROP_NAME);
			if(propVal != null && nodeType.toUpperCase().equals(propVal)) {
				Chunk chunk = new Chunk(bn.getNodeId(hNode), bn.getId());
				chunks.add(chunk);
			}
		}
		return chunks;
	}
	
	/**
	 * Converts the <code>Distribution</code> object to <code>double[]</code> array.
	 * @param distribution
	 * @param chunk (just for reference)
	 * @return <code>double[]</code> array
	 */
	private double[] getDoubleArrayFromDistribution(Distribution distribution, Chunk chunk) {
		
		List<Double> values = distribution.getValues();
		double[] result = new double[values.size()];
		int i = 0; double sum = 0.0;
		for (Double d : values) {
			result[i++] = d.doubleValue();
			sum += d;
		}
		/* sum != 1.0 */
		// seems that we need to introduce less rigorous check here, because of the floating point imprecision
		// therefore, lower and upper limits are introduced. 3 decimal places should be sufficient...
		if(sum < DIST_SUM_LIMIT_LOW || sum > DIST_SUM_LIMIT_HI) {
			throw new ReasonerException(String.format(
					"Sum of distribution values [raw: " + sum + ", formatted: %f] is not equal to 1.0 for node: '%s'. "
					+ "Check performed within limits: %f - %f", sum, chunk.getId(), DIST_SUM_LIMIT_LOW, DIST_SUM_LIMIT_HI));
		}
		return result;
	}
	
	/**
	 * Converts the <code>double[]</code> array to <code>Distribution</code> object.
	 * @param array
	 * @return <code>Distribution</code>
	 */
	private Distribution getDistributionFromDoubleArray(double[] array) {
		
		List<Double> values = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			values.add(array[i]);
		}
		Distribution result = new Distribution();
		result.setValues(values);
		return result;
	}
	
	/**
	 * Retrieves the chunk list for <code>ModelSlice.INPUT_DATA</code> or empty list if no chunks.
	 * @return chunk list
	 */
	private Iterable<Chunk> getInputModelSlice() {
		
		ArrayList<Chunk> chunks = new ArrayList<>();
		for(String bnId : this.networkMap.keySet()) {
			Network bn = this.networkMap.get(bnId);	
			// we get input chunks from indicator networks (tier 1), therefore those nodes
			// are marked as such (in KPA terminology: risk drivers)
			// we also need to get input chunks from Risk network (tier 2), therefore those 
			// nodes are also marked as such (in KPA terminology: contextual indicators)
			chunks.addAll(getChunksForNodeType(bn, NODE_TYPE_PROP_VALUE_INPUT));
		}
		return chunks;
	}
	
	/**
	 * Retrieves the chunk list for <code>ModelSlice.OUTPUT_DATA</code> or empty list if no chunks.
	 * In the context of KPA risk analysis tool, output is represented by RISK nodes, as well as INDICATOR nodes 
	 * in the RISK network exclusively (tier 2, i.e. KPA level 3).
	 * @return chunk list
	 */
	private Iterable<Chunk> getOutputModelSlice() {
		
		ArrayList<Chunk> chunks = new ArrayList<>();
		for(String bnId : this.networkMap.keySet()) {
			Network bn = this.networkMap.get(bnId);
			String bnPropVal = getUserPropertyValue(bn.getUserProperties(), BN_TYPE_PROP_NAME);
			if(BN_TYPE_PROP_VALUE_RISK.equals(bnPropVal)) {
				// we only get the output from the RISK network - in the RISK network output 
				// is represented by all RISK nodes, as well as all INDICATOR nodes
				chunks.addAll(getChunksForNodeType(bn, NODE_TYPE_PROP_VALUE_RISK));
				chunks.addAll(getChunksForNodeType(bn, NODE_TYPE_PROP_VALUE_INDICATOR));
				break;
			}
		}
		return chunks;
	}
	
	/**
	 * Gets the <code>Field</code> object with type DISTRIBUTION for a given input chunk (node).
	 * @param chunk input chunk
	 * @return <code>Field</code> object
	 */
	private Field getDistributionFieldForChunk(Chunk chunk) {
		
		Field result = null;
		Network bn = null;
		try {
			bn = this.networkMap.get(chunk.getStereotype());
			if(bn == null) {
				throw new ReasonerException(String.format(
						"No model was loaded for given chunk: id = %s, stereotype = %s", chunk.getId(), chunk.getStereotype()));
			}
			if(!bn.isValueValid(chunk.getId())) {
				bn.updateBeliefs(); // need to update, the state has changed by setting new evidences
				// TODO this is probably superfluous now as update is performed during setField() - remove ?
			}
			// we need to query the network at this point, in order to retrieve the node value, or the
			// actual virtual evidence value, if the virtual evidence was previously set for the node
			if(bn.isVirtualEvidence(chunk.getId())) {
				result = new Field(DataType.DISTRIBUTION, getDistributionFromDoubleArray(bn.getVirtualEvidence(chunk.getId())));
			} else {
				result = new Field(DataType.DISTRIBUTION, getDistributionFromDoubleArray(bn.getNodeValue(chunk.getId())));
			}
			return result;
		} catch(SMILEException se) {
			throw new ReasonerException(se.getMessage());
		}
	}
	
	/**
	 * Checks if input distribution matches the actual node distribution size. 
	 * @param distribution input distribution
	 * @param array node value
	 * @return
	 */
	private boolean checkDistributionCount(Distribution distribution, double[] array) {
		
		return (distribution.getValues().size() == array.length);
	}
		
	/**
	 * Checks if a supplied user property value is valid, based on agreed BN convention.
	 * @param type network element type (network or node)
	 * @param value property value
	 * @return <code>true</code> / <code>false</code>, depending on property value validity
	 */
	private boolean isValidPropertyValue(NetworkElementType type, String value) {
		
		switch(type) {
		case NETWORK:
			return ( BN_TYPE_PROP_VALUE_INDICATOR.equals(value) || 
					 BN_TYPE_PROP_VALUE_RISK.equals(value) );
		case NODE:
			return ( NODE_TYPE_PROP_VALUE_INPUT.equals(value) || 
					 NODE_TYPE_PROP_VALUE_INDICATOR.equals(value) || 
					 NODE_TYPE_PROP_VALUE_RISK.equals(value) );
		default:
			return false;
		}
	}
	
	/**
	 * Checks the network and its nodes for validity of user properties.
	 * @param bn BayesiaN network to check for user properties.
	 * @return list of elements with invalid user properties or <code>null</code>
	 */
	private List<UserPropertyCheckResult> checkNetworkUserProperties(Network bn) {
		
		List<UserPropertyCheckResult> result = null;
		String val = getUserPropertyValue(bn.getUserProperties(), BN_TYPE_PROP_NAME);
		if(!isValidPropertyValue(NetworkElementType.NETWORK, val)) {
			if(result == null) {
				result = new ArrayList<>(0);
			}
			result.add(new UserPropertyCheckResult(NetworkElementType.NETWORK, bn.getId()));
		}
		for(String nodeId : bn.getAllNodeIds()) {
			val = getUserPropertyValue(bn.getNodeUserProperties(nodeId), NODE_TYPE_PROP_NAME);
			if(!isValidPropertyValue(NetworkElementType.NODE, val)) {
				if(result == null) {
					result = new ArrayList<>(0);
				}
				result.add(new UserPropertyCheckResult(NetworkElementType.NODE, nodeId));
			}
		}
		return result;
	}
	
	@Override
	public void loadModel(String xmlModel) {
		
		PrintWriter pw = null;
		File tmpFile = null;
		try {
			// unfortunately, current official jSmile bundle does not provide a method to load the network 
			// in another way than reading it from a file, so we need to store the contents to a temporary file
			String tmpPath = System.getProperty("java.io.tmpdir");
			String tmpFilePath = tmpPath + File.separatorChar + UUID.randomUUID().toString() + ".xdsl";
			tmpFile = new File(tmpFilePath);
			pw = new PrintWriter(tmpFilePath);
			pw.write(xmlModel);
			pw.flush();
			
			// create (load) network
			Network bn = new Network();
			try {
				bn.readFile(tmpFilePath);
			} catch(SMILEException se) {
				//se.printStackTrace();
//				System.err.println("Unrecognizable model loaded, skipping. Error message: " + se.getMessage());
				return;
			}
			
			// check network and node properties are valid
			List<UserPropertyCheckResult> chkResult = checkNetworkUserProperties(bn);
			if(chkResult != null) {
				throw new Exception("Network '" + bn.getId() + "' has invalid user properties for elements: " + 
									UserPropertyCheckResult.checkResultsToString(chkResult));
			}
			
			// update network beliefs and store in reference map 
			bn.updateBeliefs();
			this.networkMap.put(bn.getId(), bn);
			
		} catch(Exception ex) {
			// for now, just wrap the generic exception
			throw new ReasonerException(ex.getMessage());
		} finally {
			if(pw != null)
				pw.close();
			if(tmpFile != null) // delete temporary file
				tmpFile.delete();
		}
	}

	@Override
	public Iterable<Chunk> queryModel(ModelSlice slice) {
		
//		if(this.networkMap.keySet() == null || this.networkMap.keySet().isEmpty())
//			throw new ReasonerException("No model was loaded");
		switch(slice) {
		case INPUT_DATA:
			return getInputModelSlice();
		case OUTPUT_DATA:
			return getOutputModelSlice();
		default:
			throw new ReasonerException("Unsupported model-slice type");
		}
	}

	@Override
	public Field getField(Chunk chunk, FieldType field) {

		// should we check here if the node identified by input chunk actually exists / has value ?
		// the thing is that SMILEException ( a runtime exception ), and this one will be thrown in
		// the case node id (i.e. chunk id) supplied is invalid. It's wrapped in getFieldForChunk().
		// E.G. smile.SMILEException: Invalid node id: VerticesXX
		switch(field) {
		case INPUT_VALUE:
			return getDistributionFieldForChunk(chunk);
		case OUTPUT_VALUE:
			return getDistributionFieldForChunk(chunk);
		case QUESTION:
			// as explained, this should return a 'natural language' question.
			// currently, this is extremely simplified, needs further discussion for forming proper questions
			Field result = new Field(DataType.STRING, 
					String.format("What are the distribuion values for '%s' ?", chunk.getId()));
			return result;
		default:
			return null;
//			throw new ReasonerException("Unsupported field type");
		}
	}

	@Override
	public void setField(Chunk chunk, FieldType type, Field field) {
		
		switch(type) {
		case INPUT_VALUE:
			if(field.getDataType() != DataType.DISTRIBUTION) {
				throw new ReasonerException("Wrong field data-type: " + field.getDataType() + 
											" - eu.riscoss.reasoner.Distribution expected");
			}
			Network bn = this.networkMap.get(chunk.getStereotype());
			Distribution distribution = (Distribution)field.getValue();
			double[] nodeVal = bn.getNodeValue(chunk.getId());
			if(!checkDistributionCount(distribution, nodeVal)) {
				throw new ReasonerException(String.format(
						"Mismatch between input (%d) and actual (%d) distribution count for node: '%s'", 
						distribution.getValues().size(), nodeVal.length, chunk.getId()));
			}
			try {
				// set new evidence
				String nodeId = chunk.getId();
				bn.setVirtualEvidence(nodeId, getDoubleArrayFromDistribution(distribution, chunk));
				
				// indicate that (INPUT) node is set
				// TODO move setting/adding the property to separate method ?
				UserProperty[] props = bn.getNodeUserProperties(nodeId);
				String setVal = getUserPropertyValue(props, NODE_SET_PROP_NAME);
				if(setVal == null) {
					props = Arrays.copyOf(props, props.length + 1);
					props[props.length - 1] = new UserProperty(NODE_SET_PROP_NAME, NODE_SET_PROP_VAL);
					bn.setNodeUserProperties(nodeId, props);
				}
				else if(!NODE_SET_PROP_VAL.equals(setVal)) {
					for(UserProperty up : props) {
						if(up.name != null && NODE_SET_PROP_NAME.toUpperCase().equals(up.name.toUpperCase())) {
							up.value = NODE_SET_PROP_VAL;
							bn.setNodeUserProperties(nodeId, props);
							break;
						}
					}
//					setUserPropertyValue(props, NODE_SET_PROP_NAME, NODE_SET_PROP_VAL);
				}
				
				// update beliefs after single evidence is set to avoid throwing exceptions
				// on successive evidence setting for the same node
				bn.updateBeliefs();
				
			} catch (SMILEException se) {
				throw new ReasonerException(se.getMessage());
			}
			break;
		default:
			// I don't think it is meaningful to set output fields... throw an exception here ?
			// For the rest of the field types I'm not sure what's that all about... 
			// UPDATE: Albert0 explained that this is redundant, and can be disregarded.
			break;
		}
	}

	@Override
	public <T> T getDefaultValue(Chunk chunk) {
		
		Field f = getDistributionFieldForChunk(chunk);
		
		if( f == null ) return null;
		
		return f.getValue();
		
		// TODO still not clear on what this represents in KPA context for now 
		// we'll just return null value, as we don't have the actual 'default'
//		return null;
	}

	@Override
	public String getLastMessage() {

		return this.lastMessage;
	}

	@Override
	public AnalysisResponse runAnalysis(String[] args) {
						
		AnalysisResponse result = null;
		try {
			
			// check if any model was loaded
			if(this.networkMap.keySet() == null || this.networkMap.keySet().isEmpty()) {
				throw new ReasonerException("No model was loaded");
			}
			
			// more in-depth check is to analyze if all required networks are loaded, e.g.:
			// if risk was loaded, and it expects input from some indicator network that was not 
			// loaded, we'd need to indicate this case (performed below by counting while match)
			// TODO as this is basically masked by input nodes check, maybe consider removing ?
			int indicatorCountInput = 0, indicatorCountInRisk = 0, indicatorCountMatched = 0;
			
			Network riskBn = null;
	    	for (Network bn : this.networkMap.values()) {
	    		for(int hNode : bn.getAllNodes()) {
	    			// check if all input nodes are set first
	    			if(NODE_TYPE_PROP_VALUE_INPUT.equals(getUserPropertyValue(bn.getNodeUserProperties(hNode), NODE_TYPE_PROP_NAME))) {
		    			String val = getUserPropertyValue(bn.getNodeUserProperties(hNode), NODE_SET_PROP_NAME);
		    			if(!NODE_SET_PROP_VAL.equals(val)) {
		    				throw new ReasonerException("Not all input nodes have been set");
		    			}
	    			}
	    		}
				String bnPropVal = getUserPropertyValue(bn.getUserProperties(), BN_TYPE_PROP_NAME);
				if(BN_TYPE_PROP_VALUE_INDICATOR.equals(bnPropVal)) {
					// update beliefs for all indicator networks
					bn.updateBeliefs();
				}
				else if(BN_TYPE_PROP_VALUE_RISK.equals(bnPropVal)) {
					// store risk reference
					riskBn = bn;
				}
	    	}
	    	
	    	if(riskBn != null) {	    		
		    	// propagate evidences to the risk network
		    	for (Network bn : this.networkMap.values()) {
//		    		if(riskBn == bn) {
//		    			continue;
//		    		}
		    		for(String nodeId1 : bn.getAllNodeIds()) {// tier 1		    			
		    			String propVal = getUserPropertyValue(bn.getNodeUserProperties(nodeId1), NODE_TYPE_PROP_NAME);
		    			if(NODE_TYPE_PROP_VALUE_INDICATOR.equals(propVal)) {// indicator located
			    			if(riskBn == bn) {
			    				// we found 'indicator' node in the risk network
			    				indicatorCountInRisk++;
			    			}
			    			else {
			    				// we found 'indicator' node in the indicator network, now we
			    				// need to match it in the risk network to update the evidence
			    				indicatorCountInput++;
			    				for(String nodeId2 : riskBn.getAllNodeIds()) {// tier 2
			    					if(nodeId2.equals(nodeId1)) {// node matched by ID
			    						indicatorCountMatched++;
			    						double[] value = bn.getNodeValue(nodeId1);
			    						riskBn.setVirtualEvidence(nodeId2, value);
			    					}
			    				}
			    			}
		    			}
		    		}
		    	}
		    	// compare indicator count in both tiers
		    	if(indicatorCountMatched < indicatorCountInRisk) {
		    		throw new ReasonerException(String.format(
		    				"Indicators count mismatch: input = %d, risk = %d, matched = %d", 
		    				indicatorCountInput, indicatorCountInRisk, indicatorCountMatched));
		    	}
		    	// finally, update risk network beliefs
		    	riskBn.updateBeliefs();
		    	result = AnalysisResponse.DONE;
	    	} else {
	    		throw new ReasonerException("Risk network was not loaded");
	    	}
		}
		catch(Exception ex) {
			// TODO this seems like a very stupid way of throwing and catching exceptions...
			// I guess it will be more clear after API is defined in more detail what to do.
			this.lastMessage = ex.getMessage();
			result = AnalysisResponse.FAILED;
		}
    	
    	return result;
	}

	@Override
	public void resetFields() {
		// TODO Auto-generated method stub
		
	}

}
