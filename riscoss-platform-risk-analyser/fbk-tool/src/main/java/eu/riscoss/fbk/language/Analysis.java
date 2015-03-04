package eu.riscoss.fbk.language;


public interface Analysis
{
	public void run( Program program );
	
	public Result getResult();
}
