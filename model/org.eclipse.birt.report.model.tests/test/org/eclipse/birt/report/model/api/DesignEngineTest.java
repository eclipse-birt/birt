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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests for DesignEngine.
 */

public class DesignEngineTest extends BaseTestCase {

	/**
	 * Test cases:
	 * <p>
	 * Use the factory way to create DesignEngine.
	 * 
	 * @throws Exception
	 */

	public void testNewDesignEngine() throws Exception {
		IDesignEngineFactory factory = (IDesignEngineFactory) Platform
				.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
		IDesignEngine engine = factory.createDesignEngine(new DesignConfig());
		assertNotNull(engine);

		assertNotNull(engine.newSessionHandle(ULocale.ENGLISH));
		assertNotNull(engine.getMetaData());
	}

	/**
	 * Test cases:
	 * <p>
	 * Directly create a DesignEngine.
	 * 
	 * @throws Exception
	 */

	public void testDesignEngine() throws Exception {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		assertNotNull(engine);

		assertNotNull(engine.newSessionHandle(ULocale.ENGLISH));
		assertNotNull(engine.getMetaData());
	}

	/**
	 * Test cases:
	 * <p>
	 * Static methods on DesignEngine for the compaitibility.
	 * 
	 * @throws Exception
	 */

	public void testObsoleteDesignEngine() throws Exception {

		assertNotNull(DesignEngine.newSession(ULocale.ENGLISH));
		assertNotNull(DesignEngine.getMetaDataDictionary());
	}
}
