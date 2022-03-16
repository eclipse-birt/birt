/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui;

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtilTest;
import org.eclipse.birt.report.designer.util.DNDUtilTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUITests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.designer.ui");
		// $JUnit-BEGIN$
		suite.addTest(new TestSuite(SimpleUITest.class));
		suite.addTest(new TestSuite(ReportPlatformUIImagesTest.class));
		suite.addTest(new TestSuite(DNDUtilTest.class));
		suite.addTest(new TestSuite(ReportPluginTest.class));
		suite.addTest(new TestSuite(InsertInLayoutUtilTest.class));

		suite.addTest(org.eclipse.birt.report.designer.ui.extensions.AllTests.suite());
		suite.addTest(org.eclipse.birt.report.designer.internal.ui.palette.AllTests.suite());
		suite.addTest(org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AllTests.suite());
		suite.addTest(org.eclipse.birt.report.designer.ui.views.attributes.AllTests.suite());
		// $JUnit-END$
		return suite;
	}
}
