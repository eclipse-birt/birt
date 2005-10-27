package org.eclipse.birt.report.tests.model.api;

import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;



public class LibraryIncludeLibraryTest extends BaseTestCase
{
	private String inputLibraryName1 = "Library_ElementID_Lib.xml";
	private String inputLibraryName2 = "LibraryCreatLib.xml";
    private String outputFileName = "Library_IncludeLibrary_Lib.xml"; 
	private String goldenFileName = "Library_IncludeLibrary_Lib_golden.xml"; 
    
	String LibFile1 = PLUGIN_PATH + getClassFolder( ) + INPUT_FOLDER + inputLibraryName1;
    String LibFile2 = PLUGIN_PATH + getClassFolder( ) + INPUT_FOLDER + inputLibraryName2;
    
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
	public void testIncludeLibrary( ) throws Exception
	{
		openLibrary( LibFile2 );
		
		libraryHandle.includeLibrary( LibFile1, "LibA" );
		LibraryHandle libHandle = libraryHandle.getLibrary( "LibA" );
		
		TableHandle tableLibHandle = (TableHandle)libHandle.findElement( "tableA" );
		assertNotNull("Table should not be null", tableLibHandle);
		DataSourceHandle dataSourceLibHandle = (DataSourceHandle)libHandle.findDataSource( "mysql" );
		assertNotNull("Datasource should not be null", dataSourceLibHandle);
		DataSetHandle dataSetLibHandle = (DataSetHandle)libHandle.findDataSet( "mysqlds" );
		assertNotNull("Dataset should not be null", dataSetLibHandle);
		
		TableHandle tableHandle = (TableHandle)libraryHandle.getElementFactory().newElementFrom( tableLibHandle, "tableA" );
		DataSourceHandle dataSourceHandle = (DataSourceHandle)libraryHandle.getElementFactory().newOdaDataSource( "mysql" );
		DataSetHandle dataSetHandle = (DataSetHandle)libraryHandle.getElementFactory().newOdaDataSet( "mysqlds" );
		
		
		libraryHandle.getDataSources().add( dataSourceHandle );
		libraryHandle.getDataSets().add( dataSetHandle );
		
		assertNotNull(libraryHandle);
		super.saveLibraryAs(outputFileName);
		assertTrue( compareTextFile( goldenFileName, outputFileName ) );
	}
}
