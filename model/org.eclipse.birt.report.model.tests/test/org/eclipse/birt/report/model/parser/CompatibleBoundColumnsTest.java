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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests parser compatibility.
 */

public class CompatibleBoundColumnsTest extends BaseTestCase {

	/**
	 * Test cases:
	 *
	 * <ul>
	 * <li>parse the old design and create bound columns during parsing procedure.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testExpressionCompatible() throws Exception {
		openDesign("CompatibleBirt2_1M5Expr.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatibleBirt2_1M5Expr_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 *
	 * <ul>
	 * <li>parse the new design file with data item.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testDataValueExpr() throws Exception {
		openDesign("Birt2_1_0Expr.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("Birt2_1_0Expr_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 *
	 * <ul>
	 * <li>parse the obsolete design file with text item. Content of text item is a
	 * special case.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testTextContent() throws Exception {
		openDesign("Birt2_1_M5TextContent.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("Birt2_1_M5TextContent_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 *
	 * <ul>
	 * <li>parse the obsolete design file with nested table.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testNestedTable() throws Exception {
		openDesign("CompatibleBoundColumnNestedTable.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatibleBoundColumnNestedTable_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 *
	 * <ul>
	 * <li>parse the obsolete design file with multi data items in the group. For
	 * same result set columns in different group, must create a table-unique bound
	 * column name with aggregateOn of groupName.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testDataValueExprWithGroup() throws Exception {
		openDesign("CompatibleDataValueExprWithGroup.xml"); //$NON-NLS-1$
		save();

		assertTrue(compareFile("CompatibleDataValueExprWithGroup_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * 1.parser the design file and can parser old file 2.check if giving a
	 * groupName to 'group' element 3.change the value of 'aggrerateOn' property
	 *
	 * @throws Exception
	 */

	public void testGroupBoundCOlumns() throws Exception {
		openDesign("CompatibleBoundDataColumnGroupTest.xml"); //$NON-NLS-1$
		ListingHandle tableHandle = (ListingHandle) designHandle.findElement("MyTable1"); //$NON-NLS-1$

		GroupHandle group = (GroupHandle) tableHandle.getGroups().get(0);
		assertEquals("NewTableGroup1", group.getName()); //$NON-NLS-1$

		Iterator iter = tableHandle.columnBindingsIterator();
		ComputedColumnHandle structure = (ComputedColumnHandle) iter.next();
		assertEquals("column1", structure.getName());//$NON-NLS-1$

		structure = (ComputedColumnHandle) iter.next();
		assertEquals("column2", structure.getName());//$NON-NLS-1$
		assertEquals(group.getName(), structure.getAggregateOn());

		tableHandle = (ListingHandle) designHandle.findElement("MyTable2"); //$NON-NLS-1$

		group = (GroupHandle) tableHandle.getGroups().get(1);
		assertEquals("NewTableGroup1", group.getName()); //$NON-NLS-1$

		group = (GroupHandle) tableHandle.getGroups().get(0);
		assertEquals("NewTableGroup2", group.getName()); //$NON-NLS-1$

		iter = tableHandle.columnBindingsIterator();
		structure = (ComputedColumnHandle) iter.next();
		structure = (ComputedColumnHandle) iter.next();
		assertEquals("column3", structure.getName());//$NON-NLS-1$
		assertEquals(group.getName(), structure.getAggregateOn());
		structure = (ComputedColumnHandle) iter.next();
		assertFalse(iter.hasNext());
	}

	/**
	 * Test cases:
	 *
	 * <ul>
	 * For the design file with version "3" or early.
	 * <li>parser the design file without bound columns feature.
	 * <li>the result is: bound column created automatically. And aggregateOn set
	 * with group name.
	 * </ul>
	 *
	 * <ul>
	 * For the design file with version greater than "3" and smaller than "3.2.2".
	 * That is, has bound column feature. And the group has the bound column
	 * property.
	 * <li>parser the design file without bound columns feature.
	 * <li>the result is: bound column created automatically. If the bound columns
	 * already on the table. Do nothing.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testGroupExprssion() throws Exception {
		openDesign("CompatibleGroupExpression.xml"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatibleGroupExpression_golden.xml")); //$NON-NLS-1$

		openDesign("CompatibleGroupExpression_1.xml"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("CompatibleGroupExpression_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests cases:
	 *
	 * <ul>
	 * For the design file with version "3" or early.
	 * <li>Creates the column bindings for rows[0]. And change rows[0] to row._outer
	 * <li>the result is expected.
	 * </ul>
	 *
	 * <ul>
	 * For the design file with version "3.2.1" or early.
	 * <li>And change rows[0] to row._outer
	 * <li>the result is expected.
	 * </ul>
	 *
	 * <ul>
	 * For the design file with version "3.2.1" or early. The nested table has an
	 * extended item.
	 * <li>Change rows[0] to row._outer. Column bindings should be created in the
	 * nested table.
	 * <li>the result is expected.
	 * </ul>
	 *
	 * <ul>
	 * If the design file has the column binding and now rows[index].
	 * <li>The file should not be changed except the version number
	 * <li>the result is expected.
	 * </ul>
	 *
	 *
	 * <ul>
	 * If a table has bound data columns with rows[] defined in it.
	 * <li>Create bound data columns on outer data container. And convert rows[] to
	 * row._outer.
	 * <li>the result is expected.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testRows() throws Exception {
		openDesign("CompatibleRowsExpr_1.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleRowsExpr_golden_1.xml")); //$NON-NLS-1$

		openDesign("CompatibleRowsExpr_2.xml", ULocale.ENGLISH); //$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleRowsExpr_golden_2.xml")); //$NON-NLS-1$

		openDesign("CompatibleRowsExpr_3.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleRowsExpr_golden_3.xml")); //$NON-NLS-1$

		openDesign("CompatibleRowsExpr_NoChange.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleRowsExpr_NoChange_golden.xml")); //$NON-NLS-1$

		openDesign("CompatibleRowsExpr_4.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("CompatibleRowsExpr_golden_4.xml")); //$NON-NLS-1$
	}
}
