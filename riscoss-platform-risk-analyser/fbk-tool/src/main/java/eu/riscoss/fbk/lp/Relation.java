package eu.riscoss.fbk.lp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import eu.riscoss.reasoner.Evidence;

public class Relation
{
	private Solver satSolver;
	private Solver denSolver;

	private Node				target;				// target node for this relation
	private LinkedList<Node>	sources;			// source nodes for this relation
	private float				weight		= 1;
	private String				mnemonic	= "" + this.hashCode();
	
	private String				js = null;
	
	
	public Relation() {
		this( null );
	}

	public Relation( Solver solver ) {
		this( solver, null, new LinkedList<Node>() );
	}
	
	public Relation( Solver solver, Node theTarget, LinkedList<Node> theSources ) {
		satSolver = solver;
		denSolver = new Solver.AndSolver( true );
		
		target	= theTarget;
		sources	= theSources;
	}
	
	@SuppressWarnings("unused")
	private static LinkedList<Node> mkLinkedList( Node aNode ) {
		LinkedList<Node> aList = new LinkedList<Node>();
		aList.add(aNode);
		return aList;
	}
	
	Label solveForS() {
		return satSolver.solve( this );
	}
	
	Label solveForD() {
		return denSolver.solve( this );
	}
	
	public Node getTarget() {
		return target;
	}
	
	public LinkedList<Node> getSources() {
		return sources;
	}
	
	public void setTargetNode(Node aNode) {
		target = aNode;
	}
	
	public void setSourceNodes( LinkedList<Node> theChildren ) {
		sources = theChildren;
	}
	
	public void addSourceNode(Node aNode) {
		sources.add(aNode);
	}
	
	public void addChildrenNodes( LinkedList<Node> additionalChildren ) {
		sources.addAll( additionalChildren );
	}
	
	public void informNodes() {
		target.addIncomingRelation(this);
		
		for( Node node : sources )
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
	
	public void propagate() {
		
		if( js != null ) {
			try {
				Evidence e = new Evidence( 0, 0 );
				JsExtension.get().set( "evidence", e );
				String code = "";
				JsExtension.get().put( "nodes", getSources() );
				JsExtension.get().put( "sources", toArray( getSources() ) );
				code += "e=" + js;
				try {
					e = (Evidence)JsExtension.get().eval( code );
				}
				catch( ClassCastException ex ) {}
				
				Label sat = new Label( (float)e.getPositive() * weight );
				Label den = new Label( (float)e.getNegative() * weight );
				
				if( sat.isGreaterThan( target.getSatLabel() ) ) 
					target.setSatLabel( sat );
				if( den.isGreaterThan( target.getDenLabel() ) )
					target.setDenLabel( den );
				
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		else {
			Label sat = solveForS();
			Label den = solveForD();
			
			if( sat.isGreaterThan( target.getSatLabel() ) )
				target.setSatLabel( sat );
			if( den.isGreaterThan( target.getDenLabel() ) )
				target.setDenLabel( den );
		}
	}
	
	List<Evidence> toArray( List<Node> nodes ) {
		List<Evidence> list = new ArrayList<>();
		for( Node node : nodes ) {
			list.add( new Evidence( node.getOldSatLabel().getValue(), node.getOldDenLabel().getValue() ) );
		}
		return list;
	}
	
	String srcSatArray( Relation r ) {
		String ret = "";
		String sep = "";
		for( Node n : r.getSources() ) {
			ret += sep + "'" + n.getSatLabel().getValue() + "'";
			sep = ",";
		}
		return ret;
	}
	
	String srcDenArray( Relation r ) {
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
}
