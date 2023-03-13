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

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * TestCases for ActionState and Action class. Parse ActionState and get the
 * property value from the element it bind to.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testGetLinkExpr()}</td>
 * <td>LinkType is hyperLink</td>
 * <td>Return is Expression value for HyperLink</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>LinkType is Drillthrough</td>
 * <td>Return is Expression value for BookmarkLink in Drillthrough</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>LinkType is BookmarkLink</td>
 * <td>Return is Expression value for BookmarkLink.</td>
 * </tr>
 *
 *
 * <tr>
 * <td>{@link #testGetDrillthroughParameters()}</td>
 * <td>LinkType is not Drillthrough</td>
 * <td>Return is null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>LinkType is Drillthrough, containing 2 Parameters</td>
 * <td>Return a list containing 2 items.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetDrillthroughSearchKeys()}</td>
 * <td>LinkType is not Drillthrough</td>
 * <td>Return is null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>LinkType is Drillthrough, containing 2 searchKeys</td>
 * <td>Return is a list containing 2 items</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetDrillthroughReportName()}</td>
 * <td>LinkType is not Drillthrough.</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>LinkType is Drillthrough, reportName="iserver/report1"</td>
 * <td>Return is "iserver/report1"</td>
 * </tr>
 *
 * </table>
 *
 */

public class ActionParseTest extends BaseTestCase {

