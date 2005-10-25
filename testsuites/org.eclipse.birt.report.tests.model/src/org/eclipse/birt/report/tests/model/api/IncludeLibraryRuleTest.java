package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.tests.model.BaseTestCase;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import java.io.File;

public class IncludeLibraryRuleTest extends BaseTestCase
{
	String fileName = "BlankReport.xml";
//	private String inputLibraryName = "LibA.xml"; //$NON-NLS-1$
	private String outputFileName = "IncludeLibraryRuleTest10.xml";
	
//	protected static final String PLUGIN_PATH =System.getProperty("user.dir")+ "\\plugins\\"+BaseTestCase.PLUGINLOC.substring(BaseTestCase.PLUGINLOC.indexOf("/")+1) + "bin/";
	private String LibA = PLUGIN_PATH + getClassFolder() + INPUT_FOLDER + "LibA.xml";
	private String LibB = PLUGIN_PATH + getClassFolder() + INPUT_FOLDER + "LibB.xml";
	private String LibC = PLUGIN_PATH + getClassFolder() + INPUT_FOLDER + "LibC.xml";
	
	private String outLibA = PLUGIN_PATH + getClassFolder() + GOLDEN_FOLDER + "LibA.xml";
	private String outLibB = PLUGIN_PATH + getClassFolder() + GOLDEN_FOLDER + "LibB.xml";
	private String outLibC = PLUGIN_PATH + getClassFolder() + GOLDEN_FOLDER + "LibC.xml";
	
//	private String design = PLUGIN_PATH + getClassFolder() + INPUT_FOLDER + "DesignIncludeLibraryTest.xml";
	
	public IncludeLibraryRuleTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(IncludeLibraryRuleTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
	public void testIncludeLibraryRule1( ) throws Exception
	{

		openDesign( fileName );
		designHandle.includeLibrary( LibA , "LibA" );
		designHandle.includeLibrary( LibB , "LibB" );
		designHandle.includeLibrary( LibC , "LibC" );
		
	}
	
	
	public void testIncludeLibraryRule2( ) throws Exception
	{
		openDesign( fileName );
		
		try
		{
			designHandle.includeLibrary( LibA , "" );
		    designHandle.includeLibrary( LibB , "" );
		    designHandle.includeLibrary( LibC , "" );
		}
		catch(Exception e)
		{
			System.out.println( "Library2 has the same namespace" );
		}
	}
	
	
	
	public void testIncludeLibraryRule3( ) throws Exception
	{
		openDesign( fileName );
		try
		{
			designHandle.includeLibrary( LibA , "LibA" );
			designHandle.includeLibrary( LibB , "LibA" );
		}
		catch(Exception e)
		{
			System.out.println( "Library3 has the same namespace" );
		}
	}
	
	
	
	public void testIncludeLibraryRule4( ) throws Exception
	{
		openDesign( fileName );

		designHandle.includeLibrary( LibA , "" );
		designHandle.includeLibrary( LibB , "LibA" );

	}
	
	
	
	
	public void testIncludeLibraryRule5( ) throws Exception
	{
		openDesign(fileName);
		designHandle.includeLibrary( LibA , "" );
		designHandle.includeLibrary( LibA , "LibB" );
	}
	
	
	public void testIncludeLibraryRule6( ) throws Exception
	{
		openLibrary( LibA );
		libraryHandle.includeLibrary( LibB , "LibB");
		libraryHandle.includeLibrary( LibC , "LibC");
	}
	
	
	public void testIncludeLibraryRule7( ) throws Exception
	{
		openDesign(fileName);
		testIncludeLibraryRule6();
		designHandle.includeLibrary( LibA , "LibA");
	}
	
	
	public void testIncludeLibraryRule8( ) throws Exception
	{
		openDesign(fileName);
		testIncludeLibraryRule6();
		designHandle.includeLibrary( LibA , "LibA" );
		designHandle.includeLibrary( LibB , "" );
	}
	
	
	public void testIncludeLibraryRule9( ) throws Exception
	{
		openLibrary( LibA );
		libraryHandle.includeLibrary( LibB , "LibB" );
		libraryHandle.saveAs( outLibA );
		libraryHandle.close();
		
		openLibrary( LibB );
		libraryHandle.includeLibrary( outLibA , "LibA" );
		
	}
	
	
	public void testIncludeLibraryRule10( ) throws Exception
	{
		String fileName = "DesignIncludeLibraryTest.xml";
		openDesign( fileName );
		designHandle.includeLibrary( LibA , "LibA" );
		designHandle.includeLibrary( LibB , "LibB" );
		
		TextItemHandle textDesignHandle = (TextItemHandle)designHandle.findElement( "text1" );
		assertNotNull("Text should not be null", textDesignHandle);  //$NON-NLS-1$
		textDesignHandle.setProperty( StyleHandle.BACKGROUND_COLOR_PROP , null );
		assertEquals( "#0000FF" , textDesignHandle.getStringProperty( "backgroundColor" ));
		
		StyleHandle styleDesignHandle = (StyleHandle)designHandle.findStyle( "style1" );
		assertNotNull("Style should not be null", styleDesignHandle);
		styleDesignHandle.drop();
		
		designHandle.saveAs( outputFileName );

		TextItemHandle text2DesignHandle = (TextItemHandle)designHandle.findElement( "text1" );
		assertNotNull("Text should not be null", text2DesignHandle);
		assertEquals( null , textDesignHandle.getStringProperty( "backgroundColor" ));
	}

}
