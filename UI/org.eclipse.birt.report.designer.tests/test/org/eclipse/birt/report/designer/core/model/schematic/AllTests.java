/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.core.model.schematic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.designer.core.views.designer.model");
		// $JUnit-BEGIN$
		suite.addTestSuite(TableHandleAdapterTest.class);
		suite.addTestSuite(CellHandleAdapterTest.class);
		// $JUnit-END$
		return suite;
	}
}