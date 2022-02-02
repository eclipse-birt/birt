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
import java.util.List;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * The test case of <code>TableItem</code> parser and writer.
 * <p>
 * <code>TableGroup</code> is also tested in this test case.
 * <p>
 * <code>TableColumn</code>,<code>TableRow</code> and <code>Cell</code> are
 * tested in {@link GridItemParseTest}.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Test all properties and slots of <code>TableItem</code> after parsing
 * design file</td>
 * <td>All properties are right</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test all properties of Sorting after parsing design file</td>
 * <td>Sorting is not implemented</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test all properties of <code>TableGroup</code> after parsing design
 * file</td>
 * <td>All properties are right</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test all properties of GroupFilter after parsing design file</td>
 * <td>GroupFilter is not implemented</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test all properties of TOC after parsing design file</td>
 * <td>TOC is not implemented</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Compare the written file with the golden file</td>
 * <td>Two files are same</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSemanticCheck()}</td>
 * <td>Test inconsistant column count error</td>
 * <td>Error found</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Test cell overlapping error</td>
 * <td>Error found</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSemanticCheck()}</td>
 * <td>Table is placed in header slot of table item</td>
 * <td>Context containment error found</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Table is placed in header slot of list item</td>
 * <td>Context containment error found</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Table has no data set in the List/Table container of any level.</td>
 * <td>Missing Data set error found</td>
 * </tr>
 * </table>
 * 
 * @see TableItem
 * @see GridItemParseTest
 */

public class TableItemParseTest extends ParserTestCase {

	String fileName = "TableItemParseTest.xml"; //$NON-NLS-1$
	String goldenFileName = "TableItemParseTest_golden.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "TableItemParseTest_1.xml"; //$NON-NLS-1$

	String summaryFileName = "TableItemParseTest_2.xml"; //$NON-NLS-1$
	String summaryGoldenFileName = "TableItemParseTest_1_golden.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test to read hide rules.
	 * 
	 * @param handle the table row handle
	 * 
	 * @throws Exception if open the design file with errors.
	 */

	public void testReadVisibilityRules(RowHandle handle) throws Exception {
		Iterator rules = handle.visibilityRulesIterator();
		assertNotNull(rules);

		// checks with the first visibility rule.

		HideRuleHandle hideHandle = (HideRuleHandle) rules.next();
		assertNotNull(hideHandle);

		assertEquals(DesignChoiceConstants.FORMAT_TYPE_PDF, hideHandle.getFormat());
		assertEquals("pdf, 10 people", hideHandle.getExpression()); //$NON-NLS-1$

		// the second visibility rule

		hideHandle = (HideRuleHandle) rules.next();
		assertNotNull(hideHandle);

		assertEquals(DesignChoiceConstants.FORMAT_TYPE_ALL, hideHandle.getFormat());
		assertEquals("excel, 10 people", hideHandle.getExpression()); //$NON-NLS-1$
	}

