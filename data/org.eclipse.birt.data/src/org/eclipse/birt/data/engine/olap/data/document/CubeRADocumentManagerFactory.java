
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
