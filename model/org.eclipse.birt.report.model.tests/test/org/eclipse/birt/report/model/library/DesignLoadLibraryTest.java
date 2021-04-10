/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.CustomColorHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.IncludedLibraryHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests whether report design can handle the cases of loading libraires.
 */

public class DesignLoadLibraryTest extends BaseTestCase {

	/**
	 * Tests one report design including an inexistent library.
	 * 
	 * And test status of elements that extends library elements.
	 * 
	 * @throws Exception if any exception
	 */

	public void testLoadDesignWithInexistentLibrary() throws Exception {
		openDesign("DesignWithInexistentLibrary.xml"); //$NON-NLS-1$

		List libraries = designHandle.getAllLibraries();
		assertEquals(1, libraries.size());
		LibraryHandle libHandle = (LibraryHandle) libraries.get(0);
		assertFalse(libHandle.isValid());
		assertEquals("inexistentLibrary.xml", libHandle.getRelativeFileName()); //$NON-NLS-1$
		assertEquals(DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND,
				((ErrorDetail) libHandle.getErrorList().get(0)).getErrorCode());

		ReportElementHandle element = (ReportElementHandle) designHandle.findElement("myText"); //$NON-NLS-1$
		assertTrue(element.isValidLayoutForCompoundElement());

		element = (ReportElementHandle) designHandle.findElement("myGrid"); //$NON-NLS-1$
		assertFalse(element.isValidLayoutForCompoundElement());

		element = (ReportElementHandle) designHandle.findElement("myTable"); //$NON-NLS-1$
		assertFalse(element.isValidLayoutForCompoundElement());

		element = (ReportElementHandle) designHandle.findElement("myTable1"); //$NON-NLS-1$
		assertFalse(element.isValidLayoutForCompoundElement());

	}

	/**
	 * Tests one report design including an invalid library.
	 * 
	 * @throws Exception if any exception
	 */

	public void testLoadDesignWithInvalidLibrary() throws Exception {
		openDesign("DesignWithInvalidLibrary.xml"); //$NON-NLS-1$

		List libraries = designHandle.getAllLibraries();
		assertEquals(1, libraries.size());
		assertFalse(((LibraryHandle) libraries.get(0)).isValid());
	}

	/**
	 * Tests one report design including one library with semantic errors.
	 * 
	 * @throws Exception if any exception
	 */

