package eu.riscoss.fbk.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class CfgFile
{
	static class CfgOption
	{
		String name;
		
		ArrayList<String> flags = new ArrayList<String>();
		
		CfgOption() {}
		
		public String getFlag( int n )
		{
			return flags.get( n );
		}

		public String getName()
		{
			return name;
		}

//		public void addFlag( String flag, String value )
//		{
//			flags.add( value );
//		}
		
		public void addValue( String value )
		{
			flags.add( value );
		}

		public int size()
		{
			return this.flags.size();
		}
	}
	
	static class CfgSection
	{
		HashMap<String,CfgOption> flags = new HashMap<String,CfgOption>();

		public void addFlag( String name, CfgOption opt )
		{
			flags.put( name, opt );
		}

		public CfgOption getFlag( String optionName )
		{
			return flags.get( optionName );
		}

		public Iterable<String> flags()
		{
			return flags.keySet();
		}

		public Iterable<String> values( String flag )
		{
			CfgOption opt = flags.get( flag );
			
			if( opt == null ) return new ArrayList<String>();
			
			return opt.flags;
		}
	}
	
	HashMap<String,CfgSection> options = 
			new HashMap<String,CfgSection>();
	
	public static CfgFile fromFile( File file )
	{
		CfgFile cfg = new CfgFile();
		
		String currentSection = "";
		
		RandomAccessFile input = null;
		try
		{
			input = new RandomAccessFile( file, "r" );
			
			String line = input.readLine();
			
			while( line != null )
			{
				{
					int start = line.indexOf( "#" );
					if( start != -1 )
						line = line.substring( 0, start );
				}
				
				line = line.trim();
				
				if( line.startsWith( "[" ) )
				{
					if( line.endsWith( "]" ) )
					{
						currentSection = line;
						
						cfg.addSection( currentSection );
					}
				}
				else
				{
					CfgOption opt = parseOption( line );
					
					if( opt != null )
					{
						cfg.addOption( currentSection, opt );
					}
				}
				
				line = input.readLine();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if( input != null )
			{
				try{
					input.close();
				}
				catch( IOException e ) {}
				
			}
		}
		
		return cfg;
	}
	
	private void addSection( String sectionName )
	{
		sectionName = sectionName.trim();
		
		CfgSection section = options.get( sectionName );
		
		if( section == null )
		{
			section = new CfgSection();
			options.put( sectionName, section );
		}
	}

	private void addOption( String sectionName, CfgOption opt )
	{
		opt.name = opt.name.trim();
		
		CfgSection section = options.get( sectionName );
		
		if( section == null )
		{
			section = new CfgSection();
			options.put( sectionName, section );
		}
		
		section.addFlag( opt.getName(), opt );
	}

	public static CfgOption parseOption( String flag )
	{
		CfgOption cmd = new CfgOption();
		
		String[] parts = flag.split( "[=]" );
		
		if( parts.length == 1 )
		{
			return null;
		}
		else if( parts.length == 2 )
		{
			cmd.name = parts[0].trim();
			
			String[] opt = parts[1].split( "[;]" );
			
			for( String o : opt )
			{
//				String[] kv = o.split( "[:]" );
				
//				if( kv.length == 2 )
//				{
//					cmd.setFlag( parts[0], kv[0], kv[1] );
//				}
//				else if( kv.length == 1 )
				{
//					kv = o.split( "[,]" );
//					
//					for( String v : kv )
//						cmd.setFlag( parts[0], o );
					
//					if( kv.length > 1 )
//					{
//						for( String k : kv )
//							cmd.setFlag( parts[0], o );
//					}
//					else
//					{
					o = o.trim();
					
						cmd.addValue( o );
//					}
				}
			}
		}
		
		return cmd;
	}

	public String get( String sectionName, String optionName, String flagName, String def )
	{
		CfgSection section = options.get( sectionName );
		
		if( section == null )
		{
			return def;
		}
		
		CfgOption opt = section.getFlag( optionName );
		
		if( opt == null )
		{
			return def;
		}
		
		String ret = opt.getFlag( 0 );
		
		if( ret == null )
			ret = def;
		
		return ret;
	}
	
	public String get( String sectionName, String optionName, String def )
	{
		return get( sectionName, optionName, "", def );
	}

//	public Iterable<String> section( String secName )
//	{
//		CfgSection s = options.get( secName );
//		
//		if( s == null ) return new ArrayList<String>();
//		
//		return s.keySet();
//	}

	public Iterable<String> sections()
	{
		return options.keySet();
	}

	public Iterable<String> flags( String sectionName )
	{
		CfgSection s = options.get( sectionName );
		
		if( s == null ) return new ArrayList<String>();
		
		return s.flags();
	}

	public Iterable<String> values( String sectionName, String flag )
	{
		CfgSection s = options.get( sectionName );
		
		if( s == null ) return new ArrayList<String>();
		
		return s.values( flag );
	}

	public int count( String sectionName, String flag )
	{
		CfgSection s = options.get( sectionName );
		
		if( s == null ) return 0;
		
		CfgOption opt = s.getFlag( flag );
		
		return opt.size();
	}

	public String[] getValuesArray( String sect, String key )
	{
		CfgSection s = options.get( sect );
		
		if( s == null ) return new String[]{};
		
		CfgOption opt = s.getFlag( key );
		
		String[] ret = new String[]{};
		
		return opt.flags.toArray( ret );
	}

	public void setFlag( String sect, String key, String value )
	{
		CfgSection s = options.get( sect );
		
		if( s == null )
		{
			addSection( sect );
			s = options.get( sect );
		}
		
		CfgOption opt = s.getFlag( key );
		
		if( opt == null )
		{
			opt = new CfgOption();
			opt.name = key;
			opt.addValue( value );
			addOption( sect, opt );
		}
		else
		{
			opt.flags.clear();
			opt.flags.add( value );
		}
	}

	public void print( PrintStream out )
	{
		for( String sect: sections() )
		{
			for( String key : flags( sect ) )
			{
				out.println( sect + "." + key + " = " + get( sect, key, "" ) );
			}
		}
	}
}
