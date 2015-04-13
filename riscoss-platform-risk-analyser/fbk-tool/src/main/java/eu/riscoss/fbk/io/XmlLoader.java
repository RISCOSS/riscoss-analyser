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

import eu.riscoss.fbk.language.Model;
import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.util.XmlNode;

public class XmlLoader
{
	public void load( XmlNode xml, Program program )
	{
		if( xml.getTag().equalsIgnoreCase( "riscoss" ) == false )
			throw new RuntimeException( "Wrong input XML file" );
		
		for( XmlNode x: xml.getChildren( "script" ) ) {
			program.getOptions().setValue( "code", program.getOptions().getValue( "code", "" ) + x.getValue() );
		}
		
		for( XmlNode xmlmodel : xml.getChildren( "model" ) ) {
			loadERModel( xmlmodel, program.getModel() );
		}
		
		for( XmlNode x : xml.getChildren( "scenario" ) ) {
			loadValues( x, program );
		}
	}

	private void loadERModel( XmlNode ermodel, Model model )
	{
		for( XmlNode entities : ermodel.getChildren( "entities" ) ) {
			for( XmlNode entity : entities ) {
				Proposition p = model.getProposition( entity.getAttr( "id" ) );
				
				if( p == null ) {
					
					p = new Proposition( entity.getTag(), entity.getAttr( "id" ) );
					
					for( String key : entity.listAttributes() ) {
						
						if( key.compareTo( "id" ) == 0 ) continue;
						
						if( key.compareTo( "istarmlId" ) == 0 ) continue;
						
						p.setProperty( key, entity.getAttr( key ) );
					}
					
					for( XmlNode prop : entity.getChildren( "property" ) ) {
						
						String key = prop.getAttr( "name", null );
						
						if( key == null ) continue;
						
						if( key.equalsIgnoreCase( "id" ) ) continue;
						if( key.equalsIgnoreCase( "istarmlId" ) ) continue;
						
						p.setProperty( key, prop.getAttr( "value", "" ) );
					}
					
					model.addProposition( p );
				}
			}
		}
		
		for( XmlNode relations : ermodel.getChildren( "relationships" ) )
		{
			for( XmlNode x : relations )
			{
				Relation r = new Relation( x.getTag() );
				
				String[] sources = x.getAttr( "source" ).split( "[,]" );
				
				for( String source : sources )
				{
					Proposition p = model.getProposition( source.trim() );
					
					if( p == null )
					{
						System.err.println( "Source " + source + " not found" );
						continue;
					}
					
					r.addSource( p );
				}
				
				if( r.getSourceCount() < 1 ) continue;
				
				String target = x.getAttr( "target" );
				
				Proposition p = model.getProposition( target );
				
				if( p == null )
				{
					System.err.println( "Target " + target + " not found" );
					continue;
				}
				
				r.setTarget( p );
				
				for( String key : x.listAttributes() )
				{
					if( key.compareTo( "source" ) == 0 ) continue;
					if( key.compareTo( "target" ) == 0 ) continue;
					if( key.compareTo( "operator" ) == 0 ) continue;
					
					r.setProperty( key, x.getAttr( key ) );
				}
				
				r.setOperator( 
						x.getAttr( "operator", "and" ).toLowerCase().compareTo( "or" ) == 0 ? Relation.OR : Relation.AND );
				
				model.addRelation( r );
			}
		}
	}
	
	private void loadValues( XmlNode xml, Program program )
	{
		for( XmlNode node : xml.getChildren( "label" ) )
		{
			program.getScenario().addConstraint( 
					node.getAttr( "id"), node.getAttr( "name" ), node.getAttr( "value", null ) );
		}
	}
}
