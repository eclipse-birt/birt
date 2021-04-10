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

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests cases in the library.
 * 
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
 * collapse" bordercolor="#111111" width="100%" id="AutoNumber3" height="50">
 * <tr>
 * <td width="33%" height="16"><b>Method </b></td>
 * <td width="33%" height="16"><b>Test Case </b></td>
 * <td width="34%" height="16"><b>Expected Result </b></td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testMakeUniqueName()}</td>
 * <td>Element factory from library handle creates a not null name to an element
 * that can be no name.</td>
 * <td>A new table has not null name.</td>
 * </tr>
 * 
 * 
 * </table>
 * 
 */

public class LibraryHandleTest extends BaseTestCase {

	/**
	 * Test cases:
	 * 
	 * A new element from factory of library handle can be added into the library
	 * directly.
	 * 
	 * @throws Exception no exception thrown
	 */

	public void testMakeUniqueName() throws Exception {
		libraryHandle = createLibrary(ULocale.ENGLISH);
		ElementFactory libFactory = libraryHandle.getElementFactory();

		TableHandle tableHandle = libFactory.newTableItem(null);
		assertEquals("NewTable", tableHandle.getName()); //$NON-NLS-1$

		tableHandle = libFactory.newTableItem(null);
		assertEquals("NewTable1", tableHandle.getName()); //$NON-NLS-1$

		tableHandle = libFactory.newTableItem("NewTable2"); //$NON-NLS-1$
		libraryHandle.getComponents().add(tableHandle);

		tableHandle = libFactory.newTableItem("NewTable2"); //$NON-NLS-1$
		assertEquals("NewTable21", tableHandle.getName()); //$NON-NLS-1$

		libraryHandle.getComponents().add(tableHandle);

		tableHandle = libraryHandle.getElementFactory().newTableItem(null);
		tableHandle.setName(null);

		assertNull(tableHandle.getName());

		libraryHandle.rename(tableHandle);
		assertEquals("NewTable", tableHandle.getName()); //$NON-NLS-1$
	}

	/**
	 * Create Style in Library and test style name.
	 * 
	 * @throws Exception
	 */
	public void testGetStyleName() throws Exception {
		libraryHandle = createLibrary(ULocale.ENGLISH);
		StyleHandle styleHandle = libraryHandle.getElementFactory().newStyle(null);
		assertEquals("NewStyle", styleHandle.getName());//$NON-NLS-1$
	}

