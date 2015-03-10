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

public class Program
{
	private final Model				model;
	private final Scenario			scenario;
	private final Query				query;
	private final Options			options;
	
	public Program() {
		this( new Model() );
	}
	
	public Program( Model m ) {
		model		= m;
		scenario	= new Scenario( model );
		query		= new Query( model );
		options		= new Options();
	}
	
	public Model getModel() {
		return model;
	}
	
	public Scenario getScenario() {
		return scenario;
	}
	
	public Query getQuery() {
		return query;
	}
	
	public Options getOptions() {
		return options;
	}
}
