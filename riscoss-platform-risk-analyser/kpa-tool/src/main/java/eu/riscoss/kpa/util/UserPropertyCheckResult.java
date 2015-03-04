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