	/**
	 * Tests cases:
	 * 
	 * <ul>
	 * <li>1: Create an image in design file from the library image which reference
	 * a library embedded image. Get the embedded image name from the design image
	 * item. The name should have library namespace prefix.
	 * <li>2: Create a table in design file from the library table which reference a
	 * dataset in library. Get the dataSet name from the design table. The name
	 * should have the library namespace prefix.
	 * </ul>
	 * 
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	public void testGetNamePrefix() throws SemanticException, DesignFileException {

		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library_namespace_test.xml", "libraryNameSpace"); //$NON-NLS-1$ //$NON-NLS-2$
		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$

		ImageHandle libImage = (ImageHandle) libraryHandle.getComponents().get(0);

		assertNotNull(libImage);

		ImageHandle designImage = (ImageHandle) designHandle.getElementFactory().newElementFrom(libImage,
				"newDesignImage"); //$NON-NLS-1$

		designHandle.getComponents().add(designImage);

		assertEquals("image1", libImage.getImageName()); //$NON-NLS-1$
		assertEquals("libraryNameSpace.image1", designImage.getImageName()); //$NON-NLS-1$

		TableHandle libTable = (TableHandle) libraryHandle.getComponents().get(1);

		assertEquals("libraryNameSpace.dataSet1", libTable.getDataSet().getQualifiedName()); //$NON-NLS-1$

		TableHandle designTable = (TableHandle) designHandle.getElementFactory().newElementFrom(libTable,
				"designTable"); //$NON-NLS-1$

		designHandle.getComponents().add(designTable);
		assertEquals("libraryNameSpace.dataSet1", designTable.getDataSet().getQualifiedName()); //$NON-NLS-1$

		ScriptDataSetHandle dataSet = designHandle.getElementFactory().newScriptDataSet("scriptDataSet"); //$NON-NLS-1$

		designHandle.getDataSets().add(dataSet);
		designTable.setDataSet(dataSet);
		assertEquals("scriptDataSet", designTable.getProperty("dataSet")); //$NON-NLS-1$//$NON-NLS-2$

		ScalarParameterHandle libParameter = (ScalarParameterHandle) libraryHandle.getParameters().get(0);
		assertNotNull(libParameter);

		ScalarParameterHandle designParameter = (ScalarParameterHandle) designHandle.getElementFactory()
				.newElementFrom(libParameter, "designParameter"); //$NON-NLS-1$

		designHandle.getParameters().add(designParameter);

		assertEquals("libraryNameSpace.dataSet1", designParameter //$NON-NLS-1$
				.getDataSetName());
	}

	/**
	 * Test cases: Use the getAllXXX method on ModuleHandle to get all dataSets,
	 * dataSources & masterPages in the design file & the included libraries. The
	 * element which comes from the library should return the name with library
	 * prefix when call the getQualifiedName().
	 * 
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void testGetQualifiedName() throws DesignFileException, SemanticException {
		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library_2.xml", "libraryNameSpace"); //$NON-NLS-1$ //$NON-NLS-2$
		List list = designHandle.getAllDataSets();
		assertEquals(1, list.size());

		list = designHandle.getAllDataSources();
		assertEquals(1, list.size());

		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$
		list = libraryHandle.getAllDataSets();

		DataSetHandle dataSet = (DataSetHandle) list.iterator().next();
		assertEquals("libraryNameSpace.dataSet1", dataSet.getQualifiedName()); //$NON-NLS-1$

		list = libraryHandle.getAllDataSources();
		DataSourceHandle dataSource = (DataSourceHandle) list.iterator().next();
		assertEquals("libraryNameSpace.dataSource1", dataSource.getQualifiedName()); //$NON-NLS-1$

		list = designHandle.getAllPages();
		MasterPageHandle page = (MasterPageHandle) list.iterator().next();
		assertEquals("My Page", page.getQualifiedName()); //$NON-NLS-1$

		list = libraryHandle.getAllPages();

		page = (MasterPageHandle) list.get(0);
		assertEquals("libraryNameSpace.My Page", page.getQualifiedName()); //$NON-NLS-1$
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testGetHostHandle() throws DesignFileException, SemanticException {

		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library_2.xml", "libraryNameSpace"); //$NON-NLS-1$ //$NON-NLS-2$

		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$

		assertEquals(designHandle, libraryHandle.getHostHandle());

	}

	/**
	 * 1).Include library to design. 2).Copy a dataSet element in library. The
	 * dataSet is referenced to a dataSoource in library file. 3).Paste the dataSet
	 * into design file. 4).The dataSource referenced by the dataSet should can be
	 * resolved properly.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 * 
	 */

	public void testResolvCopyElementFromLibraryToDesign() throws DesignFileException, SemanticException {

		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library_namespace_test.xml", "libraryNameSpace"); //$NON-NLS-1$ //$NON-NLS-2$
		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$

		DataSetHandle libDataSet = (DataSetHandle) libraryHandle.getDataSets().get(0);
		assertEquals("dataSource1", libDataSet.getDataSourceName()); //$NON-NLS-1$

		DataSetHandle designDataSet = (DataSetHandle) libDataSet.copy().getHandle(designHandle.getModule());

		designHandle.getDataSets().add(designDataSet);

		assertEquals("libraryNameSpace.dataSource1", designDataSet //$NON-NLS-1$
				.getDataSourceName());

		// Test the table with a dataSet. To see if the dataSet is accessable

	}

	/**
	 * Test cases: Copy/Paste a table defined in library, table content is
	 * displayed.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCopyPaste() throws Exception {
		openDesign("DesignCopyPaste.xml"); //$NON-NLS-1$

		TableHandle tHandle = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$

		TableHandle extended = (TableHandle) designHandle.getElementFactory().newElementFrom(tHandle, "extended"); //$NON-NLS-1$
		extended.setHeight(50);
		designHandle.getBody().paste(extended);

		IDesignElement copied = extended.copy();
		designHandle.rename(copied.getHandle(design));
		designHandle.getBody().paste(copied.getHandle(design));

		save();
		assertTrue(compareFile("DesignCopyPaste_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test cases: Copy/Paste a label newed in library
	 * 
	 * @throws NameException
	 * @throws Exception
	 * 
	 */

