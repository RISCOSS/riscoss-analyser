package eu.riscoss.fbk.lp;


import java.security.InvalidParameterException;
import java.util.LinkedList;

public class Node {
	private Label satLabel;      // node label for the satisfaction
	private Label denLabel;      // node label for the negation
	private Label oldSatLabel;   // doubled values for safe propagation
	private Label oldDenLabel;   // doubled values for safe propagation
	private LinkedList<Relation> in;  // list of  rels. where this node is parent
	private LinkedList<Relation> out;   // list of rels. where this node is child
	
	
	public Node(Label aSatLabel, Label aDenLabel) {
		initSatLabel(aSatLabel);
		initDenLabel(aDenLabel);
		in = new LinkedList<Relation>();
		out  = new LinkedList<Relation>();
	};
	
	
	boolean hasChanged() {
		if (getSatLabel().isEqualTo(getOldSatLabel()) &&
				getDenLabel().isEqualTo(getOldDenLabel()))
			return false;
		else
			return true;
	}
	
	void syncLabels() {
		syncSatLabel();
		syncDenLabel();
	}
	
	public Label getSatLabel() {
		return satLabel;
	}
	
	Label getOldSatLabel() {
		return oldSatLabel;
	}
	
	public Label getDenLabel() {
		return denLabel;
	}
	
	Label getOldDenLabel() {
		return oldDenLabel;
	}
	
	private void checkLabelConsistency(Label newLabel, Label oldLabel)
			throws InvalidParameterException {
		if (oldLabel == null) return;
		if (newLabel.getClass() != oldLabel.getClass())
			throw new InvalidParameterException
			("Passed label type" + newLabel.getClass().getName()
					+ " does not match current label type"
					+ oldLabel.getClass().getName());
	}
	
	public void setSatLabel(Label aLabel) {
		checkLabelConsistency(aLabel,getSatLabel());
		// stores the current label in the old label field
		oldSatLabel = satLabel;
		// writes in the current label the passed one
		satLabel = aLabel;
	}
	
	private void initSatLabel(Label aLabel) {
		setSatLabel(aLabel);
		setSatLabel(aLabel);
	}
	
	private void syncSatLabel() {
		setSatLabel(getSatLabel());
	}
	
	void setDenLabel(Label aLabel) {
		checkLabelConsistency(aLabel, getDenLabel());
		// stores the current label in the old label field
		oldDenLabel = denLabel;
		// writes in the current label the passed one
		denLabel = aLabel;
	}
	
	private void initDenLabel( Label aLabel ) {
		setDenLabel(aLabel);
		setDenLabel(aLabel);
	}
	
	private void syncDenLabel() {
		setDenLabel( getDenLabel() );
	}
	
	void addIncomingRelation( Relation r ) {
		in.add( r );
	}
	
	void addOutgoingRelation( Relation r ) {
		out.add( r );
	}
	
	LinkedList<Relation> getIncomingRelations() {
		return in;
	}
	
	public float getSatisfaction() {
		return getSatLabel().getValue();
	}
	
	public float getDenial() {
		return getDenLabel().getValue();
	}
}
