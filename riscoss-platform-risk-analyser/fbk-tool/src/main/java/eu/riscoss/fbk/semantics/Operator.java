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

package eu.riscoss.fbk.semantics;

public abstract class Operator
{
	public static final Operator	Equals	= new Operator() {
		@Override
		public boolean eval(String value, String match) {
			return value.equals( match );
		} };
	
	public static final Operator Not = new Operator() {
		@Override
		public boolean eval(String value, String match) {
			return !value.equals( match );
		}};

	public abstract boolean eval( String value, String match );
}
