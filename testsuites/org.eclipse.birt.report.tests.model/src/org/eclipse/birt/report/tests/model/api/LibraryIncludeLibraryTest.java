package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.tests.model.BaseTestCase;

public class LibraryIncludeLibraryTest extends BaseTestCase
{
	protected static final String PLUGIN_PATH =System.getProperty("user.dir")+ "\\plugins\\"+BaseTestCase.PLUGINLOC.substring(BaseTestCase.PLUGINLOC.indexOf("/")+1) + "bin/";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	String LibA = PLUGIN_PATH + getClassFolder() + INPUT_FOLDER + "LibA.xml";
	String LibB = PLUGIN_PATH + getClassFolder() + INPUT_FOLDER + "LibB.xml";
	String LibC = PLUGIN_PATH + getClassFolder() + INPUT_FOLDER + "LibC.xml";
	
	public LibraryIncludeLibraryTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(LibraryIncludeLibraryTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
	public void testLibraryIncludeLibrary( ) throws Exception
	{
		openLibrary(LibB);
//		libraryHandle.includeLibrary(LibC, "LibC");
		libraryHandle.includeLibrary(LibC,"LibC");
		libraryHandle.saveAs("d:/LibBincludeLibC.xml");
		
	}
}