	public void testLoadDesignWithSemanticErrorLibrary() throws Exception {
		// In this file, structure list validator ( design, library) and master
		// page required validator ( design ) are tested.

		openDesign("DesignWithSemanticErrorLibrary.xml"); //$NON-NLS-1$

		List libraries = designHandle.getAllLibraries();
		assertEquals(1, libraries.size());
		LibraryHandle oneLibraryHandle = (LibraryHandle) libraries.get(0);
		assertTrue(oneLibraryHandle.isValid());
		List errorList = oneLibraryHandle.getErrorList();
		assertEquals(1, errorList.size());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
				((ErrorDetail) errorList.get(0)).getErrorCode());
	}

	/**
	 * Tests one report design including two library with same namespace.
	 * 
	 * @throws Exception if any exception
	 */

	public void testLoadDesignWithDuplicateNamespace() throws Exception {
		try {
			openDesign("DesignWithDuplicateNamespace.xml"); //$NON-NLS-1$

			fail();
		} catch (DesignFileException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE,
					((ErrorDetail) e.getErrorList().get(0)).getErrorCode());
		}

		// this is an allowed case.

		openDesign("DesignWithDuplicateNamespace1.xml"); //$NON-NLS-1$

	}

	/**
	 * Tests extended element with duplicate name in design file and library file.
	 * Now master page just support grid , so just test gird which contains slot
	 * handle.
	 * 
	 * @throws Exception if any exception
	 */

	public void testLoadDesignWithDuplicateNames() throws Exception {
		try {
			openDesign("DesignWithDuplicatedNameLibrary.xml"); //$NON-NLS-1$
			assertTrue(true);
		} catch (DesignFileException e) {
			fail("Open DesignWithDuplicatedNameLibrary.xml Error: " + e.getMessage());//$NON-NLS-1$
		}

		SimpleMasterPageHandle masterPageHandle = (SimpleMasterPageHandle) designHandle.getElementByID(74);
		SimpleMasterPageHandle parentHandle = (SimpleMasterPageHandle) masterPageHandle.getExtends();

		DesignElementHandle handle = parentHandle.getPageHeader().get(0);
		assertEquals("NewGrid", handle.getName()); //$NON-NLS-1$

		handle = masterPageHandle.getPageHeader().get(0);
		assertEquals("NewGrid211", handle.getName());//$NON-NLS-1$

		GridHandle gridHandle = (GridHandle) designHandle.getBody().get(2);
		assertEquals("NewGrid", gridHandle.getName()); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle.getBody().get(1);
		assertEquals("label", labelHandle.getName()); //$NON-NLS-1$

		assertEquals("label", labelHandle.getExtends().getName()); //$NON-NLS-1$
	}

	/**
	 * A design include the same library twice.
	 * 
	 * @throws DesignFileException
	 */

	public void testLoadDesignWithSameLibraryFiles() throws DesignFileException {
		openDesign("DesignWithSameLibraryFiles.xml"); //$NON-NLS-1$
		List errors = designHandle.getErrorList();
		assertEquals(1, errors.size());

		ErrorDetail error1 = (ErrorDetail) errors.get(0);
		assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED, error1.getErrorCode());
	}

	/**
	 * Tests one report design includes three libraries.
	 * 
	 * @throws Exception if any exception
	 */

	public void testLoadDesignWithThreeLibrary() throws Exception {
		openDesign("DesignWithThreeLibrary.xml"); //$NON-NLS-1$

		List libraries = designHandle.getAllLibraries();
		assertEquals(3, libraries.size());

		LibraryHandle libraryHandle1 = (LibraryHandle) libraries.get(0);
		LibraryHandle libraryHandle2 = (LibraryHandle) libraries.get(1);
		assertTrue(libraryHandle1.isValid());
		assertTrue(libraryHandle2.isValid());

		PropertyHandle propHandle = designHandle.getPropertyHandle(ReportDesignHandle.LIBRARIES_PROP);
		ArrayList list = propHandle.getListValue();
		assertEquals(3, list.size());

		IncludedLibrary includeLibrary = (IncludedLibrary) list.get(0);
		assertEquals("Library_1.xml", includeLibrary.getFileName()); //$NON-NLS-1$
		assertEquals("Lib1", includeLibrary.getNamespace()); //$NON-NLS-1$
		assertEquals(includeLibrary.getFileName(), libraryHandle1.getRelativeFileName());

		includeLibrary = (IncludedLibrary) list.get(1);
		assertEquals("Library_2.xml", includeLibrary.getFileName()); //$NON-NLS-1$
		assertEquals("Library_2", includeLibrary.getNamespace()); //$NON-NLS-1$
		assertEquals(includeLibrary.getFileName(), libraryHandle2.getRelativeFileName());

		includeLibrary = (IncludedLibrary) list.get(2);
		assertEquals("Library_3.xml", includeLibrary.getFileName()); //$NON-NLS-1$
		assertEquals("Lib3", includeLibrary.getNamespace()); //$NON-NLS-1$

		assertEquals("W.C. Fields", libraryHandle2.getStringProperty(Library.AUTHOR_PROP)); //$NON-NLS-1$
		assertEquals("http://company.com/reportHelp.html", libraryHandle2.getStringProperty(Library.HELP_GUIDE_PROP)); //$NON-NLS-1$
		assertEquals("Whiz-Bang Plus", libraryHandle2.getStringProperty(Library.CREATED_BY_PROP)); //$NON-NLS-1$

		// title

		assertEquals("TITLE_ID", libraryHandle2.getStringProperty(Library.TITLE_ID_PROP)); //$NON-NLS-1$
		assertEquals("Sample Report", libraryHandle2.getStringProperty(Library.TITLE_PROP)); //$NON-NLS-1$

		// comments

		assertEquals("First sample report.", libraryHandle2.getStringProperty(Library.COMMENTS_PROP)); //$NON-NLS-1$

		// description

		assertEquals("DESCRIP_ID", libraryHandle2.getStringProperty(Library.DESCRIPTION_ID_PROP)); //$NON-NLS-1$
		assertEquals("This is a first sample report.", libraryHandle2.getStringProperty(Library.DESCRIPTION_PROP)); //$NON-NLS-1$

		// color-palette

		PropertyHandle colorPalette = libraryHandle2.getPropertyHandle(Library.COLOR_PALETTE_PROP);
		List colors = colorPalette.getListValue();
		assertEquals(2, colors.size());
		CustomColor color = (CustomColor) colors.get(0);
		assertEquals("cus red", color.getName()); //$NON-NLS-1$
		assertEquals(111, color.getRGB());
		assertEquals("cus red key", color.getDisplayNameID()); //$NON-NLS-1$
		assertEquals("cus red display", color.getDisplayName()); //$NON-NLS-1$
		color = (CustomColor) colors.get(1);
		assertEquals("cus blue", color.getName()); //$NON-NLS-1$
		assertEquals(222, color.getRGB());
		assertEquals("cus blue key", color.getDisplayNameID()); //$NON-NLS-1$

		// config-vars
		PropertyHandle configVarHandle = libraryHandle2.getPropertyHandle(Library.CONFIG_VARS_PROP);
		List configVars = configVarHandle.getListValue();
		assertEquals(4, configVars.size());
		ConfigVariable var = (ConfigVariable) configVars.get(0);
		assertEquals("var1", var.getName()); //$NON-NLS-1$
		assertEquals("mumble.jpg", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(1);
		assertEquals("var2", var.getName()); //$NON-NLS-1$
		assertEquals("abcdefg", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(2);
		assertEquals("var3", var.getName()); //$NON-NLS-1$
		assertEquals("", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(3);
		assertEquals("var4", var.getName()); //$NON-NLS-1$
		assertEquals(null, var.getValue());

		// images
		PropertyHandle imageHandle = libraryHandle2.getPropertyHandle(Library.IMAGES_PROP);
		List images = imageHandle.getListValue();
		assertEquals(3, images.size());
		EmbeddedImage image = (EmbeddedImage) images.get(0);
		assertEquals("image1", image.getName()); //$NON-NLS-1$
		assertEquals("image/bmp", image.getType(design)); //$NON-NLS-1$
		assertEquals("imagetesAAA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(design))).substring(0, 11));

		image = (EmbeddedImage) images.get(1);
		assertEquals("image2", image.getName()); //$NON-NLS-1$
		assertEquals("image/gif", image.getType(design)); //$NON-NLS-1$
		assertEquals("/9j/4AAQSkZJRgA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(design))).substring(0, 15));

		image = (EmbeddedImage) images.get(2);
		assertEquals("image3", image.getName()); //$NON-NLS-1$
		assertEquals("image/bmp", image.getType(design)); //$NON-NLS-1$
		assertEquals("AAAA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(design))));
	}

	/**
	 * Tests the style resolution.
	 * 
	 * @throws Exception if any exception
	 */

	public void testVisibleElements() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		assertEquals(3, designHandle.getAllStyles().size());
		assertEquals(6, designHandle.getAllDataSources().size());
		assertEquals(3, designHandle.getVisibleDataSets().size());
		assertEquals(4, designHandle.getAllDataSets().size());
		assertEquals(3, designHandle.getVisibleDataSets().size());
	}

	/**
	 * Tests all operations of finding or getting elements in module name space or
	 * name space.
	 * 
	 * @throws Exception if any exception
	 */

	public void testFindAndNativeFind() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		List libraries = designHandle.getAllLibraries();
		assertTrue(((LibraryHandle) libraries.get(0)).isValid());
		assertTrue(((LibraryHandle) libraries.get(1)).isValid());
		assertTrue(((LibraryHandle) libraries.get(2)).isValid());

		// Test finding and native finding for report item.

		assertNotNull(designHandle.findElement("label1")); //$NON-NLS-1$
		assertNull(designHandle.findElement("LibA.libLabel1")); //$NON-NLS-1$

		// Test finding and native finding for style.

		assertNotNull(designHandle.findNativeStyle("style1")); //$NON-NLS-1$
		assertNotNull(designHandle.findStyle("style1")); //$NON-NLS-1$
		assertNotNull(designHandle.findNativeStyle("style2")); //$NON-NLS-1$
		assertNotNull(designHandle.findStyle("style2")); //$NON-NLS-1$
		assertNull(designHandle.findNativeStyle("style3")); //$NON-NLS-1$
		assertNotNull(designHandle.findStyle("style3")); //$NON-NLS-1$

		List elements = designHandle.getAllStyles();
		assertEquals(new String[] { "style1", "style3", "style2" }, getNameArray(elements)); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

		// Test finding and native finding for data source.

		assertNotNull(designHandle.findDataSource("LibA.dataSource1")); //$NON-NLS-1$
		assertNotNull(designHandle.findDataSource("dataSource2")); //$NON-NLS-1$

		elements = designHandle.getVisibleDataSources();
		assertEquals(new String[] { "dataSource1", "dataSource2", "dataSource3" }, getNameArray(elements)); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

		elements = designHandle.getAllDataSources();
		assertEquals(new String[] { "dataSource1", "dataSource2", "dataSource3", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				"dataSource1", "dataSource1", "dataSource1" }, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				getNameArray(elements));

		// Test finding and native finding for data set.

		assertNotNull(designHandle.findDataSet("LibA.dataSet1")); //$NON-NLS-1$
		assertNotNull(designHandle.findDataSet("dataSet2")); //$NON-NLS-1$

		elements = designHandle.getVisibleDataSets();
		assertEquals(new String[] { "dataSet2", "dataSet3", "dataSet4" }, getNameArray(elements)); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

		elements = designHandle.getVisibleDataSets();
		assertEquals(new String[] { "dataSet2", "dataSet3", "dataSet4" }, getNameArray(elements)); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

	}

	/**
	 * Test cases:
	 * 
	 * Includes a library without given namespace, a default namespace will be
	 * generated.
	 * 
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	public void testInludeLibraryWithoutGivenNamespace() throws DesignFileException, SemanticException {
		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library_1.xml", null); //$NON-NLS-1$

		List list = designHandle.getLibraries();
		assertEquals(1, list.size());

		LibraryHandle lib = (LibraryHandle) list.get(0);
		assertEquals("Library_1", lib.getNamespace()); //$NON-NLS-1$
	}

	/**
	 * Returns an string array containing element names with the given element list.
	 * 
	 * @param list a list containg elements
	 * @return an string array containing element names
	 * 
	 */

	private String[] getNameArray(List list) {
		assert list != null;

		String[] elementNames = new String[list.size()];

		int i = 0;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			DesignElementHandle element = (DesignElementHandle) iter.next();

			elementNames[i++] = element.getName();
		}

		return elementNames;
	}

	/**
	 * @param strings1
	 * @param strings2
	 */
	private void assertEquals(String[] strings1, String[] strings2) {
		assertEquals(strings1.length, strings2.length);

		for (int i = 0; i < strings1.length; i++) {
			assertEquals(strings1[i], strings2[i]);
		}
	}

	/**
	 * Tests the style resolution.
	 * 
	 * @throws Exception if any exception
	 */

	public void testStyleResolver() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		// Test whether one desing label can refer one library style.

		LabelHandle label = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		assertEquals("style3", label //$NON-NLS-1$
				.getStringProperty(LabelHandle.STYLE_PROP));
		StyleHandle libStyleHandle = label.getStyle();
		assertNotNull(libStyleHandle);
		assertEquals("style3", libStyleHandle.getName()); //$NON-NLS-1$

		// Add one design style overriding the library one, and the design label
		// should refer the library one.

		StyleHandle designStyleHandle = designHandle.getElementFactory().newStyle("style3"); //$NON-NLS-1$
		designHandle.getStyles().add(designStyleHandle);
		assertEquals(designStyleHandle, label.getStyle());

		// Remove the design style, and the design label should refer the
		// library one.

		designHandle.getStyles().drop(designStyleHandle);
		assertEquals(libStyleHandle, label.getStyle());

		// Undo it

		designHandle.getCommandStack().undo();
		assertEquals(designStyleHandle, label.getStyle());

		// Undo again.

		designHandle.getCommandStack().undo();
		assertEquals(libStyleHandle, label.getStyle());

		// Redo

		designHandle.getCommandStack().redo();
		assertEquals(designStyleHandle, label.getStyle());

		// Drop the design style, and the design label should refer the library
		// one.

		designStyleHandle.drop();
		assertEquals(libStyleHandle, label.getStyle());

		// Add the style3 again in design file.

		designStyleHandle = designHandle.getElementFactory().newStyle("style3"); //$NON-NLS-1$
		designHandle.getStyles().add(designStyleHandle);
		assertEquals(designStyleHandle, label.getStyle());

		// Rename the style in design file.

		designStyleHandle.setName("newStyle"); //$NON-NLS-1$
		assertEquals("newStyle", label.getStyle().getName()); //$NON-NLS-1$

		// Undo

		designHandle.getCommandStack().undo();
		assertEquals("style3", label.getStyle().getName()); //$NON-NLS-1$

		// Redo

		designHandle.getCommandStack().redo();
		assertEquals("newStyle", label.getStyle().getName()); //$NON-NLS-1$

		// Drop the style from design file.

		designStyleHandle.drop();
		assertNull(label.getStyle());
	}

	/**
	 * Tests the data source and data set resolution.
	 * 
	 * @throws Exception if any exception
	 */

	public void testDataSourceReference() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		DataSourceHandle libADataSource1 = designHandle.findDataSource("LibA.dataSource1"); //$NON-NLS-1$
		DataSourceHandle dataSource1 = designHandle.findDataSource("dataSource1"); //$NON-NLS-1$
		DataSourceHandle dataSource3 = designHandle.findDataSource("dataSource3"); //$NON-NLS-1$

		assertEquals("LibA.dataSource1", dataSource3.getExtends() //$NON-NLS-1$
				.getQualifiedName());
		assertEquals("lib_beforeopen", dataSource3.getBeforeOpen()); //$NON-NLS-1$

		DataSetHandle libADataSet1 = designHandle.findDataSet("LibA.dataSet1"); //$NON-NLS-1$
		DataSetHandle dataSet2 = designHandle.findDataSet("dataSet2"); //$NON-NLS-1$
		DataSetHandle dataSet3 = designHandle.findDataSet("dataSet3"); //$NON-NLS-1$

		assertNotNull(libADataSource1);
		assertNotNull(dataSource1);
		assertNotNull(libADataSet1);
		assertNotNull(dataSet2);
		assertNotNull(dataSet3);

		assertTrue(dataSet2.getRoot() instanceof ReportDesignHandle);
		assertTrue(dataSet3.getRoot() instanceof ReportDesignHandle);

		// Set dataSet3.dataSource = LibA.dataSource1

		dataSet3.setDataSource("LibA.dataSource1"); //$NON-NLS-1$
		assertEquals("LibA.dataSource1", dataSet3 //$NON-NLS-1$
				.getProperty(SimpleDataSet.DATA_SOURCE_PROP));
		assertNotNull(dataSet3.getDataSource());

		// Set dataSet3.dataSource = dataSource1

		dataSet3.setDataSource("dataSource1"); //$NON-NLS-1$
		assertEquals(dataSource1, dataSet3.getDataSource());

		// Undo

		designHandle.getCommandStack().undo();
		assertEquals("LibA.dataSource1", dataSet3 //$NON-NLS-1$
				.getProperty(SimpleDataSet.DATA_SOURCE_PROP));
		assertNotNull(dataSet3.getDataSource());

		// Undo

		designHandle.getCommandStack().undo();
		assertEquals(dataSource1, dataSet3.getDataSource());

		// Redo

		designHandle.getCommandStack().redo();
		assertEquals("LibA.dataSource1", dataSet3 //$NON-NLS-1$
				.getProperty(SimpleDataSet.DATA_SOURCE_PROP));
		assertNotNull(dataSet3.getDataSource());

		// Redo

		designHandle.getCommandStack().redo();
		assertEquals(dataSource1, dataSet3.getDataSource());
	}

	/**
	 * Tests the direct reference to data set.
	 * 
	 * @throws Exception if any exception
	 */

	public void testDataSetReference() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		DataSetHandle dataSet2 = designHandle.findDataSet("dataSet2"); //$NON-NLS-1$
		DataSetHandle libADataSet1 = designHandle.findDataSet("LibA.dataSet1"); //$NON-NLS-1$

		DataSetHandle dataSet4 = designHandle.findDataSet("dataSet4"); //$NON-NLS-1$
		assertEquals("LibA.dataSet1", dataSet4.getExtends() //$NON-NLS-1$
				.getQualifiedName());

		TableHandle table1 = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		assertNotNull(table1.getDataSet());

		assertEquals("LibA.dataSet1", table1 //$NON-NLS-1$
				.getProperty(IReportItemModel.DATA_SET_PROP));
		assertNotNull(table1.getDataSet());

		// Set table1.dataSet = dataSet2

		table1.setDataSet(dataSet2);
		assertEquals("dataSet2", table1.getStringProperty(TableHandle.DATA_SET_PROP)); //$NON-NLS-1$

		// Set table1.dataSet = LibA.dataSet1

		table1.setDataSet(libADataSet1);
		assertEquals("LibA.dataSet1", table1 //$NON-NLS-1$
				.getStringProperty(IReportItemModel.DATA_SET_PROP));
		assertNotNull(table1.getDataSet());

		// Undo

		designHandle.getCommandStack().undo();
		assertEquals("dataSet2", table1.getStringProperty(TableHandle.DATA_SET_PROP)); //$NON-NLS-1$
		assertEquals(dataSet2, table1.getDataSet());

		// Undo

		designHandle.getCommandStack().undo();
		assertEquals("LibA.dataSet1", table1.getStringProperty(TableHandle.DATA_SET_PROP)); //$NON-NLS-1$
		assertNotNull(table1.getDataSet());

		// Redo

		designHandle.getCommandStack().redo();
		assertEquals("dataSet2", table1.getStringProperty(TableHandle.DATA_SET_PROP)); //$NON-NLS-1$
		assertEquals(dataSet2, table1.getDataSet());

		// Redo

		designHandle.getCommandStack().redo();
		assertEquals("LibA.dataSet1", table1 //$NON-NLS-1$
				.getStringProperty(TableHandle.DATA_SET_PROP));

		// Remove one design data set which is used by one design table, and the
		// table should have no data set.

		TableHandle table2 = (TableHandle) designHandle.findElement("table2"); //$NON-NLS-1$
		assertNotNull(table2.getDataSet());
		assertEquals("dataSet2", table2.getDataSet().getName()); //$NON-NLS-1$
		table2.getDataSet().drop();
		assertNull(table2.getDataSet());
		assertEquals("dataSet2", table2.getStringProperty(TableHandle.DATA_SET_PROP)); //$NON-NLS-1$

		TableHandle table4 = (TableHandle) designHandle.findElement("table4"); //$NON-NLS-1$
		assertNotNull(table4.getDataSet());
		assertEquals("dataSet4", table4.getDataSet().getName()); //$NON-NLS-1$
	}

	/**
	 * Tests the property search rules for the design element extending one lib
	 * element.
	 * 
	 * @throws Exception if any exception
	 */

	public void testPropertiesOfDesignElementExtendingLibraryElement() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		LabelHandle label2 = (LabelHandle) designHandle.findElement("label2"); //$NON-NLS-1$
		assertEquals("libLabel1", label2.getExtends().getName()); //$NON-NLS-1$

		// Check local property

		assertEquals("Design Label", label2.getText()); //$NON-NLS-1$
		assertEquals(designHandle.findStyle("style1"), label2.getStyle()); //$NON-NLS-1$

		// Check local style property

		assertEquals("blue", label2.getPrivateStyle().getColor().getStringValue()); //$NON-NLS-1$

		// Check property inherited from parent

		assertEquals("15cm", label2.getX().getStringValue()); //$NON-NLS-1$
		assertEquals("18pt", label2.getPrivateStyle().getFontSize().getStringValue()); //$NON-NLS-1$

		// Check property inherited from grandparent

		assertEquals("5cm", label2.getY().getStringValue()); //$NON-NLS-1$

		// Check style property inherited from parent's style

		assertEquals("bolder", label2.getPrivateStyle().getFontWeight()); //$NON-NLS-1$

		// Get label2's parent from library

		LibraryHandle libHandle = (LibraryHandle) designHandle.getAllLibraries().get(0);
		LabelHandle libLabel1 = (LabelHandle) libHandle.findElement("libLabel1"); //$NON-NLS-1$

		// Check local property

		assertEquals("Library Label", libLabel1.getText()); //$NON-NLS-1$
		assertEquals(libHandle.findStyle("style1"), libLabel1.getStyle()); //$NON-NLS-1$
		assertEquals("15cm", libLabel1.getX().getStringValue()); //$NON-NLS-1$
		assertEquals("18pt", libLabel1.getPrivateStyle().getFontSize().getStringValue()); //$NON-NLS-1$

		// Check style property from local style

		assertEquals("red", libLabel1.getPrivateStyle().getColor() //$NON-NLS-1$
				.getStringValue());
		assertEquals("bolder", libLabel1.getPrivateStyle().getFontWeight()); //$NON-NLS-1$

		// Check property from parent

		assertEquals("5cm", libLabel1.getY().getStringValue()); //$NON-NLS-1$

	}

	/**
	 * Tests one design element extends one library element.
	 * 
	 * @throws Exception if any exception
	 */

	public void testExtendingLibraryElement() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		// Get one library label

		LibraryHandle libHandle = (LibraryHandle) designHandle.getAllLibraries().get(0);
		LabelHandle libLabel1 = (LabelHandle) libHandle.findElement("libLabel1"); //$NON-NLS-1$

		// Check back reference

		Iterator iter = libLabel1.derivedIterator();
		assertEquals(designHandle.findElement("label2"), iter.next()); //$NON-NLS-1$
		assertFalse(iter.hasNext());

		// Create one design element based on the library one

		LabelHandle designLabel3 = (LabelHandle) designHandle.getElementFactory().newElementFrom(libLabel1, "label3"); //$NON-NLS-1$

		// Check back reference

		iter = libLabel1.derivedIterator();
		assertEquals(designHandle.findElement("label2"), iter.next()); //$NON-NLS-1$
		assertEquals(designLabel3, iter.next());
		assertFalse(iter.hasNext());

		// Add this design element into design file

		designHandle.getBody().add(designLabel3);
		designLabel3.setText("New Label3"); //$NON-NLS-1$
		designLabel3.setStyleName("style1"); //$NON-NLS-1$

		// Check local property

		assertEquals("New Label3", designLabel3.getText()); //$NON-NLS-1$
		assertEquals(designHandle.findStyle("style1"), designLabel3.getStyle()); //$NON-NLS-1$

		// Check local style property

		assertEquals("blue", designLabel3.getPrivateStyle().getColor().getStringValue()); //$NON-NLS-1$

		// Check property inherited from parent

		assertEquals("15cm", designLabel3.getX().getStringValue()); //$NON-NLS-1$
		assertEquals("18pt", designLabel3.getPrivateStyle().getFontSize().getStringValue()); //$NON-NLS-1$

		// Check property inherited from grandparent

		assertEquals("5cm", designLabel3.getY().getStringValue()); //$NON-NLS-1$

		// Check style property inherited from parent's style

		assertEquals("bolder", designLabel3.getPrivateStyle().getFontWeight()); //$NON-NLS-1$

		// Drop the new design element

		designLabel3.drop();

		// Check back reference

		iter = libLabel1.derivedIterator();
		assertEquals(designHandle.findElement("label2"), iter.next()); //$NON-NLS-1$
		assertFalse(iter.hasNext());

		// Undo

		designHandle.getCommandStack().undo();

		// Check back reference

		iter = libLabel1.derivedIterator();
		assertEquals(designHandle.findElement("label2"), iter.next()); //$NON-NLS-1$
		assertEquals(designLabel3, iter.next());
		assertFalse(iter.hasNext());

		// Redo

		designHandle.getCommandStack().redo();

		// Check back reference

		iter = libLabel1.derivedIterator();
		assertEquals(designHandle.findElement("label2"), iter.next()); //$NON-NLS-1$
		assertFalse(iter.hasNext());

		// Create one compound element based on the library one
		TableHandle libTable1 = (TableHandle) libHandle.findElement("libTable1"); //$NON-NLS-1$
		TableHandle childTable = (TableHandle) designHandle.getElementFactory().newElementFrom(libTable1, "childTable"); //$NON-NLS-1$
		assertEquals("Employee Table", childTable.getCaption()); //$NON-NLS-1$

		LabelHandle innerLabel = (LabelHandle) ((CellHandle) ((RowHandle) childTable.getDetail().get(0)).getCells()
				.get(0)).getContent().get(0);
		assertEquals("Cell 1-1", innerLabel.getText()); //$NON-NLS-1$
	}

	/**
	 * Tests one design table extending one library table.
	 * 
	 * @throws Exception if any exception
	 */

	public void testExtendingLibraryTable() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		// Check the property from parent library table

		TableHandle table = (TableHandle) designHandle.findElement("table3"); //$NON-NLS-1$
		assertEquals("Employee Table", table.getCaption()); //$NON-NLS-1$

		// Check the header slot from parent library table

		SlotHandle headerSlot = table.getHeader();
		assertEquals(1, headerSlot.getCount());
		RowHandle row = (RowHandle) headerSlot.get(0);
		assertEquals(2, row.getCells().getCount());

		// Check the detail slot from parent library table

		SlotHandle detailSlot = table.getDetail();
		assertEquals(2, detailSlot.getCount());
		row = (RowHandle) detailSlot.get(0);
		assertEquals(2, row.getCells().getCount());
		row = (RowHandle) detailSlot.get(1);
		assertEquals(2, row.getCells().getCount());

		// Check the first row

		row = (RowHandle) detailSlot.get(0);

		CellHandle cell = null;
		cell = (CellHandle) row.getCells().get(0); // Check the first cell
		assertEquals("label_1_1", cell.getContent().get(0).getName()); //$NON-NLS-1$

		LabelHandle interLabel = (LabelHandle) cell.getContent().get(0);
		String value = interLabel.getStringProperty(LabelHandle.TEXT_PROP);
		assertEquals("Cell 1-1", value); //$NON-NLS-1$

		cell = (CellHandle) row.getCells().get(1); // Check the second cell
		assertEquals("label_1_2", cell.getContent().get(0).getName()); //$NON-NLS-1$
		assertEquals("Cell 1-2", cell.getContent().get(0).getStringProperty(LabelHandle.TEXT_PROP)); //$NON-NLS-1$

		// Check the second row

		row = (RowHandle) detailSlot.get(1);
		cell = (CellHandle) row.getCells().get(0); // Check the first cell
		TableHandle innerTable = (TableHandle) cell.getContent().get(0);
		assertEquals("libInnerTable", innerTable.getName()); //$NON-NLS-1$
	}

	/**
	 * Tests adding library.
	 * 
	 * @throws Exception if any exception
	 */

	public void testAddingLibrary() throws Exception {
		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$

		// Add one library

		designHandle.includeLibrary("Library_1.xml", "Lib1"); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			designHandle.includeLibrary("Library_1.xml", "Lib2"); //$NON-NLS-1$ //$NON-NLS-2$
			fail();
		} catch (LibraryException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED, e.getErrorCode());
		}

		// Check "includeLibraries" is set.

		PropertyHandle propHandle = designHandle.getPropertyHandle(ReportDesignHandle.LIBRARIES_PROP);
		List includeLibraries = propHandle.getListValue();
		assertEquals(1, includeLibraries.size());
		IncludedLibrary includeLibrary = (IncludedLibrary) includeLibraries.get(0);
		assertEquals("Library_1.xml", includeLibrary.getFileName()); //$NON-NLS-1$

		// Check library list

		assertEquals(1, designHandle.getAllLibraries().size());
		LibraryHandle libHandle = (LibraryHandle) designHandle.getAllLibraries().get(0);
		assertTrue(libHandle.getFileName().endsWith("Library_1.xml")); //$NON-NLS-1$
		assertEquals("W.C. Fields", libHandle.getAuthor()); //$NON-NLS-1$
		DesignElementHandle pageHandle = libHandle.getMasterPages().get(0);
		assertEquals("My Page", pageHandle.getName()); //$NON-NLS-1$

		// Undo adding one library

		designHandle.getCommandStack().undo();

		// Check "includedLibraries" property

		includeLibraries = propHandle.getListValue();
		assertNull(includeLibraries);

		// Check library list

		assertEquals(0, designHandle.getAllLibraries().size());

		// Redo adding one library

		designHandle.getCommandStack().redo();

		// Check "includedLibraries" property

		propHandle = designHandle.getPropertyHandle(ReportDesignHandle.LIBRARIES_PROP);
		includeLibraries = propHandle.getListValue();
		assertEquals(1, includeLibraries.size());
		includeLibrary = (IncludedLibrary) includeLibraries.get(0);
		assertEquals("Library_1.xml", includeLibrary.getFileName()); //$NON-NLS-1$

		// Check library list

		assertEquals(1, designHandle.getAllLibraries().size());
		libHandle = (LibraryHandle) designHandle.getAllLibraries().get(0);
		assertTrue(libHandle.getFileName().endsWith("Library_1.xml")); //$NON-NLS-1$
		assertEquals("W.C. Fields", libHandle.getAuthor()); //$NON-NLS-1$
		pageHandle = libHandle.getMasterPages().get(0);
		assertEquals("My Page", pageHandle.getName()); //$NON-NLS-1$
	}

	/**
	 * Tests adding library begin with space. The file name of the library should
	 * not be trimmed and the name space should be trimmed.
	 * 
	 * @throws Exception
	 */
	public void testAddingLibraryBeginWithSpace() throws Exception {
		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$

		// Add one library

		designHandle.includeLibrary("  Library.xml", "  Lib1"); //$NON-NLS-1$ //$NON-NLS-2$
		List<IncludedLibrary> libs = designHandle.getListProperty(IModuleModel.LIBRARIES_PROP);
		IncludedLibrary lib = libs.get(0);
		assertEquals("  Library.xml", lib.getFileName()); //$NON-NLS-1$
		assertEquals("Lib1", lib.getNamespace()); //$NON-NLS-1$

	}

	/**
	 * Tests dropping library.
	 * 
	 * @throws Exception if any exception
	 */

	public void testRemoveLibrary() throws Exception {
		openDesign("DesignWithThreeLibrary.xml"); //$NON-NLS-1$

		assertEquals(3, designHandle.getAllLibraries().size());
		LibraryHandle libHandle = (LibraryHandle) designHandle.getAllLibraries().get(0);
		assertTrue(libHandle.getFileName().endsWith("Library_1.xml")); //$NON-NLS-1$

		// Drop one library

		designHandle.dropLibrary(libHandle);

		assertEquals(2, designHandle.getAllLibraries().size());

		// Undo dropping library

		designHandle.getCommandStack().undo();

		assertEquals(3, designHandle.getAllLibraries().size());
		libHandle = (LibraryHandle) designHandle.getAllLibraries().get(0);
		assertTrue(libHandle.getFileName().endsWith("Library_1.xml")); //$NON-NLS-1$

		// Redo dropping library

		designHandle.getCommandStack().redo();

		assertEquals(2, designHandle.getAllLibraries().size());
	}

	/**
	 * Tests shifting library. The design file includes three libraries, each of
	 * which has style named "style1"
	 * <ul>
	 * <li>style1 in Lib1 has color with the value, "red"
	 * <li>style1 in Lib2 has color with the value, "green"
	 * <li>style1 in Lib3 has color with the value, "yellow"
	 * </ul>
	 * 
	 * 
	 * @throws Exception if any exception
	 */

	public void testShiftLibraryWithoutShifting() throws Exception {
		openDesign("DesignWithThreeLibrary.xml"); //$NON-NLS-1$

		LabelHandle label = (LabelHandle) designHandle.findElement("myLabel"); //$NON-NLS-1$
		assertEquals("yellow", label.getColorProperty(Style.COLOR_PROP) //$NON-NLS-1$
				.getStringValue());

		final LibraryHandle library1 = (LibraryHandle) designHandle.getAllLibraries().get(0);
		assertEquals("Lib1", library1.getNamespace()); //$NON-NLS-1$

		final LibraryHandle library2 = (LibraryHandle) designHandle.getAllLibraries().get(1);
		assertEquals("Library_2", library2.getNamespace()); //$NON-NLS-1$

		final LibraryHandle library3 = (LibraryHandle) designHandle.getAllLibraries().get(2);
		assertEquals("Lib3", library3.getNamespace()); //$NON-NLS-1$

		checkLibrarySequence(new LibraryHandle[] { library1, library2, library3 });

		// Shift the last to a verty high position

		designHandle.shiftLibrary(library3, 100);
		checkLibrarySequence(new LibraryHandle[] { library1, library2, library3 });

		// Shift the first to a ver low position

		designHandle.shiftLibrary(library1, -1);
		checkLibrarySequence(new LibraryHandle[] { library1, library2, library3 });

		// Shift the second to middle

		designHandle.shiftLibrary(library2, 1);
		checkLibrarySequence(new LibraryHandle[] { library1, library2, library3 });

		// Shift the second to last

		designHandle.shiftLibrary(library2, 3);
		checkLibrarySequence(new LibraryHandle[] { library1, library3, library2 });

		designHandle.getCommandStack().undo();
		checkLibrarySequence(new LibraryHandle[] { library1, library2, library3 });

		designHandle.getCommandStack().redo();
		checkLibrarySequence(new LibraryHandle[] { library1, library3, library2 });

		// Shift the second to first

		designHandle.shiftLibrary(library3, 0);
		checkLibrarySequence(new LibraryHandle[] { library3, library1, library2 });

		designHandle.getCommandStack().undo();
		checkLibrarySequence(new LibraryHandle[] { library1, library3, library2 });

		designHandle.getCommandStack().redo();
		checkLibrarySequence(new LibraryHandle[] { library3, library1, library2 });

	}

	/**
	 * @param libraries
	 */
	private void checkLibrarySequence(LibraryHandle[] libraries) {
		PropertyHandle propHandle = designHandle.getPropertyHandle(Module.LIBRARIES_PROP);

		for (int i = 0; i < 3; i++) {
			assertEquals(libraries[i], designHandle.getAllLibraries().get(i));
			assertEquals(libraries[i].getNamespace(), ((IncludedLibraryHandle) propHandle.getAt(i)).getNamespace());
		}
	}

	/**
	 * Tests the writer.
	 * 
	 * @throws Exception if any exception
	 */

	public void testWriter() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		save();
		compareFile("DesignWithElementReferenceLibrary_golden.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests the structure reference.
	 * 
	 * @throws Exception if any exception
	 */

	public void testStructureReference() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		assertNotNull(designHandle.findConfigVariable("var1")); //$NON-NLS-1$
		assertNull(designHandle.findConfigVariable("var2")); //$NON-NLS-1$
		assertNotNull(designHandle.findImage("image1.jpg")); //$NON-NLS-1$
		assertNotNull(designHandle.findImage("LibA.image2")); //$NON-NLS-1$
		assertNotNull(designHandle.findColor("lighterRed")); //$NON-NLS-1$
		assertNotNull(designHandle.findColor("LibA.lighterBlue")); //$NON-NLS-1$

		LibraryHandle libAHandle = designHandle.getLibrary("LibA"); //$NON-NLS-1$

		// The design config variable overrides the library one.

		PropertyHandle propHandle = designHandle.getPropertyHandle(Module.CONFIG_VARS_PROP);

		StructureHandle structure = propHandle.getAt(0);
		assertEquals(structure.getStructure(), designHandle.findConfigVariable("var1")); //$NON-NLS-1$

		Iterator iter = designHandle.configVariablesIterator();
		ConfigVariableHandle configVar = ((ConfigVariableHandle) iter.next());
		assertEquals("var1", configVar.getName()); //$NON-NLS-1$
		assertTrue(configVar.getModule() instanceof ReportDesign);

		configVar = ((ConfigVariableHandle) iter.next());
		assertEquals("var2", configVar.getName()); //$NON-NLS-1$
		assertTrue(configVar.getModule() instanceof Library);

		assertFalse(iter.hasNext());

		// The design embedded image overrides the library one.

		propHandle = designHandle.getPropertyHandle(Module.IMAGES_PROP);
		structure = propHandle.getAt(0);
		assertEquals(structure.getStructure(), designHandle.findImage("image1.jpg")); //$NON-NLS-1$

		iter = designHandle.getAllImages().iterator();
		EmbeddedImageHandle image = (EmbeddedImageHandle) iter.next();
		assertEquals("image1.jpg", image.getName()); //$NON-NLS-1$
		assertEquals("image1.jpg", image.getQualifiedName()); //$NON-NLS-1$
		assertTrue(image.getModule() instanceof ReportDesign);

		image = ((EmbeddedImageHandle) iter.next());
		assertEquals("image1.jpg", image.getName()); //$NON-NLS-1$
		assertEquals("LibA.image1.jpg", image.getQualifiedName()); //$NON-NLS-1$
		assertTrue(image.getModule() instanceof Library);

		image = ((EmbeddedImageHandle) iter.next());
		assertEquals("image2", image.getName()); //$NON-NLS-1$
		assertEquals("LibA.image2", image.getQualifiedName()); //$NON-NLS-1$
		assertTrue(image.getModule() instanceof Library);

		assertFalse(iter.hasNext());

		// The design custom color overrides the library one.

		propHandle = designHandle.getPropertyHandle(Module.COLOR_PALETTE_PROP);
		structure = propHandle.getAt(0);
		assertEquals(structure.getStructure(), designHandle.findColor("lighterRed")); //$NON-NLS-1$

		iter = designHandle.customColorsIterator();
		CustomColorHandle color = (CustomColorHandle) iter.next();
		assertEquals("lighterRed", color.getName()); //$NON-NLS-1$
		assertEquals("lighterRed", color.getQualifiedName()); //$NON-NLS-1$
		assertTrue(color.getModule() instanceof ReportDesign);

		color = (CustomColorHandle) iter.next();
		assertEquals("lighterRed", color.getName()); //$NON-NLS-1$
		assertEquals("LibA.lighterRed", color.getQualifiedName()); //$NON-NLS-1$
		assertTrue(color.getModule() instanceof Library);

		color = (CustomColorHandle) iter.next();
		assertEquals("lighterBlue", color.getName()); //$NON-NLS-1$
		assertEquals("LibA.lighterBlue", color.getQualifiedName()); //$NON-NLS-1$
		assertTrue(color.getModule() instanceof Library);

		assertFalse(iter.hasNext());

		iter = designHandle.customColorsIterator();
		assertEquals(structure.getStructure(), ((StructureHandle) iter.next()).getStructure());
		propHandle = libAHandle.getPropertyHandle(Module.COLOR_PALETTE_PROP);
		structure = propHandle.getAt(0);
		assertEquals(structure.getStructure(), ((StructureHandle) iter.next()).getStructure());
	}

	/**
	 * Test the reference to one custom color.
	 * 
	 * @throws Exception if any exception
	 */

	public void testCustomColorReference() throws Exception {
		openDesign("DesignWithElementReferenceLibrary.xml"); //$NON-NLS-1$

		LabelHandle label = (LabelHandle) designHandle.findElement("label3"); //$NON-NLS-1$
		assertEquals(222, label.getPrivateStyle().getColor().getRGB());
		assertEquals(333, label.getPrivateStyle().getBackgroundColor().getRGB());

		label.getPrivateStyle().getColor().setStringValue("LibA.lighterRed"); //$NON-NLS-1$
		assertEquals(111, label.getPrivateStyle().getColor().getRGB());

		label.getPrivateStyle().getColor().setStringValue("lighterRed"); //$NON-NLS-1$
		assertEquals(333, label.getPrivateStyle().getColor().getRGB());

		// Undo

		designHandle.getCommandStack().undo();
		assertEquals(111, label.getPrivateStyle().getColor().getRGB());

		// Undo

		designHandle.getCommandStack().undo();
		assertEquals(222, label.getPrivateStyle().getColor().getRGB());

		// Redo

		designHandle.getCommandStack().redo();
		assertEquals(111, label.getPrivateStyle().getColor().getRGB());

		// Redo

		designHandle.getCommandStack().redo();
		assertEquals(333, label.getPrivateStyle().getColor().getRGB());
	}

	/**
	 * Tests the element reference update when user adds one library or drops it.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testElementReferenceAfterAddingOrDroppingLibrary() throws Exception {
		openDesign("DesignForTestingLibraryChange.xml"); //$NON-NLS-1$

		// Check color value

		LabelHandle label = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		assertEquals("blue", label.getPrivateStyle().getColor() //$NON-NLS-1$
				.getStringValue());

		// Drop the style this label is using

		StyleHandle style = designHandle.findNativeStyle("style1"); //$NON-NLS-1$
		style.drop();
		assertEquals("green", label.getPrivateStyle().getColor() //$NON-NLS-1$
				.getStringValue());

		// Drop the second libaray

		LibraryHandle library = designHandle.getLibrary("Lib2"); //$NON-NLS-1$
		designHandle.dropLibrary(library);
		assertEquals(ColorPropertyType.BLACK, label.getPrivateStyle().getColor().getStringValue());

		// Drop the first library

		library = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		designHandle.dropLibrary(library);

		// Default value

		assertEquals(ColorPropertyType.BLACK, label.getPrivateStyle().getColor().getStringValue());

		// undo drop the Lib1

		designHandle.getCommandStack().undo();
		assertEquals(ColorPropertyType.BLACK, label.getPrivateStyle().getColor().getStringValue());

		// undo drop the Lib2

		designHandle.getCommandStack().undo();
		assertEquals(ColorPropertyType.GREEN, label.getPrivateStyle().getColor().getStringValue());
	}

	/**
	 * Test dropping a library.
	 * <p>
	 * 1) If library has a descendents in the current module, it can not be dropped.
	 * <p>
	 * 2) When user force to drop the library with descendents, all the derivatives
	 * in the current module will be localized.
	 * <p>
	 * <strong>Case1:</strong>
	 * <p>
	 * Design.label1 -> lib1.baseLabel
	 * <p>
	 * When dropping lib1, label1 in the design will be localized, the parent/child
	 * relationship is broken.
	 * <p>
	 * <strong>Case2:</strong>
	 * <p>
	 * Design.label1 -> lib1.baseLabel -> lib2.baseLabel2
	 * <p>
	 * When dopping <strong>lib2</strong>, label1 in the design will be localized.
	 * While lib2.baseLabel1 will not be affected.
	 * <p>
	 * 
	 * @throws Exception
	 */

	public void testDropLibrary() throws Exception {

		// Case 1
		openDesign("TestDropLibrary_1.xml"); //$NON-NLS-1$
		LibraryHandle libHandle = (LibraryHandle) designHandle.getLibraries().get(0);
		TableHandle tableHandle = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		TableItem parent = (TableItem) tableHandle.getExtends().getElement();
		assertNotNull(parent);

		RowHandle rowHandle = (RowHandle) tableHandle.getDetail().get(0);
		CellHandle cellHandle = (CellHandle) rowHandle.getCells().get(0);
		cellHandle.setStringProperty(StyleHandle.COLOR_PROP, "black"); //$NON-NLS-1$

		try {
			designHandle.dropLibrary(libHandle);
			fail();
		} catch (SemanticException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS, e.getErrorCode());
		}

		designHandle.dropLibraryAndBreakExtends(libHandle);
		assertNull(tableHandle.getElement().getExtendsElement());

		// The include library structure has been removed.

		assertNull(designHandle.getListProperty(Module.LIBRARIES_PROP));

		save();
		compareFile("TestDropLibrary_golden1.xml"); //$NON-NLS-1$

		// Case 2.

		openDesign("TestDropLibrary_2.xml"); //$NON-NLS-1$
		LibraryHandle lib2Handle = (LibraryHandle) designHandle.getLibraries().get(0);

		// 2-level extends.

		LabelHandle mylabelHandle = (LabelHandle) designHandle.findElement("myLabel"); //$NON-NLS-1$

		assertEquals("25pt", mylabelHandle.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("red", mylabelHandle.getStringProperty(StyleHandle.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("First Page", mylabelHandle.getText()); //$NON-NLS-1$
		try {
			designHandle.dropLibrary(lib2Handle);
			fail();
		} catch (SemanticException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS, e.getErrorCode());
		}

		designHandle.dropLibraryAndBreakExtends(lib2Handle);
		assertNull(mylabelHandle.getElement().getExtendsElement());

		// the inheritable properties are localized.

		Label myLabel = (Label) mylabelHandle.getElement();
		assertEquals("25pt", myLabel.getLocalProperty(designHandle.getModule(), Label.HEIGHT_PROP).toString()); //$NON-NLS-1$
		assertEquals("red", myLabel.getLocalProperty(designHandle.getModule(), StyleHandle.COLOR_PROP).toString()); //$NON-NLS-1$
		assertEquals("First Page", myLabel.getLocalProperty(designHandle.getModule(), Label.TEXT_PROP)); //$NON-NLS-1$

		// The include library structure has been removed.
		assertNull(designHandle.getListProperty(Module.LIBRARIES_PROP));

		save();
		compareFile("TestDropLibrary_golden2.xml"); //$NON-NLS-1$

		design.getActivityStack().undo();

		Label baseLabel = (Label) mylabelHandle.getElement().getExtendsElement();
		assertNotNull(baseLabel);

		// local property is recovered.

		assertEquals("25pt", myLabel.getLocalProperty(designHandle.getModule(), Label.HEIGHT_PROP).toString()); //$NON-NLS-1$
		assertEquals(null, myLabel.getLocalProperty(designHandle.getModule(), StyleHandle.COLOR_PROP));
		assertEquals(null, myLabel.getLocalProperty(designHandle.getModule(), Label.TEXT_PROP));

		// Test extends properties.

		assertEquals("25pt", mylabelHandle.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("red", mylabelHandle.getStringProperty(StyleHandle.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("First Page", mylabelHandle.getText()); //$NON-NLS-1$

		// The include library structure has been added.

		assertEquals(1, designHandle.getListProperty(Module.LIBRARIES_PROP).size());

		design.getActivityStack().redo();

		// After redo, the relationship is again broken.
		// the inheritable properties are localized.

		myLabel = (Label) mylabelHandle.getElement();
		assertEquals("25pt", myLabel.getLocalProperty(designHandle.getModule(), Label.HEIGHT_PROP).toString()); //$NON-NLS-1$
		assertEquals("red", myLabel.getLocalProperty(designHandle.getModule(), StyleHandle.COLOR_PROP).toString()); //$NON-NLS-1$
		assertEquals("First Page", myLabel.getLocalProperty(designHandle.getModule(), Label.TEXT_PROP)); //$NON-NLS-1$

		// The include library structure has been removed.

		assertNull(designHandle.getListProperty(Module.LIBRARIES_PROP));

	}

	/**
	 * Tests whether the element from the included library is read only.
	 * 
	 * @throws Exception if any exception
	 */

	public void testLibraryElementReadOnly() throws Exception {
		openDesign("DesignWithThreeLibrary.xml"); //$NON-NLS-1$

		final LibraryHandle library1 = (LibraryHandle) designHandle.getAllLibraries().get(0);
		assertEquals("Lib1", library1.getNamespace()); //$NON-NLS-1$

		// Test read-only

		assertFalse(designHandle.isReadOnly());
		assertTrue(library1.isReadOnly());

		// To set style property will cause runtime exception since this library
		// is read-only.

		StyleHandle style = library1.findStyle("style1"); //$NON-NLS-1$
		try {
			style.setFontVariant(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalOperationException);
		}
	}

	/**
	 * Tests one design file including the library, which also includes another two
	 * libraries.
	 * 
	 * @throws Exception if any exception
	 */

	public void testDesignIncludeLibraries() throws Exception {
		openDesign("DesignWithOneCompositeLibrary.xml"); //$NON-NLS-1$

		assertEquals(3, designHandle.getAllLibraries().size());

		// one is directly included, two are not

		LibraryHandle libraryComposite = designHandle.getLibrary("CompositeLib"); //$NON-NLS-1$
		LibraryHandle library1 = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		LibraryHandle library2 = designHandle.getLibrary("Lib2"); //$NON-NLS-1$
		assertTrue(libraryComposite.isValid());
		assertNull(library1);
		assertNull(library2);

		// Check all styles, composite library does not have theme defined.

		List styleList = designHandle.getAllStyles();
		assertEquals(0, styleList.size());

		// Check all data sets

		DataSetHandle dataSetHandle1 = libraryComposite.findDataSet("Lib1.dataSet1"); //$NON-NLS-1$
		DataSetHandle dataSetHandle2 = libraryComposite.findDataSet("Lib2.dataSet1"); //$NON-NLS-1$

		assertNotNull(dataSetHandle1);
		assertNotNull(dataSetHandle2);

		List dataSetList = designHandle.getAllDataSets();
		assertEquals(3, dataSetList.size());
		assertEquals(0, designHandle.getVisibleDataSets().size());
		dataSetList = libraryComposite.getAllDataSets();
		assertEquals(3, dataSetList.size());
		assertEquals(1, libraryComposite.getVisibleDataSets().size());

		// table can refer the indirectly included library resources

		TableHandle tableHandle1 = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		assertNotNull(tableHandle1.getDataSet());

		TableHandle tableHandle2 = (TableHandle) designHandle.findElement("table2"); //$NON-NLS-1$
		assertNotNull(tableHandle2.getDataSet());

		// Check label's style

		SharedStyleHandle styleHandle = designHandle.findStyle("style1"); //$NON-NLS-1$
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		assertEquals(styleHandle, labelHandle.getStyle());

		// Add new style which will override the library one

		SharedStyleHandle newStyleHandle = designHandle.getElementFactory().newStyle("style1"); //$NON-NLS-1$
		designHandle.getStyles().add(newStyleHandle);
		assertEquals(newStyleHandle, labelHandle.getStyle());

	}

	/**
	 * Tests opening design file which includes recursive libraries.
	 * 
	 * @throws Exception if any exception
	 */

	public void testDesignIncludeRecursiveLibraries() throws Exception {
		try {
			openDesign("DesignIncludeRecursiveLibraries.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			assertEquals(LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY,
					((ErrorDetail) e.getErrorList().get(0)).getErrorCode());
		}
	}

	/**
	 * Tests adding one library which has the same namespace as the one in design
	 * file.
	 * <p>
	 * <ul>
	 * <li>Design file includes Lib1.
	 * <li>Add another Lib1 to design file.
	 * <li>The exception of duplicate library namespace is reported.
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testAddingLibraryWithDuplicateNamespace1() throws Exception {
		openDesign("DesignWithOneLibrary.xml"); //$NON-NLS-1$

		assertEquals(1, designHandle.getAllLibraries().size());

		LibraryHandle library1 = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		assertNotNull(library1);

		try {
			designHandle.includeLibrary("Library_1.xml", "Lib1"); //$NON-NLS-1$ //$NON-NLS-2$
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof LibraryException);
			assertEquals(LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE,
					((LibraryException) e).getErrorCode());
		}
	}

	// /**
	// * Tests adding one library which has the same namespace as the one in
	// * design file.
	// * <p>
	// * <ul>
	// * <li>Design file includes Lib1.
	// * <li>CompositeLib includes Lib1 and Lib2.
	// * <li>Add CompositeLib to design file.
	// * <li>The exception of duplicate library namespace is reported.
	// * </ul>
	// *
	// * @throws Exception
	// * if any exception
	// */
	//
	// public void testAddingLibraryWithDuplicateNamespace2( ) throws Exception
	// {
	// openDesign( "DesignWithOneLibrary.xml" ); //$NON-NLS-1$
	//
	// assertEquals( 1, designHandle.getAllLibraries( ).size( ) );
	//
	// LibraryHandle library1 = designHandle.getLibrary( "Lib1" ); //$NON-NLS-1$
	// assertNotNull( library1 );
	//
	// try
	// {
	// designHandle.includeLibrary(
	// "LibraryIncludingTwoLibraries.xml", "CompositeLib" ); //$NON-NLS-1$
	// //$NON-NLS-2$
	// fail( );
	// }
	// catch ( Exception e )
	// {
	// assertTrue( e instanceof DesignFileException );
	// ErrorDetail error = (ErrorDetail) ( (DesignFileException) e )
	// .getErrorList( ).get( 0 );
	// assertEquals(
	// LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE,
	// error.getErrorCode( ) );
	// }
	// }

	// /**
	// * Tests adding one library which has the same namespace as the one in
	// * design file.
	// * <p>
	// * <ul>
	// * <li>Design file includes CompositeLib.
	// * <li>CompositeLib includes Lib1 and Lib2.
	// * <li>Add Lib1 to design file.
	// * <li>The exception of duplicate library namespace is reported.
	// * </ul>
	// *
	// * @throws Exception
	// * if any exception
	// */
	//
	// public void testAddingLibraryWithDuplicateNamespace3( ) throws Exception
	// {
	// openDesign( "DesignWithOneCompositeLibrary.xml" ); //$NON-NLS-1$
	//
	// assertEquals( 3, designHandle.getAllLibraries( ).size( ) );
	//
	// LibraryHandle library1 = designHandle.getLibrary( "CompositeLib" );
	// //$NON-NLS-1$
	// assertNotNull( library1 );
	//
	// try
	// {
	// designHandle.includeLibrary( "Library_1.xml", "Lib1" ); //$NON-NLS-1$
	// //$NON-NLS-2$
	// fail( );
	// }
	// catch ( Exception e )
	// {
	// assertTrue( e instanceof LibraryException );
	// assertEquals(
	// LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE,
	// ( (LibraryException) e ).getErrorCode( ) );
	// }
	// }

	/**
	 * Test case:
	 * 
	 * when add a element like style to the design, names in the library do not take
	 * effect. This is because library and design have different name spaces.
	 * 
	 * @throws Exception
	 */

	public void testAddStyleInDesignWithOneLibrary() throws Exception {
		openDesign("DesignWithOneLibrary.xml"); //$NON-NLS-1$

		// this library has a custom style "style1". Make sure that "style1"
		// still can be added into the design styles slot.

		assertNotNull(designHandle.findStyle("style1")); //$NON-NLS-1$

		designHandle.getStyles().add(designHandle.getElementFactory().newStyle("style1")); //$NON-NLS-1$
	}

	/**
	 * Tests the location path related isses.
	 * 
	 * @throws Exception
	 */

	public void testLocation() throws Exception {
		// open a library first

		openLibrary("Library.xml"); //$NON-NLS-1$
		assertNotNull(libraryHandle);
		System.out.println(libraryHandle.getSystemId());
		LabelHandle baseLabel = (LabelHandle) libraryHandle.findElement("base"); //$NON-NLS-1$

		// create a child from the base, base and child are in the same module.

		LabelHandle childLabel = (LabelHandle) libraryHandle.getElementFactory().newElementFrom(baseLabel, "child"); //$NON-NLS-1$
		assertNotNull(childLabel);

		// include the same library

		openDesign("DesignWithOneLibrary.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);
		List libs = designHandle.getAllLibraries();
		assertEquals(1, libs.size());
		assertTrue(designHandle.isInclude((LibraryHandle) libs.get(0)));
		designHandle.includeLibrary("Library.xml", "Lib"); //$NON-NLS-1$ //$NON-NLS-2$
		libs = designHandle.getAllLibraries();
		assertEquals(2, libs.size());
		assertTrue(designHandle.isInclude((LibraryHandle) libs.get(0)));
		assertTrue(designHandle.isInclude((LibraryHandle) libs.get(1)));
		assertTrue(designHandle.isInclude(libraryHandle));
		LibraryHandle includeLib = (LibraryHandle) libs.get(1);

		// create a child from the base element in design and add child to
		// design

		childLabel = null;
		childLabel = (LabelHandle) designHandle.getElementFactory().newElementFrom(baseLabel, "child"); //$NON-NLS-1$
		assertNotNull(childLabel);
		designHandle.addElement(childLabel, ReportDesign.BODY_SLOT);
		assertEquals(designHandle, childLabel.getModuleHandle());
		assertEquals(includeLib, childLabel.getExtends().getModuleHandle());

		// change the name for the base element

		assertEquals("Lib.base", childLabel.getExtends().getQualifiedName()); //$NON-NLS-1$
		NameSpace ns = ((Module) includeLib.getElement()).getNameHelper().getNameSpace(Module.ELEMENT_NAME_SPACE);
		ns.rename(includeLib.findElement("base").getElement(), "base", "newBase"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		includeLib.findElement("newBase").getElement().setName("newBase"); //$NON-NLS-1$ //$NON-NLS-2$
		childLabel = null;
		childLabel = (LabelHandle) designHandle.getElementFactory().newElementFrom(baseLabel, "childTwo"); //$NON-NLS-1$
		designHandle.getBody().add(childLabel);

		save();
		compareFile("LibraryWithLocation_golden.xml"); //$NON-NLS-1$

	}

	/**
	 * Test case:
	 * 
	 * 1. When including a library which includes another library, getAllImages
	 * method can return all the images in these libraries.
	 * 
	 * 2. Above condition, the imagesIterator method returns null while there is no
	 * embedded images in report design.
	 * 
	 * @throws Exception
	 */

	public void testGetImage() throws Exception {
		openDesign("DesignIncludeLibraryWithImage.xml"); //$NON-NLS-1$

		// test imagesIterator()

		assertEquals(1, designHandle.getLibraries().size());
		assertTrue(!designHandle.imagesIterator().hasNext());
		assertNotNull(designHandle.findImage("Lib1.image1")); //$NON-NLS-1$
		assertNotNull(designHandle.findImage("Lib1.image2")); //$NON-NLS-1$
		assertNotNull(designHandle.findImage("Lib2.image2")); //$NON-NLS-1$
		assertNotNull(designHandle.findImage("Lib2.image3")); //$NON-NLS-1$

		LibraryHandle lib1 = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		assertNotNull(lib1);
		Iterator imageIter = lib1.imagesIterator();
		assertTrue(imageIter.hasNext());
		EmbeddedImageHandle image = (EmbeddedImageHandle) imageIter.next();
		assertEquals("image1", image.getName()); //$NON-NLS-1$
		assertEquals("Lib1.image1", image.getQualifiedName()); //$NON-NLS-1$
		assertTrue(image.getModule() instanceof Library);

		image = (EmbeddedImageHandle) imageIter.next();
		assertEquals("image2", image.getName()); //$NON-NLS-1$
		assertEquals("Lib1.image2", image.getQualifiedName()); //$NON-NLS-1$
		assertTrue(image.getModule() instanceof Library);

		LibraryHandle lib2 = designHandle.getLibrary("Lib2"); //$NON-NLS-1$
		assertNull(lib2);

		// test getAllImages()

		assertEquals(4, designHandle.getAllImages().size());
		assertEquals(4, lib1.getAllImages().size());
	}

	/**
	 * Tests isReadOnly in PropertyHandle.
	 * 
	 * @throws Exception
	 */

	public void testReadOnly() throws Exception {
		openDesign("DesignWithOneLibrary.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		libraryHandle = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		assertNotNull(libraryHandle);

		PropertyHandle propertyHandle = libraryHandle.getPropertyHandle(Module.CREATED_BY_PROP);
		assertTrue(propertyHandle.isReadOnly());

		propertyHandle = designHandle.getPropertyHandle(Module.CREATED_BY_PROP);
		assertFalse(propertyHandle.isReadOnly());
	}

	/**
	 * Tests property value of elements that is extended in multi-level.
	 * 
	 * cases:
	 * <ul>
	 * <li>a table with dataset in library is extended triple times.
	 * <li>a table with dataset in library is extended two times.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testMultiExtendedElements() throws Exception {
		openDesign("DesignWithOneCompositeLibrary.xml"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("table3"); //$NON-NLS-1$
		assertEquals("CompositeLib.myTable", table //$NON-NLS-1$
				.getProperty(IDesignElementModel.EXTENDS_PROP));
		assertNotNull(table.getDataSet());

		assertEquals("Lib1.dataSet1", table //$NON-NLS-1$
				.getStringProperty(IReportItemModel.DATA_SET_PROP));

		DataSetHandle dataSet = designHandle.findDataSet(table.getStringProperty(IReportItemModel.DATA_SET_PROP));
		assertNotNull(dataSet);

		dataSet = designHandle.findDataSet("CompositeLib.dataSet1"); //$NON-NLS-1$
		assertNotNull(dataSet);

		table = (TableHandle) designHandle.findElement("table4"); //$NON-NLS-1$
		assertEquals("CompositeLib.dataSet1", table //$NON-NLS-1$
				.getStringProperty(IReportItemModel.DATA_SET_PROP));

		dataSet = table.getDataSet();
		assertEquals("Lib2.dataSet1", dataSet //$NON-NLS-1$
				.getProperty(IDesignElementModel.EXTENDS_PROP));

	}

	/**
	 * Test get externalized message.
	 * 
	 * @throws Exception
	 */
	public void testGetExternalizedMessage() throws Exception {
		openDesign("LibraryWithExternalizedMessage.xml"); //$NON-NLS-1$

		LabelHandle label1Handle = (LabelHandle) designHandle.findElement("NewLabel"); //$NON-NLS-1$
		LabelHandle label2Handle = (LabelHandle) designHandle.findElement("NewLabel1");//$NON-NLS-1$

		String displayValue = label1Handle.getDisplayText();
		assertEquals("v1", displayValue);//$NON-NLS-1$

		displayValue = label2Handle.getDisplayText();
		assertEquals("v2", displayValue);//$NON-NLS-1$

	}

	/**
	 * Tests case : a label extends a library label, and the library label has a
	 * user property, then the label in the design can be dropped.
	 * 
	 * @throws Exception
	 */

	public void testLibraryWithUserProperty() throws Exception {
		openDesign("DesignWithUserProperty.xml"); //$NON-NLS-1$
		LabelHandle label = (LabelHandle) designHandle.findElement("NewLabel"); //$NON-NLS-1$
		assertEquals(designHandle, label.getRoot());

		label.drop();
		assertNull(label.getRoot());
		assertNull(designHandle.findElement("NewLabel")); //$NON-NLS-1$
	}

	/**
	 * Test cases to retrieve script libs/included scripts from the design with its
	 * included libraries.
	 * 
	 * @throws Exception
	 */

	public void testGetLibResources() throws Exception {
		openDesign("DesignWithResourcesTest.xml"); //$NON-NLS-1$

		List<? extends StructureHandle> libs = designHandle.getAllScriptLibs();
		assertEquals(4, libs.size());

		List<? extends StructureHandle> includedScripts = designHandle.getAllIncludeScripts();
		assertEquals(4, includedScripts.size());

		IncludeScriptHandle script = (IncludeScriptHandle) includedScripts.get(0);
		assertEquals("a", script.getFileName()); //$NON-NLS-1$

		script = (IncludeScriptHandle) includedScripts.get(1);
		assertEquals("outer", script.getFileName()); //$NON-NLS-1$

		script = (IncludeScriptHandle) includedScripts.get(2);
		assertEquals("inner", script.getFileName()); //$NON-NLS-1$

		script = (IncludeScriptHandle) includedScripts.get(3);
		assertEquals("outer1", script.getFileName()); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testGetAllIncludeCsses() throws Exception {
		openDesign("DesignWithResourcesTest.xml"); //$NON-NLS-1$

		List<IncludedCssStyleSheetHandle> sheets = designHandle.getAllExternalIncludedCsses();
		assertEquals(3, sheets.size());

		IncludedCssStyleSheetHandle sheetHandle = sheets.get(0);
		assertEquals("base2.css", sheetHandle.getFileName()); //$NON-NLS-1$

		sheetHandle = sheets.get(1);
		assertEquals("outer1.css", sheetHandle.getFileName()); //$NON-NLS-1$

		sheetHandle = sheets.get(2);
		assertEquals("outer2.css", sheetHandle.getFileName()); //$NON-NLS-1$
	}

	/**
	 * Tests the same library can be included in one design twice. The case: 1. the
	 * design contains libA; 2. add the libB to the design, the libB uses the libA
	 * file with the name space Lib1.
	 * <p>
	 * The expect result is libB can be used successfully and liB.Lib1 is valid. see
	 * bug 276216.
	 * 
	 * @throws Exception if any exception
	 */

	public void testLoadDesignWithSameLibraryFile() throws Exception {
		openDesign("BlankDesign.xml"); //$NON-NLS-1$

		designHandle.includeLibrary("Library_1.xml", "libA"); //$NON-NLS-1$ //$NON-NLS-2$

		designHandle.includeLibrary("LibraryIncludingTwoLibraries.xml", "libB"); //$NON-NLS-1$ //$NON-NLS-2$

		assertNotNull(design.getLibraryWithNamespace("Lib1")); //$NON-NLS-1$

		// creates a new grid, and make sure that the grid has its layout
		// structure

		GridHandle tmpGrid = (GridHandle) designHandle.getLibrary("libB") //$NON-NLS-1$
				.findElement("myGrid"); //$NON-NLS-1$
		GridHandle newGrid = (GridHandle) designHandle.getElementFactory().newElementFrom(tmpGrid, "testMyGrid"); //$NON-NLS-1$
		assertTrue(newGrid.getRows().getCount() > 0);
	}
}