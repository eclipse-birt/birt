package org.eclipse.birt.report.tests.model.api;


import junit.framework.Test;
import junit.framework.TestSuite;


import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

public class LibraryImportTest extends BaseTestCase
{
	String fileName = "Library_Import_test.xml";
	private String inputLibraryName = "LibraryCreatLib.xml";
    private String outputFileName = "Library_Import_Dpt.xml"; 
	private String goldenFileName = "LibraryImportDpt_golden.xml"; 
	
	protected static final String PLUGIN_PATH =System.getProperty("user.dir")+ "\\plugins\\"+BaseTestCase.PLUGINLOC.substring(BaseTestCase.PLUGINLOC.indexOf("/")+1) + "bin/";
	String LibFile= PLUGIN_PATH + getClassFolder() + INPUT_FOLDER + inputLibraryName;
	
	public LibraryImportTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(LibraryImportTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
	public void testImportLibrary( ) throws Exception
	{
				
		openDesign(fileName);
		designHandle.includeLibrary( LibFile, "LibA" );
		LibraryHandle libHandle = designHandle.getLibrary( "LibA" );
		
		TextItemHandle textLibHandle = (TextItemHandle)libHandle.findElement( "myText" );
		assertNotNull("Text should not be null", textLibHandle); 
		TableHandle tableLibHandle = (TableHandle)libHandle.findElement( "myTable" );
		assertNotNull("Table should not be null", tableLibHandle);
		StyleHandle styleLibHandle = (StyleHandle)libHandle.findStyle( "myStyle" );
		assertNotNull("Style should not be null", styleLibHandle);
		ParameterHandle parameterLibHandle = (ParameterHandle)libHandle.findParameter( "Parameter1" );
		assertNotNull("Parameter should not be null", parameterLibHandle);
		LabelHandle labelLibHandle = (LabelHandle)libHandle.findElement( "myLabel" );
		assertNotNull("Label should not be null", labelLibHandle);
		ImageHandle imageLibHandle = (ImageHandle)libHandle.findElement( "myImage" );
		assertNotNull("Image should not be null", imageLibHandle);
		DataItemHandle dataLibHandle = (DataItemHandle)libHandle.findElement( "myData" );
		assertNotNull("Data should not be null", dataLibHandle);
		DataSourceHandle dataSourceLibHandle = (DataSourceHandle)libHandle.findDataSource( "db2d" );
		assertNotNull("Datasource should not be null", dataSourceLibHandle);
		DataSetHandle dataSetLibHandle = (DataSetHandle)libHandle.findDataSet( "db2ds" );
		assertNotNull("Dataset should not be null", dataSetLibHandle);
		
		TextItemHandle textHandle = (TextItemHandle)designHandle.getElementFactory().newElementFrom( textLibHandle, "myText" );
		TableHandle tableHandle = (TableHandle)designHandle.getElementFactory().newElementFrom( tableLibHandle, "myTable" );
 		StyleHandle styleHandle = (StyleHandle)designHandle.getElementFactory().newStyle( "myStyle" );
		ParameterHandle parameterHandle = (ParameterHandle)designHandle.getElementFactory().newElementFrom( parameterLibHandle, "Parameter1" );
		LabelHandle labelHandle = (LabelHandle)designHandle.getElementFactory().newElementFrom( labelLibHandle, "myLabel" );
		ImageHandle imageHandle = (ImageHandle)designHandle.getElementFactory().newElementFrom( imageLibHandle, "myImage" );
		DataItemHandle dataHandle = (DataItemHandle)designHandle.getElementFactory().newElementFrom( dataLibHandle, "myData" );
		DataSourceHandle dataSourceHandle = (DataSourceHandle)designHandle.getElementFactory().newOdaDataSource( "db2d" );
		DataSetHandle dataSetHandle = (DataSetHandle)designHandle.getElementFactory().newOdaDataSet( "db2ds" );
		
		designHandle.getDataSources().add( dataSourceHandle );
		designHandle.getDataSets().add( dataSetHandle );
		designHandle.getStyles().add( styleHandle );
		designHandle.getBody().add( dataHandle );
		designHandle.getParameters().add( parameterHandle );
		designHandle.getBody().add( textHandle );
		designHandle.getBody().add( labelHandle );
		designHandle.getBody().add( tableHandle );
		designHandle.getBody().add( imageHandle );
	    assertNotNull(designHandle);
		super.saveAs( outputFileName );
		assertTrue( compareTextFile( goldenFileName, outputFileName ) );
		
		
		
	}
	
}
