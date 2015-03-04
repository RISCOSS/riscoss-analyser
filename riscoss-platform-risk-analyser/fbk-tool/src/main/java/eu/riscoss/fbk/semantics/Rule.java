package eu.riscoss.fbk.semantics;


public class Rule
{
	public enum TYPE {
		SAT_SAT, SAT_DEN, DEN_SAT, DEN_DEN
	}
	
	public static final int All = 0;
	public static final int Exists = 1;
	
	public String targetPred;
	
	
	TYPE type = TYPE.SAT_SAT;
//	public double fx = 1;
	
	public Condition condition = new Condition( "", Operator.Equals );
	
	public int connective = All;
	
	
	public Rule( int connective, String target, Condition condition )
	{
		this.connective = connective;
		this.targetPred = target;
		this.condition = condition;
	}
	
	public Rule( String target, Condition condition )
	{
		this( All, target, condition );
	}
	
	public Rule( String target, TYPE type, Condition condition )
	{
		this( All, target, condition );
//		this.fx = fx;
		this.type = type;
	}
	
	public Condition getCondition() {
		return condition;
	}
	
	public String getSourcePred() {
		return condition.getPredicate();
	}

	public TYPE getPropagationType() {
		return this.type;
	}
}