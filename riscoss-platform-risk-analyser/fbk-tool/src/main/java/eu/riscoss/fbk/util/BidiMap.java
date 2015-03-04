package eu.riscoss.fbk.util;

import java.util.HashMap;
import java.util.Map;

public class BidiMap
{
	Map<String,String> keyvalues = new HashMap<String,String>();
	Map<String,String> valuekeys = new HashMap<String,String>();
	
	public void put( String key, String value )
	{
		keyvalues.put( key,  value );
		valuekeys.put( value, key );
	}
	
	public Iterable<String> keys()
	{
		return keyvalues.keySet();
	}
	
	public Iterable<String> values()
	{
		return keyvalues.values();
	}
	
	public String getValue( String key )
	{
		return keyvalues.get( key );
	}
	
	public String getKey( String value )
	{
		return valuekeys.get( value );
	}

	public void clear()
	{
		keyvalues.clear();
		valuekeys.clear();
	}
}
