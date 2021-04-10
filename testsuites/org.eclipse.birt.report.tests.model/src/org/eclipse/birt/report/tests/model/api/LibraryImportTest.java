
package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * TestCases for Import library.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * 
 * <tr>
 * <td>{@link #testImportLibrary()}</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class LibraryImportTest extends BaseTestCase {

	String fileName = "Library_Import_Test.xml"; //$NON-NLS-1$
	private String inputLibraryName = "LibraryCreatLib.xml"; //$NON-NLS-1$
	private String outputFileName = "Library_Import_Dpt.xml"; //$NON-NLS-1$
	private String goldenFileName = "LibraryImportDpt_golden.xml"; //$NON-NLS-1$

	/**
	 * @param name
	 */
	public LibraryImportTest(String name) {
		super(name);
	}

	/**
	 * @return
	 * 
	 */
	public static Test suite() {

		return new TestSuite(LibraryImportTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + fileName);
		copyInputToFile(INPUT_FOLDER + "/" + inputLibraryName);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + goldenFileName);

	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * Test import library to report
	 * 
	 * @throws Exception
	 */
	public void testImportLibrary() throws Exception {

		openDesign(fileName);
		designHandle.includeLibrary(inputLibraryName, "LibA"); //$NON-NLS-1$
		LibraryHandle libHandle = designHandle.getLibrary("LibA"); //$NON-NLS-1$

		TextItemHandle textLibHandle = (TextItemHandle) libHandle.findElement("myText"); //$NON-NLS-1$
		assertNotNull("Text should not be null", textLibHandle); //$NON-NLS-1$
		TableHandle tableLibHandle = (TableHandle) libHandle.findElement("myTable"); //$NON-NLS-1$
		assertNotNull("Table should not be null", tableLibHandle); //$NON-NLS-1$
		StyleHandle styleLibHandle = libHandle.findStyle("myStyle"); //$NON-NLS-1$
		assertNotNull("Style should not be null", styleLibHandle); //$NON-NLS-1$
		ParameterHandle parameterLibHandle = libHandle.findParameter("Parameter1"); //$NON-NLS-1$
		assertNotNull("Parameter should not be null", parameterLibHandle); //$NON-NLS-1$
		LabelHandle labelLibHandle = (LabelHandle) libHandle.findElement("myLabel"); //$NON-NLS-1$
		assertNotNull("Label should not be null", labelLibHandle); //$NON-NLS-1$
		ImageHandle imageLibHandle = (ImageHandle) libHandle.findElement("myImage"); //$NON-NLS-1$
		assertNotNull("Image should not be null", imageLibHandle); //$NON-NLS-1$
		DataItemHandle dataLibHandle = (DataItemHandle) libHandle.findElement("myData"); //$NON-NLS-1$
		assertNotNull("Data should not be null", dataLibHandle); //$NON-NLS-1$
		DataSourceHandle dataSourceLibHandle = libHandle.findDataSource("db2d"); //$NON-NLS-1$
		assertNotNull("Datasource should not be null", dataSourceLibHandle); //$NON-NLS-1$
		DataSetHandle dataSetLibHandle = libHandle.findDataSet("db2ds"); //$NON-NLS-1$
		assertNotNull("Dataset should not be null", dataSetLibHandle); //$NON-NLS-1$

		TextItemHandle textHandle = (TextItemHandle) designHandle.getElementFactory().newElementFrom(textLibHandle,
				"myText"); //$NON-NLS-1$
		TableHandle tableHandle = (TableHandle) designHandle.getElementFactory().newElementFrom(tableLibHandle,
				"myTable"); //$NON-NLS-1$
		StyleHandle styleHandle = designHandle.getElementFactory().newStyle("myStyle"); //$NON-NLS-1$
		ParameterHandle parameterHandle = (ParameterHandle) designHandle.getElementFactory()
				.newElementFrom(parameterLibHandle, "Parameter1"); //$NON-NLS-1$
		LabelHandle labelHandle = (LabelHandle) designHandle.getElementFactory().newElementFrom(labelLibHandle,
				"myLabel"); //$NON-NLS-1$
		ImageHandle imageHandle = (ImageHandle) designHandle.getElementFactory().newElementFrom(imageLibHandle,
				"myImage"); //$NON-NLS-1$
		DataItemHandle dataHandle = (DataItemHandle) designHandle.getElementFactory().newElementFrom(dataLibHandle,
				"myData"); //$NON-NLS-1$
		DataSourceHandle dataSourceHandle = designHandle.getElementFactory().newOdaDataSource("db2d"); //$NON-NLS-1$
		DataSetHandle dataSetHandle = designHandle.getElementFactory().newOdaDataSet("db2ds"); //$NON-NLS-1$

		designHandle.getDataSources().add(dataSourceHandle);
		designHandle.getDataSets().add(dataSetHandle);
		designHandle.getStyles().add(styleHandle);
		designHandle.getBody().add(dataHandle);
		designHandle.getParameters().add(parameterHandle);
		designHandle.getBody().add(textHandle);
		designHandle.getBody().add(labelHandle);
		designHandle.getBody().add(tableHandle);
		designHandle.getBody().add(imageHandle);
		assertNotNull(designHandle);
		// super.saveAs( outputFileName );
		String TempFile = this.genOutputFile(outputFileName);
		designHandle.saveAs(TempFile);
		assertTrue(compareTextFile(goldenFileName, outputFileName));

	}

}
