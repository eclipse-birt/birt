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

package org.eclipse.birt.chart.tests.device;

import org.eclipse.birt.chart.tests.device.render.ImageRenderTest;
import org.eclipse.birt.chart.tests.device.svg.SVGGradientPaintTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class DeviceTest {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.chart.device " + //$NON-NLS-1$
				"and org.eclipse.birt.chart.device.extension"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTest(ImageRenderTest.suite());
		suite.addTestSuite(SVGGradientPaintTest.class);

		// $JUnit-END$
		return suite;
	}

}
