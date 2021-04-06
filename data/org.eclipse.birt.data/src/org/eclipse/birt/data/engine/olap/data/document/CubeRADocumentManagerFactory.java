
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class CubeRADocumentManagerFactory {
	public static IDocumentManager createRADocumentManager(String cubeName, IDocArchiveReader reader)
			throws DataException, IOException {
		return DocumentManagerFactory.createRADocumentManager(reader);
	}
}