	public void testCopyPasteLabel() throws Exception {
		openLibrary("LibraryWithImage.xml"); //$NON-NLS-1$

		LabelHandle original = libraryHandle.getElementFactory().newLabel("original"); //$NON-NLS-1$
		libraryHandle.getComponents().add(original);

		LabelHandle copied = (LabelHandle) original.copy().getHandle(libraryHandle.getModule());
		libraryHandle.rename(copied);

		libraryHandle.getComponents().add(copied);
	}

	/**
	 * Tests needSave method.
	 * 
	 * Only change happens directly on report design, isDirty mark of report design
	 * is true. So when library changed, isDirty mark of report design should be
	 * false.
	 * 
	 * <ul>
	 * <li>reload library</li>
	 * <li>isDirty=false</li>
	 * 
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testLibraryNeedsSave() throws Exception {
		openDesign("DesignWithSaveStateTest.xml"); //$NON-NLS-1$
		openLibrary("LibraryWithSaveStateTest.xml");//$NON-NLS-1$

		assertFalse(designHandle.needsSave());

		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("new test label");//$NON-NLS-1$
		designHandle.getBody().add(labelHandle);
		assertTrue(designHandle.getCommandStack().canUndo());
		assertTrue(designHandle.needsSave());

		ActivityStack stack = (ActivityStack) designHandle.getCommandStack();

		designHandle.reloadLibrary(libraryHandle);

		assertFalse(designHandle.needsSave());

		assertFalse(designHandle.getCommandStack().canUndo());
		assertFalse(designHandle.getCommandStack().canRedo());
		assertEquals(0, stack.getCurrentTransNo());
	}

	/**
	 * Tests the library include library writer.
	 * 
	 * @throws Exception
	 */

