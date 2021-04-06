
package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * TestCases for library creation.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * 
 * <tr>
 * <td>{@link #testCreatLibrary()}</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testNoNameText()}</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testNoNameTable()}</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testNoNameStyle()}</td>
 * </tr>
 * <tr>
 * <td>{@link #testDuplicateLibrary()}</td>
 * </tr>
 * </table>
 * 
 */
public class LibraryCreateTest extends BaseTestCase {

	private static String fileName = "Library_Creat_Test.xml";
	private static String outFileName = "LibraryCreatLib.xml";
	private static String goldenFileName = "LibraryCreatLib_golden.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + fileName);
		copyInputToFile(INPUT_FOLDER + "/" + outFileName);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + goldenFileName);

		openDesign(fileName);
	}

	public void tearDown() {
		removeResource();
	}

	public LibraryCreateTest(String name) {
		super(name);
	}

	public static Test suite() {

		return new TestSuite(LibraryCreateTest.class);
	}

	/**
	 * Test export elements to library
	 * 
	 * @throws Exception
	 */
	public void testCreatLibrary() throws Exception {
		// openDesign(fileName);

		// "Find the element in Design
		DataSourceHandle dataSourceHandle = (DataSourceHandle) designHandle.findDataSource("db2d");
		assertNotNull("Datasource should not be null", dataSourceHandle);
		DataSetHandle dataSetHandle = (DataSetHandle) designHandle.findDataSet("db2ds");
		assertNotNull("Dataset should not be null", dataSetHandle);
		TextItemHandle textHandle = (TextItemHandle) designHandle.findElement("myText");
		assertNotNull("Text should not be null", textHandle);
		TableHandle tableHandle = (TableHandle) designHandle.findElement("myTable");
		assertNotNull("Table should not be null", tableHandle);
		StyleHandle styleHandle = (StyleHandle) designHandle.findStyle("myStyle");
		assertNotNull("Style should not be null", styleHandle);
		ParameterHandle parameterHandle = (ParameterHandle) designHandle.findParameter("Parameter1");
		assertNotNull("Parameter should not be null", parameterHandle);
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("myLabel");
		assertNotNull("Label should not be null", labelHandle);
		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("myImage");
		assertNotNull("Image should not be null", imageHandle);
		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("myData");
		assertNotNull("Data should not be null", dataHandle);

		// Create a Library
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		libraryHandle = sessionHandle.createLibrary();

		// Import the element into Library
		ElementExportUtil.exportElement(dataSourceHandle, libraryHandle, false);
		ElementExportUtil.exportElement(dataSetHandle, libraryHandle, false);
		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);
		ElementExportUtil.exportElement(textHandle, libraryHandle, false);
		ElementExportUtil.exportElement(tableHandle, libraryHandle, false);
		ElementExportUtil.exportElement(styleHandle, libraryHandle, false);
		ElementExportUtil.exportElement(parameterHandle, libraryHandle, false);
		ElementExportUtil.exportElement(imageHandle, libraryHandle, false);
		ElementExportUtil.exportElement(dataHandle, libraryHandle, false);

		// super.saveLibraryAs(outFileName);

		String TempFile = this.genOutputFile(outFileName);
		libraryHandle.saveAs(TempFile);
		assertTrue(compareTextFile(goldenFileName, outFileName));

	}

	/**
	 * Test export text without name
	 * 
	 * @throws Exception
	 */
	public void testNoNameText() throws Exception {
		// openDesign(fileName);

		// Creat the Element without name
		TextItemHandle textHandle = (TextItemHandle) designHandle.getElementFactory().newTextItem("");
		designHandle.getBody().add(textHandle);

		// Create a Library
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		libraryHandle = sessionHandle.createLibrary();

		// Import the text into Library
		try {
			ElementExportUtil.exportElement(textHandle, libraryHandle, false);
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	/**
	 * Test export table without name
	 * 
	 * @throws Exception
	 */
	public void testNoNameTable() throws Exception {
		// openDesign(fileName);

		TableHandle tableHandle = (TableHandle) designHandle.getElementFactory().newTableItem("");
		designHandle.getBody().add(tableHandle);

		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		libraryHandle = sessionHandle.createLibrary();

		// Import the table into Library
		try {
			ElementExportUtil.exportElement(tableHandle, libraryHandle, false);
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	/**
	 * Test export style without name
	 * 
	 * @throws Exception
	 */
	public void testNoNameStyle() throws Exception {
		// openDesign(fileName);

		StyleHandle styleHandle = (StyleHandle) designHandle.getElementFactory().newStyle("");
		designHandle.getStyles().add(styleHandle);

		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		libraryHandle = sessionHandle.createLibrary();

		// Import the style into Library
		try {
			ElementExportUtil.exportElement(styleHandle, libraryHandle, false);
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	/**
	 * Test export element with duplicate name to library
	 * 
	 * @throws Exception
	 */
	public void testDuplicateLibrary() throws Exception {
		// openDesign(fileName);
		LabelHandle labelHandle = (LabelHandle) designHandle.getElementFactory().newLabel("label1");
		designHandle.getBody().add(labelHandle);

		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		libraryHandle = sessionHandle.createLibrary();

		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);

		labelHandle.drop();

		LabelHandle label2Handle = (LabelHandle) designHandle.getElementFactory().newLabel("label1");
		designHandle.getBody().add(label2Handle);

		try {
			ElementExportUtil.exportElement(label2Handle, libraryHandle, false);
		} catch (Exception e) {
			assertNotNull(e);
		}

	}

}
