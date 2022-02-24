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

import org.eclipse.birt.data.engine.binding.SubQueryTest;

/**
 *
 */
public class CacheSubqueryTest extends SubQueryTest {

	protected Map getAppContext() {
		Map appContext = new HashMap();
		appContext.put("birt.data.engine.test.memcachesize", "20000");
		return appContext;
	}
}
