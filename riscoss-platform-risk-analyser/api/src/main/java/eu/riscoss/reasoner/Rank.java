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