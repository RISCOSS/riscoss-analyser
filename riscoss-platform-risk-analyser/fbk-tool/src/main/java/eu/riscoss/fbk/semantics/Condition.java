package eu.riscoss.fbk.semantics;

public class Condition
{
	private String predicate;
//	private String value;
	private Operator op;
	
	public Condition( String predicate, Operator op )
	{
		this.predicate = predicate;
//		this.value = value;
		this.op = op;
	}

	public Operator getOperator() {
		return op;
	}
	
	protected Condition() {}
	
	public String getPredicate()
	{
		return predicate;
	}
	
//	public String getValue() {
//		return value;
//	}
	
	public boolean isPositive()
	{
		return !(op == Operator.Not);
	}
}
