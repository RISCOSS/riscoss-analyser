package eu.riscoss.fbk.sysex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;


public class StreamGobbler extends Thread
{
	InputStream			is;
//	String				type;
	OutputStream		os;
	
	ArrayList<String>	buffer	= new ArrayList<String>();
	
	StringBuilder b;
	
	StreamGobbler( InputStream is, String type )
	{
		this( is, type, null );
	}
	
	StreamGobbler( InputStream is, String type, OutputStream redirect )
	{
		this.is = is;
//		this.type = type;
		this.os = redirect;
	}
	
	public void run()
	{
		InputStreamReader isr = null;
		BufferedReader br = null;
		try
		{
			b = new StringBuilder();
			
			PrintWriter pw = null;
			if( os != null )
				pw = new PrintWriter( os );
			
			isr = new InputStreamReader( is );
			br = new BufferedReader( isr );
			String line = null;
			while( (line = br.readLine()) != null )
			{
				if( pw != null )
				{
					pw.println( line );
				}
				else
				{
					buffer.add( line );
				}
			}
			if( pw != null )
				pw.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			if( isr != null ) try {
				isr.close();
				} catch (IOException e) {}
			
			if( br != null ) try{
				br.close();
			}
			catch( IOException ex ){}
			
//			notifyAll();
		}
	}
}