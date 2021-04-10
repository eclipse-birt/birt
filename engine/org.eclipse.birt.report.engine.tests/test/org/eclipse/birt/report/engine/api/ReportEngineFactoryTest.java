
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
