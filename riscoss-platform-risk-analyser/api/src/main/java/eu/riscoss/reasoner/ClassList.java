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

package eu.riscoss.reasoner;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class ClassList implements Iterable<String> {
	
	String packageName;

	public ClassList( Package p ) {
		this( p.getName() );
	}
	public ClassList( String packageName ) {
		this.packageName = packageName;
	}
	
	public static ArrayList<String>getClassNamesFromPackage(String qualified_package) throws Exception{
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    URL packageURL;
	    ArrayList<String> names = new ArrayList<String>();;

	    String packageName = qualified_package.replace(".", "/");
	    packageURL = classLoader.getResource(packageName);

	    if(packageURL.getProtocol().equals("jar")){
	        String jarFileName;
	        JarFile jf ;
	        Enumeration<JarEntry> jarEntries;
	        String entryName;

	        // build jar file name, then loop through zipped entries
	        jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
	        jarFileName = jarFileName.substring(5,jarFileName.indexOf("!"));
	        System.out.println(">"+jarFileName);
	        jf = new JarFile(jarFileName);
	        jarEntries = jf.entries();
	        while(jarEntries.hasMoreElements()){
	            entryName = jarEntries.nextElement().getName();
	            if(entryName.startsWith(packageName) && entryName.length()>packageName.length()+5){
//	                entryName = entryName.substring(packageName.length(),entryName.lastIndexOf('.'));
	                names.add(entryName);
	            }
	        }
	        jf.close();

	    // loop through files in classpath
	    }else{
	    URI uri = new URI(packageURL.toString());
	    File folder = new File(uri.getPath());
	        // won't work with path which contains blank (%20)
	        // File folder = new File(packageURL.getFile()); 
	        File[] contenuti = folder.listFiles();
	        String entryName;
	        for(File actual: contenuti){
	            entryName = actual.getName();
	            entryName = entryName.substring(0, entryName.lastIndexOf('.'));
	            if( entryName.contains( "$" ) )
	            	continue;
	            names.add(qualified_package + "." + entryName);
	        }
	    }
	    return names;
	}

	@Override
	public Iterator<String> iterator() {
		try {
			return getClassNamesFromPackage( packageName ).iterator();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>().iterator();
		}
	}
	
}
