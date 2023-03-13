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
package org.eclipse.birt.data.engine.executor.cache;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.binding.NestedQueryTest;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class CacheNestedQueryTest extends NestedQueryTest {

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void cacheNestedQuerySetUp() throws Exception {

		System.setProperty("birt.data.engine.test.memcachesize", "20000");
	}

	@Override
	@Test
	public void test6() throws Exception {
		System.setProperty("birt.data.engine.test.memcachesize", "20000");
		super.test6();
	}

	@Override
	protected Map getAppContext() {
		Map appContext = new HashMap();
		appContext.put("birt.data.engine.test.memcachesize", "20000");
		return appContext;
	}

}
