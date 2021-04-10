
package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * TestCases for ExternalCssStyleSheet.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * 
 * <tr>
 * <td>{@link #testIncludeLibrary()}</td>
 * </tr>
 * </table>
 * 
 */
public class LibraryIncludeLibraryTest extends BaseTestCase {

	private String inputLibraryName1 = "Library_ElementID_Lib.xml"; //$NON-NLS-1$

	private String inputLibraryName2 = "LibraryCreatLib.xml"; //$NON-NLS-1$

	// private String goldenFileName = "LibraryCreatLib_golden.xml";
	// //$NON-NLS-1$
	private String goldenFileName = "Library_IncludeLibrary_Lib_golden.xml"; //$NON-NLS-1$

	private String outputFileName = "Library_IncludeLibrary_Lib.xml"; //$NON-NLS-1$

	/**
	 * @param name
	 */
	public LibraryIncludeLibraryTest(String name) {
		super(name);
	}

	/**
	 * @return
	 */
	public static Test suite() {

		return new TestSuite(LibraryIncludeLibraryTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + inputLibraryName1);
		copyInputToFile(INPUT_FOLDER + "/" + inputLibraryName2);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + goldenFileName);

	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * Test include library into another library
	 * 
	 * @throws Exception
	 */
	public void testIncludeLibrary() throws Exception {
		openLibrary(inputLibraryName2);

		libraryHandle.includeLibrary(inputLibraryName1, "LibA"); //$NON-NLS-1$
		LibraryHandle libHandle = libraryHandle.getLibrary("LibA"); //$NON-NLS-1$

		TableHandle tableLibHandle = (TableHandle) libHandle.findElement("tableA"); //$NON-NLS-1$
		assertNotNull("Table should not be null", tableLibHandle); //$NON-NLS-1$
		DataSourceHandle dataSourceLibHandle = libHandle.findDataSource("mysql"); //$NON-NLS-1$
		assertNotNull("Datasource should not be null", dataSourceLibHandle); //$NON-NLS-1$
		DataSetHandle dataSetLibHandle = libHandle.findDataSet("mysqlds"); //$NON-NLS-1$
		assertNotNull("Dataset should not be null", dataSetLibHandle); //$NON-NLS-1$

		TableHandle tableHandle = (TableHandle) libraryHandle.getElementFactory().newElementFrom(tableLibHandle,
				"tableA"); //$NON-NLS-1$
		DataSourceHandle dataSourceHandle = libraryHandle.getElementFactory().newOdaDataSource("mysql"); //$NON-NLS-1$
		DataSetHandle dataSetHandle = libraryHandle.getElementFactory().newOdaDataSet("mysqlds");

		libraryHandle.getDataSources().add(dataSourceHandle);
		libraryHandle.getDataSets().add(dataSetHandle);

		assertNotNull(libraryHandle);

		// super.saveLibraryAs( outputFileName );
		String TempFile = this.genOutputFile(outputFileName);
		libraryHandle.saveAs(TempFile);

		assertTrue(compareTextFile(goldenFileName, outputFileName));
	}
}
