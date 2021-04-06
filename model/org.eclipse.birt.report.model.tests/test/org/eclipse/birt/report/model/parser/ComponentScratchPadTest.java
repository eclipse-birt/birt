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

package org.eclipse.birt.report.model.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The test case of component and scratch-pad slot parse and writer.
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testParse()}</td>
 * <td>Test the slot information of the component and scratch-pad slot</td>
 * <td>All information is right.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Add some new element into the component slot and set the extend name of
 * them.</td>
 * <td>The out put file is in the right order of derivation.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Compare the written file with the golden file</td>
 * <td>Two files are same</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testParseError()}</td>
 * <td>Super element is not found.</td>
 * <td>Error found</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Super element is not in component slot.</td>
 * <td>Error found</td>
 * </tr>
 * </table>
 * 
 */

public class ComponentScratchPadTest extends BaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * This test writes the design file and compare it with golden file.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testWriter() throws Exception {
		openDesign("ComponentScratchPadTest.xml"); //$NON-NLS-1$
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		designHandle.serialize(out);
		ByteArrayInputStream is = new ByteArrayInputStream(out.toByteArray());
		openDesign("", is); //$NON-NLS-1$
		assertNotNull(design);
		save();
		assertTrue(compareFile("ComponentScratchPadTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test parsing elements from ScratchPad.
	 * 
	 * @throws Exception if any exception.
	 */

	public void testParse() throws Exception {
		openDesign("ComponentScratchPadTest.xml"); //$NON-NLS-1$
		assertEquals(0, design.getErrorList().size());

		SlotHandle components = designHandle.getComponents();
		assertEquals(2, components.getCount());

		GridHandle grid = null;

		// if the element in component slot has children, then the operation of
		// moving it from the slot operation is forbidden.

		grid = (GridHandle) components.get(0);
		assertEquals(grid, components.get(1).getExtends());
		try {
			grid.moveTo(designHandle, ReportDesign.BODY_SLOT);
			fail();
		} catch (ContentException e) {

		}
		assertEquals(2, components.getCount());

		// drop is forbidden too.
		try {
			grid.dropAndClear();
			fail();
		} catch (ContentException e) {

		}
		assertEquals(2, components.getCount());

		// If we move the children before its parent in component slot, then the
		// moving operation is forbidden.

		grid = (GridHandle) components.get(0);
		try {
			designHandle.getComponents().shift(grid, 1);
			fail();
		} catch (ContentException e) {

		}

		grid = (GridHandle) components.get(1);
		try {
			designHandle.getComponents().shift(grid, 0);
			fail();
		} catch (ContentException e) {

		}

		// add some gird and label into the component slot and check the out.
		// In the out file, the parent is always before his children in the
		// component slot.
		SlotHandle comHandle = designHandle.getComponents();

		GridItem gridItem = new GridItem("grand"); //$NON-NLS-1$
		// grid.setExtendsName( "grid0" );
		comHandle.add(gridItem.getHandle(design));

		grid = (GridHandle) components.get(0);
		// grid.setExtendsName( "grand" );
		Label labelItem = new Label("label"); //$NON-NLS-1$
		// label.setExtendsName( "labelParent" );
		comHandle.add(labelItem.getHandle(design));
		gridItem = new GridItem("grid0"); //$NON-NLS-1$
		comHandle.add(gridItem.getHandle(design));
		labelItem = new Label("labelParent"); //$NON-NLS-1$
		comHandle.add(labelItem.getHandle(design));

		design.findElement("grand").setExtendsElement(design.findElement("grid0")); //$NON-NLS-1$ //$NON-NLS-2$
		design.findElement("parent").setExtendsElement(design.findElement("grand")); //$NON-NLS-1$ //$NON-NLS-2$
		design.findElement("label").setExtendsElement(design.findElement("labelParent")); //$NON-NLS-1$ //$NON-NLS-2$
		save(); // $NON-NLS-1$
	}
}