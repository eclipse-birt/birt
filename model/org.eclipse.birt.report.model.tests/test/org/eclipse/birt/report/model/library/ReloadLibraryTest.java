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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.LibraryReloadedEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests whether report design can handle the cases of loading libraires.
 */

public class ReloadLibraryTest extends BaseTestCase {

	/**
	 * Test reloading a library.
	 * <p>
	 * 1) If library is reloaded, its element references are updated.
	 * <p>
	 * <strong>Case1:</strong>
	 * <p>
	 * Design.table1 -> lib1.libTable1
	 * <p>
	 * The structure is synchronized with table in library. Local values of virtual
	 * elements are kept.
	 * <p>
	 * <strong>Case2:</strong>
	 * <p>
	 * lib1.theme1.style1
	 * <p>
	 * If style is removed, the design can only find the style name, not style
	 * instance.
	 * <p>
	 * <strong>Case3:</strong>
	 * <p>
	 * name space
	 * <p>
	 * If the libTable1 drops 2 rows, Designl.table1 will not see these rows any
	 * more. And names of report elements in these rows are removed from the
	 * namespace.
	 * <p>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibrary() throws Exception {
		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "DesignToReloadLibrary.xml"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "LibraryToReload.xml"); //$NON-NLS-1$

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);
		String designFilePath = (String) filePaths.get(0);
		openDesign(designFilePath, false);

		// tests in name sapces,

		NameSpace ns = designHandle.getModule().getNameHelper().getNameSpace(ReportDesign.ELEMENT_NAME_SPACE);
		assertEquals(7, ns.getCount());

		// tests element references.

		TableHandle table1 = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		LabelHandle label1 = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$

		assertEquals(ColorPropertyType.RED, table1.getStringProperty(Style.COLOR_PROP));

		assertEquals(ColorPropertyType.RED, label1.getStringProperty(Style.COLOR_PROP));

		assertEquals(ColorPropertyType.RED, label1.getStringProperty(Style.COLOR_PROP));

		// verify resolved element references.

		TableHandle parent = (TableHandle) table1.getExtends();
		assertNotNull(parent);

		RowHandle rowHandle = (RowHandle) table1.getHeader().get(0);
		CellHandle cellHandle = (CellHandle) rowHandle.getCells().get(0);
		assertEquals(cellHandle.getStringProperty(StyleHandle.COLOR_PROP), ColorPropertyType.BLUE);

		TableHandle table2 = (TableHandle) designHandle.findElement("table2");//$NON-NLS-1$
		assertNotNull(table2.getExtends());
		assertEquals(table2.getStringProperty(StyleHandle.COLOR_PROP), ColorPropertyType.LIME);

		// make modification on its library.

		String libFilePath = (String) filePaths.get(1);

		openLibrary(libFilePath, false);

		TableHandle libTable1 = (TableHandle) libraryHandle.findElement("libTable1"); //$NON-NLS-1$

		// drop the style1 in the theme

		((ThemeHandle) libraryHandle.getThemes().get(0)).getStyles().drop(0);

		// drop 1st and 2nd rows in table detail.

		libTable1.getDetail().drop(0);
		libTable1.getDetail().drop(0);

		assertEquals(0, libTable1.getDetail().getCount());

		// parent element is removed

		TableHandle libTable2 = (TableHandle) libraryHandle.findElement("libTable2"); //$NON-NLS-1$
		libTable2.drop();

		libraryHandle.save();

		// setup the listener

		MyLibraryListener libraryListener = new MyLibraryListener();
		designHandle.addListener(libraryListener);

		designHandle.reloadLibrary(libraryHandle);

		assertEquals(1, libraryListener.events.size());
		assertTrue(libraryListener.events.get(0) instanceof LibraryReloadedEvent);

		// test the count in namespace

		assertEquals(3, ns.getCount());

		// test element references. Theme was dropped so that cannot get color.

		assertEquals(ColorPropertyType.BLACK, table1.getStringProperty(Style.COLOR_PROP));

		assertEquals(ColorPropertyType.BLACK, label1.getStringProperty(Style.COLOR_PROP));

		assertNull(label1.getStyle());
		assertEquals("style1", label1 //$NON-NLS-1$
				.getStringProperty(StyledElement.STYLE_PROP));

		// test the structure change.

		assertEquals(0, table1.getDetail().getCount());
		assertEquals(1, table1.getHeader().getCount());

		// test undo/redo, activity stack is flused after reloadLibrary.

		assertFalse(designHandle.getCommandStack().canRedo());
		assertFalse(designHandle.getCommandStack().canUndo());

		save();
		assertTrue(compareFile("DesignToReloadLibrary_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>remove the table lib1.libTable1 from the library and add a grid
	 * lib1.libTable1. Design.table1 can not be resolved to gri lib1.libTable1.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibraryWithInvalidExtends() throws Exception {
		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "DesignToReloadLibrary.xml"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "LibraryToReload.xml"); //$NON-NLS-1$

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);
		String designFilePath = (String) filePaths.get(0);
		openDesign(designFilePath, false);

		String libFilePath = (String) filePaths.get(1);

		// make modification on its library.

		openLibrary(libFilePath, false);

		libraryHandle.findElement("libTable2").drop(); //$NON-NLS-1$

		GridHandle grid = libraryHandle.getElementFactory().newGridItem("libTable2", 2, 2); //$NON-NLS-1$

		libraryHandle.getComponents().add(grid);
		libraryHandle.save();

		designHandle.reloadLibrary(libraryHandle);

		TableHandle table2 = (TableHandle) designHandle.findElement("table2");//$NON-NLS-1$
		assertNull(table2.getExtends());

		assertEquals("Lib1.libTable2", table2.getElement().getExtendsName()); //$NON-NLS-1$
	}

	/**
	 * Reloads the library with exceptions.
	 * 
	 * @throws Exception
	 */

