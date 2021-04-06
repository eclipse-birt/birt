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
