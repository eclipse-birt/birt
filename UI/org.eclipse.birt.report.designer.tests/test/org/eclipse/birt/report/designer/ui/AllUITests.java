/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtilTest;
import org.eclipse.birt.report.designer.util.DNDUtilTest;

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