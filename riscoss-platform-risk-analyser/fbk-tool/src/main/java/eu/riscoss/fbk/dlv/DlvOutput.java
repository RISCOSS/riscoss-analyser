package eu.riscoss.fbk.dlv;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class DlvOutput implements Iterable<DlvOutput.Solution>
{
	
	public class Solution implements Iterable<DlvOutput.Solution.Predicate>
	{
		ArrayList<Predicate>	predicates	= new ArrayList<Predicate>();
		int						cost[]		= { 0, 0 };
		
		public class Predicate
		{
			public String	name		= "";
			public String[] vars;
			
			public Predicate( String name, String variable )
			{
				this.name = name;
				vars = new String[] { variable };
			}
			
			public Predicate(String name, String[] vars)
			{
				this.name = name;
				this.vars = vars;
			}
		}
		
		boolean scan( BufferedReader br )
		{
			boolean pending = false;
			
			try
			{
				String line = null;
				
				while( (line = br.readLine()) != null )
				{
					line = line.trim();
					
					if( line.startsWith( "DLV" ) )
					{
						continue;
					} else if( line.length() < 1 )
					{
						if( pending )
						{
							return true;
						}
					} else if( line.startsWith( "{" ) )
					{
						parsePredicateList( line );
						
						// Do not return; scan another line to check if there
						// are costs
						pending = true;
					} else if( line.startsWith( "Best model" ) )
					{
						parsePredicateList( line );
						
						// Do not return; need to scan another line with costs
						pending = true;
					} else if( line.startsWith( "Cost" ) )
					{
						return parseCosts( line );
					}
				}
				
				// If there was a scan ongoing, return true
				return pending;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
				return false;
			}
		}
		
		boolean parsePredicateList( String line )
		{
			predicates.clear();
			
			int p = line.indexOf( "{" );
			
			if( p != -1 )
			{
				String[] tokens = line.substring( p + 1, line.length() - 1 ).split( "[,]" );
				if( tokens.length < 1 )
					return false;
				for( String t : tokens )
				{
					String[] parts = t.replaceAll( "[{})]", " " ).trim().split( "[(]" );
					if( parts.length == 2 )
					{
						String[] vars = parts[1].split( "[,]" );
						if( vars.length > 1 )
							predicates.add( new Predicate( parts[0], vars ) );
						else
							predicates.add( new Predicate( parts[0], parts[1] ) );
					}
				}
				return true;
			}
			
			return false;
		}
		
		boolean parseCosts( String line )
		{
			String costString = line.substring( line.indexOf( "<" ) + 2, line.lastIndexOf( "]>" ) );
			
			String parts[] = costString.split( "[:]" );
			
			// System.out.println( parts[0] + ":" + parts[1] );
			
			try
			{
				cost[0] = Integer.parseInt( parts[0] );
			}
			catch (Exception ex)
			{
				cost[0] = -1;
			}
			try
			{
				cost[1] = Integer.parseInt( parts[1] );
			}
			catch (Exception ex)
			{
				cost[1] = -1;
			}
			
			return true;
		}
		
		@Override
		public Iterator<Predicate> iterator()
		{
			return predicates.iterator();
		}
		
		public String getCost()
		{
			return cost[0] + ":" + cost[1];
		}
		
	}
	
	public class LineIterator implements Iterator<DlvOutput.Solution>
	{
		BufferedReader	br;
		String			line			= null;
		Solution		formattedLine	= new Solution();
		
		LineIterator( InputStream is )
		{
			br = new BufferedReader( new InputStreamReader( is ) );
		}
		
		@Override
		public boolean hasNext()
		{
			return formattedLine.scan( br );
		}
		
		@Override
		public Solution next()
		{
			return formattedLine;
		}
		
		@Override
		public void remove()
		{
		}
		
	}
	
	InputStream	is;
	
	public DlvOutput( InputStream is )
	{
		this.is = is;
	}
	
	@Override
	public Iterator<DlvOutput.Solution> iterator()
	{
		return new LineIterator( is );
	}
	
	static class SolutionIterator implements Iterable<eu.riscoss.fbk.language.Solution>,
			Iterator<eu.riscoss.fbk.language.Solution>
	{
		LineIterator			it;
		eu.riscoss.fbk.language.Solution	solution	= new eu.riscoss.fbk.language.Solution();
		
		public SolutionIterator( DlvOutput dlvOutput )
		{
			it = dlvOutput.new LineIterator( dlvOutput.is );
		}
		
		@Override
		public boolean hasNext()
		{
			return it.hasNext();
		}
		
		@Override
		public eu.riscoss.fbk.language.Solution next()
		{
			solution.clear();
			
			DlvOutput.Solution s = it.next();
			
			for( DlvOutput.Solution.Predicate pred : s )
			{
				for( String var : pred.vars )
					solution.addValue( var, pred.name, "true" );
			}
			
			solution.setProperty( "cost", s.getCost() );
			
			return solution;
		}
		
		@Override
		public void remove()
		{
		}
		
		@Override
		public Iterator<eu.riscoss.fbk.language.Solution> iterator()
		{
			return this;
		}
		
	}
	
	public Iterable<eu.riscoss.fbk.language.Solution> solutions()
	{
		return new SolutionIterator( this );
	}
}
