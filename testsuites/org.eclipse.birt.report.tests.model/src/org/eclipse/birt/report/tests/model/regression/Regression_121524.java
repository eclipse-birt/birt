/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Border is still displayed in the layout after delete the style
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>New a custom style with background color: yellow,
 * border{solid,red,10pixels}
 * <li>Add a table and apply the style to the table (see the attachement)
 * <li>Delete the style
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * All style disappear
 * <p>
 * <b>Actual result:</b>
 * <p>
 * Border is still displayed in the layout,while other style disappear. In
 * preview, border is not there.
 * </p>
 * Test description:
 * <p>
 * Ensure that Model will send style event if application deletes a custom style
 * and un-resolve the applied style in some report items, such as table, grid
 * and so on.
 * </p>
 */
public class Regression_121524 extends BaseTestCase {

	/**
	 * @throws SemanticException
	 * 
	 */
	public void test_regression_121524() throws SemanticException {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = sessionHandle.createDesign();

		ElementFactory factory = designHandle.getElementFactory();

		// new table, add listener.
		TableHandle tableHandle = factory.newTableItem("table1"); //$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		MyListener listener = new MyListener();
		tableHandle.addListener(listener);

		// apply a style
		StyleHandle style = factory.newStyle("s1"); //$NON-NLS-1$
		style.setBorderBottomStyle("dotted"); //$NON-NLS-1$
		designHandle.getStyles().add(style);

		tableHandle.setStyleName("s1"); //$NON-NLS-1$
		assertEquals("dotted", tableHandle.getStringProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP)); //$NON-NLS-1$

		// drop the style and check that table has received an notification
		// event.

		designHandle.findStyle("s1").drop(); //$NON-NLS-1$

		assertTrue(listener.event instanceof StyleEvent);
		assertEquals("table1", listener.focus.getName()); //$NON-NLS-1$
		assertEquals("none", tableHandle //$NON-NLS-1$
				.getStringProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP));
	}

	class MyListener implements Listener {
		NotificationEvent event = null;
		DesignElementHandle focus = null;

		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			this.event = ev;
			this.focus = focus;
		}

	}
}
