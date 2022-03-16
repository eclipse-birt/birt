/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api;

import java.util.Date;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

public class EngineExceptionTest extends TestCase {

	public void testLocalizedEngineException() {
		Date date = new Date();

		EngineException.setULocale(ULocale.CHINA);
		EngineException.setULocale(ULocale.CHINA);

		EngineException cnEx = new EngineException("date:{0}", date);

		System.out.println(cnEx.getLocalizedMessage());

		EngineException.setULocale(ULocale.ENGLISH);
		EngineException enEx = new EngineException("date:{0}", date);

		System.out.println(enEx.getLocalizedMessage());
	}

}
