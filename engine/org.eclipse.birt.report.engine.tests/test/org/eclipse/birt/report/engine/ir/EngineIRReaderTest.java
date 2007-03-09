package org.eclipse.birt.report.engine.ir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.parser.ReportDesignWriter;
import org.eclipse.birt.report.engine.parser.ReportParser;

/**
 * Used to test the different version reader. 
 * Current EngineIRWriter and EngineIRReader test can see class EngineIRIOTest.
 * If we change the EngineIRWriter and EngineIRReader, we should do as following: 
 * 1. use current EngineIRReader to read streams from the stored old version files. 
 * 2. compare the current wrote and readed stream with result in step1.
 * 
 * Do not forget to use current EngineIRWriter to write the design to a stream, 
 * and store it to a file like ir_io_test.V1. This will be used for later versions. 
 * 
 */
public class EngineIRReaderTest extends EngineCase
{

	static final String DESIGN_STREAM = "ir_io_test.rptdesign";
	static final String GOLDEN_V1_STREAM = "ir_io_test_V1.golden";
	static final String GOLDEN_V2_STREAM = "ir_io_test_V2.golden";

	String value = "";

	public EngineIRReaderTest( )
	{
		try
		{
			value = getCurrentStream( );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}

	public void testV1( ) throws Exception
	{
		String golden = getGolden( GOLDEN_V1_STREAM );
		assertEquals( golden, value );
	}

	public void testV2( ) throws Exception
	{
		String golden = getGolden( GOLDEN_V2_STREAM );
		assertEquals( golden, value );
	}

	public String getCurrentStream( ) throws Exception
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
		EngineIRReader reader = new EngineIRReader( false );
		Report report2 = reader.read( in );

		ByteArrayOutputStream out2 = new ByteArrayOutputStream( );
		ReportDesignWriter writer = new ReportDesignWriter( );
		writer.write( out2, report2 );

		String value = new String( out2.toByteArray( ) );

		return value;
	}

	private String getGolden( String fileName ) throws Exception
	{
		InputStream in = this.getClass( ).getResourceAsStream( fileName );
		assertTrue( in != null );
		EngineIRReader reader = new EngineIRReader( false );
		Report report = reader.read( in );
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		ReportDesignWriter writer = new ReportDesignWriter( );
		writer.write( outputStream, report );
		return new String( outputStream.toByteArray( ) );
	}

	/**
	 * Used to create the current stream wrote by current version EngineWriter.
	 * The file will be stored in eclipse folder. Please copy it here and commit it to CVS.
	 * 
	 * now the version is 2
	 * 
	 * @throws Exception
	 */
	public void writeGolden( ) throws Exception
	{
		//load the report design
		ReportParser parser = new ReportParser( );
		Report report = parser.parse( ".", this.getClass( )
				.getResourceAsStream( DESIGN_STREAM ) );
		assertTrue( report != null );

		// write it into the stream
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		new EngineIRWriter( ).write( out, report );
		out.close( );

		File file = new File( GOLDEN_V2_STREAM );
		if ( !file.exists( ) )
		{
			file.createNewFile( );
			RandomAccessFile rf = new RandomAccessFile( file, "rw" );
			rf.write( out.toByteArray( ) );
		}
	}
}
