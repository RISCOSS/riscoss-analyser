package eu.riscoss.fbk.dlv;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import eu.riscoss.fbk.language.Program;

public class TemplateCompiler implements DlvCompiler {

	private File template;
	
	VelocityEngine ve = new VelocityEngine();
	
	Map<String,Object> context_variables = new HashMap<String,Object>();
	
	public TemplateCompiler() {
		Properties p = new Properties();
		p.setProperty( 
				RuntimeConstants.RESOURCE_LOADER, "file" );
		p.setProperty( 
				RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "/" );
		p.setProperty( 
				"file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader" );
		p.setProperty( 
				"file.resource.loader.path", "/" );
		ve.init( p );
	}
	
	public void setContextVar( String key, Object obj ) {
		context_variables.put( key, obj );
	}
	
	@Override
	public String compile( Program program ) {
		
		VelocityContext context = new VelocityContext();
		
		context.put( "program", program );
		
		for( String key : context_variables.keySet() ) {
			context.put( key,  context_variables.get( key ) );
		}
		
//		context.put( "grades", new String[] { "low", "medium", "high", "extreme" } );
//		context.put( "grades", new String[] { "none", "low", "medium", "high", "extreme" } );
//		context.put( "grades", new String[] { "true" } );
		
		try {
			Template t = ve.getTemplate( template.getAbsolutePath() );
			
			StringWriter writer = new StringWriter();
			t.merge( context, writer );
			
			System.out.println( writer.toString() );
			
			return writer.toString();
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
		
		return "";
	}
	
	public void setTemplateFile(File file) {
		this.template = file;
	}
	
}
