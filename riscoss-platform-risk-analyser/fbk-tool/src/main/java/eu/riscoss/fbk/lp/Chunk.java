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

package eu.riscoss.fbk.lp;

import java.util.HashMap;

import eu.riscoss.fbk.language.Proposition;

public class Chunk {
	
	Proposition				proposition;
	HashMap<String,Node>	predicates = new HashMap<String,Node>();
	
	public Iterable<String> predicates() {
		return predicates.keySet();
	}
	
	public Node getPredicate( String id ) {
		return predicates.get( id );
	}
	
	public Proposition getProposition() {
		return proposition;
	}
}