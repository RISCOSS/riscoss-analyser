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

import java.security.InvalidParameterException;
import java.util.LinkedList;

import eu.riscoss.reasoner.Evidence;

public class Node {
	private Label satLabel;      // node label for the satisfaction
	private Label denLabel;      // node label for the negation
	private Label oldSatLabel;   // doubled values for safe propagation
	private Label oldDenLabel;   // doubled values for safe propagation
	private LinkedList<Edge> in;  // list of  rels. where this node is parent
	private LinkedList<Edge> out;   // list of rels. where this node is child
	
	private double object = 0.0; //Double.NaN;
	private String name;
	
	
	public Node(Label aSatLabel, Label aDenLabel) {
		initSatLabel(aSatLabel);
		initDenLabel(aDenLabel);
		in = new LinkedList<Edge>();
		out  = new LinkedList<Edge>();
	};
	
	
	public boolean hasChanged() {
		if (getSatLabel().isEqualTo(getOldSatLabel()) &&
				getDenLabel().isEqualTo(getOldDenLabel()))
			return false;
		else
			return true;
	}
	
	public void syncLabels() {
		syncSatLabel();
		syncDenLabel();
	}
	
	public Label getSatLabel() {
		return satLabel;
	}
	
	public Label getOldSatLabel() {
		return oldSatLabel;
	}
	
	public Label getDenLabel() {
		return denLabel;
	}
	
	public Label getOldDenLabel() {
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
	
	public void setDenLabel(Label aLabel) {
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
	
	void addIncomingRelation( Edge r ) {
		in.add( r );
	}
	
	void addOutgoingRelation( Edge r ) {
		out.add( r );
	}
	
	public LinkedList<Edge> in() {
		return in;
	}
	
	public float getSatisfaction() {
		return getSatLabel().getValue();
	}
	
	public float getDenial() {
		return getDenLabel().getValue();
	}
	
	public Evidence getEvidence() {
		return new Evidence( satLabel.getValue(), denLabel.getValue() );
	}
	
	public void setEvidence( Evidence e ) {
		setSatLabel( new Label( (float) e.getPositive() ) );
		setDenLabel( new Label( (float) e.getNegative() ) );
	}
	
	public void setUserObject( double object ) {
		this.object = object;
	}
	
	public void setValue( double value ) {
		if( Double.isNaN(value) )
			return;
		this.object = value;
	}
	
	public Double getUserObject() {
		return this.object;
	}
	
	public Double getValue() {
		return this.object;
	}


	public void setName( String id ) {
		this.name = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return getName() + " (" + super.toString() + ")";
	}
	
}
