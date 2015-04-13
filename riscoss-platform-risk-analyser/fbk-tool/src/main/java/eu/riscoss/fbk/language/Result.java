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

import java.util.ArrayList;

public interface Result
{
	public static final Result NO_SOLUTIONS = new Result() {
		@Override
		public String getDescription() {
			return "No solutions";
		}

		@Override
		public Iterable<Solution> solutions() {
			return new ArrayList<Solution>();
		}};
	
	
	public String getDescription();
	
	public Iterable<Solution> solutions();
}
