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

package org.eclipse.birt.report.model.metadata;

import java.io.InputStream;

import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The test cases to for custom-load ROM cases.
 */

abstract public class AbstractMetaTest extends BaseTestCase {

	protected void tearDown() throws Exception {
		MetaDataDictionary.reset();
		engine = null;
		super.tearDown();
	}

	/**
	 * @param is
	 * @throws MetaDataParserException
	 */

	protected final void loadMetaData(InputStream is) throws MetaDataParserException {
		MetaDataDictionary.reset();
		MetadataTestUtil.readRom(is);
	}

}
