/*
   (C) Copyright 2013-2016 The RISCOSS Project Consortium
   
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

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
