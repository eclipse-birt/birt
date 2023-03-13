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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;

/**
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ListDesignTest extends AbstractDesignTestCase {

	protected ListItemDesign list;

	@Override
	public void setUp() throws Exception {
		loadDesign("ListItem_test.xml");
		list = (ListItemDesign) report.getContent(0);
		assertTrue(list != null);
	}

	public void testListItem() {
		// script
//		assertEquals( list.getOnFinish( ), "onFinish" );
//		assertEquals( list.getOnRow( ), "onRow" );
//		assertEquals( list.getOnStart( ), "onStart" );

		// width,height, x, y
		assertEquals(2, list.getHeight().getMeasure(), Double.MIN_VALUE);
		assertEquals(2, list.getWidth().getMeasure(), Double.MIN_VALUE);
		assertEquals(2, list.getX().getMeasure(), Double.MIN_VALUE);
		assertEquals(2, list.getY().getMeasure(), Double.MIN_VALUE);
		assertEquals("myList", list.getName());

		// header content
		ListBandDesign listBand = (ListBandDesign) list.getHeader();
		assertEquals(1, listBand.getContentCount());

		// group
		assertEquals(list.getGroupCount(), 2);

		GroupDesign group = list.getGroup(0);
		assertEquals("group1", group.getName());

		// group header
		assertEquals(1, group.getHeader().getContentCount());

		// group footer
		assertEquals(1, group.getFooter().getContentCount());

		// details
		assertEquals(2, list.getDetail().getContentCount());

		// footer
		assertEquals(1, list.getFooter().getContentCount());
	}
}
