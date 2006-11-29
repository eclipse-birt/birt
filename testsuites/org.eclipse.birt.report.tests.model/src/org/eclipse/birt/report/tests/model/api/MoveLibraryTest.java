package org.eclipse.birt.report.tests.model.api;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;


public class MoveLibraryTest extends BaseTestCase
{
	String fileName = "BlankReport.xml";
	
//	protected static final String PLUGIN_PATH =System.getProperty("user.dir")+ "\\plugins\\"+BaseTestCase.PLUGINLOC.substring(BaseTestCase.PLUGINLOC.indexOf("/")+1) + "bin/";
	private String LibA = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + "LibA.xml";
//	private String LibB = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + "LibB.xml";
//	private String LibC = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + "LibC.xml";
	private String LibD = this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + "LibD.xml";
	
//	private String outLibA = getClassFolder() + GOLDEN_FOLDER + "LibA.xml";
//	private String outLibB = getClassFolder() + GOLDEN_FOLDER + "LibB.xml";
//	private String outLibC = getClassFolder() + GOLDEN_FOLDER + "LibC.xml";
	private String outLibA = "LibA.xml";
	private String outLibB = "LibB.xml";
	private String outLibC = "LibB.xml";
	private String outLibD = "LibD.xml";
	
	public MoveLibraryTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(MoveLibraryTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( fileName, fileName );
		copyResource_INPUT( outLibA, outLibA );
		copyResource_INPUT( outLibB, outLibB );
		copyResource_INPUT( outLibC, outLibC );
		copyResource_INPUT( outLibD, outLibD );
	}
	public void tearDown( )
	{
		removeResource( );
	}
	
	public void testCopyLibA( ) throws Exception
	{
		sessionHandle = DesignEngine.newSession( ULocale.ENGLISH );
		assertNotNull( sessionHandle );

		libraryHandle = sessionHandle.openLibrary( LibA);
		assertNotNull(libraryHandle);
		super.saveLibraryAs(outLibD);
	}
	public void testMoveLibrary( ) throws Exception
	{
		openDesign(fileName);
		designHandle.includeLibrary( LibD, "LibD" );
		LibraryHandle libHandle = designHandle.getLibrary( "LibD" );
		
		TextItemHandle textLibHandle = (TextItemHandle)libHandle.findElement( "text1" );
		assertNotNull("Text should not be null", textLibHandle);
		DataItemHandle dataLibHandle = (DataItemHandle)libHandle.findElement( "data1" );
		assertNotNull("Data should not be null", dataLibHandle);
	//  SharedStyleHandle styleLibHandle = (SharedStyleHandle)libHandle.findStyle( "style1" );
	//	assertNotNull("Style should not be null", styleLibHandle);
		
		TextItemHandle textHandle = (TextItemHandle)designHandle.getElementFactory().newElementFrom( textLibHandle, "text1" );
		DataItemHandle dataHandle = (DataItemHandle)designHandle.getElementFactory().newElementFrom( dataLibHandle, "data1" );
	//	StyleHandle styleHandle = (StyleHandle)designHandle.getElementFactory().newStyle( "style1" );
		
		designHandle.getBody().add( dataHandle );
		designHandle.getBody().add( textHandle );
		//designHandle.getStyles().add( styleHandle );

		
		
		assertEquals( "yellow" , dataHandle.getExtends().getStringProperty("backgroundColor"));
		assertEquals( "red" , textHandle.getExtends().getStringProperty("backgroundColor"));
		
		File deleteLibD = new File(LibD);
		deleteLibD.delete();
		designHandle.saveAs(this.getFullQualifiedClassName( ) + "/" + INPUT_FOLDER + "/" + "SavedReport.xml");
		
		openDesign("SavedReport.xml");
		assertNotNull((TextItemHandle)designHandle.findElement("text1"));
		assertNotNull((DataItemHandle)designHandle.findElement("data1"));
	    assertEquals( null , ((TextItemHandle)designHandle.findElement("text1")).getStringProperty("backgroundColor"));
		assertEquals( null , ((DataItemHandle)designHandle.findElement("data1")).getStringProperty("backgroundColor"));

	}
}
