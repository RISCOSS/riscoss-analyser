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

import eu.riscoss.fbk.language.Model;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.util.XmlNode;

public class IStarMLImporter {
	
	Model model;
	
	public void importIstarML( XmlNode xml, Program program ) {
		
		if( !"istarml".equals( xml.getTag() ) ) return;
		if( xml.getChildCount( "diagram" ) < 1 ) return;
		
		model = program.getModel();
		
		importDiagram( xml.item( "diagram", 0 ) );
	}
	
	void importDiagram( XmlNode x ) {
		for( XmlNode child : x.getChildren() ) {
			extractProposition( child );
		}
		for( XmlNode child : x.getChildren() ) {
			extractRelation( child );
		}
	}
	
	void extractProposition( XmlNode x ) {
		
		if( "actor".equals( x.getTag() ) ) {
			
		}
		if( "ielement".equals( x.getTag() ) ) {
			
			try {
				Proposition p = null;
				
				if( x.getAttr( "iref", null ) != null ) {
					p = model.getProposition( x.getAttr("iref" ) );
				}
				else {
					String type = x.getAttr( "type" );
					if( type == null )
						type = "proposition";
					p = new Proposition( type, x.getAttr( "id", x.getAttr( "name" ) ) );
					model.addProposition( p );
//					System.out.println( "Adding " + p.getId() );
				}
				
				if( p != null ) {
					for( String key : x.listAttributes() ) {
						String value = x.getAttr( key );
						if( "id".equals( value ) ) continue;
						p.setProperty( key, value );
					}
				}
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
		}
		
		for( XmlNode child : x.getChildren() ) {
			extractProposition( child );
		}
	}
	
	void extractRelation( XmlNode xml ) {
		
		for( XmlNode x : xml.getChildren() ) {
			
			try {
			if( "ielementLink".equals( x.getTag() ) ) {
				String type = x.getAttr( "type", null );
				if( type == null )
					type = "";
				Relation r = new Relation( type );
				r.setTarget( model.getProposition( xml.getAttr( "id", xml.getAttr( "iref", xml.getAttr( "name" ) ) ) ) );
				if( x.getAttr( "operator", null ) != null ) {
					r.setOperator( x.getAttr( "operator", "and" ) );
				}
				
				for( XmlNode child : x.getChildren( "ielement" ) ) {
					r.addSource( model.getProposition( child.getAttr( "id", child.getAttr( "iref", child.getAttr( "name" ) ) ) ) );
				}
				if( r.getSourceCount() < 1 ) {
					Proposition p = model.getProposition( xml.getAttr( "iref", "" ) );
					if( p != null ) {
						r.addSource( p );
					}
				}
				
				if( r.getTarget() == null ) {
					continue;
				}
				if( r.getSourceCount() < 1 ) {
					continue;
				}
				
				model.addRelation( r );
			}
//			else if( "boundary".equals( x.getTag() ) ) {
//				Relation r = new Relation( "belongs-to" );
//				System.out.println( xml.getAttr( "id" ) );
//				r.setTarget( model.getProposition( xml.getAttr( "id" ) ) );
//				
//				for( XmlNode child : x.getChildren( "ielement" ) ) {
//					r.addSource( model.getProposition( child.getAttr( "id", child.getAttr( "iref" ) ) ) );
//				}
//				
//				model.addRelation( r );
//			}
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
			
			extractRelation( x );
		}
	}
	
	public static void main( String[] args ) {
		File f = new File( "/Users/albertosiena/Moodbile_M20_v3.istarml" );
		XmlNode xml = XmlNode.load( f );
		
		Program program = new Program();
		
		new IStarMLImporter().importIstarML( xml, program );
		
		System.out.println( "NODES" );
		
		for( String t : program.getModel().propositionTypes() ) {
			System.out.println( "> " + t );
			for( Proposition p : program.getModel().propositions( t ) ) {
				System.out.println( p );
			}
		}
		
		System.out.println( "RELATIONS" );
		
		for( String t : program.getModel().relationTypes() ) {
			System.out.println( t );
			for( Relation r : program.getModel().relations( t ) ) {
				System.out.println( r );
			}
		}
	}
}
