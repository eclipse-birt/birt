
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

/**
 * 
 */

public interface IDocumentManager {
	/**
	 * Create a document object by name.
	 * 
	 * @param pathName
	 * @return document object if the named document object was successfully
	 *         created; null if the named file already exists
	 */
	public IDocumentObject createDocumentObject(String documentObjectName) throws IOException;

	/**
	 * Check the named document object whether exists or not
	 * 
	 * @param documentObjectName
	 * @return if the named document object exists; false otherwise
	 */
	public boolean exist(String documentObjectName);

	/**
	 * Open the named document object.
	 * 
	 * @param documentObjectName
	 * @return
	 */
	public IDocumentObject openDocumentObject(String documentObjectName) throws IOException;

	/**
	 * 
	 * @throws IOException
	 */
	public void flush() throws IOException;

	/**
	 * Close this document manager and releases any system resources associated with
	 * the object.
	 * 
	 * @param documentObjectName
	 * @return
	 */
	public void close() throws IOException;

}
