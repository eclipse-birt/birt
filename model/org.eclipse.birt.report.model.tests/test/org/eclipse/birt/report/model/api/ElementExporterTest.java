/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.io.File;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.LibraryUtil;

import com.ibm.icu.util.ULocale;

/**
 * Tests exporting one element in design file or library file to library file.
 */

public class ElementExporterTest extends BaseTestCase {

	/**
	 * Tests exporting one label to library file.
	 *
	 * @throws Exception if any exception.
	 */

	public void testExportingLabel() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$

		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one label with style to library file.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingLabelWithStyle() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label2"); //$NON-NLS-1$

		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_2.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one label with extends to library file.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingLabelWithExtends() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label3"); //$NON-NLS-1$

		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_3.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting table to library file.
	 *
	 * @throws Exception if any exception.
	 */

	public void testExportingTable() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		TableHandle tableHandle = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$

		ElementExportUtil.exportElement(tableHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_4.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting the label which has duplicate name in library file.
	 *
	 * @throws Exception if any exception.
	 */

	public void testExportingDuplicateLabel() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$

		// The label named "libLabel" already exists.

		labelHandle.setName("libLabel"); //$NON-NLS-1$

		try {
			ElementExportUtil.exportElement(labelHandle, libraryHandle, false);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof NameException);
		}
	}

	/**
	 * Tests exporting style to library file.
	 *
	 * @throws Exception if any exception.
	 */

	public void testExportingStyle() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		SharedStyleHandle styleHandle = designHandle.findStyle("style2"); //$NON-NLS-1$

		ElementExportUtil.exportElement(styleHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_5.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting data source/set to library file.
	 *
	 * @throws Exception if any exception.
	 */

	public void testExportingDataMumble() throws Exception {

		openDesign("ElementExporterTest_5.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		DataSetHandle dataSetHandle = designHandle.findDataSet("dataSet1"); //$NON-NLS-1$

		ElementExportUtil.exportElement(dataSetHandle, libraryHandle, false);

		// The exported data set has unresolved data source

		dataSetHandle = libraryHandle.findDataSet("dataSet1"); //$NON-NLS-1$
		assertEquals("dataSource1", dataSetHandle.getDataSourceName()); //$NON-NLS-1$
		assertEquals(null, dataSetHandle.getDataSource());

		DataSourceHandle dataSourceHandle = designHandle.findDataSource("dataSource1"); //$NON-NLS-1$

		ElementExportUtil.exportElement(dataSourceHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_6.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one structure to library file.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingCustomColor() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		PropertyHandle propHandle = designHandle.getPropertyHandle(Module.COLOR_PALETTE_PROP);
		CustomColorHandle colorHandle = (CustomColorHandle) propHandle.getAt(0);

		ElementExportUtil.exportStructure(colorHandle, libraryHandle, false);

		try {
			// This color is exported already, so the exception of duplicate
			// name will be thrown.

			ElementExportUtil.exportStructure(colorHandle, libraryHandle, false);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_7.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one label existing in one table.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingOneLabelInTable() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("innerLabel"); //$NON-NLS-1$

		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_8.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one label existing in masterpage.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingOneLabelInMasterPage() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("labelInMasterPage"); //$NON-NLS-1$

		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_9.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting one element to new library file.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingOneLabelToNewLibraryFile() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("labelInMasterPage"); //$NON-NLS-1$

		String libraryDir = getTempFolder() + OUTPUT_FOLDER;
		File file = new File(libraryDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		String libraryFile = libraryDir + "ElementExporterTestLibrary_out_10.xml"; //$NON-NLS-1$
		file = new File(libraryFile);
		if (file.exists()) {
			file.delete();
		}

		ElementExportUtil.exportElement(labelHandle, libraryFile, false);
		assertTrue(compareFile("ElementExporterTestLibrary_golden_10.xml", "ElementExporterTestLibrary_out_10.xml")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * test to export a design handle to a given library file.
	 *
	 *
	 * <ul>
	 * <li>1. one sucessful case
	 * <li>2. one design file with template-data-set
	 * <li>3. one design file with template-label that directly resides in body
	 * <li>3. one design file with template-label that resides in the cell slot
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testExportDesignToNewLibraryFile() throws Exception {
		testExportDesignToNewLibraryFile("ElementExporterTest.xml", //$NON-NLS-1$
				"ElementExporterTestLibrary_out_12.xml"); //$NON-NLS-1$

		assertTrue(compareFile("ElementExporterTestLibrary_golden_12.xml", //$NON-NLS-1$
				"ElementExporterTestLibrary_out_12.xml")); //$NON-NLS-1$

		String libraryFile = getTempFolder() + OUTPUT_FOLDER + "ElementExporterTestLibrary_out_12.xml"; //$NON-NLS-1$

		ReportDesignHandle newDesign = sessionHandle.createDesign();
		ElementFactory factory = newDesign.getElementFactory();

		// duplicate master page names.
		newDesign.getMasterPages().add(factory.newGraphicMasterPage("My Page")); //$NON-NLS-1$

		try {
			// export with duplicate names.

			ElementExportUtil.exportDesign(newDesign, libraryFile, false, true);
			fail();
		} catch (NameException e) {
		}

		newDesign.getMasterPages().drop(0);
		newDesign.getBody().add(factory.newLabel("")); //$NON-NLS-1$

		try {
			// export element without a name.

			ElementExportUtil.exportDesign(newDesign, libraryFile, true, false);
			fail();
		} catch (IllegalArgumentException e) {
		}

		// library cannot have template label

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest1.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_13.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());

			assertTrue(e.getElement() instanceof Library);
		}

		// library cannot have template data set

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest2.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_14.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());

			assertTrue(e.getElement() instanceof Library);
		}

		// cell with template label cannot be exported to library

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest3.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_15.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT, e.getErrorCode());
			assertTrue(e.getElement() instanceof Cell);
		}

		// library cannot have template label

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest1.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_13.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());

			assertTrue(e.getElement() instanceof Library);
		}

		// library cannot have template data set

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest2.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_14.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());

			assertTrue(e.getElement() instanceof Library);
		}

		// cell with template label cannot be exported to library

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest3.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_15.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT, e.getErrorCode());
			assertTrue(e.getElement() instanceof Cell);
		}

		// library cannot have template label

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest1.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_13.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());

			assertTrue(e.getElement() instanceof Library);
		}

		// library cannot have template data set

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest2.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_14.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());

			assertTrue(e.getElement() instanceof Library);
		}

		// cell with template label cannot be exported to library

		try {
			testExportDesignToNewLibraryFile("ElementExporterTest3.xml", //$NON-NLS-1$
					"ElementExporterTestLibrary_out_15.xml"); //$NON-NLS-1$
			fail();
		} catch (ContentException e) {
			assertEquals(ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT, e.getErrorCode());
			assertTrue(e.getElement() instanceof Cell);
		}

	}

	/**
	 * test to export a design handle to a given library file.
	 *
	 * @param inputFile
	 * @param outputFile
	 * @throws Exception
	 */

	private void testExportDesignToNewLibraryFile(String inputFile, String outputFile) throws Exception {
		openDesign(inputFile, ULocale.ENGLISH);
		String libraryDir = getTempFolder() + OUTPUT_FOLDER;
		File file = new File(libraryDir);
		if (!file.exists()) {
			file.mkdirs();
		}

		String libraryFile = libraryDir + outputFile;
		file = new File(libraryFile);
		if (file.exists()) {
			file.delete();
		}

		ElementExportUtil.exportDesign(designHandle, libraryFile, true, true);
	}

	/**
	 * Tests exporting one label with user property.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingOneLabelWithUserProperty() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label5"); //$NON-NLS-1$

		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_11.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests char is exported with a significative name.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingExtendItemWithSignificativeName() throws Exception {
		// new PeerExtensionLoader( ).load( );

		testExportDesignToNewLibraryFile("ElementExporterTest_4.xml", //$NON-NLS-1$
				"ElementExporterTestLibrary_out_13.xml"); //$NON-NLS-1$
		assertTrue(compareFile("ElementExporterTestLibrary_golden_13.xml", //$NON-NLS-1$
				"ElementExporterTestLibrary_out_13.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting EmbeddedImage to library file.
	 *
	 * @throws Exception if any exception
	 */

	public void testExportingEmbeddedImage() throws Exception {
		openDesign("DesignUsesLibraryEmbeddedImage.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		PropertyHandle propHandle = designHandle.getPropertyHandle(Module.IMAGES_PROP);
		EmbeddedImageHandle embeddedImageHandle = (EmbeddedImageHandle) propHandle.getAt(0);

		ElementExportUtil.exportStructure(embeddedImageHandle, libraryHandle, true);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_14.xml")); //$NON-NLS-1$
	}

	/**
	 * Test 'hasLibrary' method in <code>ModelUtil</code> contains in exportDesign
	 * method in <code>ElementExportUtil</code>.
	 *
	 * <ul>
	 * <li>test relative path. Report Design and library have the same absolute
	 * path</li>
	 * <li>test relative paht. Report Design and library haven't the same absolut
	 * path</li>
	 * <li>test relative path. Report Design and library have the same absolute
	 * path</li>
	 * <li></li>
	 * </ul>
	 *
	 * @throws Exception if any exception
	 */

	public void testHasLibrary() throws Exception {
		openDesign("ModelUtilTest_hasContainLibrary.xml"); //$NON-NLS-1$

		openLibrary("Containlibrary.xml");//$NON-NLS-1$
		try {
			ElementExportUtil.exportDesign(designHandle, libraryHandle, true, true);
			fail();

		} catch (SemanticException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY, e.getErrorCode());
		}

		libraryHandle.setFileName("notcontainlibrary.xml");//$NON-NLS-1$
		LibraryUtil.hasLibrary(designHandle, libraryHandle);
		libraryHandle.close();

		openLibrary("RelativeContainlibrary.xml");//$NON-NLS-1$
		try {
			ElementExportUtil.exportDesign(designHandle, libraryHandle, true, true);
			fail();

		} catch (SemanticException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY, e.getErrorCode());
		}

	}

	/**
	 * Tests exportDesign method
	 *
	 * @throws Exception
	 */

	public void testExportDesign() throws Exception {
		openDesign("ElementExporterTest_6.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		DataItemHandle itemHandle = (DataItemHandle) designHandle.getElementByID(5);
		assertNull(itemHandle.getName());

		ElementExportUtil.exportDesign(designHandle, libraryHandle, true, true);

		itemHandle = (DataItemHandle) libraryHandle.findElement("NewData1");//$NON-NLS-1$
		assertEquals(1, itemHandle.getListProperty("boundDataColumns").size());//$NON-NLS-1$
		itemHandle = (DataItemHandle) libraryHandle.findElement("NewData");//$NON-NLS-1$

		assertNotNull(libraryHandle.findCube("testCube")); //$NON-NLS-1$

	}

	/**
	 * When export property binding , should change 'id' property. See bugzilla
	 * 198076
	 *
	 * @throws Exception
	 */

	public void testExportPropertyBinding() throws Exception {
		openDesign("ElementExporterTest_PropertyBinding.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		ElementExportUtil.exportDesign(designHandle, libraryHandle, false, false);

		DataSetHandle dsHandle = (DataSetHandle) libraryHandle.getDataSets().get(0);
		long id = dsHandle.getID();
		List propertyBindings = libraryHandle.getListProperty(ReportDesignHandle.PROPERTY_BINDINGS_PROP);
		PropertyBinding propBinding = (PropertyBinding) propertyBindings.get(0);
		assertEquals(id, propBinding.getID().longValue());

		DataSetHandle designDsHandle = (DataSetHandle) designHandle.getDataSets().get(0);
		assertFalse(designDsHandle.getID() == id);
	}

	/**
	 *
	 * test to export the element type property correctly. The value of the element
	 * type property is an element.
	 *
	 * @throws Exception
	 *
	 */

	public void testExportXtabFromLibrary() throws Exception {

		openDesign("ExportXtab.xml"); //$NON-NLS-1$
		openLibrary("Library_5.xml"); //$NON-NLS-1$

		ElementExportUtil.exportDesign(designHandle, libraryHandle, true, true);

		save(libraryHandle);

		assertTrue(compareFile("ElementExporterTestLibrary_golden_15.xml")); //$NON-NLS-1$
	}

	/**
	 * Test the function that whether an element/structure can be exported.
	 *
	 * @throws Exception
	 */

	public void testCanExport() throws Exception {
		openDesign("ElementExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		DataSetHandle ds = designHandle.findDataSet("dataSet1"); //$NON-NLS-1$

		assertTrue(ElementExportUtil.canExport(ds, libraryHandle, false));
		assertTrue(ElementExportUtil.canExport(ds));

		// if add a dataset1 to the library, cannot export
		DataSetHandle tmpDs = libraryHandle.getElementFactory().newScriptDataSet("dataSet1"); //$NON-NLS-1$
		libraryHandle.getDataSets().add(tmpDs);

		assertFalse(ElementExportUtil.canExport(ds, libraryHandle, false));
		assertTrue(ElementExportUtil.canExport(ds, libraryHandle, true));
		assertTrue(ElementExportUtil.canExport(ds));

		StyleHandle style1 = designHandle.findStyle("style1"); //$NON-NLS-1$
		assertFalse(ElementExportUtil.canExport(style1, libraryHandle, false));
		assertTrue(ElementExportUtil.canExport(style1));

		// group cannot be exported.

		TableHandle table = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		GroupHandle group = (GroupHandle) table.getGroups().get(0);
		assertFalse(ElementExportUtil.canExport(group, libraryHandle, false));
		assertFalse(ElementExportUtil.canExport(group));

		CustomColorHandle color1 = (CustomColorHandle) designHandle.customColorsIterator().next();

		assertTrue(ElementExportUtil.canExport(color1, libraryHandle, false));
		assertTrue(ElementExportUtil.canExport(color1));

		// if add a custom color with "customColor1" into library, cannot export
		CustomColor tmpColor1 = StructureFactory.createCustomColor();
		tmpColor1.setName("customColor1"); //$NON-NLS-1$
		libraryHandle.getPropertyHandle(IModuleModel.COLOR_PALETTE_PROP).addItem(tmpColor1);

		assertFalse(ElementExportUtil.canExport(color1, libraryHandle, false));
		assertTrue(ElementExportUtil.canExport(color1, libraryHandle, true));
		assertTrue(ElementExportUtil.canExport(color1));

	}

	/**
	 * Tests export design successful and the element in top level in design file
	 * should not be modified.
	 *
	 * @throws Exception
	 */
	public void testExportLabelWithoutNameToLib() throws Exception {
		openDesign("ExportLabelToLibTest.xml"); //$NON-NLS-1$
		openLibrary("ExportLabelToLibTestLibrary.xml"); //$NON-NLS-1$

		// tests export label without name to library

		LabelHandle labelHandle = (LabelHandle) designHandle.getElementByID(6);

		assertNull(labelHandle.getName());
		ElementExportUtil.exportElement(labelHandle, libraryHandle, false);
		assertNull(labelHandle.getName());

		// tests export label in grid to library, the name of exported elements
		// in report are not modified.

		GridHandle gridHandle = (GridHandle) designHandle.getElementByID(25);
		LabelHandle labelWithoutName = (LabelHandle) designHandle.getElementByID(41);
		LabelHandle labelWithName = (LabelHandle) designHandle.getElementByID(43);

		assertNull(gridHandle.getName());
		assertNull(labelWithoutName.getName());
		assertNotNull(labelWithName.getName());

		ElementExportUtil.exportElement(gridHandle, libraryHandle, false);

		assertNull(gridHandle.getName());
		assertNull(labelWithoutName.getName());
		assertNotNull(labelWithName.getName());

		// tests library has some element with the same name as one or more
		// content elements in grid
		gridHandle = (GridHandle) designHandle.getElementByID(50);
		try {
			ElementExportUtil.exportElement(gridHandle, libraryHandle, false);
			fail();

		} catch (NameException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE, e.getErrorCode());
		}

		// tests library has some element with the same name as that of the grid
		// itself
		gridHandle = (GridHandle) designHandle.getElementByID(60);
		try {
			ElementExportUtil.exportElement(gridHandle, libraryHandle, false);
			fail();

		} catch (NameException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE, e.getErrorCode());
		}

		save(libraryHandle);

		assertTrue(compareFile("ExportLableToLibTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests export design successful and the element in library with the same name
	 * should be drop.
	 *
	 * @throws Exception
	 */
	public void testDropDuplicatedElement() throws Exception {
		openDesign("DropDuplicatedElementTest.xml"); //$NON-NLS-1$
		openLibrary("DropDuplicatedElementTestLibrary.xml"); //$NON-NLS-1$

		DesignElementHandle rptElement_a = designHandle.findElement("a");//$NON-NLS-1$
		DesignElementHandle rptElement_b = designHandle.findElement("b");//$NON-NLS-1$
		assertNotNull(rptElement_a.getRoot());
		assertNotNull(rptElement_b.getRoot());

		// get tableHandle in design
		DesignElementHandle tableHandle = designHandle.getElementByID(7);

		// gets the elements (libElement_a, libElement_b)in library with the
		// same name a and b
		DesignElementHandle libElement_a = libraryHandle.findElement("a");//$NON-NLS-1$
		DesignElementHandle libElement_b = libraryHandle.findElement("b");//$NON-NLS-1$

		assertNotNull(libElement_a.getRoot());
		assertNotNull(libElement_b.getRoot());

		// export table
		ElementExportUtil.exportElement(tableHandle, libraryHandle, true);

		// test the old elements are dropped and the new elements are added.
		assertNull(libElement_a.getRoot());
		assertNull(libElement_b.getRoot());

		libElement_a = libraryHandle.findElement("a");//$NON-NLS-1$
		libElement_b = libraryHandle.findElement("b");//$NON-NLS-1$

		assertNotNull(libElement_a.getRoot());
		assertNotNull(libElement_b.getRoot());

	}

	/**
	 * Test the function(canExport in IReportItem) that whether if extended item can
	 * be exported to library
	 *
	 * @throws Exception
	 */

	public void testCanExportExtendedItem() throws Exception {
		openDesign("ExtendedItemExporterTest.xml"); //$NON-NLS-1$
		openLibrary("ExtendedItemExporterTestLibrary.xml"); //$NON-NLS-1$

		// tests extended item which can be exported.
		DesignElementHandle handle = designHandle.findElement("action1");//$NON-NLS-1$
		assertTrue(ElementExportUtil.canExport(handle, libraryHandle, true));
		assertTrue(ElementExportUtil.canExport(handle, true));

		// tests extended item which can not be exported.
		handle = designHandle.findElement("testBox");//$NON-NLS-1$
		assertFalse(ElementExportUtil.canExport(handle, libraryHandle, true));
		assertFalse(ElementExportUtil.canExport(handle, true));
	}

	/**
	 * Tests the design exporting including table that defines data-binding-ref.
	 *
	 * @throws Exception
	 */
	public void testExportTableWithBindingRef() throws Exception {
		openDesign("ElementExporterTest_1.xml"); //$NON-NLS-1$
		openLibrary("ExportLabelToLibTestLibrary.xml"); //$NON-NLS-1$

		ElementExportUtil.exportDesign(designHandle, libraryHandle, true, false);
		save(libraryHandle);

		assertTrue(compareFile("ElementExporterTestLibrary_golden_16.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests export master page.
	 *
	 * @throws Exception
	 */
	public void testExportMasterPage() throws Exception {
		openDesign("ExportMasterPageTest.xml"); //$NON-NLS-1$
		openLibrary("ExportMasterPageTestLibrary.xml"); //$NON-NLS-1$

		// test export master page to library which has the same master page
		// name.
		DesignElementHandle handle = designHandle.findMasterPage("NewSimpleMasterPage");//$NON-NLS-1$
		assertFalse(ElementExportUtil.canExport(handle, libraryHandle, false));

		// test export master page to library which does not have the same
		// master page name.
		handle = designHandle.findMasterPage("Simple MasterPage");//$NON-NLS-1$
		assertTrue(ElementExportUtil.canExport(handle, libraryHandle, false));

		ElementExportUtil.exportElement(handle, libraryHandle, true);
		save(libraryHandle);

		assertTrue(compareFile("ExportMasterPageTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests export style element.
	 *
	 * @throws Exception
	 */
	public void testExportStyle() throws Exception {
		openDesign("ExportStyleTest.xml"); //$NON-NLS-1$
		openLibrary("ExportStyleTestLibrary.xml"); //$NON-NLS-1$

		// test export style to library which has the same style name.

		DesignElementHandle handle = designHandle.findStyle("crosstab"); //$NON-NLS-1$
		assertFalse(ElementExportUtil.canExport(handle, libraryHandle, false));

		// test export style to library which does not have the same style name.

		handle = designHandle.findStyle("report"); //$NON-NLS-1$
		assertTrue(ElementExportUtil.canExport(handle, libraryHandle, false));

		ElementExportUtil.exportElement(handle, libraryHandle, true);
		save(libraryHandle);

		assertTrue(compareFile("ExportStyleTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests export style element to specified theme.
	 *
	 * @throws Exception
	 */
	public void testExportStyleToTheme() throws Exception {
		openDesign("ExportStyleToThemeTest.xml"); //$NON-NLS-1$
		openLibrary("ExportStyleToThemeTestLibrary.xml"); //$NON-NLS-1$

		ThemeHandle theme = libraryHandle.findTheme("theme1"); //$NON-NLS-1$

		// tests export style to theme which has the same style name.
		StyleHandle style = designHandle.findStyle("style1"); //$NON-NLS-1$
		assertFalse(ElementExportUtil.canExport(style, theme, false));
		assertTrue(ElementExportUtil.canExport(style, theme, true));

		style = designHandle.findStyle("report"); //$NON-NLS-1$
		ElementExportUtil.exportStyle(style, theme, true);

		save(libraryHandle);

		assertTrue(compareFile("ExportStyleToThemeTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests export expression values.
	 *
	 * @throws Exception
	 */

	public void testExportExpressionValues() throws Exception {
		openDesign("ExportExpressionValuesTest.xml"); //$NON-NLS-1$
		openLibrary("ElementExporterTestLibrary.xml"); //$NON-NLS-1$

		ElementExportUtil.exportDesign(designHandle, libraryHandle, false, false);
		save(libraryHandle);
		assertTrue(compareFile("ExportExpressionValuesTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests export Xtab to library. Both the report and library have Xtab.The
	 * report's Xtab contains element with same names as the element locates in
	 * library's Xtab. The report's Xtab can not be exported to the library.
	 *
	 * @throws Exception
	 */
	public void testExportXtabWithDuplicatedElementName() throws Exception {
		openDesign("ExportXtabWithDuplicatedElementNameTest.xml"); //$NON-NLS-1$
		openLibrary("ExportXtabWithDuplicatedElementNameTestLibrary.xml"); //$NON-NLS-1$

		// some item in the cube has duplicate name with that in the library :
		// if override is false, we throw exception; otherwise rename the
		// duplicate element, which will cause some binding invalid and user
		// have to revise them manually
		DesignElementHandle handle = designHandle.findCube("Cube"); //$NON-NLS-1$
		assertTrue(ElementExportUtil.canExport(handle, libraryHandle, true));
		assertFalse(ElementExportUtil.canExport(handle, libraryHandle, false));

		handle = designHandle.findElement("table"); //$NON-NLS-1$
		assertTrue(ElementExportUtil.canExport(handle, libraryHandle, true));
		assertFalse(ElementExportUtil.canExport(handle, libraryHandle, false));

		handle = designHandle.findElement("table1"); //$NON-NLS-1$
		assertFalse(ElementExportUtil.canExport(handle, libraryHandle, true));
		assertFalse(ElementExportUtil.canExport(handle, libraryHandle, false));
		try {
			ElementExportUtil.exportElement(handle, libraryHandle, true);
			fail();
		} catch (SemanticException e) {

			assertEquals(SemanticException.DESIGN_EXCEPTION__EXPORT_ELEMENT_FAIL, e.getErrorCode());
			assertEquals(
					"Duplicated element name in target library, the element NewMeasure View1 can not be exported. Please rename the exported element name.", //$NON-NLS-1$
					e.getLocalizedMessage());
		}
	}

	public void testExportCubeWithDuplicatedName() throws Exception {
		openDesign("ExportCubeWithDuplicatedNameTest.xml"); //$NON-NLS-1$
		openLibrary("ExportCubeWithDuplicatedNameTestLib.xml"); //$NON-NLS-1$

		DesignElementHandle cube = designHandle.findCube("Cube"); //$NON-NLS-1$
		assertTrue(ElementExportUtil.canExport(cube, libraryHandle, true));
		assertFalse(ElementExportUtil.canExport(cube, libraryHandle, false));

		ElementExportUtil.exportElement(cube, libraryHandle, true);
		save(libraryHandle);
		assertTrue(compareFile("ExportCubeWithDuplicatedNameTestLib_golden.xml")); //$NON-NLS-1$
	}

	public void testExportCubeWithDuplicatedDimensionName() throws Exception {
		openDesign("ExportCubeWithDuplicatedNameTest.xml"); //$NON-NLS-1$
		openLibrary("ExportCubeWithDuplicatedNameTestLib_1.xml"); //$NON-NLS-1$

		DesignElementHandle cube = designHandle.findCube("Cube"); //$NON-NLS-1$
		assertTrue(ElementExportUtil.canExport(cube, libraryHandle, true));
		assertFalse(ElementExportUtil.canExport(cube, libraryHandle, false));

		assertTrue(ElementExportUtil.canExport(cube, libraryHandle, true));
		assertFalse(ElementExportUtil.canExport(cube, libraryHandle, false));

		ElementExportUtil.exportElement(cube, libraryHandle, true);
		save(libraryHandle);
		assertTrue(compareFile("ExportCubeWithDuplicatedNameTestLib_1_golden.xml")); //$NON-NLS-1$

		openLibrary("ExportCubeWithDuplicatedNameTestLib_2.xml"); //$NON-NLS-1$
		assertTrue(ElementExportUtil.canExport(cube, libraryHandle, true));
		assertFalse(ElementExportUtil.canExport(cube, libraryHandle, false));

		ElementExportUtil.exportElement(cube, libraryHandle, true);
		save(libraryHandle);
		assertTrue(compareFile("ExportCubeWithDuplicatedNameTestLib_2_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests exporting table with a theme to library file.
	 *
	 * @throws Exception if any exception.
	 */

	public void testExportingTableWithTheme() throws Exception {
		openDesign("ElementExporterTest4.xml"); //$NON-NLS-1$
		createLibrary();

		TableHandle tableHandle = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$

		ElementExportUtil.exportElement(tableHandle, libraryHandle, false);

		saveLibrary();
		assertTrue(compareFile("ElementExporterTestLibrary_golden_17.xml")); //$NON-NLS-1$
	}
}
