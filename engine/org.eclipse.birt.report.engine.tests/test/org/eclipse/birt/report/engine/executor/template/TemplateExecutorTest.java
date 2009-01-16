package org.eclipse.birt.report.engine.executor.template;

import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.birt.core.template.TemplateParser;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.report.engine.executor.ExecutionContext;


public class TemplateExecutorTest extends TestCase
{
	
	String input = "<value-of>textData</value-of> DEF <image type=''>imageData</image>";
	
	public void testExecutor()
	{
		TextTemplate template = new TemplateParser( ).parse( input );
		ExecutionContext context = new ExecutionContext(); 
		TemplateExecutor executor = new TemplateExecutor(context);
		HashMap<String, Object> values = new HashMap<String, Object>( );
		values.put("textData", "RESULT");
		values.put("imageData", new byte[]{} );
		String output = executor.execute( template, values );
		boolean matched = output.matches( "RESULT DEF <img src=.*>");
		assertTrue(matched);
	}

	public void testFormat()
	{
		testValueOf( "<value-of format=\"0.00\">textData</value-of>" );
	}

	public void testFormatExpression( )
	{
		testValueOf( "<value-of format-expr=format>textData</value-of>" );
	}

	private void testValueOf( String input )
	{
		TextTemplate template = new TemplateParser( ).parse( input );
		ExecutionContext context = new ExecutionContext(); 
		TemplateExecutor executor = new TemplateExecutor(context);
		HashMap<String, Object> values = new HashMap<String, Object>( );
		values.put( "textData", 78.9711 );
		values.put( "format", "0.00" );
		String output = executor.execute( template, values );
		assertEquals( "78.97", output );
	}
}
