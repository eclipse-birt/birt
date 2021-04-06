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

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Test class for test methods of DefaultPageGenerator.
 * 
 * 
 */
public class TabPageGeneratorTest extends TestCase {

	/**
	 * Testcase for test createTabItems() method
	 */
	public void testCreateTabItems() {
		Shell shell = new Shell();
		TabPageGenerator generator = new TabPageGenerator();
		generator.createControl(shell, new ArrayList());
		Control control = generator.getControl();
		if (control instanceof CTabFolder)
			assertEquals(0, ((CTabFolder) control).getItemCount());
		shell.dispose();
	}

}