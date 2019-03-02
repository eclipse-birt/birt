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

package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;

import org.eclipse.birt.data.engine.olap.data.document.DocumentObjectCache;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;

import org.junit.After;
import org.junit.Before;

/**
 * 
 */

public class CachedDocumentObjectManagerTest extends FileDocumentManagerTest
{

	private DocumentObjectCache cachedManager;
	@Before
    public void cachedDocumentObjectManagerSetUp() throws Exception
	{
		cachedManager = new DocumentObjectCache( documentManager,
				generateRandomInt( 1024 ) );
	}
	@After
    public void cachedDocumentObjectManagerTearDown() throws Exception
	{
		cachedManager.closeAll( );
	}

	protected IDocumentObject openIDocumentObject( String documentObjectName )
			throws IOException
	{
		return cachedManager.getIDocumentObject( documentObjectName );
	}

}
