
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
