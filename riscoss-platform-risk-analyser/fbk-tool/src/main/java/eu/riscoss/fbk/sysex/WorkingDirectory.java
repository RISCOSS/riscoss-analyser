package eu.riscoss.fbk.sysex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

/**
 * Utility class to work with paths
 * 
 * @author albertosiena
 * 
 */
public class WorkingDirectory
{
	File			dir;
	
	HashSet<String>	files		= new HashSet<String>();
	HashSet<String>	outputFiles	= new HashSet<String>();
	
	public WorkingDirectory( Class<?> locationClass )
	{
		this( findLocation( locationClass ) );
	}
	
	public WorkingDirectory( File file )
	{
		dir = new File( file.getAbsolutePath() );
	}
	
	public static File findLocation( Class<?> cls )
	{
		String t = cls.getPackage().getName() + ".";
		String clsname = cls.getName().substring( t.length() );
		String s = cls.getResource( clsname + ".class" ).toString();
		
		s = s.substring( s.indexOf( "file:" ) + 5 );
		int p = s.indexOf( "!" );
		if( p != -1 )
		{
			s = s.substring( 0, p );
		}
		return new File( new File( s ).getParent() );
	}
	
	public FileWriter createWriter( String filename ) throws IOException
	{
		return new FileWriter( this.dir.getPath() + File.pathSeparator + filename );
	}
	
	public void write( String filetitle, String content )
	{
		try
		{
			FileWriter writer = new FileWriter( new File( dir, filetitle ), true );
			
			writer.write( content );
			
			writer.close();
			
			files.add( filetitle );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public OutputStream getOutputStream( String filetitle ) throws FileNotFoundException
	{
		return new FileOutputStream( new File( dir, filetitle ) );
	}
	
	public File getFile( String filetitle )
	{
		File file = new File( filetitle );
		
		if( file.isAbsolute() ) return file;
		
		return new File( dir, filetitle );
	}
	
	public void mkdir( String dirname )
	{
		File dir = getFile( dirname );
		
		if( dir.exists() ) return;
		
		dir.mkdirs();
	}
	
	public Executable exe( String command, String args )
	{
		return exe( command, args, "output.txt" );
	}
	
	public Executable exe( String command, String args, String outputFile )
	{
		outputFiles.clear();
		
		File cmd = new File( dir, command );
		File output = getFile( outputFile );
		
		Executable exe = new Executable( new File( cmd.getAbsoluteFile() + " " + args ) );
		
		exe.setLocking( true );
		exe.redirectOutput( output );
		
		outputFiles.add( output.getName() );
		
		return exe;
	}
	
	public void rm( String filetitle )
	{
		try
		{
			new File( dir, filetitle ).delete();
		}
		catch (Exception ex)
		{
		}
	}
	
	public Iterable<String> inputFiles()
	{
		return this.files;
	}
	
	public Iterable<String> outputFiles()
	{
		return this.outputFiles;
	}
	
	public Executable getExecutable( String filetitle )
	{
		return new Executable( getFile( filetitle ) );
	}

	public File touch( String filename )
	{
		File file = new File( filename );
		
		if( !file.isAbsolute() )
			file = new File( dir, filename );
		
		File parent = file.getParentFile();
		
		parent.mkdirs();
		
		if( !file.exists() )
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return file;
	}
}
