
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.core.framework.Platform;

/**
 * 
 */

public class ReportEngineFactoryTest extends EngineCase {
	public void testCreateFactoryObject() {
		try {
			EngineConfig config = new EngineConfig();
			Object factory = Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			assertTrue(factory instanceof IReportEngineFactory);
			IReportEngine engine = null;
			if (factory instanceof IReportEngineFactory) {
				engine = ((IReportEngineFactory) factory).createReportEngine(config);
				assert (engine != null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}
}