	String goldenFileName = "action_test_golden.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("action_test.xml"); //$NON-NLS-1$
		assertNotNull(design);
	}

	/**
	 * Test getLinkExpr().
	 * <p>
	 * Case1: linkType = "Hyperlink" Return should be Expression value for HyperLink
	 * <p>
	 * Case2: linkType = "Drillthrough" drillThroughLinkType = "BookmarkLink" Return
	 * should be Expression value for BookmarkLink in Drillthrough
	 * <p>
	 * Case3: linkType = "BookmarkLink" Return should be Expression value for
	 * BookmarkLink.
	 *
	 * @throws Exception
	 */
	public void testGetLinkExpr() throws Exception {
		// 1.
		ActionHandle actionHandle = ((ImageHandle) designHandle.findElement("Image1")).getActionHandle(); //$NON-NLS-1$

		// test the target-window property of the actionHandle

		assertEquals("Window2", actionHandle.getTargetWindow()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK, actionHandle.getLinkType());
		assertEquals("www.rock.com.cn/haha/test.html", actionHandle.getURI()); //$NON-NLS-1$

		actionHandle = ((ImageHandle) designHandle.findElement("Image2")).getActionHandle(); //$NON-NLS-1$ ;

		assertEquals(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH, actionHandle.getLinkType());
		assertEquals("www.rock.com/bookmarks/1.jsp", actionHandle.getTargetBookmark());//$NON-NLS-1$

		// 3.
		actionHandle = ((ImageHandle) designHandle.findElement("Image4")).getActionHandle(); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK, actionHandle.getLinkType());
		assertEquals("www.rock.com.cn/haha/index.html/bookmarklink1", actionHandle.getTargetBookmark());//$NON-NLS-1$

	}

	/**
	 * Tests toolTip in the action structure.
	 *
	 * @throws Exception
	 */
	public void testToolTip() throws Exception {
		ActionHandle actionHandle = ((ImageHandle) designHandle.findElement("Image1")).getActionHandle(); //$NON-NLS-1$
		assertEquals("toolTip", actionHandle.getToolTip()); //$NON-NLS-1$
	}

	/**
	 * Test getDrillthroughParameters().
	 * <p>
	 * case1: linkType = "HyperLink" Return should be null.
	 * <p>
	 * Case2: linkType = "Drillthrough" drillThroughLinkType = "BookmarkLink" Return
	 * should be list containing 2 items.
	 *
	 * @throws Exception
	 */

	public void testGetDrillthroughParameters() throws Exception {
		ActionHandle actionHandle = ((ImageHandle) designHandle.findElement("Image1")).getActionHandle(); //$NON-NLS-1$ ;

		Iterator parameters = actionHandle.paramBindingsIterator();
		assertFalse(parameters.hasNext());

		actionHandle = ((ImageHandle) designHandle.findElement("Image2")).getActionHandle(); //$NON-NLS-1$ ;

		String linkType = actionHandle.getLinkType();
		assertEquals(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH, linkType);

		String drillThroughReportName = actionHandle.getReportName();
		assertEquals("Another report", drillThroughReportName); //$NON-NLS-1$

		String bookmark = actionHandle.getTargetBookmark();
		assertEquals("www.rock.com/bookmarks/1.jsp", bookmark); //$NON-NLS-1$

		parameters = actionHandle.paramBindingsIterator();
		assertNotNull(parameters);
		ParamBindingHandle param1 = (ParamBindingHandle) parameters.next();

		assertEquals("param1", param1.getParamName()); //$NON-NLS-1$
		assertEquals("1+1=3", param1.getExpression()); //$NON-NLS-1$

		assertNotNull(parameters.next());
		assertNull(parameters.next());
	}

	/**
	 * Test getDrillthroughSearchKeys().
	 * <p>
	 * Case1: linkType = "HyperLink"
	 * <p>
	 * Return is null
	 * <p>
	 * Case2: linkType = "Drillthrough" drillThroughLinkType = "Search" Return
	 * should be a list containing 2 items
	 *
	 * @throws Exception
	 */
	public void testGetDrillthroughSearchKeys() throws Exception {
		ActionHandle actionHandle = ((ImageHandle) designHandle.findElement("Image1")).getActionHandle(); //$NON-NLS-1$

		Iterator searchKeys = actionHandle.searchIterator();
		assertFalse(searchKeys.hasNext());

		actionHandle = ((ImageHandle) designHandle.findElement("Image3")).getActionHandle(); //$NON-NLS-1$ ;

		searchKeys = actionHandle.searchIterator();
		assertNotNull(searchKeys);

		SearchKeyHandle key = (SearchKeyHandle) searchKeys.next();
		assertEquals("searchKey1", key.getExpression()); //$NON-NLS-1$

		assertNotNull(searchKeys.next());
		assertNull(searchKeys.next());

	}

	/**
	 * Test getDrillthroughReportName().
	 * <p>
	 * Case: linkType = "Drillthrough"
	 *
	 * @throws Exception
	 */

	public void testGetDrillthroughReportName() throws Exception {
		ActionHandle actionHandle = ((ImageHandle) designHandle.findElement("Image1")).getActionHandle(); //$NON-NLS-1$
		assertNull(actionHandle.getReportName());
		actionHandle = ((ImageHandle) designHandle.findElement("Image3")).getActionHandle(); //$NON-NLS-1$ ;
		assertEquals("iserver/report1", actionHandle.getReportName()); //$NON-NLS-1$
	}

	/**
	 * This test writes the design file and compare it with golden file.
	 *
	 * @throws Exception
	 *
	 */

	public void testWriter() throws Exception {

		ActionHandle actionHandle = ((ImageHandle) designHandle.findElement("Image1")).getActionHandle(); //$NON-NLS-1$
		actionHandle.setToolTip("new toolTip");//$NON-NLS-1$
		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Tests the action property changes. Now action in label, image, data, level
	 * and measure is changed to structure list rather than single structure.
	 * Bugzilla 265391.
	 *
	 * @throws Exception
	 */
	public void testListAction() throws Exception {
		openDesign("action_test_1.xml"); //$NON-NLS-1$
		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("Image1"); //$NON-NLS-1$
		PropertyHandle propHandle = imageHandle.getPropertyHandle(IImageItemModel.ACTION_PROP);
		Iterator<ActionHandle> iter = imageHandle.actionsIterator();
		ActionHandle actionHandle = iter.next();
		assertNotSame(actionHandle.getStructure(), iter.next().getStructure());

		// add the first action to the end
		propHandle.addItem(actionHandle.getStructure());

		// save report
		save();
		assertTrue(compareFile("action_test_golden_1.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests the ClassCastException for method dealAction in BoundColumnsMgr. TED
	 * 27597.
	 *
	 * @throws Exception
	 */
	public void testDealAction() throws Exception {
		openDesign("action_test_2.xml"); //$NON-NLS-1$

		save();
	}

}