	/**
	 * Test parser and properties of table element.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testParser() throws Exception {
		openDesign(fileName);

		save();

		TableHandle table = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$
		assertNotNull(table);

		// checks event handler

		assertEquals("birt.js.myTableHandler", table //$NON-NLS-1$
				.getEventHandlerClass());

		assertTrue(table.newHandlerOnEachEvent());

		// checks on-prepare, on-create and on-render values

		assertEquals("create on the table", table.getOnCreate()); //$NON-NLS-1$

		assertEquals("render on the table", table.getOnRender()); //$NON-NLS-1$

		assertEquals("prepare on the table", table.getOnPrepare()); //$NON-NLS-1$

		assertFalse(table.repeatHeader());

		assertEquals("bluehero", table.getCaption()); //$NON-NLS-1$

		assertEquals("blue he", table.getCaptionKey()); //$NON-NLS-1$

		assertEquals("summary", table.getSummary()); //$NON-NLS-1$

		assertEquals(3, table.getColumnCount());

		assertEquals("Table", table.getTagType()); //$NON-NLS-1$
		assertEquals("English", table.getLanguage()); //$NON-NLS-1$
		assertEquals(1, table.getOrder()); // $NON-NLS-1$

		// test sorting

		Iterator sorts = table.sortsIterator();
		SortKeyHandle sortHandle = (SortKeyHandle) sorts.next();

		assertEquals("age", sortHandle.getKey()); //$NON-NLS-1$
		assertEquals("asc", sortHandle.getDirection()); //$NON-NLS-1$

		sortHandle = (SortKeyHandle) sorts.next();

		assertEquals("grade", sortHandle.getKey()); //$NON-NLS-1$
		assertEquals("desc", sortHandle.getDirection()); //$NON-NLS-1$

		assertNull(sorts.next());

		// test filter

		Iterator filters = table.filtersIterator();

		FilterConditionHandle filterHandle = (FilterConditionHandle) filters.next();

		assertEquals("lt", filterHandle.getOperator()); //$NON-NLS-1$
		assertEquals("filter expression", filterHandle.getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", filterHandle.getValue1()); //$NON-NLS-1$
		assertEquals("value2 expression", filterHandle.getValue2()); //$NON-NLS-1$

		// test sort-by-group
		assertFalse(table.isSortByGroups());

		// Test header properties

		SlotHandle headerSlot = table.getHeader();
		assertNotNull(headerSlot);

		RowHandle rowHandle = (RowHandle) headerSlot.get(0);
		assertFalse(rowHandle.repeatable());
		testReadVisibilityRules(rowHandle);
		assertEquals(1, headerSlot.getCount());

		// Test detail properties

		SlotHandle detailSlot = table.getDetail();
		assertNotNull(detailSlot);
		assertEquals(1, detailSlot.getCount());

		RowHandle row = (RowHandle) detailSlot.get(0);

		// checks suppress dupliactes flag
		assertTrue(row.suppressDuplicates());

		// check bookmark and its display name
		assertEquals("Row Bookmark", row.getBookmark()); // $NON-NLS-1
		assertEquals("Row Bookmark Display Name", row.getBookmarkDisplayName()); // $NON-NLS-1

		// checks event handler
		assertEquals("birt.js.myDetailRowHandler", row //$NON-NLS-1$
				.getEventHandlerClass());

		assertTrue(row.newHandlerOnEachEvent());

		// checks on-prepare, on-create and on-render values

		assertEquals("create on the row", row.getOnCreate()); //$NON-NLS-1$

		assertEquals("render on the row", row.getOnRender()); //$NON-NLS-1$

		assertEquals("prepare on the row", row.getOnPrepare()); //$NON-NLS-1$

		assertEquals("TR", row.getTagType()); //$NON-NLS-1$
		assertEquals("English", row.getLanguage()); //$NON-NLS-1$

		SlotHandle headSlot = table.getHeader();
		RowHandle headRow = (RowHandle) headSlot.get(0);
		// test default value of Role in Row
		assertEquals("tr", headRow.getTagType()); //$NON-NLS-1$

		ColumnHandle column = (ColumnHandle) table.getColumns().get(2);

		assertTrue(column.getBooleanProperty(TableColumn.SUPPRESS_DUPLICATES_PROP));

		// verify the visibility property

		Iterator hideRules = column.visibilityRulesIterator();
		assertTrue(hideRules.hasNext());

		HideRuleHandle hideRule = (HideRuleHandle) hideRules.next();
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_VIEWER, hideRule.getFormat());
		assertEquals("viewer, 10 people", hideRule.getExpression()); //$NON-NLS-1$

		SlotHandle cells = row.getCells();
		assertNotNull(cells);
		assertEquals(1, cells.getCount());

		CellHandle cell = (CellHandle) cells.get(0);

		// check bookmark and its display name
		assertEquals("Cell Bookmark", cell.getBookmark()); //$NON-NLS-1$
		assertEquals("Cell Bookmark Display Name", cell.getBookmarkDisplayName()); //$NON-NLS-1$

		// checks event handler

		assertEquals("birt.js.myDetailCellHandler", cell //$NON-NLS-1$
				.getEventHandlerClass());

		assertTrue(cell.newHandlerOnEachEvent());

		// checks on-prepare, on-create and on-render values

		assertEquals("create on the cell", cell.getOnCreate()); //$NON-NLS-1$

		assertEquals("render on the cell", cell.getOnRender()); //$NON-NLS-1$

		assertEquals("prepare on the cell", cell.getOnPrepare()); //$NON-NLS-1$

		// Test group properties

		SlotHandle groupSlot = table.getGroups();
		assertNotNull(groupSlot);
		assertEquals(1, groupSlot.getCount());

		TableGroupHandle group = (TableGroupHandle) groupSlot.get(0);
		assertEquals(DesignChoiceConstants.INTERVAL_WEEK, group.getInterval());
		assertTrue(3.0 == group.getIntervalRange());
		assertEquals("desc", group.getSortDirection()); //$NON-NLS-1$
		assertEquals("[Country]", group.getKeyExpr()); //$NON-NLS-1$
		assertEquals("toc Country", group.getTOC().getExpression()); //$NON-NLS-1$

		assertEquals("acl expression test", group.getACLExpression()); //$NON-NLS-1$
		assertFalse(group.cascadeACL());

		// check bookmark and its display name
		assertEquals("Group Bookmark", group.getBookmark()); // $NON-NLS-1
		assertEquals("Group Bookmark Display Name", group.getBookmarkDisplayName()); // $NON-NLS-1

		// checks event handler
		assertEquals("birt.js.myGroup1Handler", group //$NON-NLS-1$
				.getEventHandlerClass());

		assertTrue(group.newHandlerOnEachEvent());

		// checks on-prepare, on-create and on-render values

		assertEquals("create on the group", group.getOnCreate()); //$NON-NLS-1$

		assertEquals("render on the group", group.getOnRender()); //$NON-NLS-1$

		assertEquals("prepare on the group", group.getOnPrepare()); //$NON-NLS-1$

		// test sorting

		sorts = group.sortsIterator();

		sortHandle = (SortKeyHandle) sorts.next();

		assertEquals("name", sortHandle.getKey()); //$NON-NLS-1$
		assertEquals("asc", sortHandle.getDirection()); //$NON-NLS-1$

		sortHandle = (SortKeyHandle) sorts.next();

		assertEquals("birthday", sortHandle.getKey()); //$NON-NLS-1$
		assertEquals("desc", sortHandle.getDirection()); //$NON-NLS-1$

		filters = group.filtersIterator();

		filterHandle = (FilterConditionHandle) filters.next();

		assertEquals("lt", filterHandle.getOperator()); //$NON-NLS-1$
		assertEquals("filter expression", filterHandle.getExpr()); //$NON-NLS-1$
		assertEquals("value1 expression", filterHandle.getValue1()); //$NON-NLS-1$
		assertEquals("value2 expression", filterHandle.getValue2()); //$NON-NLS-1$

		SlotHandle groupHeaderSlot = group.getHeader();
		assertEquals(2, groupHeaderSlot.getCount());

		SlotHandle groupFooterSlot = group.getFooter();
		assertEquals(1, groupFooterSlot.getCount());

		// test data binding on group

		// Iterator iter1 = group.columnBindingsIterator( );
		// ComputedColumnHandle computedColumn = (ComputedColumnHandle) iter1
		// .next( );
		// assertEquals( "column1", computedColumn.getName( ) ); //$NON-NLS-1$
		// assertEquals( "column1 expr", computedColumn.getExpression( ) );
		// //$NON-NLS-1$

		// Test footer properties

		SlotHandle footerSlot = table.getFooter();
		assertNotNull(footerSlot);
		assertEquals(1, footerSlot.getCount());
		rowHandle = (RowHandle) footerSlot.get(0);
		assertTrue(rowHandle.repeatable());

		// reads in a table that exists in the components.

		table = (TableHandle) designHandle.findElement("componentsTable"); //$NON-NLS-1$
		assertNotNull(table);

		// test default value of Role in Table
		assertEquals("table", table.getTagType()); //$NON-NLS-1$

		// reads in a table that exists in the scratch pad.

		table = (TableHandle) designHandle.findElement("scratchpadTable"); //$NON-NLS-1$
		assertNotNull(table);

		// reads in a table that exists in the free form.

		table = (TableHandle) designHandle.findElement("freeformTable"); //$NON-NLS-1$
		assertNotNull(table);

		// reads in a table that exists in the list header.

		table = (TableHandle) designHandle.findElement("listHeaderTable"); //$NON-NLS-1$
		assertNotNull(table);

		// reads in a table that exists in the list detail.

		table = (TableHandle) designHandle.findElement("listDetailTable"); //$NON-NLS-1$
		assertNotNull(table);

		// reads in a table that exists in the list footer.

		table = (TableHandle) designHandle.findElement("listFooterTable"); //$NON-NLS-1$
		assertNotNull(table);

		// reads in a table that exists in the list group header.

		table = (TableHandle) designHandle.findElement("listgroupHeaderTable"); //$NON-NLS-1$
		assertNotNull(table);

		// reads in a table that exists in the list group footer.

		table = (TableHandle) designHandle.findElement("listgroupFooterTable"); //$NON-NLS-1$
		assertNotNull(table);

	}

	/**
	 * Test to write hide rules.
	 * 
	 * @param handle the table row handle
	 * 
	 * @throws Exception if open the design file with errors.
	 */

