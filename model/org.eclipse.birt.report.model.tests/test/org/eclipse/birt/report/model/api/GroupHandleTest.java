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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests GroupHandle.
 *
 * <p>
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testProperties()}</td>
 * <td>Tests to read and set properties on a list group.</td>
 * <td>Reads expected values and Values are set correctly.</td>
 * </tr>
 *
 * </table>
 */

public class GroupHandleTest extends BaseTestCase {

	/**
	 * Tests Set interval range. value is dependant with locale.
	 *
	 * @throws Exception
	 */

	public void testIntervalRange() throws Exception {
		ULocale locale = ULocale.GERMANY;
		createDesign(locale);

		TableHandle tableHandle = designHandle.getElementFactory().newTableItem("table", 3); //$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		TableGroupHandle groupHandle = designHandle.getElementFactory().newTableGroup();
		groupHandle.setName("group");//$NON-NLS-1$
		groupHandle.setKeyExpr("row[\"hello\"]");//$NON-NLS-1$
		groupHandle.setIntervalRange("1,234.5");//$NON-NLS-1$

		tableHandle.getGroups().add(groupHandle);

		TableGroupHandle groupHandle2 = designHandle.getElementFactory().newTableGroup();
		groupHandle2.setName("group2");//$NON-NLS-1$
		groupHandle2.setKeyExpr("row[\"hello2\"]");//$NON-NLS-1$
		groupHandle2.setIntervalRange(1000.5);

		tableHandle.getGroups().add(groupHandle2);

		assertEquals("1.234", groupHandle.getStringProperty(GroupElement.INTERVAL_RANGE_PROP));//$NON-NLS-1$
		assertEquals(new Double(1000.5), new Double(groupHandle2.getIntervalRange()));
	}

	/**
	 *
	 * Tests to read and set properties on a GroupElement.
	 *
	 * @throws Exception if errors occur when opens the design file
	 */

	public void testProperties() throws Exception {
		openDesign("GroupHandleTest.xml"); //$NON-NLS-1$

		ListHandle list = (ListHandle) designHandle.findElement("My List"); //$NON-NLS-1$
		assertNotNull(list);

		// group slot

		SlotHandle groupSlot = list.getGroups();
		GroupHandle group = (GroupHandle) groupSlot.get(0);
		group.setName("group1"); //$NON-NLS-1$
		assertEquals("group1", group //$NON-NLS-1$
				.getDisplayLabel(DesignElement.FULL_LABEL));

		assertEquals("2004/12/12", group.getGroupStart()); //$NON-NLS-1$

		group.setPageBreakAfter(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, group.getPageBreakAfter());
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS,
				(String) group.getFactoryPropertyHandle(Style.PAGE_BREAK_AFTER_PROP).getValue());

		group.setPageBreakBefore(DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST, group.getPageBreakBefore());

		group.setRepeatHeader(false);
		assertFalse(group.repeatHeader());

		group.setHideDetail(true);
		assertTrue(group.hideDetail());

		try {
			group.setPageBreakAfter("inherit"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		assertNull(group.getPropertyDefn(IStyleModel.BACKGROUND_ATTACHMENT_PROP));

		group.setName("  "); //$NON-NLS-1$
		assertEquals(null, group.getDisplayLabel(DesignElement.USER_LABEL));

		group.setName(""); //$NON-NLS-1$
		assertEquals(null, group.getDisplayLabel(DesignElement.USER_LABEL));

		// group filter

		group.setInterval(DesignChoiceConstants.INTERVAL_PREFIX);
		assertEquals(DesignChoiceConstants.INTERVAL_PREFIX, group.getInterval());
		group.setIntervalRange(0.1234);
		assertTrue(0.1234 == group.getIntervalRange());

		group.setKeyExpr("new key expression"); //$NON-NLS-1$
		assertEquals("new key expression", group.getKeyExpr()); //$NON-NLS-1$

		group.setTocExpression("new toc expression"); //$NON-NLS-1$
		assertEquals("new toc expression", group.getTocExpression()); //$NON-NLS-1$

		group.setTocExpression(null);
		assertEquals("new key expression", group.getKeyExpr()); //$NON-NLS-1$
		assertNull(group.getTocExpression());

		group.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_DESC);
		assertEquals(DesignChoiceConstants.SORT_DIRECTION_DESC, group.getSortDirection());

		group.setOnPrepare("new prepare on the group"); //$NON-NLS-1$
		assertEquals("new prepare on the group", group.getOnPrepare()); //$NON-NLS-1$

		// test bookmark
		assertNull(group.getBookmark());

		group.setBookmark("bookmark");//$NON-NLS-1$
		assertEquals("bookmark", group.getBookmark());//$NON-NLS-1$

	}

	/**
	 * Test case for testing whether the header slot and footer slot is existed in
	 * the group element.
	 *
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void testHasHeaderAndFooter() throws DesignFileException, SemanticException {

		openDesign("GroupHandleTest.xml"); //$NON-NLS-1$
		ListHandle list = (ListHandle) designHandle.findElement("My List"); //$NON-NLS-1$
		SlotHandle groupSlot = list.getGroups();
		GroupHandle group = (GroupHandle) groupSlot.get(0);

		assertEquals(true, group.hasHeader());

		group.clearContents(GroupElement.HEADER_SLOT);
		assertEquals(false, group.hasHeader());
		assertEquals(true, group.hasFooter());
		group.clearContents(GroupElement.FOOTER_SLOT);
		assertEquals(false, group.hasHeader());

	}

	/**
	 *
	 * <ul>
	 * <li>Tests addTOC , getTOC , setTOCExpression, getTOCExpression.
	 * <li>drop the style set on toc
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testTOC() throws Exception {
		openDesign("GroupHandleTest.xml"); //$NON-NLS-1$

		ListHandle list = (ListHandle) designHandle.findElement("My List"); //$NON-NLS-1$
		SlotHandle groupSlot = list.getGroups();
		GroupHandle group = (GroupHandle) groupSlot.get(0);

		TOCHandle tocHandle = group.getTOC();
		StyleHandle styleHandle = designHandle.findNativeStyle(tocHandle.getStyleName());
		styleHandle.dropAndClear();

		assertNull(tocHandle.getStyleName());

		TOC toc = StructureFactory.createTOC("toc"); //$NON-NLS-1$
		tocHandle = group.addTOC(toc);
		assertNotNull(tocHandle);
		assertEquals("toc", tocHandle.getExpression());//$NON-NLS-1$

		assertEquals("toc", group.getTocExpression());//$NON-NLS-1$

		group.setTocExpression("toc2");//$NON-NLS-1$
		assertEquals("toc2", group.getTocExpression());//$NON-NLS-1$

	}
}
