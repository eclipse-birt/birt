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

package org.eclipse.birt.report.designer.core.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * @author xzhang
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SetPropertyCommandTest extends BaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testSetLabelProperty() {

		LabelHandle label = getReportDesignHandle().getElementFactory().newLabel("Label");

		try {
			label.setText("Label Test");
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Set label text");
		}
		assertTrue(label.getText().equals("Label Test"));

		Map extendsData = new HashMap();
		extendsData.put(DEUtil.ELEMENT_LABELCONTENT_PROPERTY, "New Test");

		SetPropertyCommand cmd = new SetPropertyCommand(label, extendsData);
		cmd.execute();

		assertTrue(label.getText().equals("New Test"));

	}

	public void testSetTextProperty() {

		TextItemHandle text = getReportDesignHandle().getElementFactory().newTextItem("Text");

		try {
			text.setContent("TextItem Test");
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Set TextItem Content");
		}
		assertTrue(text.getContent().equals("TextItem Test"));

		Map extendsData = new HashMap();
		extendsData.put(DEUtil.ELEMENT_LABELCONTENT_PROPERTY, "New TextItem Test");

		SetPropertyCommand cmd = new SetPropertyCommand(text, extendsData);
		cmd.execute();

		assertTrue(text.getContent().equals("New TextItem Test"));

	}

}