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

package org.eclipse.birt.chart.tests.i18n;

import org.eclipse.birt.chart.tests.i18n.MessagesTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class I18nTest {
	public static Test suite() {

		TestSuite suite = new TestSuite("Test for org.eclipse.birt.chart.device.extension.i18n" + //$NON-NLS-1$
				", org.eclipse.birt.chart.device.svg.i18n" + //$NON-NLS-1$
				", org.eclipse.birt.chart.engine.i18n" + //$NON-NLS-1$
				", org.eclipse.birt.chart.engine.extension.i18n" + //$NON-NLS-1$
				", org.eclipse.birt.chart.reportitem.i18n");//$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(MessagesTest.class);

		// $JUnit-END$
		return suite;
	}

}
