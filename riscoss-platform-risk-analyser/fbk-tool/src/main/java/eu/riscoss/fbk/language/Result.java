package eu.riscoss.fbk.language;

import java.util.ArrayList;

public interface Result
{
	public static final Result NO_SOLUTIONS = new Result() {
		@Override
		public String getDescription() {
			return "No solutions";
		}

		@Override
		public Iterable<Solution> solutions() {
			return new ArrayList<Solution>();
		}};
	
	
	public String getDescription();
	
	public Iterable<Solution> solutions();
}
