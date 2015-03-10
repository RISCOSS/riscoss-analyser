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

public class Condition
{
	private String predicate;
//	private String value;
	private Operator op;
	
	public Condition( String predicate, Operator op )
	{
		this.predicate = predicate;
//		this.value = value;
		this.op = op;
	}

	public Operator getOperator() {
		return op;
	}
	
	protected Condition() {}
	
	public String getPredicate()
	{
		return predicate;
	}
	
//	public String getValue() {
//		return value;
//	}
	
	public boolean isPositive()
	{
		return !(op == Operator.Not);
	}
}
