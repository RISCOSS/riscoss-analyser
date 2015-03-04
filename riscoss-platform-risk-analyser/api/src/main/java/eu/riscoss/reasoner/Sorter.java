package eu.riscoss.reasoner;

public interface Sorter {
	public void add( Chunk c, Field field );
	public Iterable<Rank> order();
}
