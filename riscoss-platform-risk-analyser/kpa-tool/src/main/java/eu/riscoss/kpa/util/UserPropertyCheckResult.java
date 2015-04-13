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

package eu.riscoss.kpa.util;

import java.util.List;

public class UserPropertyCheckResult {
	
	public NetworkElementType type;
	public String id;
	
	public UserPropertyCheckResult(NetworkElementType elType, String elId) {
		
		this.type = elType;
		this.id = elId;
	}
	
	@Override
	public String toString() {
		
		return "[" + this.type + " = " + this.id + "]";
	}
	
	public static String checkResultsToString(List<UserPropertyCheckResult> results) {
		
		String res = "";
		int size = results.size();
		if(size == 1) {
			res = results.get(0).toString();
		}
		int ind = 0;
		for (UserPropertyCheckResult userPropertyCheckResult : results) {
			res += userPropertyCheckResult.toString();
			if(ind < size - 1) {
				res += ", ";
			}
			ind++;
		}
		return res;
	}
}
