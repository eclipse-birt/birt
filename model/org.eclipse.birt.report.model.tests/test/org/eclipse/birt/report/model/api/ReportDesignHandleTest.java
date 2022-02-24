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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.core.AttributeEvent;
import org.eclipse.birt.report.model.api.core.DisposeEvent;
import org.eclipse.birt.report.model.api.core.IAttributeListener;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.IDisposeListener;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.olap.Hierarchy;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.TabularHierarchy;
import org.eclipse.birt.report.model.elements.olap.TabularLevel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.ReportDesignSerializer;

import com.ibm.icu.util.ULocale;

/**
 * 
 * Tests cases for ReportDesignHandle.
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testConfigVars()}</td>
 * <td>Tests to read, remove, replace ConfigVars.</td>
 * <td>Operations are finished correctly and the output file matches the golden
 * file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testReportDesignOtherMethods()}</td>
 * <td>Tests to get element and design handle.</td>
 * <td>Returns the design and design handle correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests to get numbers of errors and warnings.</td>
 * <td>Returns numbers of errors and warnings correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testReportDesignProperties()}</td>
 * <td>Tests to get and set properties like base and default units.</td>
 * <td>Values are set correctly and the output file matches the golden
 * file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testReportDesignSlots()}</td>
 * <td>Tests to get different kinds of slots like body, components, etc.</td>
 * <td>Information of slots matches with the input design file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testIteratorMethods()}</td>
 * <td>Tests to get iterators.</td>
 * <td>Information of iterators matches with the input design file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testTranslations()}</td>
 * <td>Tests to get translations.</td>
 * <td>Information of translations matches with the input design file.</td>
 * </tr>
 * 
 * <tr>
 * <td>Test add / drop css style sheet
 * </tr>
 * 
 * </table>
 * 
 * 
 */
