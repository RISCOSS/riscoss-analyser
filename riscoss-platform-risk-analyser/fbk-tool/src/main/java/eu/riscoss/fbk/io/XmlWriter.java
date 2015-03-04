package eu.riscoss.fbk.io;

import java.io.File;
import java.io.OutputStream;

import eu.riscoss.fbk.language.Program;
import eu.riscoss.fbk.language.Proposition;
import eu.riscoss.fbk.language.Relation;
import eu.riscoss.fbk.util.XmlNode;

public class XmlWriter {
	
	public void write( Program program, File file ) {
		generateXml( program ).write( file );
	}
	
	public void write( Program program, OutputStream out ) {
		generateXml( program ).write( out );
	}
	
	public XmlNode generateXml( Program program ) {
		XmlNode xml = new XmlNode( "riscoss" );
		
		if( !"".equals( program.getOptions().getValue( "code", "" ) ) ) {
			XmlNode xcode = xml.add( "script" );
			xcode.setValue( program.getOptions().getValue( "code", "" ) );
			xcode.setAttr( "$cdata", "true" );
		}
		
		XmlNode xmodel = xml.add( "model" );
		
		XmlNode xentities = xmodel.add( "entities" );
		
		for( String type : program.getModel().propositionTypes() ) {
			for( Proposition p : program.getModel().propositions( type ) ) {
				if( "always".equals( p.getId() ) ) 
					continue;
				
				XmlNode x = xentities.add( type );
				
				x.setAttr( "id", p.getId() );
				
				for( String key : p.properties() ) {
					XmlNode xattr = x.add( "property" );
					xattr.setAttr( "name", key );
					xattr.setAttr( "value", p.getProperty( key, "" ) );
				}
			}
		}
		
		XmlNode xrels = xmodel.add( "relationships" );
		
		for( String type : program.getModel().relationTypes() ) {
			for( Relation r : program.getModel().relations( type ) ) {
				if( r.getSourceCount() < 1 ) continue;
				if( "always".equals( r.getSources().get( 0 ).getId() ) ) continue;
				
				XmlNode x = xrels.add( type );
				
				x.setAttr( "target", r.getTarget().getId() );
				
				String sources = "";
				String sep = "";
				
				for( Proposition p : r.getSources() ) {
					sources += sep + p.getId();
					sep = ",";
				}
				
				x.setAttr( "source", sources );
				
				for( String key : r.properties() ) {
					x.setAttr( key, r.getProperty( key, "" ) );
				}
			}
		}
		
		return xml;
	}
}
