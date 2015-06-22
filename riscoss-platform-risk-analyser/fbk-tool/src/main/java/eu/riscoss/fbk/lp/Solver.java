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

import java.util.LinkedList;

public abstract class Solver {
	
	public static final class AndSolver extends Solver {
		public AndSolver( boolean param ) {
			super( param );
		}
		
		public Label solve( Edge rel ) {
			Label productLabel = Label.TOTAL;
			
			for( Node node : rel.getSources() ) {
				Label tempLabel = getOldLabel( node );
				productLabel = product( productLabel, tempLabel );
			}
			
			return new Label( productLabel.getValue() * rel.getWeight() );
		}
	}
	
	public static final class OrSolver extends Solver
	{
		public OrSolver( boolean param )
		{
			super( param );
		}
		
		public Label solve( Edge rel )
		{
			Label sumLabel = Label.NO;
			
			for( Node node : rel.getSources() )
			{
				Label tempLabel = getOldLabel( node );
				sumLabel = sum( sumLabel, tempLabel ); //product( tempLabel, rel.getWeight() ) );
			}
			return new Label( sumLabel.getValue() * rel.getWeight() );
		}
	}
	
	public static final class NoSolver extends Solver
	{
		public NoSolver( boolean param )
		{
			super( param );
		}
		
		public Label solve( Edge rel )
		{
			return new Label( Label.NO.getValue() );
		}
	}
	
	public static final class WeightedSolver extends Solver
	{
		public WeightedSolver( boolean param )
		{
			super( param );
		}
		
		public Label solve( Edge rel )
		{
			Label sumLabel = Label.NO;
			
			for( Node node : rel.getSources() )
			{
				Label tempLabel = getOldLabel( node );
				sumLabel = max( sumLabel, product( tempLabel, rel.getWeight() ) );
			}
			
			return sumLabel;
		}
	}
	
	public static final class NotSolver extends Solver
	{
		public NotSolver( boolean param )
		{
			super( param );
		}
		
		public Label solve( Edge rel )
		{
			for( Node node : rel.getSources() )
			{
				if( getOldLabel( node ).getValue() > 0 )
					return new Label( 0f );
			}
			
			return Label.TOTAL;
		}
	}
	
	boolean negative;
	
	Solver( boolean param )
	{
		negative = param;
	}
	
	public abstract Label solve( Edge rel );
	
	void mustHaveExactlyOneSource( LinkedList<Node> childNodes )
	{
		if (childNodes.size() != 1)
			throw new UnsupportedOperationException
			("children list of " + this.getClass().getName()
					+ " does not contain exactly one element");
	}
	
	boolean getParameterValue()
	{
		return negative;
	}
	
	Label getOldLabel( Node aNode )
	{
//		return new Label( getSignal( aNode ) );
		
		if( negative )
			return aNode.getOldDenLabel();
		else
			return aNode.getOldSatLabel();
	}
	
	public float getSignal( Node node ) {
		return (1 + (node.getSatisfaction() - node.getDenial())) /2;
	}
	
	protected Label sum( Label l1, final Label aLabel )
	{
		float p1 = l1.getValue();
		float p2 = aLabel.getValue();
		return new Label( Math.abs( p1 + p2 - p1 * p2 ) );
	}
	
	protected Label product( Label l1, final Label aLabel )
	{
		float p1 = l1.getValue();
		float p2 = aLabel.getValue();
		return new Label( p1 * p2 );
	}
	
	protected Label product( Label l1, final float aWeight )
	{
		float p1 = l1.getValue();
		return new Label( p1 * aWeight );
	}
	
	protected Label max( Label l1, Label l2 )
	{
		if( Math.abs( l1.getValue() ) > Math.abs( l2.getValue() ) ) return new Label( l1.getValue() );
		else return new Label( l2.getValue() );
		
//		return new Label( Math.max( l1.getValue(), l2.getValue() ) );
	}
}

