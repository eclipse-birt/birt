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

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.layout.pdf.text.Chunk;

public interface ISplitter {

	/**
	 * Returns <tt>true</tt> if the splitter has more chunks.
	 */
	public boolean hasMore();

	/**
	 * Gets the next chunk. It should return null if the splitter has no more
	 * chunks.
	 */
	public Chunk getNext();
}
