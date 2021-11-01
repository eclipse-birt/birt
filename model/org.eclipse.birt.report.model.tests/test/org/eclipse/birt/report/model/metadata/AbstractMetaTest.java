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