	public void testLibraryIncludeLibraryWriter() throws Exception {
		openLibrary("Library_1.xml"); //$NON-NLS-1$
		libraryHandle.includeLibrary("Library_2.xml", "lib2"); //$NON-NLS-1$//$NON-NLS-2$

		saveLibrary();

		assertTrue(compareFile("LibraryIncludeLibrary_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests canEdit() in DesignElementHandle.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testCanEdit() throws DesignFileException, SemanticException {

		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library.xml", "libraryNameSpace"); //$NON-NLS-1$ //$NON-NLS-2$

		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$

		List list = libraryHandle.getAllDataSets();
		assertEquals(1, list.size());

		DataSetHandle dataSet = (DataSetHandle) list.iterator().next();
		assertFalse(dataSet.canEdit());

		DataSourceHandle dataSource = (DataSourceHandle) libraryHandle.getAllDataSources().iterator().next();
		assertFalse(dataSource.canEdit());

		ThemeHandle theme = (ThemeHandle) libraryHandle.getThemes().get(0);
		assertFalse(theme.canEdit());

		StyleHandle style = (StyleHandle) theme.getStyles().get(0);
		assertFalse(style.canEdit());

		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$
		LabelHandle labelHandle = libraryHandle.getElementFactory().newLabel(null);
		assertFalse(labelHandle.canEdit());
		labelHandle = designHandle.getElementFactory().newLabel(null);
		assertTrue(labelHandle.canEdit());

	}

	/**
	 * Tests testCanTransformToTemplate() in DesignElementHandle.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testCanTransformToTemplate() throws DesignFileException, SemanticException {
		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library.xml", "libraryNameSpace"); //$NON-NLS-1$ //$NON-NLS-2$

		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$
		TableHandle libTable = (TableHandle) libraryHandle.getComponents().get(1);
		assertFalse(libTable.canTransformToTemplate());

		TableHandle designTable = (TableHandle) designHandle.getElementFactory().newElementFrom(libTable,
				"designTable"); //$NON-NLS-1$

		assertNotNull(designTable);

		LabelHandle label = (LabelHandle) ((CellHandle) ((RowHandle) designTable.getDetail().get(0)).getCells().get(0))
				.getContent().get(0);
		assertFalse(label.canTransformToTemplate());

		assertTrue(designTable.canTransformToTemplate());
	}

	/**
	 * 
	 * @throws Exception
	 */

	public void testCopyPasteAndClearNameSpace() throws Exception {
		openDesign("DesignCopyPaste.xml"); //$NON-NLS-1$
		libraryHandle = designHandle.getLibrary("new_library"); //$NON-NLS-1$

		TableHandle table = (TableHandle) libraryHandle.findElement("NewTable"); //$NON-NLS-1$

		TableHandle copiedLibTable = (TableHandle) table.copy().getHandle(design);
		designHandle.rename(copiedLibTable);

		ElementRefValue refValue = (ElementRefValue) copiedLibTable.getElement().getLocalProperty(design,
				IReportItemModel.DATA_SET_PROP);
		assertNotNull(refValue);
		assertEquals("new_library", refValue.getLibraryNamespace()); //$NON-NLS-1$

		designHandle.getBody().paste(copiedLibTable);
		refValue = (ElementRefValue) copiedLibTable.getElement().getLocalProperty(design,
				IReportItemModel.DATA_SET_PROP);
		assertNotNull(refValue);
		assertNull(refValue.getLibraryNamespace());
	}

	/**
	 * Tests canContain() and canDrop() in DesignElementHandle.
	 * 
	 * <ul>
	 * <li>
	 * <li>
	 * </ul>
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testCanContainCanDrop() throws DesignFileException, SemanticException {
		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library.xml", "libraryNameSpace"); //$NON-NLS-1$ //$NON-NLS-2$

		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$

		ThemeHandle theme = (ThemeHandle) libraryHandle.getThemes().get(0);
		assertFalse(theme.canDrop());
		assertFalse(theme.canContain(IThemeModel.STYLES_SLOT, ReportDesignConstants.STYLE_ELEMENT));
		assertFalse(theme.canContain(IThemeModel.STYLES_SLOT,
				libraryHandle.getElementFactory().newStyle("newCreatedStyle"))); //$NON-NLS-1$

		StyleHandle style = (StyleHandle) theme.getStyles().get(0);
		assertFalse(style.canDrop());

		LabelHandle labelHandle = libraryHandle.getElementFactory().newLabel(null);
		assertFalse(labelHandle.canDrop());

		theme = libraryHandle.getElementFactory().newTheme("newCreatedTheme"); //$NON-NLS-1$
		assertFalse(theme.canDrop());
		assertFalse(theme.canContain(IThemeModel.STYLES_SLOT, ReportDesignConstants.STYLE_ELEMENT));
		assertFalse(theme.canContain(IThemeModel.STYLES_SLOT,
				libraryHandle.getElementFactory().newStyle("newCreatedStyle"))); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>Includes a library, test its default display label.
	 * <li>Includes a library, test its user display label.
	 * <li>Includes a library, test its short display label.
	 * <li>Includes a library, test its long display label.
	 * </ul>
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testGetLibraryDisplayLabel() throws DesignFileException, SemanticException {
		openDesign("DesignWithoutLibrary.xml"); //$NON-NLS-1$
		designHandle.includeLibrary("Library_2.xml", "libraryNameSpace"); //$NON-NLS-1$ //$NON-NLS-2$

		libraryHandle = designHandle.getLibrary("libraryNameSpace"); //$NON-NLS-1$
		assertEquals("libraryNameSpace", libraryHandle.getDisplayLabel()); //$NON-NLS-1$

		assertEquals("libraryNameSpace", libraryHandle //$NON-NLS-1$
				.getDisplayLabel(IDesignElementModel.USER_LABEL));
		assertEquals("libraryNameSpace", libraryHandle //$NON-NLS-1$
				.getDisplayLabel(IDesignElementModel.SHORT_LABEL));
		assertEquals("libraryNameSpace", libraryHandle //$NON-NLS-1$
				.getDisplayLabel(IDesignElementModel.FULL_LABEL));
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>Find a library with a relative file path.
	 * <li>Find a library with an absolute file path.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testFindLibrary() throws Exception {
		openDesign("DesignWithRelativePathLibrary.xml"); //$NON-NLS-1$

		libraryHandle = designHandle.findLibrary("../golden/LibraryParseTest_golden.xml"); //$NON-NLS-1$

		assertNotNull(libraryHandle);

		LibraryHandle libToCompare = designHandle
				.findLibrary(design.findResource("../golden/LibraryParseTest_golden.xml", //$NON-NLS-1$
						IResourceLocator.LIBRARY).toExternalForm());
		assertNotNull(libToCompare);

		assertSame(libToCompare, libraryHandle);
	}
}