	public void testReloadLibraryWithException() throws Exception {
		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "DesignToReloadLibrary.xml"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "LibraryToReload.xml"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "Library_1.xml"); //$NON-NLS-1$

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);
		String designFilePath = (String) filePaths.get(0);
		openDesign(designFilePath, false);

		String lib1FilePath = (String) filePaths.get(2);
		openLibrary(lib1FilePath, false);

		// When reload library, if the library is not found, exception should
		// not be thrown. see bug 246664.
		designHandle.reloadLibrary(libraryHandle);

		String libFilePath = (String) filePaths.get(1);

		openLibrary(libFilePath, false);

		File f = new File(libFilePath);
		if (f.exists())
			f.delete();

		designHandle.reloadLibrary(libraryHandle);
		assertNull(designHandle.findElement("table1").getExtends()); //$NON-NLS-1$
		assertNotNull(designHandle.findElement("table1").getStringProperty(DesignElementHandle.EXTENDS_PROP)); //$NON-NLS-1$

		InputStream fis = getResourceAStream(INPUT_FOLDER + "LibraryToReload_errors.xml"); //$NON-NLS-1$
		FileOutputStream fos = new FileOutputStream(f);

		byte[] data = new byte[10000];
		fis.read(data);
		fos.write(data);

		fis.close();
		fos.close();

		// When reload library, if the library is not found, exception should
		// not be thrown. see bug 246664.
		designHandle.reloadLibrary(libraryHandle);

		save();

		assertTrue(compareFile("DesignToReloadLibrary_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Test reloading a library, in which there is only a label.
	 * <p>
	 * <strong>Case1:</strong>
	 * <p>
	 * lib1.label1
	 * <p>
	 * if change the color and text of the label, after the loading, it should show
	 * the effect.
	 * <p>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibrary1() throws Exception {
		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "DesignToReloadLibrary1.xml"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "LibraryToReload1.xml"); //$NON-NLS-1$

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);
		String designFilePath = (String) filePaths.get(0);
		openDesign(designFilePath, false);

		LabelHandle label1 = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$

		assertEquals("aaa", label1.getText()); //$NON-NLS-1$

		String libFilePath = (String) filePaths.get(1);
		openLibrary(libFilePath, false);

		LabelHandle libLabel1 = (LabelHandle) libraryHandle.findElement("libLabel1"); //$NON-NLS-1$

		libLabel1.setText("bbb"); //$NON-NLS-1$
		libLabel1 = (LabelHandle) libraryHandle.findElement("libPageLabel1"); //$NON-NLS-1$

		libLabel1.setText("ccc"); //$NON-NLS-1$

		libraryHandle.save();

		designHandle.reloadLibrary(libraryHandle);
		libraryHandle = designHandle.getLibrary("Lib1"); //$NON-NLS-1$

		libLabel1 = (LabelHandle) libraryHandle.findElement("libLabel1"); //$NON-NLS-1$

		assertEquals("bbb", libLabel1.getText()); //$NON-NLS-1$
		assertEquals("bbb", label1.getText()); //$NON-NLS-1$

		SimpleMasterPageHandle page = (SimpleMasterPageHandle) designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		assertEquals(1, page.getPageHeader().getCount());
		libLabel1 = (LabelHandle) page.getPageHeader().get(0);
		assertEquals("ccc", libLabel1.getText()); //$NON-NLS-1$
	}

	/**
	 * Test reloading a library, in which there is only a label.
	 * <p>
	 * <strong>Case1:</strong>
	 * <p>
	 * both resource path and report folder has the library file.
	 * <p>
	 * Changed resource path to null. And try to reload(). No exception. And the
	 * library location becomes library in the report folder.
	 * <p>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibrary2() throws Exception {
		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "DesignToReloadLibrary.xml"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "LibraryToReload.xml"); //$NON-NLS-1$
		fileNames.add("/org/eclipse/birt/report/model/library/" + INPUT_FOLDER //$NON-NLS-1$
				+ "LibraryToReload.xml"); //$NON-NLS-1$

		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(null);
		assertNotNull(sessionHandle);

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);
		String apiFilePath = (String) filePaths.get(2);
		int lastSlash = apiFilePath.lastIndexOf("/"); //$NON-NLS-1$
		String apiFileDir = ""; //$NON-NLS-1$
		if (lastSlash != -1) {
			apiFileDir = apiFilePath.substring(0, lastSlash + 1);
		}

		sessionHandle.setResourceFolder(apiFileDir);

		// must use the same session, cannot call openDesign.

		String designFilePath = (String) filePaths.get(0);
		designHandle = sessionHandle.openDesign(designFilePath);

		libraryHandle = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		assertNotNull(libraryHandle);

		String location1 = libraryHandle.getModule().getLocation();
		sessionHandle.setResourceFolder(null);

		designHandle.reloadLibrary(libraryHandle);

		libraryHandle = designHandle.getLibrary("Lib1"); //$NON-NLS-1$
		assertNotNull(libraryHandle);

		String location2 = libraryHandle.getModule().getLocation();

		assertFalse(location1.equalsIgnoreCase(location2));
	}

	/**
	 * Tests reload library for cube.
	 * 
	 * @throws Exception
	 */
	public void testReloadLibrary3() throws Exception {
		openDesign("DesignWithCube.xml"); //$NON-NLS-1$
		designHandle.reloadLibraries();
		save();
		assertTrue(compareFile("DesignWithCube_golden.xml")); //$NON-NLS-1$
	}

	private static class MyLibraryListener implements Listener {

		List events = new ArrayList();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */

		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			events.add(ev);
		}
	}

	/**
	 * Copies a bunch of design/library files to the temporary folder.
	 * 
	 * @param fileNames the design/library file names. The first item is the main
	 *                  design file.
	 * @return the file path of the design file
	 * @throws Exception
	 */

	private List dumpDesignAndLibrariesToFile(List fileNames) throws Exception {
		List filePaths = new ArrayList();
		for (int i = 0; i < fileNames.size(); i++) {
			String resourceName = (String) fileNames.get(i);
			filePaths.add(copyContentToFile(resourceName));
		}

		return filePaths;
	}

	/**
	 * Tests needSave method.
	 * 
	 * Only change happens directly on report design, isDirty mark of report design
	 * is true. So when library changed, isDirty mark of report design should be
	 * false.
	 * 
	 * <ul>
	 * <li>reload error library and throw out exception</li>
	 * <li>isDirty not changed</li>
	 * 
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testErrorLibraryNeedsSave() throws Exception {
		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "DesignWithSaveStateTest.xml"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "LibraryWithSaveStateTest.xml"); //$NON-NLS-1$

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);
		String designFilePath = (String) filePaths.get(0);
		openDesign(designFilePath, false);

		String libFilePath = (String) filePaths.get(1);
		openLibrary(libFilePath, false);

		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("new test label");//$NON-NLS-1$
		designHandle.getBody().add(labelHandle);

		assertTrue(designHandle.needsSave());
		assertTrue(designHandle.getCommandStack().canUndo());
		assertFalse(designHandle.getCommandStack().canRedo());

		ActivityStack stack = (ActivityStack) designHandle.getCommandStack();

		File f = new File(libFilePath);
		RandomAccessFile raf = new RandomAccessFile(f, "rw");//$NON-NLS-1$

		// Seek to end of file
		raf.seek(906);

		// Append to the end
		raf.writeBytes("<label name=\"NewLabel1\"/>");//$NON-NLS-1$
		raf.close();

		// reloadlibrary

		try {
			designHandle.reloadLibrary(libraryHandle);
			fail();
		} catch (DesignFileException e) {

		}

		assertTrue(stack.canUndo());
		assertFalse(stack.canRedo());
		assertEquals(1, stack.getCurrentTransNo());
		assertTrue(designHandle.needsSave());

		// restore file

		libraryHandle.close();
		libraryHandle = null;
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>modify the extended item in library, then reload the design, the design
	 * should be updated.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testReloadLibraryWithExtendedItem() throws Exception {
		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "DesignToReloadExtendedItem.xml"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "LibraryWithExtendedItemToReload.xml"); //$NON-NLS-1$

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);

		String designFilePath = (String) filePaths.get(0);
		openDesign(designFilePath, false);
		ExtendedItemHandle child = (ExtendedItemHandle) designHandle.findElement("child");//$NON-NLS-1$
		assertEquals(new Integer(0), child.getProperty("xScale")); //$NON-NLS-1$

		String libFilePath = (String) filePaths.get(1);

		// make modification on its library.

		openLibrary(libFilePath, false);
		ExtendedItemHandle parent = (ExtendedItemHandle) libraryHandle.findElement("parent");//$NON-NLS-1$
		parent.setProperty("xScale", "3");//$NON-NLS-1$ //$NON-NLS-2$
		libraryHandle.save();

		designHandle.reloadLibrary(libraryHandle);

		child.loadExtendedElement();
		assertEquals(new Integer(3), child.getProperty("xScale")); //$NON-NLS-1$
	}

	/**
	 * Tests overridden element reference value in ReloadLibrary.
	 * <ul>
	 * <li>no modify in library, then reload the design, the default hierarchy of
	 * dimension is still resolved.
	 * </ul>
	 * 
	 * @throws Exception
	 */
	public void testReloadWithOverideElementReference() throws Exception {
		openDesign("LibraryWithCubeTest.xml"); //$NON-NLS-1$

		// before reload, the default hierarchy is not null
		DimensionHandle dimension = (DimensionHandle) designHandle.getElementByID(9);
		assertNotNull(dimension.getDefaultHierarchy());
		assertEquals(dimension, dimension.getDefaultHierarchy().getContainer());

		// after reload, the default hierarchy is still not null
		designHandle.reloadLibraries();
		dimension = (DimensionHandle) designHandle.getElementByID(9);
		assertNotNull(dimension.getDefaultHierarchy());
		assertEquals(dimension, dimension.getDefaultHierarchy().getContainer());
	}

	/**
	 * Tests reload library with table contains extended item.
	 * 
	 * @throws Exception
	 */
	public void testReloadTableContainExtendedItem() throws Exception {
		// tests extended item which does not have Local Property Values On Own
		// Model.
		openDesign("DesignWithExtendedItem.xml"); //$NON-NLS-1$
		designHandle.reloadLibraries();
		save();
		assertTrue(compareFile("DesignWithExtendedItem_golden.xml")); //$NON-NLS-1$

		// tests extended item which has Local Property Values On Own Model.
		openDesign("DesignWithExtendedItem_1.xml"); //$NON-NLS-1$
		designHandle.reloadLibraries();
		save();
		assertTrue(compareFile("DesignWithExtendedItem_golden_1.xml")); //$NON-NLS-1$
	}

}