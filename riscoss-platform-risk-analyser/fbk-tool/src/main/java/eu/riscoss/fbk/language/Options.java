package eu.riscoss.fbk.language;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Options implements Iterable<String>
{
	Map<String,String> values = new HashMap<String,String>();
	
	public void setValue( String key, String value ) {
		values.put( key, value );
	}

	public boolean isSet( String key, String valueToCheck ) {
		String val = values.get( key );
		if( val == null ) return false;
		return val.compareTo( valueToCheck ) == 0;
	}

	public String getValue( String key ) {
		return values.get( key );
	}

	public String getValue( String key, String def ) {
		String ret = values.get( key );
		if( ret == null ) ret = def;
		return ret;
	}

	@Override
	public Iterator<String> iterator() {
		return values.keySet().iterator();
	}
	
}
