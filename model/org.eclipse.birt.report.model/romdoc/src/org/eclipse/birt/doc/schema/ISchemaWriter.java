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

package org.eclipse.birt.doc.schema;

/**
 * Interface of schema writer
 *
 */
public interface ISchemaWriter {

	/**
	 * Close the write at the completion of the file.
	 */

	void close();

	/**
	 * Start html
	 */

	void startHtml();

	/**
	 * Close html
	 */

	void closeHtml();

	/**
	 * Write table row
	 * 
	 * @param css css store prop name , allowed value and so on.see
	 *            <code>CssType</code>
	 */

	public void writeRow(CssType css);

}