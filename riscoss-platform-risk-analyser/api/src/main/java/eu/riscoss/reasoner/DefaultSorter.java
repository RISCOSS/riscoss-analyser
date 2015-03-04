package eu.riscoss.reasoner;

import java.util.TreeSet;

public class DefaultSorter implements Sorter {
	
	TreeSet<Rank> set = new TreeSet<>();
	
	@Override
	public void add( Chunk c, Field field) {
		set.add( new Rank( c, field ) );
	}
	
	@Override
	public Iterable<Rank> order() {
		return set;
	}
	
}
