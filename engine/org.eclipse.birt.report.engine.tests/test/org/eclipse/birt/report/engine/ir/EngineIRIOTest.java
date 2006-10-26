
package org.eclipse.birt.report.engine.ir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.parser.ReportDesignWriter;
import org.eclipse.birt.report.engine.parser.ReportParser;

public class EngineIRIOTest extends EngineCase
{

	// static final String DESIGN_STREAM =
	// "org/eclipse/birt/report/engine/ir/ir_io_test.rptdesign";
	static final String DESIGN_STREAM = "ir_io_test.rptdesign";

	public void testIO( ) throws Exception
	{
		// load the report design
		ReportParser parser = new ReportParser( );
		Report report = parser.parse( ".", this.getClass( )
				.getResourceAsStream( DESIGN_STREAM ) );
		assertTrue( report != null );

		// write it into the stream
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		new EngineIRWriter( ).write( out, report );
		out.close( );

		// load it from the stream
		InputStream in = new ByteArrayInputStream( out.toByteArray( ) );
		EngineIRReader reader = new EngineIRReader( );
		Report report2 = reader.read( in );
		reader.link( report2, report.getReportDesign( ) );
		// check if the report 2 equals the report 1
		ByteArrayOutputStream out1 = new ByteArrayOutputStream( );
		ByteArrayOutputStream out2 = new ByteArrayOutputStream( );

		ReportDesignWriter writer = new ReportDesignWriter( );

		writer.write( out1, report );
		writer.write( out2, report2 );

		String golden = new String( out1.toByteArray( ) );
		String value = new String( out2.toByteArray( ) );

		assertEquals( golden, value );

		System.out.print( golden );
	}

}
