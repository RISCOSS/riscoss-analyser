package eu.riscoss.fbk.util;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CommandLine
{
	public static class Option
	{
		String	name;
		String	template	= null;
		String	description	= "";
		
		public Option( String name, String template, String description )
		{
			this.name = name;
			this.template = template;
			this.description = description;
		}
		
		void print( PrintStream out )
		{
			out.print( "-" + name );
			out.print( "\t" );
			if( template != null )
				out.print( template );
			else
				out.print( "" );
			out.print( "\t" );
			out.print( description );
			out.println();
		}
	}
	
	ArrayList<String>				files		= new ArrayList<String>();
	Map<String, HashMap<String,String>>				arguments	= new HashMap<String, HashMap<String,String>>();
	
	static TreeMap<String, Option>	options		= new TreeMap<String, Option>();
	
	public static CommandLine parse( String[] args )
	{
		CommandLine cmd = new CommandLine();
		
		for( String arg : args )
		{
			if( arg.compareTo( "-help" ) == 0 )
			{
				cmd.setArgument( "help", "true" );
			}
			else if( arg.startsWith( "-" ) )
			{
				String[] tok = arg.split( "[=]" );
				if( tok.length == 2 )
				{
					String[] options = tok[1].split( "[;]" );
					
					for( String o : options )
					{
						String[] parts = o.split( "[:]" );
						
						if( parts.length == 2 )
						{
							cmd.setArgument( tok[0].substring( 1 ), parts[0], parts[1] );
						}
						else if( parts.length == 1 )
						{
							String name = tok[0].substring( 1 );
							cmd.setArgument( name, name, parts[0] );
						}
					}
				}
				else if( tok.length == 1 )
				{
					cmd.setArgument( tok[0].substring( 1 ), "" );
				}
			}
			else
			{
				try
				{
					File file = new File( arg );
					if( file.exists() )
					{
						cmd.files.add( file.getAbsolutePath() );
						System.out.println( "Using file " + file.getAbsolutePath() );
					} else
					{
						System.out.println( "File " + file.getAbsolutePath() + " does not exist" );
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				// cmd.files.add( arg );
			}
		}
		
		return cmd;
	}
	
	public void setArgument( String key, String value )
	{
		HashMap<String,String> map = new HashMap<String,String>();
		
		map.put( key,  value );
		
		arguments.put( key, map );
	}
	
	public void setArgument( String flag, String option, String value )
	{
		HashMap<String,String> map = arguments.get( flag );
		
		if( map == null )
		{
			map = new HashMap<String,String>();
			arguments.put( flag, map );
		}
		
		map.put( option,  value );
	}
	
	public String getArgument( String key, String def )
	{
		HashMap<String,String> map = arguments.get( key );
		
		if( map == null ) return def;
		
		String ret = map.get( key );
		
		if( ret == null )
			ret = def;
		
		return ret;
	}
	
	public String getArgument( String flag, String option, String def )
	{
		HashMap<String,String> map = arguments.get( flag );
		
		if( map == null ) return def;
		
		String ret = map.get( option );
		
		if( ret == null )
			ret = def;
		
		return ret;
	}
	
	public int getInt( String flag, String option, int def )
	{
		HashMap<String,String> map = arguments.get( flag );
		
		if( map == null ) return def;
		
		String ret = map.get( option );
		
		if( ret == null )
			return def;
		
		try
		{
			return Integer.parseInt( ret );
		}
		catch( Exception ex )
		{
			return def;
		}
	}
	
	public boolean isArgument( String key, String value )
	{
		HashMap<String,String> map = arguments.get( key );
		
		if( map == null )
			if( value == null )
				return true;
			else
				return false;
		
		String opt = map.get( key );
		
		if( opt == null )
		{
			if( value == null )
				return true;
			return false;
		}
		
		return opt.compareTo( value ) == 0;
	}
	
	public boolean isSet( String key )
	{
		return arguments.get( key ) != null;
	}
	
	public static void addOption( Option option )
	{
		options.put( option.name, option );
	}
	
	public void print( PrintStream out )
	{
		out.println( "Command\tParameter\tDescription" );
		for( Option option : options.values() )
		{
			option.print( out );
		}
	}

	public ArrayList<String> getFiles()
	{
		return files;
	}

	public Iterable<String> files()
	{
		return files;
	}
}
