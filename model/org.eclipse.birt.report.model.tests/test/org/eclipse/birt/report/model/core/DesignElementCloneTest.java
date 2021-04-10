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

package org.eclipse.birt.report.model.core;

import java.io.IOException;
import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test the clone feature of
 * <code>{@link org.eclipse.birt.report.model.core.DesignElement}</code> Clone
 * the original design element, and use the <code>rename</code> method, which is
 * provided by the
 * <code>{@link org.eclipse.birt.report.model.api.ReportDesignHandle#rename(DesignElementHandle)}</code>
 * to check whether is there any duplicate name existed. Then add the cloned
 * element into the design tree again. Use the golden file to check the cloned
 * result.
 * 
 */

public class DesignElementCloneTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

	}

	/**
	 * Tests the clone method of
	 * <code>{@link org.eclipse.birt.report.model.core.DesignElement}</code>.
	 * 
	 * @throws Exception If the test fails.
	 */

	public void testCloneLabel() throws Exception {
		openDesign("DesignElementTest_1.xml"); //$NON-NLS-1$
		Label label = (Label) design.findElement("label1"); //$NON-NLS-1$
		assertNotNull(label);

		DesignElement element = (DesignElement) label.clone();
		assertNotNull(element);
	}

	/**
	 * Tests cloning the grid element and renaming it. Add the new grid into the
	 * design tree.
	 * 
	 * @throws Exception if any exception
	 */

	public void testCloneGrid() throws Exception {
		openDesign("DesignElementTest_3.xml"); //$NON-NLS-1$

		GridHandle gridHandle = (GridHandle) designHandle.findElement("hexingjie"); //$NON-NLS-1$
		assertNotNull(gridHandle);
		assertEquals("hexingjie", gridHandle.getName()); //$NON-NLS-1$

		GridHandle grid = (GridHandle) gridHandle.copy().getHandle(design);
		assertNotNull(grid);
		assertEquals("hexingjie", grid.getName()); //$NON-NLS-1$
		RowHandle row = (RowHandle) grid.getRows().get(0);
		assertEquals(grid, row.getContainer());

		designHandle.rename(grid);
		designHandle.getBody().paste(grid);

		save();

		assertTrue(compareFile("DeisgnElementCloneTest_gridClone_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests the style reference in new element.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testCloneImage() throws Exception {

		openDesign("DesignElementCloneTest_ImageClone.xml"); //$NON-NLS-1$

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("Image1"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		assertEquals("Image1", imageHandle.getName()); //$NON-NLS-1$

		ImageHandle image = (ImageHandle) imageHandle.copy().getHandle(design);
		assertNotNull(image);

		designHandle.rename(image);
		designHandle.findMasterPage("My Page").getSlot( //$NON-NLS-1$
				GraphicMasterPage.CONTENT_SLOT).add(image);

		imageHandle = (ImageHandle) designHandle.findElement("Image3"); //$NON-NLS-1$
		assertNotNull(imageHandle);
		assertEquals("Image3", imageHandle.getName()); //$NON-NLS-1$

		image = (ImageHandle) imageHandle.copy().getHandle(design);
		assertNotNull(image);

		designHandle.rename(image);
		designHandle.findMasterPage("My Page").getSlot( //$NON-NLS-1$
				GraphicMasterPage.CONTENT_SLOT).paste(image);

		save();
		assertTrue(compareFile("DesignElementCloneTest_ImageClone_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests the extend relationship in element.
	 * <ul>
	 * <li>Extends is not cloned
	 * <li>Derived is not cloned
	 * </ul>
	 * 
	 * @throws Exception if any exception.
	 */

	public void testCloneExtendedLabel() throws Exception {

		openDesign("DesignElementCloneTest.xml"); //$NON-NLS-1$

		// The derived list is not cloned

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("Base Label"); //$NON-NLS-1$
		assertNotNull(labelHandle);
		assertEquals(1, labelHandle.getElement().getDerived().size());

		LabelHandle label = (LabelHandle) labelHandle.copy().getHandle(design);
		assertNotNull(label);
		assertEquals(0, label.getElement().getDerived().size());
		design.makeUniqueName(label.getElement());
		designHandle.getComponents().paste(label);

		// Add again.

		labelHandle = (LabelHandle) designHandle.findElement(label.getName());
		label = (LabelHandle) labelHandle.copy().getHandle(design);
		design.makeUniqueName(label.getElement());
		designHandle.getComponents().paste(label);
	}

	/**
	 * Tests the listeners in new element.
	 * <ul>
	 * <li>Listener is not cloned.
	 * <li>The container is not cloned
	 * </ul>
	 * 
	 * @throws Exception if any exception
	 */

	public void testTheReferencesAfterClone() throws Exception {

		openDesign("DesignElementCloneTest.xml"); //$NON-NLS-1$

		GraphicMasterPageHandle pageHandle = (GraphicMasterPageHandle) designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		assertNotNull(pageHandle);

		pageHandle.addListener(new MyListener("listener1")); //$NON-NLS-1$
		pageHandle.addListener(new MyListener("listener2")); //$NON-NLS-1$

		assertEquals(2, CoreTestUtil.getListeners(pageHandle.getElement()).size());
		assertEquals(design, pageHandle.getContainer());

		GraphicMasterPageHandle masterPage = (GraphicMasterPageHandle) pageHandle.copy().getHandle(design);

		// the listeners should be set to null after clone.

		assertNull(CoreTestUtil.getListeners(masterPage.getElement()));

		// the container should not be kept after clone

		assertNull(masterPage.getContainer());

		DataSetHandle dataset = designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		DataSetHandle newDataSet = (DataSetHandle) dataset.copy().getHandle(design);

		dataset = newDataSet;

		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle newDesignHandle = sessionHandle.createDesign();

		newDesignHandle.getDataSets().paste(dataset);

		designHandle.checkReport();
		List list = designHandle.getErrorList();
		assertEquals(3, list.size());
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF, ((ErrorDetail) list.get(0)).getErrorCode());
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_STRUCTURE_REF, ((ErrorDetail) list.get(1)).getErrorCode());
		assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_STRUCTURE_REF, ((ErrorDetail) list.get(2)).getErrorCode());
		assertEquals(1, designHandle.getWarningList().size());

	}

	/**
	 * Tests the style in new element.
	 * <ul>
	 * <li>Style is not cloned
	 * <li>Cached element definition is not cloned
	 * </ul>
	 * 
	 * @throws Exception if any exception.
	 */

	public void testStyleAfterClone() throws Exception {

		openDesign("DesignElementCloneTest.xml"); //$NON-NLS-1$

		// Copy style and check clients

		SharedStyleHandle styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		assertNotNull(styleHandle);
		assertNotNull(CoreTestUtil.getCachedElementDefn(styleHandle.getElement()));

		List client = ((Style) styleHandle.getElement()).getClientList();
		assertEquals(3, client.size());

		StyleHandle style = (StyleHandle) styleHandle.copy().getHandle(design);
		assertNotNull(style);
		// assertNull( style.cachedDefn );

		assertEquals(0, ((Style) style.getElement()).getClientList().size());

		// Copy styled element and check style

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("Base Label"); //$NON-NLS-1$
		assertNotNull(labelHandle);
		assertNotNull(labelHandle.getStyle());

		LabelHandle page = (LabelHandle) labelHandle.copy().getHandle(design);
		designHandle.rename(page);
		labelHandle = page;

		designHandle.getBody().paste(labelHandle);

		// resolved style reference

		assertEquals(styleHandle, labelHandle.getStyle());
		assertEquals("My-Style", page.getStyle().getName()); //$NON-NLS-1$
	}

	/**
	 * Tests cloning table and grid. The new element has new rows and cells.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testCloneTableAndGrid() throws Exception {
		openDesign("DesignElementCloneTest_Container.xml"); //$NON-NLS-1$

		// Clone a table

		TableHandle tableHandle = (TableHandle) designHandle.findElement("my table1"); //$NON-NLS-1$
		assertEquals(1, tableHandle.getDetail().getCount());
		TableHandle newTable = (TableHandle) tableHandle.copy().getHandle(design);
		newTable.setName("new table 1"); //$NON-NLS-1$
		designHandle.getSlot(ReportDesign.BODY_SLOT).paste(newTable);

		// Drop the old table and check the new one.

		tableHandle.dropAndClear();

		TableHandle newTableHandle = newTable;
		assertEquals(newTableHandle, newTableHandle.getDetail().get(0).getContainer());
		assertEquals(1, newTableHandle.getDetail().getCount());

		// Clone a grid

		GridHandle grid = (GridHandle) designHandle.findElement("my grid1"); //$NON-NLS-1$
		GridHandle newGrid = (GridHandle) grid.copy().getHandle(design);
		newGrid.setName("new grid 1"); //$NON-NLS-1$

		designHandle.rename(newGrid);

		designHandle.getSlot(ReportDesign.BODY_SLOT).paste(newGrid);

		// Drop the old grid and check the new one

		grid.dropAndClear();

		GridHandle newGridHandle = newGrid;
		assertEquals(2, newGridHandle.getRows().getCount());

		// make sure there is no NPE to copy empty tables.

		tableHandle = designHandle.getElementFactory().newTableItem("table3"); //$NON-NLS-1$
		tableHandle.copy();
	}

	/**
	 * Test cases:
	 * 
	 * Test element id for the clone case.
	 * 
	 * @throws DesignFileException
	 * @throws ContentException
	 * @throws NameException
	 * @throws IOException
	 */

	public void testElementId() throws DesignFileException, ContentException, NameException, IOException {
		MetaDataDictionary.getInstance().enableElementID();
		openDesign("DesignElementCloneTest.xml"); //$NON-NLS-1$

		StyleHandle myStyle = (StyleHandle) designHandle.getStyles().get(0);

		StyleHandle newStyle = (StyleHandle) myStyle.copy().getHandle(design);
		assertEquals(myStyle.getID(), newStyle.getID());
		assertEquals(4, newStyle.getID());

		designHandle.rename(newStyle);
		designHandle.getStyles().paste(newStyle);

		assertFalse(newStyle.getID() == myStyle.getID());
	}

	/**
	 * Tests the clone method of the module elements, such as report design and
	 * library.
	 * 
	 * @throws Exception
	 */

	public void testDesignWithIncludeLibrary() throws Exception {
		openDesign("DesignElementCloneTest_Module.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);
		assertNotNull(design);

		ReportDesign clonedDesign = (ReportDesign) design.clone();
		assertNotNull(clonedDesign);

		assertNotSame(design, clonedDesign);

		// tests some simple properties

		assertNotSame(design.getActivityStack(), clonedDesign.getActivityStack());
		assertNotNull(clonedDesign.getActivityStack());
		assertEquals(3, clonedDesign.getAllExceptions().size());
		assertNull(clonedDesign.getFatalException());

		// test "theme" property

		ElementRefValue theme = (ElementRefValue) clonedDesign.getProperty(clonedDesign,
				ISupportThemeElementConstants.THEME_PROP);
		assertNotNull(theme);
		assertEquals("theme1", theme.getName()); //$NON-NLS-1$
		assertNull(theme.getElement());

		// tests id-map

		assertEquals(clonedDesign, clonedDesign.getElementByID(1));
		testID(design, clonedDesign, 2, ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT);
		testID(design, clonedDesign, 3, ReportDesignConstants.FREE_FORM_ITEM);
		testID(design, clonedDesign, 4, ReportDesignConstants.LABEL_ITEM);
		assertEquals(5, design.getNextID(), clonedDesign.getNextID());

		// tests namespace

		testNameSpace(design, clonedDesign, ReportDesign.PAGE_NAME_SPACE, "page", //$NON-NLS-1$
				ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT);
		testNameSpace(design, clonedDesign, ReportDesign.ELEMENT_NAME_SPACE, "freeForm", //$NON-NLS-1$
				ReportDesignConstants.FREE_FORM_ITEM);
		testNameSpace(design, clonedDesign, ReportDesign.ELEMENT_NAME_SPACE, "label", ReportDesignConstants.LABEL_ITEM); //$NON-NLS-1$

		// tests included libraries

		List libraries = clonedDesign.getAllLibraries();
		assertEquals(1, design.getAllLibraries().size(), libraries.size());
		assertNotSame(design.getAllLibraries().get(0), clonedDesign.getAllLibraries().get(0));
		assertEquals(clonedDesign, ((Library) clonedDesign.getAllLibraries().get(0)).getHost());

	}

	/**
	 * Tests the style property get be retrieved properly from factorty property
	 * handle after the design is copied.
	 * 
	 * @throws DesignFileException
	 */
	public void testElementResolveAfterClone() throws DesignFileException {

		openDesign("TestElementResolveAfterClone.xml"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("myTable"); //$NON-NLS-1$

		assertEquals("red", table.getProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("red", table.getFactoryPropertyHandle(Style.COLOR_PROP).getValue()); //$NON-NLS-1$

		ReportDesignHandle myDesignHandle = (ReportDesignHandle) designHandle.copy().getHandle(null);
		table = (TableHandle) myDesignHandle.findElement("myTable"); //$NON-NLS-1$
		assertEquals("red", table.getProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("red", table.getFactoryPropertyHandle(Style.COLOR_PROP).getValue()); //$NON-NLS-1$
	}

	/**
	 * Tests the id-map of the cloned module.
	 * 
	 * @param module       the original module
	 * @param clonedModule the cloned module
	 * @param id           the element id to test
	 * @param elementName  the element type name
	 */

	private void testID(Module module, Module clonedModule, long id, String elementName) {
		assert module != null;
		assert clonedModule != null;

		assertNotNull(clonedModule.getElementByID(id));
		assertNotSame(module.getElementByID(id), clonedModule.getElementByID(id));
		assertEquals(elementName, module.getElementByID(id).getElementName(),
				clonedModule.getElementByID(id).getElementName());
	}

	/**
	 * Tests the namespace of the cloned module.
	 * 
	 * @param module       the original module
	 * @param clonedModule the cloned module
	 * @param id           the namespace id
	 * @param name         the element name to check
	 * @param elementName  the element type name of the element
	 */

	private void testNameSpace(Module module, Module clonedModule, String id, String name, String elementName) {
		assert module != null;
		assert clonedModule != null;

		NameSpace ns = module.getNameHelper().getNameSpace(id);
		NameSpace clonedNs = clonedModule.getNameHelper().getNameSpace(id);
		assertNotSame(ns, clonedNs);

		assertNotNull(ns.getElement(name));
		assertNotNull(clonedNs.getElement(name));
		assertNotSame(ns.getElement(name), clonedNs.getElement(name));
		assertEquals(elementName, ns.getElement(name).getElementName(), clonedNs.getElement(name).getElementName());
	}

	class MyListener implements Listener {

		String name = null;

		ContentEvent event = null;

		MyListener(String name) {
			this.name = name;
		}

		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			return;

		}
	}

	/**
	 * test if the display name is exist or not after cloning
	 * 
	 * @throws Exception
	 */
	public void testDisplayNameAfterClone() throws Exception {
		String name = "DesignElementCloneTest_DisplayName.xml"; //$NON-NLS-1$
		openDesign(name);
		DataSet ds = (DataSet) design.findDataSet("Data Set"); //$NON-NLS-1$
		DataSet copyDs = (DataSet) ds.clone();
		assertNull(copyDs.getProperty(design.getRoot(), DesignElement.DISPLAY_NAME_PROP));
		assertNull(copyDs.getProperty(design.getRoot(), DesignElement.DISPLAY_NAME_ID_PROP));

		DataSource source = (DataSource) design.findDataSource("Data Source"); //$NON-NLS-1$
		DataSource copySource = (DataSource) source.clone();
		assertNull(copySource.getProperty(design.getRoot(), DesignElement.DISPLAY_NAME_PROP));
		assertNull(copySource.getProperty(design.getRoot(), DesignElement.DISPLAY_NAME_ID_PROP));

	}

	/**
	 * Tests if the the extension properties are cloned correctly.
	 * 
	 * @throws Exception
	 */

	public void testExtensionPropertyAfterClone() throws Exception {
		String name = "DesignElementCloneTest_ExtensionProperty.xml"; //$NON-NLS-1$
		openDesign(name);

		DesignElementHandle testTable = designHandle.findElement("testTable"); //$NON-NLS-1$
		List valueList = testTable.getListProperty("filter"); //$NON-NLS-1$
		FilterConditionElementHandle filterElementHandle = (FilterConditionElementHandle) valueList.get(0);
		DesignElement element = filterElementHandle.getElement();

		// validate the element has the container.

		assertNotNull(element.getContainer());

		ExtendedItemHandle copyExtendedItemHandle = (ExtendedItemHandle) testTable.copy().getHandle(design);

		List copyvalueList = copyExtendedItemHandle.getListProperty("filter"); //$NON-NLS-1$
		FilterConditionElementHandle copyFilterElementHandle = (FilterConditionElementHandle) copyvalueList.get(0);
		DesignElement copyFilterElement = copyFilterElementHandle.getElement();

		// validate the copy element has the container

		assertNotNull(copyFilterElement.getContainer());
	}

}