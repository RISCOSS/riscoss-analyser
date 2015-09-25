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


public class Rule
{
	public enum TYPE {
		SAT_SAT, SAT_DEN, DEN_SAT, DEN_DEN
	}
	
//	public static final int All = 0;
//	public static final int Exists = 1;
	
	public String targetPred;
	
	
	TYPE type = TYPE.SAT_SAT;
	
	public Condition condition = new Condition( "", Operator.Equals );


	private String clusterName;
	
	
	public Rule( String target, Condition condition )
	{
		this.targetPred = target;
		this.condition = condition;
	}
	
	public Rule( String target, TYPE type, Condition condition )
	{
		this( target, condition );
		this.type = type;
	}
	
	public Condition getCondition() {
		return condition;
	}
	
	public String getSourcePred() {
		return condition.getPredicate();
	}

	public TYPE getPropagationType() {
		return this.type;
	}


	public void setCluster(String clusterName) {
		this.clusterName = clusterName;
	}
	
	public String getCluster() {
		return this.clusterName;
	}
}