public class ReportDesignHandleTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ReportDesignHandleTest.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests cases for reading and setting ConfigVars.
	 * 
	 * @throws Exception if any exception
	 */

	public void testConfigVars() throws Exception {
		PropertyHandle configVars = designHandle.getPropertyHandle(ReportDesign.CONFIG_VARS_PROP);
		List list = configVars.getListValue();
		assertEquals(2, list.size());

		ConfigVariable var = designHandle.findConfigVariable("var1"); //$NON-NLS-1$
		assertEquals("mumble.jpg", var.getValue()); //$NON-NLS-1$

		designHandle.dropConfigVariable("var2"); //$NON-NLS-1$

		list = configVars.getListValue();
		assertEquals(1, list.size());

		ConfigVariable newvar = new ConfigVariable();
		newvar.setName("newvar2"); //$NON-NLS-1$
		newvar.setValue("new value 2"); //$NON-NLS-1$
		designHandle.addConfigVariable(newvar);

		newvar = new ConfigVariable();
		newvar.setName("new var1"); //$NON-NLS-1$
		newvar.setValue("new value 1"); //$NON-NLS-1$
		designHandle.replaceConfigVariable(var, newvar);

		list = configVars.getListValue();
		assertEquals(2, list.size());
		designHandle.dropConfigVariable("newvar2"); //$NON-NLS-1$
		designHandle.dropConfigVariable("new var1"); //$NON-NLS-1$

		configVars.setValue(null);
		assertNull(configVars.getValue());

		ConfigVariable structure3 = StructureFactory.createConfigVar();
		structure3.setName("myvar"); //$NON-NLS-1$
		structure3.setValue("my value"); //$NON-NLS-1$
		designHandle.addConfigVariable(structure3);

		PropertyHandle propHandle = designHandle.getPropertyHandle(ReportDesign.CONFIG_VARS_PROP);
		Iterator iter = propHandle.iterator();
		ConfigVariableHandle structureHandle3 = (ConfigVariableHandle) iter.next();

		structureHandle3.setName("new name"); //$NON-NLS-1$
		structureHandle3.setValue("new value"); //$NON-NLS-1$

		assertEquals("new name", structureHandle3.getName()); //$NON-NLS-1$
		assertEquals("new value", structureHandle3.getValue()); //$NON-NLS-1$

		try {
			designHandle.replaceConfigVariable(structure3, structure3);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		ConfigVariable structure4 = StructureFactory.createConfigVar();
		structure4.setName("myvar1"); //$NON-NLS-1$
		structure4.setValue("my value1"); //$NON-NLS-1$

		designHandle.replaceConfigVariable(structure3, structure4);
		designHandle.dropConfigVariable("myvar1"); //$NON-NLS-1$

		try {
			designHandle.dropConfigVariable("myvar"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * Tests css style sheet.
	 * 
	 * @throws Exception
	 */

	public void testCssStyleSheet() throws Exception {
		openDesign("BlankReportDesign.xml"); //$NON-NLS-1$

		assertTrue(designHandle.canAddCssStyleSheet(getResource("input/base.css").getFile()));//$NON-NLS-1$
		assertTrue(designHandle.canAddCssStyleSheet("base.css"));//$NON-NLS-1$
		// test add css sheet

		CssStyleSheetHandle sheetHandle = designHandle.openCssStyleSheet(getResource("input/base.css").getFile());//$NON-NLS-1$
		assertNull(sheetHandle.getContainerHandle());
		designHandle.addCss(sheetHandle);

		assertFalse(designHandle.canAddCssStyleSheet(getResource("input/base.css").getFile()));//$NON-NLS-1$
		assertFalse(designHandle.canAddCssStyleSheet(sheetHandle));

		assertNotNull(sheetHandle.getContainerHandle());

		List styles = designHandle.getAllStyles();
		assertEquals(5, styles.size());

		try {
			designHandle.addCss(sheetHandle);
			fail();
		} catch (CssException e) {
			assertEquals(CssException.DESIGN_EXCEPTION_DUPLICATE_CSS, e.getErrorCode());
		}
		// label use it
		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("label");//$NON-NLS-1$
		designHandle.getBody().add(labelHandle);
		labelHandle.setStyle((SharedStyleHandle) styles.get(0));

		// drop css

		assertTrue(designHandle.canDropCssStyleSheet(sheetHandle));
		// before drop , element is resolved. after drop element is unresolved
		ElementRefValue value = (ElementRefValue) labelHandle.getElement().getLocalProperty(designHandle.getModule(),
				"style");//$NON-NLS-1$
		assertTrue(value.isResolved());

		designHandle.dropCss(sheetHandle);
		assertFalse(value.isResolved());
		assertNull(designHandle.includeCssesIterator().next());
		assertNull(labelHandle.getStyle());

		assertFalse(designHandle.canDropCssStyleSheet(sheetHandle));
		assertNull(sheetHandle.getContainerHandle());
		// test add css file name

		designHandle.addCss("base.css");//$NON-NLS-1$
		styles = designHandle.getAllStyles();
		assertEquals(5, styles.size());

		designHandle.getCommandStack().undo();

		IncludedCssStyleSheet cssStruct = StructureFactory.createIncludedCssStyleSheet();
		cssStruct.setFileName("base.css"); //$NON-NLS-1$
		cssStruct.setExternalCssURI("/tmp"); //$NON-NLS-1$
		designHandle.addCss(cssStruct);

		cssStruct = (IncludedCssStyleSheet) designHandle.getListProperty(ReportDesignHandle.CSSES_PROP).get(0);
		assertEquals("base.css", cssStruct.getFileName()); //$NON-NLS-1$
		assertEquals("/tmp", cssStruct.getExternalCssURI()); //$NON-NLS-1$

		styles = designHandle.getAllStyles();
		assertEquals(5, styles.size());

		CssStyleSheetHandle stylySheetHandle = designHandle.findCssStyleSheetHandleByFileName("base.css"); //$NON-NLS-1$
		assertNotNull(stylySheetHandle);
		assertEquals("base.css", stylySheetHandle.getFileName());//$NON-NLS-1$

		IncludedCssStyleSheetHandle includedStylySheetHandle = designHandle
				.findIncludedCssStyleSheetHandleByFileName("base.css");//$NON-NLS-1$
		assertNotNull(includedStylySheetHandle);
		assertEquals("base.css", includedStylySheetHandle.getFileName());//$NON-NLS-1$

	}

	/**
	 * Tests rename included css style sheet.
	 * 
	 * @throws Exception
	 */
	public void testRenameCss() throws Exception {
		// test rename css.

		openDesign("RenameCssTest.xml"); //$NON-NLS-1$
		IncludedCssStyleSheetHandle includedStylySheetHandle = designHandle
				.findIncludedCssStyleSheetHandleByFileName("base.css");//$NON-NLS-1$

		// can not rename the css file with the same file name.
		assertFalse(designHandle.canRenameCss(includedStylySheetHandle, "base.css")); //$NON-NLS-1$

		// can not rename the css file which has already been
		// included in the design file.
		assertFalse(designHandle.canRenameCss(includedStylySheetHandle, "base1.css")); //$NON-NLS-1$

		// can rename the css file which has not been included
		// in the design file.
		assertTrue(designHandle.canRenameCss(includedStylySheetHandle, "base2.css")); //$NON-NLS-1$

		// can not rename the css file which has not been included
		// in the design file.
		assertFalse(designHandle.canRenameCss(includedStylySheetHandle, "base3.css")); //$NON-NLS-1$

		// can not rename the css file with the same file name.
		try {

			designHandle.renameCss(includedStylySheetHandle, "base1.css");//$NON-NLS-1$
			fail();
		} catch (CssException e) {
			assertEquals(CssException.DESIGN_EXCEPTION_DUPLICATE_CSS, e.getErrorCode());
		}

		// can not rename the css file which has not been included
		// in the design file.
		try {
			designHandle.renameCss(includedStylySheetHandle, "base3.css");//$NON-NLS-1$
			fail();
		} catch (CssException e) {
			assertEquals(CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND, e.getErrorCode());
		}

		designHandle.renameCss(includedStylySheetHandle, "base2.css");//$NON-NLS-1$

		includedStylySheetHandle = designHandle.findIncludedCssStyleSheetHandleByFileName("base2.css");//$NON-NLS-1$

		assertEquals("base2.css", includedStylySheetHandle.getFileName()); //$NON-NLS-1$
		List list = designHandle.getAllCssStyleSheets();
		CssStyleSheetHandle cssStyleSheetHandle = (CssStyleSheetHandle) list.get(0);
		assertEquals("base2.css", cssStyleSheetHandle.getFileName()); //$NON-NLS-1$

		designHandle.getCommandStack().undo();

		includedStylySheetHandle = designHandle.findIncludedCssStyleSheetHandleByFileName("base.css");//$NON-NLS-1$
		assertEquals("base.css", includedStylySheetHandle.getFileName()); //$NON-NLS-1$
		list = designHandle.getAllCssStyleSheets();
		cssStyleSheetHandle = (CssStyleSheetHandle) list.get(0);
		assertEquals("base.css", cssStyleSheetHandle.getFileName()); //$NON-NLS-1$

		designHandle.getCommandStack().redo();

		includedStylySheetHandle = designHandle.findIncludedCssStyleSheetHandleByFileName("base2.css");//$NON-NLS-1$

		assertEquals("base2.css", includedStylySheetHandle.getFileName()); //$NON-NLS-1$
		list = designHandle.getAllCssStyleSheets();
		cssStyleSheetHandle = (CssStyleSheetHandle) list.get(0);
		assertEquals("base2.css", cssStyleSheetHandle.getFileName()); //$NON-NLS-1$
	}

	/**
	 * Tests cases for methods on ReportDesignHandle.
	 * 
	 */

	public void testReportDesignOtherMethods() {
		DesignElement element = designHandle.getElement();
		assertTrue(element instanceof ReportDesign);
		assertTrue(element == design);
		assertTrue(designHandle.getDesign() == design);

		assertTrue(designHandle.getDesignHandle() != null);

		// error for master page size

		// oda data set and data source can have no extension
		// assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION,
		// ( (ErrorDetail) list.get( 1 ) ).getErrorCode( ) );
		// assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION,
		// ( (ErrorDetail) list.get( 2 ) ).getErrorCode( ) );

		ParameterHandle paramHandle = designHandle.findParameter("Param 2"); //$NON-NLS-1$
		assertNotNull(paramHandle);
	}

	/**
	 * Tests cases for methods on slots.
	 * 
	 */

	public void testReportDesignSlots() {

		SlotHandle slotHandle = designHandle.getBody();
		assertEquals(1, slotHandle.getCount());

		slotHandle = designHandle.getParameters();
		assertEquals(3, slotHandle.getCount());

		List list = designHandle.getFlattenParameters();
		assertEquals(4, list.size());

		slotHandle = designHandle.getComponents();
		assertEquals(0, slotHandle.getCount());

		slotHandle = designHandle.getDataSets();
		assertEquals(1, slotHandle.getCount());

		slotHandle = designHandle.getDataSources();
		assertEquals(1, slotHandle.getCount());

		slotHandle = designHandle.getMasterPages();
		assertEquals(1, slotHandle.getCount());

		slotHandle = designHandle.getScratchPad();
		assertEquals(0, slotHandle.getCount());

		slotHandle = designHandle.getStyles();
		assertEquals(0, slotHandle.getCount());

		// no custom color.

		PropertyHandle colorPalette = designHandle.getPropertyHandle(ReportDesign.COLOR_PALETTE_PROP);
		assertNull(colorPalette.getListValue());

	}

	/**
	 * Tests cases for reading and setting properties of report design.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testReportDesignProperties() throws Exception {
		// get properties.

		assertEquals(0, designHandle.getImageDPI());
		assertEquals("c:\\", designHandle.getBase()); //$NON-NLS-1$
		assertEquals(getResource(INPUT_FOLDER + "ReportDesignHandleTest.xml").toString(), designHandle.getFileName()); //$NON-NLS-1$

		// sets properties.

		designHandle.setBase("../test/input/"); //$NON-NLS-1$
		assertEquals("../test/input/", designHandle.getBase()); //$NON-NLS-1$
		assertEquals("W.C. Fields", designHandle.getAuthor()); //$NON-NLS-1$
		assertEquals("http://company.com/reportHelp.html", designHandle.getHelpGuide()); //$NON-NLS-1$
		assertEquals("Whiz-Bang Plus", designHandle.getCreatedBy()); //$NON-NLS-1$
		assertEquals(30, designHandle.getRefreshRate());

		designHandle.setAuthor("Eclipse BIRT 1.00"); //$NON-NLS-1$
		designHandle.setHelpGuide("http://www.eclipse.org/birt/help.html"); //$NON-NLS-1$
		designHandle.setCreatedBy("Eclipse BIRT"); //$NON-NLS-1$
		designHandle.setRefreshRate(50);

		assertEquals("Eclipse BIRT 1.00", designHandle.getAuthor()); //$NON-NLS-1$
		assertEquals("http://www.eclipse.org/birt/help.html", designHandle.getHelpGuide()); //$NON-NLS-1$
		assertEquals("Eclipse BIRT", designHandle.getCreatedBy()); //$NON-NLS-1$
		assertEquals(50, designHandle.getRefreshRate());

		List<VariableElementHandle> variables = designHandle.getAllVariables();
		Iterator<VariableElementHandle> iterator = variables.iterator();

		while (iterator.hasNext()) {
			VariableElementHandle variable = iterator.next();
			String name = variable.getVariableName();
			assertEquals(name, "test"); //$NON-NLS-1$

			String expression = variable.getValue();
			assertEquals(expression, "\"the test value\""); //$NON-NLS-1$

		}

	}

	/**
	 * Test case for rename method. Give a free-form element, check the name of all
	 * of elements within its slot. if the name is duplicate with the current name
	 * space
	 * 
	 * @throws Exception
	 * 
	 */
	public void testRename() throws Exception {

		openDesign("ReportDesignHandleTest1.xml"); //$NON-NLS-1$
		FreeFormHandle handle = (FreeFormHandle) designHandle.getBody().get(0);

		assertTrue(handle.getElement() instanceof FreeForm);

		FreeForm form = (FreeForm) handle.getElement();
		designHandle.rename(form.getHandle(design));

		SimpleMasterPage page = (SimpleMasterPage) designHandle.getMasterPages().get(0).getElement();
		designHandle.rename(page.getHandle(design));

	}

	/**
	 * Test rename( Object , DesignElementHandle ) method Test cases:
	 * 
	 * <ul>
	 * <li>style in theme
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testUniqueStyleName() throws Exception {
		createLibrary();

		// Data prepare
		ElementFactory libFactory = libraryHandle.getElementFactory();

		ThemeHandle theme = libFactory.newTheme("theme1"); //$NON-NLS-1$
		libraryHandle.getThemes().add(theme);

		StyleHandle style = libFactory.newStyle("NewStyle");//$NON-NLS-1$
		theme.getStyles().add(style);
		assertEquals("NewStyle", style.getName());//$NON-NLS-1$
		StyleHandle style1 = libFactory.newStyle("NewStyle1");//$NON-NLS-1$
		theme.getStyles().add(style1);
		assertEquals("NewStyle1", style1.getName());//$NON-NLS-1$
		StyleHandle style2 = libFactory.newStyle("NewStyle2");//$NON-NLS-1$
		theme.getStyles().add(style2);
		assertEquals("NewStyle2", style2.getName());//$NON-NLS-1$

		IDesignElement clonedElement = style2.copy();
		DesignElementHandle clonedElementHandle = clonedElement.getHandle(libraryHandle.getModule());

		// rename in themehandle
		libraryHandle.getRoot().rename(theme, clonedElementHandle);
		assertEquals("NewStyle21", clonedElementHandle.getName());//$NON-NLS-1$

		// do nothing in libraryhandle
		clonedElement = style2.copy();
		clonedElementHandle = clonedElement.getHandle(libraryHandle.getModule());
		libraryHandle.getRoot().rename(libraryHandle, clonedElementHandle);
		assertEquals("NewStyle2", clonedElementHandle.getName());//$NON-NLS-1$
	}

	/**
	 * Test rename( Object , DesignElementHandle ) method Test cases:
	 * 
	 * <ul>
	 * <li>level in hierarchy
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testUniqueLevelName() throws Exception {
		openLibrary("ReportDesignHandleTest_UniqueLevelName.xml");//$NON-NLS-1$

		// hierarchy is in tree.
		// in this case name change in level

		CubeHandle cubeHandle = (CubeHandle) libraryHandle.getCubes().get(0);
		DimensionHandle dimensionHandle = cubeHandle.getDimension("Group");//$NON-NLS-1$
		HierarchyHandle hierarchyHandle = (HierarchyHandle) dimensionHandle
				.getListProperty(DimensionHandle.HIERARCHIES_PROP).get(0);

		Level level = new TabularLevel("ORDERNUMBER"); //$NON-NLS-1$
		LevelHandle levelHandle = (LevelHandle) level.getHandle(libraryHandle.getModule());
		libraryHandle.getRoot().rename(hierarchyHandle, levelHandle);
		assertEquals("ORDERNUMBER1", levelHandle.getName());//$NON-NLS-1$

		// hierarchy not in the tree and not find the dimension
		// in this case no name change in level.

		Hierarchy hierarchy = new TabularHierarchy("NewTabularHierarchy");//$NON-NLS-1$
		hierarchyHandle = (HierarchyHandle) hierarchy.getHandle(libraryHandle.getModule());
		level = new TabularLevel("ORDERNUMBER");//$NON-NLS-1$
		levelHandle = (LevelHandle) level.getHandle(libraryHandle.getModule());
		hierarchyHandle.add(HierarchyHandle.LEVELS_PROP, levelHandle);

		libraryHandle.getRoot().rename(hierarchyHandle, levelHandle);
		assertEquals("ORDERNUMBER", levelHandle.getName());//$NON-NLS-1$

		// hierarchy not in the tree, but can find dimension
		// in this case name change in level

		dimensionHandle = (DimensionHandle) dimensionHandle.copy().getHandle(libraryHandle.getModule());

		hierarchy = new TabularHierarchy("NewTabularHierarchy");//$NON-NLS-1$
		hierarchyHandle = (HierarchyHandle) hierarchy.getHandle(libraryHandle.getModule());
		dimensionHandle.add(DimensionHandle.HIERARCHIES_PROP, hierarchyHandle);
		level = new TabularLevel("ORDERNUMBER");//$NON-NLS-1$
		levelHandle = (LevelHandle) level.getHandle(libraryHandle.getModule());
		hierarchyHandle.add(HierarchyHandle.LEVELS_PROP, levelHandle);

		libraryHandle.getRoot().rename(hierarchyHandle, levelHandle);
		assertEquals("ORDERNUMBER1", levelHandle.getName());//$NON-NLS-1$

		// rename about' hierarchy': hierarchy is managed by module however
		// the content level is managed by dimension

		dimensionHandle = cubeHandle.getDimension("Group");//$NON-NLS-1$
		dimensionHandle = (DimensionHandle) dimensionHandle.copy().getHandle(libraryHandle.getModule());

		hierarchy = new TabularHierarchy("NewTabularHierarchy");//$NON-NLS-1$
		hierarchyHandle = (HierarchyHandle) hierarchy.getHandle(libraryHandle.getModule());
		dimensionHandle.add(DimensionHandle.HIERARCHIES_PROP, hierarchyHandle);
		level = new TabularLevel("ORDERNUMBER");//$NON-NLS-1$
		levelHandle = (LevelHandle) level.getHandle(libraryHandle.getModule());
		hierarchyHandle.add(HierarchyHandle.LEVELS_PROP, levelHandle);

		libraryHandle.getRoot().rename(dimensionHandle, hierarchyHandle);

		assertEquals("NewTabularHierarchy2", hierarchyHandle.getName());//$NON-NLS-1$
		assertEquals("ORDERNUMBER1", levelHandle.getName());//$NON-NLS-1$

		// rename about table

		TableItem table = new TableItem("table");//$NON-NLS-1$
		libraryHandle.getComponents().add(table.getHandle(libraryHandle.getModule()));
		TableHandle tableHandle = (TableHandle) libraryHandle.getComponents().get(0);
		TableItem innerTable = new TableItem("table");//$NON-NLS-1$

		libraryHandle.getRoot().rename(tableHandle, innerTable.getHandle(libraryHandle.getModule()));
		assertEquals("table1", innerTable.getName());//$NON-NLS-1$

		// rename about table contains table.

		table = new TableItem("another table");//$NON-NLS-1$
		ContainerSlot slot = table.getSlot(TableItem.DETAIL_SLOT);
		innerTable = new TableItem("another table");//$NON-NLS-1$
		slot.add(innerTable);

		libraryHandle.getRoot().rename(libraryHandle, table.getHandle(libraryHandle.getModule()));
		assertEquals("another table", table.getName());//$NON-NLS-1$
		assertEquals("another table1", innerTable.getName());//$NON-NLS-1$
	}

	/**
	 * Tests iterator methods of a report design.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testIteratorMethods() throws Exception {
		Iterator iter = designHandle.imagesIterator();
		assertFalse(iter.hasNext());

		iter = designHandle.configVariablesIterator();
		assertNotNull(iter.next());
		assertNotNull(iter.next());
		assertFalse(iter.hasNext());
	}

	/**
	 * Tests the save state of a report design after several undo and redo.
	 * 
	 * @throws Exception
	 */

	public void testNeedsSave() throws Exception {

		String outputPath = getTempFolder() + OUTPUT_FOLDER;
		File outputFolder = new File(outputPath);
		Files.createDirectories(outputFolder.toPath());

		ElementFactory factory = new ElementFactory(design);
		GridHandle grid = factory.newGridItem("new grid"); //$NON-NLS-1$

		SlotHandle slot = designHandle.getBody();
		slot.add(grid);

		assertTrue(designHandle.needsSave());
		designHandle.saveAs(outputPath + "ReportDesignTestNew.xml"); //$NON-NLS-1$
		assertFalse(designHandle.needsSave());

		grid = factory.newGridItem("new second grid"); //$NON-NLS-1$
		slot.add(grid);
		assertTrue(designHandle.needsSave());
		slot.dropAndClear(grid);
		assertTrue(designHandle.needsSave());

		ActivityStack as = design.getActivityStack();
		as.undo();
		assertTrue(designHandle.needsSave());
		as.undo();
		assertFalse(designHandle.needsSave());

		as.undo();
		assertFalse(as.canUndo());
		assertTrue(designHandle.needsSave());

	}

	/**
	 * Execute an command and undo it, the state should be clean.
	 * 
	 * @throws Exception
	 */

	public void testNeedsSave2() throws Exception {
		save(designHandle);

		ElementFactory factory = new ElementFactory(design);
		LabelHandle label = factory.newLabel("Label1"); //$NON-NLS-1$

		SlotHandle slotHandle = designHandle.getBody();
		slotHandle.add(label);

		assertTrue(designHandle.needsSave());
		design.getActivityStack().undo();

		assertFalse(designHandle.needsSave());

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testEmbeddedImage() throws Exception {
		openDesign("EmbeddedImageTest.xml", ULocale.ENGLISH); //$NON-NLS-1$
		PropertyHandle images = designHandle.getPropertyHandle(ReportDesign.IMAGES_PROP);
		assertNotNull(images);

		// get the embedded images

		EmbeddedImageHandle image1 = (EmbeddedImageHandle) images.getAt(0);
		EmbeddedImageHandle image2 = (EmbeddedImageHandle) images.getAt(1);
		String image1Name = image1.getName();
		String image2Name = image2.getName();
		ArrayList imageList = new ArrayList();
		imageList.add(image1);
		imageList.add(image2);
		designHandle.dropImage(imageList);

		// undo and test again
		design.getActivityStack().undo();
		image1 = (EmbeddedImageHandle) images.getAt(0);
		image2 = (EmbeddedImageHandle) images.getAt(1);

		design.getActivityStack().redo();
		assertNull(images.getListValue());
		try {
			designHandle.dropImage(image1Name);
			designHandle.dropImage(image2Name);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * Tests attribute and dispose listeners in module.
	 * 
	 * @throws Exception
	 */

	public void testFileNameAndDisposeListener() throws Exception {
		FileNameListener fListener = new FileNameListener();
		designHandle.addAttributeListener(fListener);
		DisposeListener dListener = new DisposeListener();
		designHandle.addDisposeListener(dListener);

		designHandle.setFileName("test file"); //$NON-NLS-1$
		designHandle.close();
		assertEquals("test file", fListener.getStatus()); //$NON-NLS-1$
		assertEquals("disposed", dListener.getStatus()); //$NON-NLS-1$
		openDesign("ReportDesignHandleTest.xml"); //$NON-NLS-1$

		SelfDisposeListener dropListener = new SelfDisposeListener();

		designHandle.addDisposeListener(dropListener);
		designHandle.getModule().broadcastDisposeEvent(new DisposeEvent(designHandle.getModule()));
		assertEquals(1, dropListener.getStatus());

		designHandle.getModule().broadcastDisposeEvent(new DisposeEvent(designHandle.getModule()));
		assertEquals(1, dropListener.getStatus());
	}

	/**
	 * Tests the function to find the resource with the given file name. Test cases
	 * are:
	 * 
	 * <ul>
	 * <li>Uses the file path to find the relative resource.</li>
	 * <li>Uses network protocol to find the relative resource.</li>
	 * <li>Uses the file protcol to find the relative resource</li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testFindResource() throws Exception {
		designHandle.setFileName(null);
		designHandle.getModule().setSystemId(null);

		// uses the file path to find, file exists

		URL filePath = getResource(INPUT_FOLDER + "ReportDesignHandleTest.xml"); //$NON-NLS-1$

		designHandle.setFileName(filePath.toString());
		URL url = designHandle.findResource("ReportDesignHandleTest.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY);
		assertNotNull(url);

		// a file not existed.

		url = designHandle.findResource("NoExistedDesign.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY);
		assertNull(url);

		// resources with relative uri file path

		designHandle.setFileName(getResource(INPUT_FOLDER).toString() + "NoExistedDesign.xml"); //$NON-NLS-1$

		url = designHandle.findResource("ReportDesignHandleTest.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY);
		assertNotNull(url);

		url = designHandle.findResource("NoExistedDesign.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY);
		assertNull(url);

		// resources with HTTP protocols.

		designHandle.getModule().setSystemId(new URL("http://www.eclipse.org/")); //$NON-NLS-1$

		url = designHandle.findResource("images/EclipseBannerPic.jpg", //$NON-NLS-1$
				IResourceLocator.IMAGE);

		assertEquals("http://www.eclipse.org/images/EclipseBannerPic.jpg", //$NON-NLS-1$
				url.toString());

		// resources with HTTP protocols.

		url = designHandle.findResource("NoExistedDir/NoExistedDesign.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY);
		assertNotNull(url);

		// TODO:
		// // resources with both system id and path.
		//
		// File f = new File( filePath ).getParentFile( );
		//
		// designHandle.getModule( ).setSystemId( f.toURL( ) );
		//
		// url = designHandle.findResource( "ReportDesignHandleTest.xml",
		// //$NON-NLS-1$
		// IResourceLocator.LIBRARY );
		// assertNotNull( url );
		//
		// url = designHandle.findResource( "NoExistedDesign.xml", //$NON-NLS-1$
		// IResourceLocator.LIBRARY );
		// assertNull( url );
		//
		// f = new File( filePath );
		// url = designHandle.findResource( f.toURL( ).toString( ),
		// IResourceLocator.LIBRARY );
		// assertNotNull( url );
		//
		// // test with new feature "deploy resource in resource path"
		//
		// designHandle.setFileName( null );
		// designHandle.getModule( ).setSystemId( null );
		//
		// url = designHandle.findResource( getClassFolder( ) + GOLDEN_FOLDER
		// + "ActionHandleTest2_golden.xml", IResourceLocator.LIBRARY );
		// assertNotNull( url );
		//
		// url = null;
		// designHandle.getModule( ).getSession( ).setResourceFolder(
		// getClassFolder( ) + GOLDEN_FOLDER );
		// url = designHandle.findResource( "ActionHandleTest2_golden.xml",
		// //$NON-NLS-1$
		// IResourceLocator.LIBRARY );
		// assertNotNull( url );
		//
		// designHandle.getModule( ).getSession( ).setResourceFolder( null );
		// url = designHandle.findResource( "ActionHandleTest2_golden.xml",
		// //$NON-NLS-1$
		// IResourceLocator.LIBRARY );
		// assertNull( url );
	}

	/**
	 * Tests <code>setFileName</code> function. Cases are
	 * 
	 * <ul>
	 * <li>setFileName with HTTP protocol</li>
	 * <li>setFileName with HTTP protocol and Chinese character.</li>
	 * <li>setFileName with unix file schema.</li>
	 * <li>setFileName with windows file schema.</li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testSetFileName() throws Exception {

		// resources with HTTP protocols.

		designHandle.setFileName("http://www.eclipse.org/ima%23ge%20%20/s/"); //$NON-NLS-1$
		assertEqualsOnWindows("http://www.eclipse.org/ima%23ge%20%20/s/", designHandle //$NON-NLS-1$
				.getSystemId().toString());

		designHandle.setFileName("http://hello.com/\u4e0d\u5b58\u5728\u7684\u5355\u4f4d"); //$NON-NLS-1$
		assertEquals("http://hello.com/", designHandle //$NON-NLS-1$
				.getSystemId().toString());

		designHandle.setFileName("http://hello.com/\u4e0d\u5b58/index.rtpdesign"); //$NON-NLS-1$
		assertEquals("http://hello.com/\u4e0d\u5b58/", designHandle //$NON-NLS-1$
				.getSystemId().toString());

		designHandle.setFileName("/usr/home/birt/report.xml"); //$NON-NLS-1$
		assertEquals(new File("/usr/home/birt/report.xml").getParentFile().toURL() //$NON-NLS-1$
				.toString(), designHandle.getSystemId().toString());

		designHandle.setFileName("C:\\reports\\1.xml"); //$NON-NLS-1$
		assertEqualsOnWindows("file:/C:/reports", designHandle //$NON-NLS-1$
				.getSystemId().toString());

		// the file name with jar and zip protocol

		designHandle.setFileName("jar:file:/C:/reports/testRead.jar!/1.xml"); //$NON-NLS-1$
		assertEquals("jar:file:/C:/reports/testRead.jar!/", designHandle //$NON-NLS-1$
				.getSystemId().toString());

		designHandle.setFileName("jar:http://hello.com/reports/testRead.jar!/1.xml"); //$NON-NLS-1$
		assertEquals("jar:http://hello.com/reports/testRead.jar!/", designHandle //$NON-NLS-1$
				.getSystemId().toString());

		designHandle.setFileName("1.xml"); //$NON-NLS-1$
		assertNotNull(designHandle.getModule().getSystemId());
		assertEquals(new File("1.xml").getAbsoluteFile().getParentFile() //$NON-NLS-1$
				.toURL().toExternalForm(), designHandle.getSystemId().toString());

		designHandle.setFileName(
				"bundleresource://22868/samplereports/Reporting Feature Examples/Combination Chart/CustomerOrdersFinal.rptdesign"); //$NON-NLS-1$
		URL tmpURL = designHandle.getSystemId();

		assertEquals("bundleresource", tmpURL.getProtocol()); //$NON-NLS-1$
		assertEquals("/samplereports/Reporting Feature Examples/Combination Chart/", //$NON-NLS-1$
				tmpURL.getPath());
	}

	/**
	 * Tests the copy-rename-add methods about the embedded images.
	 * 
	 * @throws Exception
	 */

	public void testCopyAndPasteEmbeddedImage() throws Exception {
		openDesign("EmbeddedImageTest.xml"); //$NON-NLS-1$

		EmbeddedImage image = designHandle.findImage("image one"); //$NON-NLS-1$
		assertNotNull(image);

		EmbeddedImage newImage = (EmbeddedImage) image.copy();
		assertNotNull(newImage);
		assertEquals(image.getName(), newImage.getName());
		designHandle.rename(newImage);
		assertEquals(image.getName() + "1", newImage.getName()); //$NON-NLS-1$
		designHandle.addImage(newImage);
		assertEquals(newImage, designHandle.findImage(image.getName() + "1")); //$NON-NLS-1$
	}

	class FileNameListener implements IAttributeListener {

		private String status = null;

		public void fileNameChanged(ModuleHandle targetElement, AttributeEvent ev) {
			status = targetElement.getFileName();
		}

		public String getStatus() {
			return status;
		}
	}

	class DisposeListener implements IDisposeListener {

		private String status = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.core.IDisposeListener#elementDisposed
		 * (org.eclipse.birt.report.model.api.ModuleHandle,
		 * org.eclipse.birt.report.model.api.core.DisposeEvent)
		 */
		public void moduleDisposed(ModuleHandle targetElement, DisposeEvent ev) {
			status = "disposed"; //$NON-NLS-1$

		}

		public String getStatus() {
			return status;
		}

	}

	class SelfDisposeListener implements IDisposeListener {

		private int status = 0;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.api.core.IDisposeListener#elementDisposed
		 * (org.eclipse.birt.report.model.api.ModuleHandle,
		 * org.eclipse.birt.report.model.api.core.DisposeEvent)
		 */

		public void moduleDisposed(ModuleHandle targetElement, DisposeEvent ev) {
			targetElement.removeDisposeListener(this);
			status++;
		}

		/**
		 * @return
		 */
		protected int getStatus() {
			return status;
		}

	}

	/**
	 * @throws DesignFileException
	 * 
	 * 
	 */

	public void testCascadingParameters() throws DesignFileException {
		openDesign("ReportDesignHandleTest2.xml"); //$NON-NLS-1$
		CascadingParameterGroupHandle group1 = designHandle.findCascadingParameterGroup("Country-State-City"); //$NON-NLS-1$
		assertNotNull(group1);
		assertEquals(3, group1.getParameters().getCount());

		CascadingParameterGroupHandle group2 = designHandle.findCascadingParameterGroup("group2"); //$NON-NLS-1$
		assertNotNull(group2);
		assertEquals("Group2 displayName", group2.getDisplayName()); //$NON-NLS-1$

		CascadingParameterGroupHandle group3 = designHandle.findCascadingParameterGroup("group3"); //$NON-NLS-1$
		assertNotNull(group3);
		assertEquals("Group3 displayName", group3.getDisplayName()); //$NON-NLS-1$

		CascadingParameterGroupHandle group4 = designHandle.findCascadingParameterGroup("non-exsit-group"); //$NON-NLS-1$
		assertNull(group4);
	}

	/**
	 * Test cases: Test the getAllBookmarks method on the ModuleHandle which returns
	 * all the bookmarks defined in the report design.
	 * 
	 * @throws Exception
	 */

	public void testGetBookmarksAndTocs() throws Exception {
		openDesign("ReportDesignBookmark.xml"); //$NON-NLS-1$
		List bookmarks = designHandle.getAllBookmarks();

		assertEquals(4, bookmarks.size());
		assertEquals("bookmark_label", bookmarks.get(0)); //$NON-NLS-1$
		assertEquals("\"bookmark_group\"", bookmarks.get(1)); //$NON-NLS-1$
		assertEquals("bookmark_detail_row", bookmarks.get(2)); //$NON-NLS-1$
		assertEquals("bookmark_detail_text", bookmarks.get(3)); //$NON-NLS-1$

		List tocs = designHandle.getAllTocs();
		assertEquals(3, tocs.size());
		assertEquals("Toc_label", tocs.get(0)); //$NON-NLS-1$
		assertEquals("DateTimeSpan.days(2005/01/01, 2006/01/01)", tocs.get(1)); //$NON-NLS-1$
		assertEquals("toc_detail_text", tocs.get(2)); //$NON-NLS-1$
	}

	/**
	 * Test the rerpot design initialize method. After the initialize method is
	 * called, there should be one master page created for the report. The
	 * properties values given by the argument should be set to the report deisgn
	 * element. All thos operation should not go into command stack.
	 * 
	 * @throws SemanticException
	 * @throws IOException
	 */
	public void testInitializeReportDesign() throws SemanticException, IOException {

		SessionHandle sessionHandle = new SessionHandle((ULocale) null);
		designHandle = sessionHandle.createDesign();

		Map properties = new HashMap();
		String createdBy = "test initialize"; //$NON-NLS-1$

		// bad property key value.

		properties.put("Build", "2006-12-25");//$NON-NLS-1$//$NON-NLS-2$

		// good property key value.

		properties.put(ReportDesign.CREATED_BY_PROP, createdBy);

		designHandle.initializeModule(properties);

		assertEquals(0, designHandle.getMasterPages().getCount());
		assertNull(designHandle.getProperty("Build"));//$NON-NLS-1$
		assertEquals(createdBy, designHandle.getProperty(ReportDesign.CREATED_BY_PROP));

		CommandStack stack = designHandle.getCommandStack();

		assertFalse(stack.canRedo());
		assertFalse(stack.canUndo());

	}

	/**
	 * Does the equal assert only when the platform is windows.
	 * 
	 * @param expected
	 * @param actual
	 */

	private void assertEqualsOnWindows(String expected, String actual) {
		if (isWindowsPlatform())
			assertEquals(expected, actual);
	}

	/**
	 * Tests the getReportItemsBasedonTempalates method.
	 * 
	 * This method is supposed to return report items which holds a template
	 * definition, that is, report item in body slot and page slot. Notice, nested
	 * template items is excluded.
	 * 
	 * @throws DesignFileException
	 * 
	 */
	public void testGetReportItemsBasedonTempalates() throws DesignFileException {
		openDesign("ReportDesignHandleTest3.xml"); //$NON-NLS-1$

		List result = designHandle.getReportItemsBasedonTempalates();

		assertEquals(6, result.size());
		assertEquals("template table 1", ((DesignElementHandle) result.get(0)).getName()); //$NON-NLS-1$
		assertEquals("template inner table", ((DesignElementHandle) result.get(1)).getName()); //$NON-NLS-1$
		assertEquals("inner label", ((DesignElementHandle) result.get(2)).getName()); //$NON-NLS-1$
		assertEquals("tamplate label 2", ((DesignElementHandle) result.get(3)).getName()); //$NON-NLS-1$
		assertEquals("label 3", ((DesignElementHandle) result.get(4)).getName()); //$NON-NLS-1$
		assertEquals("label in master page", ((DesignElementHandle) result.get(5)).getName()); //$NON-NLS-1$

	}

	/**
	 * if the message file with the current locale existed, the defult one will not
	 * be allocated.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void testLoadMessageFiles() throws DesignFileException, SemanticException {

		openDesign("TestLoadMessageFiles.xml", ULocale.CHINA); //$NON-NLS-1$

		LabelHandle label1 = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		LabelHandle label2 = (LabelHandle) designHandle.findElement("label2"); //$NON-NLS-1$
		LabelHandle label3 = (LabelHandle) designHandle.findElement("label3"); //$NON-NLS-1$

		assertNotNull(label1);
		assertNotNull(label2);
		assertNotNull(label3);

		label1.setTextKey("keyone"); //$NON-NLS-1$
		label2.setTextKey("keytwo"); //$NON-NLS-1$
		label2.setTextKey("keythree"); //$NON-NLS-1$

		assertEquals("zh CN message file", label1.getDisplayText()); //$NON-NLS-1$
		assertEquals("key two", label2.getDisplayText()); //$NON-NLS-1$
		assertEquals("key three", label3.getDisplayText()); //$NON-NLS-1$

	}

	/**
	 * Tests getStyle , findStyle method
	 * 
	 * @throws Exception
	 */

	public void testGetTOCStyle() throws Exception {
		createDesign();
		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("Label1");//$NON-NLS-1$
		designHandle.getBody().add(labelHandle);

		SharedStyleHandle styleHandle = labelHandle.getModuleHandle().findStyle(TOCHandle.defaultTOCPrefixName + "2");//$NON-NLS-1$
		assertEquals("12pt", styleHandle.getFontSize().getStringValue());//$NON-NLS-1$

		labelHandle.setStyleName(TOCHandle.defaultTOCPrefixName + "1");//$NON-NLS-1$

		styleHandle = labelHandle.getStyle();
		assertNotNull(styleHandle);
		assertEquals("sans-serif", styleHandle.getFontFamilyHandle().getStringValue());//$NON-NLS-1$

	}

	/**
	 * Tests the method to get the version number of the report design.
	 * 
	 * @throws Exception
	 */

	public void testGetVersionNo() throws Exception {
		assertEquals("3.2.20", designHandle.getVersion()); //$NON-NLS-1$

		createDesign();

		assertNull(designHandle.getVersion());
	}

	/**
	 * Tests the method to get the included script file of the libraries.
	 * 
	 * @throws Exception
	 */

	public void testIncludeScriptsIterator() throws Exception {
		openDesign("IncludedScriptFileTest.xml"); //$NON-NLS-1$
		Iterator scriptIter = designHandle.includeLibraryScriptsIterator();
		IncludeScriptHandle script = (IncludeScriptHandle) scriptIter.next();
		assertEquals("script first", script.getFileName()); //$NON-NLS-1$
		script = (IncludeScriptHandle) scriptIter.next();
		assertEquals("script second", script.getFileName()); //$NON-NLS-1$
	}

	/**
	 * Tests methods for include scripts values.
	 * 
	 * @throws Exception
	 */

	public void testIncludeScripts() throws Exception {
		openDesign("ReportDesignIncludeScriptParseTest.xml", ULocale.ENGLISH); //$NON-NLS-1$

		IncludeScript includeScript = new IncludeScript();
		includeScript.setFileName(null);

		try {
			designHandle.addIncludeScript(includeScript);
			fail("Not allowed set invalid value ");//$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());
		}

		List scriptList = designHandle.getAllIncludeScripts();
		assertEquals(2, scriptList.size());

		includeScript.setFileName("third"); //$NON-NLS-1$
		designHandle.addIncludeScript(includeScript);

		designHandle.shifIncludeScripts(0, 2);

		Iterator iter1 = designHandle.includeScriptsIterator();
		IncludeScriptHandle scriptHandle = (IncludeScriptHandle) iter1.next();
		assertEquals("second", scriptHandle.getFileName());//$NON-NLS-1$
		scriptHandle = (IncludeScriptHandle) iter1.next();
		assertEquals("third", scriptHandle.getFileName());//$NON-NLS-1$
		scriptHandle = (IncludeScriptHandle) iter1.next();
		assertEquals("first", scriptHandle.getFileName());//$NON-NLS-1$

		designHandle.dropIncludeScript(includeScript);
		assertEquals(2, designHandle.getListProperty(ReportDesignHandle.INCLUDE_SCRIPTS_PROP).size());
	}

	/**
	 * Copy a design as it is. That is, not to flatten any property that may extends
	 * to the library.
	 * 
	 * @throws Exception
	 */

	public void testCopyDesign() throws Exception {
		openDesign("ReportDesignCopyTest.xml"); //$NON-NLS-1$

		ReportDesignHandle designHandle1 = (ReportDesignHandle) designHandle.copy().getHandle(null);
		save(designHandle1);

		assertTrue(compareFile("ReportDesignCopyTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests copy report design which contains template parameter definition.
	 * 
	 * @throws Exception
	 */
	public void testCopyTemplateParameterDefinition() throws Exception {
		openDesign("CopyTemplateParameterDefinitionTest.xml");//$NON-NLS-1$

		// test the template parameter definition in the original report design.
		SlotHandle slot = designHandle.getSlot(IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT);
		assertEquals(1, slot.getCount());

		// test the template parameter definition in the copy report design.
		ReportDesignHandle copyDesignHandle = (ReportDesignHandle) designHandle.copy().getHandle(null);

		slot = copyDesignHandle.getSlot(IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT);
		assertEquals(0, slot.getCount());

	}

	public void testGetFlattenElementName() throws Exception {
		openDesign("ReportDesignHandleTest4.xml"); //$NON-NLS-1$

		ReportDesignSerializer visitor = new ReportDesignSerializer();

		design.apply(visitor);

		ReportDesignHandle flattenDesign = (ReportDesignHandle) visitor.getTarget().getModuleHandle();

		DataSetHandle derivedDataSet = flattenDesign.findDataSet("union"); //$NON-NLS-1$
		assertNotNull(derivedDataSet);

		DesignElementHandle dataSet1 = flattenDesign.getFlattenElement(derivedDataSet, "ds1"); //$NON-NLS-1$
		DesignElementHandle dataSet2 = flattenDesign.getFlattenElement(derivedDataSet, "ds2"); //$NON-NLS-1$
		assertNotNull(dataSet1);
		assertNotNull(dataSet2);
		assertEquals("ds11", dataSet1.getName()); //$NON-NLS-1$
		assertEquals("ds21", dataSet2.getName()); //$NON-NLS-1$
		assertTrue(dataSet1 instanceof DataSetHandle);
		assertTrue(dataSet2 instanceof DataSetHandle);
		assertEquals("Lib DataSet 1", ((DataSetHandle) dataSet1) //$NON-NLS-1$
				.getDisplayName());
		assertEquals("Lib DataSet 2", ((DataSetHandle) dataSet2) //$NON-NLS-1$
				.getDisplayName());

		assertNull(flattenDesign.getFlattenElement(null, "ds1")); //$NON-NLS-1$
		assertNull(flattenDesign.getFlattenElement(derivedDataSet, "NonExistedName")); //$NON-NLS-1$
	}

}