	public void testWriteVisibilityRules(RowHandle handle) throws Exception {
		Iterator rules = handle.visibilityRulesIterator();
		assertNotNull(rules);

		// checks with the first visibility rule.

		HideRuleHandle hideHandle = (HideRuleHandle) rules.next();
		assertNotNull(hideHandle);

		hideHandle.setFormat(null);
		hideHandle.setExpression("new rule 1"); //$NON-NLS-1$

		// the second visibility rule

		hideHandle = (HideRuleHandle) rules.next();
		assertNotNull(hideHandle);

		hideHandle.setFormat(DesignChoiceConstants.FORMAT_TYPE_REPORTLET);
		hideHandle.setExpression("new rule 2"); //$NON-NLS-1$

	}

	/**
	 * This test writes the design file and compare it with golden file.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testWriter() throws Exception {
		openDesign(fileName);

		TableHandle tableHandle = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$
		assertNotNull(tableHandle);

		tableHandle.setEventHandlerClass("birt.js.newTableHandler"); //$NON-NLS-1$
		tableHandle.setOnCreate("new create on table"); //$NON-NLS-1$
		tableHandle.setOnPrepare("new prepare on table"); //$NON-NLS-1$
		tableHandle.setOnRender("new render on table"); //$NON-NLS-1$

		tableHandle.setRepeatHeader(false);
		tableHandle.setCaption("new caption"); //$NON-NLS-1$
		tableHandle.setCaptionKey("new caption key"); //$NON-NLS-1$
		tableHandle.setSummary("new summary"); //$NON-NLS-1$
		tableHandle.setSortByGroups(true);
		tableHandle.setNewHandlerOnEachEvent(false);

		tableHandle.setTagType("Table"); //$NON-NLS-1$
		tableHandle.setLanguage("English"); //$NON-NLS-1$
		tableHandle.setOrder(1); // $NON-NLS-1$

		// visibility rules on column

		ColumnHandle column = (ColumnHandle) tableHandle.getColumns().get(2);

		PropertyHandle propHandle = column.getPropertyHandle(TableColumn.VISIBILITY_PROP);
		propHandle.setValue(null);

		HideRule hideRule = StructureFactory.createHideRule();
		propHandle.addItem(hideRule);

		hideRule.setFormat(DesignChoiceConstants.FORMAT_TYPE_REPORTLET);
		hideRule.setExpression("new expression of " //$NON-NLS-1$
				+ DesignChoiceConstants.FORMAT_TYPE_REPORTLET);

		SlotHandle groupSlot = tableHandle.getGroups();
		assertNotNull(groupSlot);
		assertEquals(1, groupSlot.getCount());

		RowHandle rowHandle = (RowHandle) tableHandle.getHeader().get(0);
		testWriteVisibilityRules(rowHandle);

		rowHandle = (RowHandle) tableHandle.getFooter().get(0);
		rowHandle.setRepeatable(false);

		TableGroupHandle group = (TableGroupHandle) groupSlot.get(0);
		group.setName("new group name"); //$NON-NLS-1$
		group.setInterval(DesignChoiceConstants.INTERVAL_DAY);
		group.setIntervalRange(99);
		group.setKeyExpr("new expression"); //$NON-NLS-1$
		group.setBookmark("group bookmark"); //$NON-NLS-1$
		group.setBookmarkDisplayName("group bookmark display name"); //$NON-NLS-1$
		group.getTOC().setExpression("new toc expression"); //$NON-NLS-1$
		group.setEventHandlerClass("birt.js.newGroup1Handler"); //$NON-NLS-1$
		group.setOnPrepare("new prepare on group"); //$NON-NLS-1$
		group.setOnCreate("new create on group");//$NON-NLS-1$
		group.setOnRender("new render on group");//$NON-NLS-1$

		group.setACLExpression("new acl expression test"); //$NON-NLS-1$
		group.setCascadeACL(true);

		// Test detail properties

		SlotHandle detailSlot = tableHandle.getDetail();
		assertNotNull(detailSlot);
		assertEquals(1, detailSlot.getCount());

		RowHandle row = (RowHandle) detailSlot.get(0);

		// checks suppress duplicates flag
		row.setSuppressDuplicates(false);

		// checks event handler

		row.setEventHandlerClass("birt.js.newRowHandler"); //$NON-NLS-1$
		row.setOnCreate("new create on row"); //$NON-NLS-1$
		row.setOnPrepare("new prepare on row"); //$NON-NLS-1$
		row.setOnRender(null);

		row.setBookmark("row bookmark"); //$NON-NLS-1$
		row.setBookmarkDisplayName("row bookmark display name"); //$NON-NLS-1$

		row.setTagType("TR"); //$NON-NLS-1$
		row.setLanguage("English"); //$NON-NLS-1$

		SlotHandle cells = row.getCells();
		assertNotNull(cells);
		assertEquals(1, cells.getCount());

		CellHandle cell = (CellHandle) cells.get(0);

		// checks event handler

		cell.setEventHandlerClass("birt.js.newNewHandler"); //$NON-NLS-1$
		cell.setOnCreate(null);
		cell.setOnPrepare("new prepare on cell"); //$NON-NLS-1$
		cell.setOnRender("new render on cell"); //$NON-NLS-1$

		cell.setBookmark("cell bookmark"); //$NON-NLS-1$
		cell.setBookmarkDisplayName("cell bookmark display name"); //$NON-NLS-1$

		UserPropertyDefn prop = new UserPropertyDefn();
		prop.setName("hello"); //$NON-NLS-1$
		PropertyType typeDefn = MetaDataDictionary.getInstance().getPropertyType(PropertyType.STRING_TYPE_NAME);
		prop.setType(typeDefn);
		cell.addUserPropertyDefn(prop);

		save();
		assertTrue(compareFile(goldenFileName));

	}

	/**
	 * Test semantic check.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testSemanticCheck() throws Exception {
		openDesign(semanticCheckFileName);

		List errors = design.getErrorList();

		int i = 0;

		assertEquals(8, errors.size());

		ErrorDetail error = ((ErrorDetail) errors.get(i++));
		assertEquals("First table", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT, error.getErrorCode());

		error = ((ErrorDetail) errors.get(i++));
		assertEquals("Second table", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT, error.getErrorCode());

		error = ((ErrorDetail) errors.get(i++));
		assertEquals("Forth table", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT, error.getErrorCode());

		error = ((ErrorDetail) errors.get(i++));
		assertEquals(SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS, error.getErrorCode());

		error = ((ErrorDetail) errors.get(i++));
		assertEquals(SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS, error.getErrorCode());

		error = ((ErrorDetail) errors.get(i++));
		assertEquals("Seventh table", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT, error.getErrorCode());

		error = ((ErrorDetail) errors.get(i++));
		assertEquals("First list", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET, error.getErrorCode());

		error = ((ErrorDetail) errors.get(i++));
		assertEquals("First inner table", error.getElement().getName()); //$NON-NLS-1$
		assertEquals(SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET, error.getErrorCode());
	}

	/**
	 * Tests to read and write a summary table and compare it with golden file.
	 * 
	 * @throws NameException
	 * @throws ContentException
	 */
	public void testSummaryTable() throws Exception {
		openDesign(summaryFileName);
		TableHandle tableHandle = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$

		assertTrue(tableHandle.isSummaryTable());

		tableHandle.setIsSummaryTable(false);
		save();

		assertTrue(compareFile(summaryGoldenFileName));
	}

	public void testHideRuleFormatCompatibility() throws Exception {
		openDesign("TableItemParseTest_3.xml"); //$NON-NLS-1$

		List<ErrorDetail> errors = design.getAllErrors();
		assertTrue(!errors.isEmpty());
		assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, errors.get(0).getErrorCode());

		TableHandle tableHandle = (TableHandle) designHandle.findElement("My table"); //$NON-NLS-1$
		RowHandle rowHandle = (RowHandle) tableHandle.getHeader().get(0);

		Iterator rules = rowHandle.visibilityRulesIterator();
		assertNotNull(rules);
		HideRuleHandle handle = (HideRuleHandle) rules.next();
		assertEquals("my/format", handle.getFormat()); //$NON-NLS-1$
		handle = (HideRuleHandle) rules.next();
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_ALL, handle.getFormat());

		// can not set a illegal format string
		try {
			handle.setFormat("wrong foramt"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}

}
