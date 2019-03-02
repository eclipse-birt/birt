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
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * TestCases for report design including library.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * 
 * 
 * <tr>
 * <td>{@link #testDesignIncludeLibrary()}</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testDuplicateDesign()}</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testNoLibrary()}</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testDBRefer()}</td>
 * </tr>
 * </table>
 * 
 */
public class DesignIncludeLibraryTest extends BaseTestCase {
	String fileName = "DesignIncludeLibraryTest.xml"; //$NON-NLS-1$

	private String inputLibraryName = "LibA.xml"; //$NON-NLS-1$

	private String outFileName = "DesignIncludeLibraryDpt.xml"; //$NON-NLS-1$

	private String goldenFileName = "DesignIncludeLibraryDpt_golden.xml"; //$NON-NLS-1$

//	String inputLibrary = PLUGIN_PATH+ getClassFolder()+ INPUT_FOLDER
//			+ inputLibraryName;

	public DesignIncludeLibraryTest(String name) {
		super(name);
	}

	public static Test suite() {

		return new TestSuite(DesignIncludeLibraryTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource( );
		
		// retrieve two input files from tests-model.jar file
		copyResource_INPUT( fileName, fileName);
		copyResource_INPUT( inputLibraryName , inputLibraryName );
		copyResource_GOLDEN (goldenFileName, goldenFileName);
		
		
	
	}

	/**
	 * Test report includes library
	 * @throws Exception
	 */
	public void testDesignIncludeLibrary() throws Exception {
		openDesign(fileName);

		// Inclued LibA
		designHandle.includeLibrary(inputLibraryName, "LibA"); //$NON-NLS-1$
		LibraryHandle libAHandle = designHandle.getLibrary("LibA"); //$NON-NLS-1$

		// Find element in LibA
		TextItemHandle textLibA = (TextItemHandle) libAHandle
				.findElement("text1"); //$NON-NLS-1$
		assertNotNull("Text should not be null", textLibA); //$NON-NLS-1$
		TableHandle tableLibA = (TableHandle) libAHandle.findElement("table1"); //$NON-NLS-1$
		assertNotNull("Table should not be null", tableLibA); //$NON-NLS-1$
		ParameterHandle parameterLibA = (ParameterHandle) libAHandle
				.findParameter("par1"); //$NON-NLS-1$
		assertNotNull("Parameter should not be null", parameterLibA); //$NON-NLS-1$
		LabelHandle labelLibA = (LabelHandle) libAHandle.findElement("label1"); //$NON-NLS-1$
		assertNotNull("Label should not be null", labelLibA); //$NON-NLS-1$
		ImageHandle imageLibA = (ImageHandle) libAHandle.findElement("pic1"); //$NON-NLS-1$
		assertNotNull("Image should not be null", imageLibA); //$NON-NLS-1$
		DataItemHandle dataLibA = (DataItemHandle) libAHandle
				.findElement("data1"); //$NON-NLS-1$
		assertNotNull("Data should not be null", dataLibA); //$NON-NLS-1$
		DataSourceHandle dataSourceLibA = (DataSourceHandle) libAHandle
				.findDataSource("sqlds1"); //$NON-NLS-1$
		assertNotNull("Datasource should not be null", dataSourceLibA); //$NON-NLS-1$
		DataSetHandle dataSetLibA = (DataSetHandle) libAHandle
				.findDataSet("sqldst1"); //$NON-NLS-1$
		assertNotNull("Dataset should not be null", dataSetLibA); //$NON-NLS-1$
		ListHandle listLibA = (ListHandle) libAHandle.findElement("list1"); //$NON-NLS-1$
		assertNotNull("List should not be null", listLibA); //$NON-NLS-1$
		GridHandle gridLibA = (GridHandle) libAHandle.findElement("grid1"); //$NON-NLS-1$
		assertNotNull("Grid should not be null", gridLibA); //$NON-NLS-1$

		// New the element in Design and extends element in LibA
		TextItemHandle textLibAHandle = (TextItemHandle) designHandle
				.getElementFactory().newElementFrom(textLibA, "text1"); //$NON-NLS-1$
		TableHandle tableLibAHandle = (TableHandle) designHandle
				.getElementFactory().newElementFrom(tableLibA, "table1"); //$NON-NLS-1$
		ParameterHandle parameterLibAHandle = (ParameterHandle) designHandle
				.getElementFactory().newElementFrom(parameterLibA, "par1"); //$NON-NLS-1$
		LabelHandle labelLibAHandle = (LabelHandle) designHandle
				.getElementFactory().newElementFrom(labelLibA, "label1"); //$NON-NLS-1$
		ImageHandle imageLibAHandle = (ImageHandle) designHandle
				.getElementFactory().newElementFrom(imageLibA, "pic1"); //$NON-NLS-1$
		DataItemHandle dataLibAHandle = (DataItemHandle) designHandle
				.getElementFactory().newElementFrom(dataLibA, "data1"); //$NON-NLS-1$
		DataSourceHandle dataSourceLibAHandle = (DataSourceHandle) designHandle
				.getElementFactory().newOdaDataSource("sqlds1"); //$NON-NLS-1$
		DataSetHandle dataSetLibAHandle = (DataSetHandle) designHandle
				.getElementFactory().newOdaDataSet("sqldst1"); //$NON-NLS-1$
			ListHandle listLibAHandle = (ListHandle) designHandle
				.getElementFactory().newElementFrom(listLibA, "list1"); //$NON-NLS-1$
		GridHandle gridLibAHandle = (GridHandle) designHandle
				.getElementFactory().newElementFrom(gridLibA, "grid1"); //$NON-NLS-1$

		// Add Body in Design
		designHandle.getDataSources().add(dataSourceLibAHandle);
		designHandle.getDataSets().add(dataSetLibAHandle);
		designHandle.getBody().add(dataLibAHandle);
		designHandle.getParameters().add(parameterLibAHandle);
		designHandle.getBody().add(textLibAHandle);
		designHandle.getBody().add(labelLibAHandle);
		designHandle.getBody().add(tableLibAHandle);
		designHandle.getBody().add(imageLibAHandle);
		designHandle.getBody().add(listLibAHandle);
		designHandle.getBody().add(gridLibAHandle);

		// Cheack the element name extends in LibA
		assertEquals("sqlds11", dataSourceLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("sqldst11", dataSetLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("data11", dataLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("par11", parameterLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("text11", textLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("label11", labelLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("table11", tableLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("pic11", imageLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("list11", listLibAHandle.getName()); //$NON-NLS-1$
		assertEquals("grid11", gridLibAHandle.getName()); //$NON-NLS-1$

		// Check the style of design label before remove design style
		LabelHandle labelDesignHandle = (LabelHandle) designHandle
				.findElement("designlabel");
		assertNotNull("designlabel should not be null", labelDesignHandle);
		assertEquals("#FFA500", labelDesignHandle.getStringProperty("color"));
		assertEquals("#0000FF", labelDesignHandle
				.getStringProperty("backgroundColor"));

		// Extends the LibA style in design
		StyleHandle styleLibA = (StyleHandle) libAHandle.findStyle("style1"); //$NON-NLS-1$
		assertNotNull("Style should not be null", styleLibA); //$NON-NLS-1$
		StyleHandle styleLibAHandle = (StyleHandle) designHandle
				.getElementFactory().newStyle("style1"); //$NON-NLS-1$
		designHandle.getStyles().add(styleLibAHandle);
		assertEquals("#FFA500", labelDesignHandle.getStringProperty("color"));
		assertEquals("#0000FF", labelDesignHandle
				.getStringProperty("backgroundColor"));

		// Remove the style of design and Check the style of design label
		StyleHandle styleDesignHandle = (StyleHandle) designHandle
				.findStyle("style1");
		assertNotNull("style1 shoule not be null", styleDesignHandle);
		styleDesignHandle.drop();
		assertEquals("black", labelDesignHandle.getStringProperty("color"));
		assertEquals(null, labelDesignHandle
				.getStringProperty("backgroundColor"));

		super.saveAs(outFileName); //$NON-NLS-1$

	}

	/**
	 * Test extend item from library
	 * @throws Exception
	 */
	public void testDuplicateDesign() throws Exception {
		openDesign(fileName);

		// Inclued LibA
		designHandle.includeLibrary(inputLibraryName, "LibA"); //$NON-NLS-1$
		LibraryHandle libAHandle = designHandle.getLibrary("LibA"); //$NON-NLS-1$

		// Find element in LibA
		TextItemHandle text1LibA = (TextItemHandle) libAHandle
				.findElement("text1"); //$NON-NLS-1$
		assertNotNull("Text should not be null", text1LibA); //$NON-NLS-1$

		// New the element in Design and extends element in LibA
		TextItemHandle text1LibAHandle = (TextItemHandle) designHandle
				.getElementFactory().newElementFrom(text1LibA, ""); //$NON-NLS-1$
		designHandle.getBody().add(text1LibAHandle);

		text1LibAHandle.setName("text4");

		// Find element in LibA
		TextItemHandle text4LibA = (TextItemHandle) libAHandle
				.findElement("text4"); //$NON-NLS-1$
		assertNotNull("Text should not be null", text4LibA); //$NON-NLS-1$

		try {
			// New the element in Design and extends element in LibA
			TextItemHandle text4LibAHandle = (TextItemHandle) designHandle
					.getElementFactory().newElementFrom(text4LibA, ""); //$NON-NLS-1$
			designHandle.getBody().add(text4LibAHandle);
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	/**
	 * Test include an no-exiting library
	 * @throws Exception
	 */
	public void testNoLibrary() throws Exception {
		openDesign(fileName);

		// Inclued a no-exiting library
		try {
			designHandle.includeLibrary("../input/LibX.xml", "LibA"); //$NON-NLS-1$
			LibraryHandle libAHandle = designHandle.getLibrary("LibA");
		} catch (Exception e) {
			assertNotNull(e);
		}
	}

	/**
	 * Test Data source/dataset reference from library
	 * @throws Exception
	 */
	public void testDBRefer() throws Exception {
		openDesign(fileName);

		// Inclued LibA
		designHandle.includeLibrary(inputLibraryName, "LibA"); //$NON-NLS-1$
		LibraryHandle libAHandle = designHandle.getLibrary("LibA"); //$NON-NLS-1$

		DataSourceHandle dataSourceLibA = (DataSourceHandle) libAHandle
				.findDataSource("sqlds1"); //$NON-NLS-1$
		assertNotNull("Datasource should not be null", dataSourceLibA); //$NON-NLS-1$
		DataSetHandle dataSetLibA = (DataSetHandle) libAHandle
				.findDataSet("sqldst1"); //$NON-NLS-1$
		assertNotNull("Dataset should not be null", dataSetLibA); //$NON-NLS-1$

		// DataSourceHandle dataSourceLibAHandle =
		// (DataSourceHandle)designHandle.getElementFactory().newOdaDataSource(
		// "sqlds1" ); //$NON-NLS-1$
		DataSetHandle dataSetLibAHandle = (DataSetHandle) designHandle
				.getElementFactory().newOdaDataSet("sqldst1"); //$NON-NLS-1$

		// designHandle.getDataSources().add( dataSourceLibAHandle );
		designHandle.getDataSets().add(dataSetLibAHandle);

		TableHandle tableHandle = (TableHandle) designHandle
				.getElementFactory().newTableItem("mytable1"); //$NON-NLS-1$ 
		designHandle.getBody().add(tableHandle);

		dataSetLibAHandle.setDataSource("LibA.sqlds1");
		tableHandle.setDataSet(dataSetLibA);
	}

}
