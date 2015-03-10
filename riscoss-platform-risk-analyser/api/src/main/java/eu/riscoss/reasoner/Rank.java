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


public class Rank implements Comparable<Rank> {
	Chunk chunk;
	Field field;
	double rank;
	public Rank( Chunk c, Field f ) {
		this.chunk = c;
		this.field = f;
		this.rank = mkrank(f);
	}
	private double mkrank( Field f ) {
		switch( f.getDataType() ) {
		case STRING:
			return 0;
		case REAL:
			return (double)f.getValue();
		case INTEGER:
			return 0;
		case EVIDENCE:
			return ((Evidence)f.getValue()).getSignal();
		case DISTRIBUTION:
			return new Evidence( (Distribution)f.getValue() ).getSignal();
		default:
			return 0;
		}
	}
	@Override
	public int compareTo(Rank o) {
		if( rank > o.rank ) return -1;
		if( rank < o.rank ) return 1;
		return chunk.id.compareTo( o.chunk.id );
	}
	public double getRank() {
		return rank;
	}
	public Chunk getChunk() {
		return chunk;
	}
	public Field getField() {
		return field;
	}
}