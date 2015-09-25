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

package eu.riscoss.fbk.lp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eu.riscoss.reasoner.Evidence;

public class Edge
{
	private Solver satSolver;
	private Solver denSolver;

	private Node				target;				// target node for this relation
	private Map<String,Node>	sources		= new HashMap<String,Node>();
	private List<Node>			nodes		= new ArrayList<Node>();			// source nodes for this relation
	private float				weight		= 1;
	private String				mnemonic	= "" + this.hashCode();
	
	private String				js = null;
	
	
	public Edge() {
		this( null );
	}

	public Edge( Solver solver ) {

		satSolver = solver;
		denSolver = new Solver.AndSolver( true );
		
		target	= null;
	
	}
	
	// Unused
//	public Edge( Solver solver, Node theTarget, LinkedList<Node> theSources ) {
//		satSolver = solver;
//		denSolver = new Solver.AndSolver( true );
//		
//		target	= theTarget;
//		sources	= theSources;
//	}
	
	@SuppressWarnings("unused")
	private static LinkedList<Node> mkLinkedList( Node aNode ) {
		LinkedList<Node> aList = new LinkedList<Node>();
		aList.add(aNode);
		return aList;
	}
	
	public Label solveForS() {
		return satSolver.solve( this );
	}
	
	public Label solveForD() {
		return denSolver.solve( this );
	}
	
	public Node getTarget() {
		return target;
	}
	
	public List<Node> getSources() {
		return nodes;
	}
	
	public void setTargetNode(Node aNode) {
		target = aNode;
	}
	
	public void addSourceNode(Node aNode) {
		sources.put( aNode.getName(), aNode );
		nodes.add(aNode);
	}
	
	public void informNodes() {
		target.addIncomingRelation(this);
		
		for( Node node : nodes )
			node.addOutgoingRelation( this );
	}

	public void setSatSolver( Solver solver ) {
		this.satSolver = solver;
	}

	public void setDenSolver( Solver solver ) {
		this.denSolver = solver;
	}

	public void setWeight( float weight ) {
		this.weight = weight;
	}

	public float getWeight() {
		return weight;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	public String getMnemonic() {
		return mnemonic;
	}
	
	public String toString() {
		return mnemonic;
	}
	
//	public void propagate() {
//		
//		if( js != null ) {
//			try {
//				Evidence e = new Evidence( 0, 0 );
////				JsExtension.get().set( "evidence", e );
//				JsExtension.get().put( "nodes", getSources() );
//				JsExtension.get().put( "sources", toArray( getSources() ) );
//				String code = "e=" + js;
//				try {
//					e = (Evidence)JsExtension.get().eval( code );
//				}
//				catch( ClassCastException ex ) {}
//				
//				Label sat = new Label( (float)e.getPositive() * weight );
//				Label den = new Label( (float)e.getNegative() * weight );
//				
//				if( sat.isGreaterThan( target.getSatLabel() ) ) 
//					target.setSatLabel( sat );
//				if( den.isGreaterThan( target.getDenLabel() ) )
//					target.setDenLabel( den );
//				
//			}
//			catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
//		else {
//			Label sat = solveForS();
//			Label den = solveForD();
//			
//			if( sat.isGreaterThan( target.getSatLabel() ) )
//				target.setSatLabel( sat );
//			if( den.isGreaterThan( target.getDenLabel() ) )
//				target.setDenLabel( den );
//		}
//	}
	
	List<Evidence> toArray( Collection<Node> nodes ) {
		List<Evidence> list = new ArrayList<>();
		for( Node node : nodes ) {
			list.add( new Evidence( node.getOldSatLabel().getValue(), node.getOldDenLabel().getValue() ) );
		}
		return list;
	}
	
	String srcSatArray( Edge r ) {
		String ret = "";
		String sep = "";
		for( Node n : r.getSources() ) {
			ret += sep + "'" + n.getSatLabel().getValue() + "'";
			sep = ",";
		}
		return ret;
	}
	
	String srcDenArray( Edge r ) {
		String ret = "";
		String sep = "";
		for( Node n : r.getSources() ) {
			ret += sep + "'" + n.getDenLabel().getValue() + "'";
			sep = ",";
		}
		return ret;
	}

	public void setJs( String code ) {
		this.js = code;
	}

	public String getCode() {
		return this.js;
	}
	
	public Node getSource( String name ) {
		return sources.get( name );
	}
}
