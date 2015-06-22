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

package eu.riscoss.fbk.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import eu.riscoss.fbk.language.Model;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.util.XmlNode;

public class RiscossLoader {
	
	private static RiscossLoader instance = new RiscossLoader();
	
	public static RiscossLoader get() {
		return instance;
	}
	
	private String file2String( File file ) {
		try {
			return new String(Files.readAllBytes( Paths.get( file.toURI() ) ) );
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void load( File file, Program program ) {
		load( file2String( file ), program );
	}
	
	public void load( String innerXml, Program program ) {
		if( innerXml != null )
			if( innerXml.length() > 50 )
				if( innerXml.substring( 0, 50 ).indexOf( "starml" ) != -1 ) {
					merge( program.getModel(), iStarML2Model(innerXml ) );
					return;
				}
		new XmlLoader().load(XmlNode.loadString(innerXml), program);
		
		if( program.getModel().getProposition( "always" ) == null ) {
			Proposition always = new Proposition( "always" );
			program.getModel().addProposition( always );
			program.getScenario().setConstraint( "always", "st", "1.0" );
		}
//		else
		{
			Proposition always = program.getModel().getProposition( "always" );
			for( Proposition p : program.getModel().propositions( "event" ) ) {
				boolean existing = false;
				for( Relation r : p.in() ) {
					if( "increase".equals( r.getStereotype() ) ) {
						existing = true;
					}
				}
				if( existing == false ) {
					Relation r = new Relation( "increase" );
					r.addSource( always );
					r.setTarget( p );
					program.getModel().addRelation( r );
				}
				existing = false;
				
//				r = new Relation( "reduce" );
//				r.addSource( always );
//				r.setTarget( p );
//				program.getModel().addRelation( r );
			}
		}
	}
	
	private Model iStarML2Model(String istarml) {
//		String innerxml = "";
//		
//		try {
//			innerxml = new XmlTransformer().IStarML2InnerXml(istarml);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		
//		XmlNode xml = XmlNode.loadString(innerxml);
//		
		Program program = new Program();
//		
//		new XmlLoader().load(xml, program);
		
		new IStarMLImporter().importIstarML( XmlNode.loadString( istarml ), program );
		
		return program.getModel();
	}
	
	@SuppressWarnings("unused")
	private Model innerXml2Model(String innerXml) {
		Program program = new Program();
		new XmlLoader().load(XmlNode.loadString(innerXml), program);
		return program.getModel();
	}
	
	private void merge(Model sourceModel, Model additionModel) {
		for (Proposition p : additionModel.propositions()) {
			try {
				Proposition p2 = p.clone();
				
				if (sourceModel.getProposition(p2.getId()) != null) {
					continue;
				}
				
				sourceModel.addProposition(p2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		for (Relation r : sourceModel.relations()) {
			try {
				Relation newr = r.clone();
				
				newr.setTarget(sourceModel.getProposition(r.getTarget().getId()));
				
				for (Proposition s : r.getSources()) {
					newr.addSource(sourceModel.getProposition(s.getId()));
				}
				
				sourceModel.addRelation(newr);
			} catch (CloneNotSupportedException e) {
				System.err.println("Skipping relation " + r);
				//				e.printStackTrace();
			}
		}
		
		for (Relation r : additionModel.relations()) {
			try {
				Relation newr = r.clone();
				
				newr.setTarget(sourceModel.getProposition(r.getTarget().getId()));
				
				for (Proposition s : r.getSources()) {
					newr.addSource(sourceModel.getProposition(s.getId()));
				}
				
				sourceModel.addRelation(newr);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
