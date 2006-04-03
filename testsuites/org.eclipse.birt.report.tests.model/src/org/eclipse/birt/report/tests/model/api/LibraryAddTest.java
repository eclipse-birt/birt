package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;
import org.eclipse.birt.report.model.api.activity.SemanticException;
;

public class LibraryAddTest extends BaseTestCase
{	
	String fileName = "Library_Addin_test.xml";
	private String inputLibraryName = "LibraryCreatLib.xml";

    private String outFileName = "Library_Addin_Test_out.xml"; 
	private String goldenFileName = "Library_Addin_Test_golden.xml"; 
	String LibFile= inputLibraryName;
	String LibFileError1 = PLUGIN_PATH +getClassFolder( ) + INPUT_FOLDER + "LibY.xml";

	public LibraryAddTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(LibraryAddTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
	public void testAddinLibrary( ) throws Exception
	{
		openDesign( "../input/Library_Import_test.xml" );
		designHandle.includeLibrary( LibFile, "LibB" );
		designHandle.includeLibrary( "../input/LibA.xml" , "");
	    super.saveAs( outFileName );
	    assertTrue( compareTextFile( goldenFileName, outFileName ) );
	    
	    try
	    {
	    designHandle.includeLibrary(LibFileError1, "LibY" );
	    }
	    catch(SemanticException e)
	    {
	    	assertNotNull(e);
	    }
	    catch(DesignFileException e)
	    {
	    	assertNotNull(e);
	    }
	    catch(Exception e)
	    {
	    	assertNotNull(e);
	    }
	    
	    try
	    {
	    designHandle.includeLibrary( "../inputLibZ.xml", "LibZ" );
	    }
	    catch(SemanticException e)
	    {
	    	assertNotNull(e);
	    }
	    catch(DesignFileException e)
	    {
	    	assertNotNull(e);
	    }
	    catch(Exception e)
	    {
	    	assertNotNull(e);
	    }
	    
	    
	    
	}
}
