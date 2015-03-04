package eu.riscoss.fbk.util;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import eu.riscoss.fbk.language.Solution;

public class XmlReport
{
	public interface Filter
	{
		boolean contains( String value );
	}
	
	XmlNode xml = new XmlNode( "riscoss-results" );
	Filter filter;
//	Set<String> filters = new HashSet<String>();
	
	public XmlReport()
	{
		filter = new Filter() {

			@Override
			public boolean contains(String value) {
				final Set<String> filters = new HashSet<String>();
				filters.add( "st" );
				filters.add( "sf" );
				filters.add( "su" );
				filters.add( "possible" );
				filters.add( "critical" );
				filters.add( "threat" );
				return filters.contains( value );
			}};
	}
	
	public void setFilters( Filter f )
	{
		this.filter = f;
	}
	
	public void addSolution( Solution solution )
	{
		XmlNode xsol = xml.add( "solution" );
		
		for( String var : solution.variables() )
		{
			XmlNode node = xsol.add( "variable" );
			node.setAttr( "name", var );
			for( String field : solution.fields( var ) )
			{
				for( String val : solution.values( var, field ) )
				{
					if( filter.contains( val ) )
					{
						XmlNode child = node.add( "property" );
						child.setAttr( "label", field );
						child.setAttr( "value", val );
					}
				}
			}
		}
	}
	
	public void print( OutputStream out )
	{
		xml.write( out );
	}
}
