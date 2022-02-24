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

package org.eclipse.birt.report.designer.core.commands;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author xzhang
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class AllCommandTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.designer.core.commands");
		// $JUnit-BEGIN$
		suite.addTestSuite(CreateCommandTest.class);
		suite.addTestSuite(DeleteCommandTest.class);
		suite.addTestSuite(FlowMoveChildCmdTest.class);
		suite.addTestSuite(MoveGuideCommandTest.class);
		suite.addTestSuite(PasteCommandTest.class);
		suite.addTestSuite(PasteStructureCommandTest.class);
		suite.addTestSuite(SetConstraintCommandTest.class);
		suite.addTestSuite(SetPropertyCommandTest.class);
		// $JUnit-END$
		return suite;
	}

}
