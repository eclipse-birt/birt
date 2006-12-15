
package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

public class IncludeLibraryRuleTest extends BaseTestCase
{

	String fileName = "BlankReport.xml";
	String libfileName = "DesignIncludeLibraryTest.xml";
	private String outputFileName = "IncludeLibraryRuleTest10.xml";

	private String LibA = "LibA.xml";
	private String LibB = "LibB.xml";
	private String LibC = "LibC.xml";

	private String outLibA = "LibA.xml";


	public IncludeLibraryRuleTest( String name )
	{
		super( name );
	}

	public static Test suite( )
	{

		return new TestSuite( IncludeLibraryRuleTest.class );
	}

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		
		copyInputToFile ( INPUT_FOLDER + "/" + fileName );
		copyInputToFile ( INPUT_FOLDER + "/" + libfileName );
		copyInputToFile ( INPUT_FOLDER + "/" + LibA );
		copyInputToFile ( INPUT_FOLDER + "/" + LibB );
		copyInputToFile ( INPUT_FOLDER + "/" + LibC );
	}

	public void tearDown( )
	{
		removeResource( );
	}
	
	public void testIncludeLibraryRule1( ) throws Exception
	{

		openDesign( fileName );
		designHandle.includeLibrary( LibA, "LibA" );
		designHandle.includeLibrary( LibB, "LibB" );
		designHandle.includeLibrary( LibC, "LibC" );

	}

	public void testIncludeLibraryRule2( ) throws Exception
	{
		openDesign( fileName );

		try
		{
			designHandle.includeLibrary( LibA, "" );
			designHandle.includeLibrary( LibB, "" );
			designHandle.includeLibrary( LibC, "" );
		}
		catch ( Exception e )
		{
			assertNotNull( e );
		}
	}

	public void testIncludeLibraryRule3( ) throws Exception
	{
		openDesign( fileName );
		try
		{
			designHandle.includeLibrary( LibA, "LibA" );
			designHandle.includeLibrary( LibB, "LibA" );
		}
		catch ( Exception e )
		{
			assertNotNull( e );
		}
	}

	public void testIncludeLibraryRule4( ) throws Exception
	{
		openDesign( fileName );

		designHandle.includeLibrary( LibA, "" );
		try
		{
			designHandle.includeLibrary( LibB, "LibA" );
			fail( );
		}
		catch ( Exception e )
		{
			assertNotNull( e );
		}

	}

	public void testIncludeLibraryRule5( ) throws Exception
	{
		openDesign( fileName );
		designHandle.includeLibrary( LibA, "" );
		try
		{
			designHandle.includeLibrary( LibA, "LibB" );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertNotNull( e );
		}
	}

	public void testIncludeLibraryRule6( ) throws Exception
	{
		openLibrary( LibA );
		libraryHandle.includeLibrary( LibB, "LibB" );
		libraryHandle.includeLibrary( LibC, "LibC" );
	}

	public void testIncludeLibraryRule7( ) throws Exception
	{
		openDesign( fileName );
		testIncludeLibraryRule6( );
		designHandle.includeLibrary( LibA, "LibA" );
	}

	public void testIncludeLibraryRule8( ) throws Exception
	{
		openDesign( fileName );
		testIncludeLibraryRule6( );
		designHandle.includeLibrary( LibA, "LibA" );
		designHandle.includeLibrary( LibB, "" );
	}

	public void testIncludeLibraryRule9( ) throws Exception
	{
		openLibrary( LibA );
		libraryHandle.includeLibrary( LibB, "LibB" );
		super.saveAs( outLibA );
		libraryHandle.close( );

		openLibrary( LibB );
		libraryHandle.includeLibrary( outLibA, "LibA" );

	}

	public void testIncludeLibraryRule10( ) throws Exception
	{
		//String fileName = "DesignIncludeLibraryTest.xml";
		//copyResource_INPUT (libfileName, libfileName);
		openDesign( libfileName );
		designHandle.includeLibrary( LibA, "LibA" );
		designHandle.includeLibrary( LibB, "LibB" );

		TextItemHandle textDesignHandle = (TextItemHandle) designHandle
				.findElement( "text1" );
		assertNotNull( "Text should not be null", textDesignHandle ); //$NON-NLS-1$
		textDesignHandle.setProperty( StyleHandle.BACKGROUND_COLOR_PROP, null );
		assertEquals( "#0000FF", textDesignHandle
				.getStringProperty( "backgroundColor" ) );

		StyleHandle styleDesignHandle = (StyleHandle) designHandle
				.findStyle( "style1" );
		assertNotNull( "Style should not be null", styleDesignHandle );
		styleDesignHandle.drop( );

		super.saveAs( outputFileName );
		
		TextItemHandle text2DesignHandle = (TextItemHandle) designHandle
				.findElement( "text1" );
		assertNotNull( "Text should not be null", text2DesignHandle );
		assertEquals( null, textDesignHandle
				.getStringProperty( "backgroundColor" ) );
	}

}
