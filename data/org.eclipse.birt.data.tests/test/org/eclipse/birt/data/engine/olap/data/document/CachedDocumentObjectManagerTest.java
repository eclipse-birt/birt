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

package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;

/**
 *
 */

public class CachedDocumentObjectManagerTest extends FileDocumentManagerTest {

	private DocumentObjectCache cachedManager;

	@Before
	public void cachedDocumentObjectManagerSetUp() throws Exception {
		cachedManager = new DocumentObjectCache(documentManager, generateRandomInt(1024));
	}

	@After
	public void cachedDocumentObjectManagerTearDown() throws Exception {
		cachedManager.closeAll();
	}

	@Override
	protected IDocumentObject openIDocumentObject(String documentObjectName) throws IOException {
		return cachedManager.getIDocumentObject(documentObjectName);
	}

}
