/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
