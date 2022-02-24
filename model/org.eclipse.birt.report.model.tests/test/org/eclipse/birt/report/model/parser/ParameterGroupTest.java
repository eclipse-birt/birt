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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.SlotIterator;
import org.eclipse.birt.report.model.api.SortedSlotIterator;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for ParameterGroup.
 * <p>
 * <strong>Unit test case </strong>
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" bordercolor="black">
 * <tr>
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected Result</th>
 * </tr>
 * 
 * <tr>
 * <td>testGetProperty</td>
 * <td>Read the property of ParameterGroup</td>
 * <td>They're identical to which defined in the design file</td>
 * </tr>
 * 
 * <tr>
 * <td>testParameters</td>
 * <td>Get the parameters in the ParameterGroup</td>
 * <td>The parameter count should be the same as the design file; and then get
 * the parameter of the parameter group, they should be the parameters definded
 * in the design file</td>
 * </tr>
 * 
 * <tr>
 * <td>testFlattenParameters</td>
 * <td>Call designHandle.getFlattenParameters() to get the flattened parameters
 * </td>
 * <td>Compare the parameters with a golden array, they should be identical</td>
 * </tr>
 * </table>
 * 
 */
public class ParameterGroupTest extends BaseTestCase {

	private final String FILE_NAME = "ParameterGroupTest.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(FILE_NAME);
	}

	/**
	 * After read the design file, get properties from ParameterGroup, then compare
	 * the properties with the design file. They should be identical to the design
	 * file.
	 * 
	 * @throws Exception
	 */

	public void testProperty() throws Exception {
		ParameterGroupHandle h = getParameterGroup("My Param Group"); //$NON-NLS-1$
		assertNotNull(h);

		assertEquals("This group contains a bunch of parameters.", h.getHelpText()); //$NON-NLS-1$

		assertEquals("group key", h.getHelpTextKey()); //$NON-NLS-1$
		assertTrue(h.startExpanded());

		h.setStartExpanded(false);
		h.setStringProperty(ParameterGroup.START_EXPANDED_PROP, "false"); //$NON-NLS-1$

		assertFalse(h.startExpanded());

		// cascading
		ParameterGroupHandle countryStateCity = getParameterGroup("Country-State-City"); //$NON-NLS-1$
		assertEquals("Group 2", countryStateCity.getDisplayName()); //$NON-NLS-1$
	}

	/**
	 * Test the parameters in this group.
	 * 
	 * @throws Exception
	 */
	public void testParameters() throws Exception {
		ParameterGroupHandle h = getParameterGroup("My Param Group"); //$NON-NLS-1$
		assertNotNull(h);

		SlotHandle slotHandle = h.getParameters();
		assertTrue(slotHandle.getCount() == 1);

		DesignElementHandle elementHandle = slotHandle.get(0);
		Object o = elementHandle.getName();
		assertEquals("Param 2", o); //$NON-NLS-1$

		// cascading

		ParameterGroupHandle countryStateCity = getParameterGroup("Country-State-City"); //$NON-NLS-1$
		SlotHandle cascadingParameters = countryStateCity.getParameters();
		assertEquals(3, cascadingParameters.getCount());

		ScalarParameterHandle p1 = (ScalarParameterHandle) cascadingParameters.get(0);
		assertEquals("dynamic", p1.getValueType()); //$NON-NLS-1$
		assertEquals("ds1", p1.getDataSetName()); //$NON-NLS-1$
		assertEquals("country", p1.getValueExpr()); //$NON-NLS-1$
		assertEquals("Enter country:", p1.getLabelExpr()); //$NON-NLS-1$
	}

	/**
	 * Tests flattern parameters method.
	 * 
	 * @throws Exception
	 */
	public void testFlattenParameters() throws Exception {
		String[] goldenArray = { "City", "Country", "Group 1", "Group 2", "Param 1", "Param 2", "Param 3", "State" }; //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$

		List it = designHandle.getFlattenParameters();
		for (int i = 0; i < it.size(); i++) {
			DesignElementHandle h = (DesignElementHandle) it.get(i);
			String displayName = h.getElement().getDisplayName();
			assertEquals(goldenArray[i], displayName);
		}
	}

	/**
	 * Test SortedSlotIterator.
	 * 
	 * @throws Exception
	 */
	public void testSortedSlotIterator() throws Exception {
		String[] goldenArray = { "Group 1", "Group 2", "Param 1", "Param 3" }; //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$

		SlotHandle handle = designHandle.getParameters();
		SortedSlotIterator it = new SortedSlotIterator(handle);
		int i = 0;
		while (it.hasNext()) {
			DesignElementHandle h = (DesignElementHandle) it.next();
			String displayName = h.getElement().getDisplayName();
			assertEquals(goldenArray[i++], displayName);
		}
	}

	/**
	 * Find a ParameterGroup by name.
	 * 
	 * @param name the parameter group name.
	 * @return the handle to the ParameterGroup of the provided name.
	 */
	private ParameterGroupHandle getParameterGroup(String name) {
		SlotHandle slotHandle = designHandle.getParameters();
		SlotIterator iter = new SlotIterator(slotHandle);
		while (iter.hasNext()) {
			DesignElementHandle h = (DesignElementHandle) iter.next();
			if (h instanceof ParameterGroupHandle && name.equals(h.getName())) {
				return (ParameterGroupHandle) h;
			}
		}
		return null;
	}

	/**
	 * Write back the design file then compare it with a golden file.
	 * 
	 * @throws Exception
	 */

	public void testWriter() throws Exception {
		// printErrors();

		ParameterGroupHandle h = getParameterGroup("My Param Group"); //$NON-NLS-1$
		assertNotNull(h);

		h.setHelpText("new help text"); //$NON-NLS-1$
		h.setHelpTextKey("key of new help text"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("ParameterGroupTest_golden.xml")); //$NON-NLS-1$
	}
}
