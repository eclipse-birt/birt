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
