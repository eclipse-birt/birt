
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
package org.eclipse.birt.report.data.adapter.api;

/**
 * Provide a linked IResultIterator instance for engine to wrapper an
 * IResultIterator in case that they have parent.
 */

public interface ILinkedResult {
	public static final int TYPE_CUBE = 1;
	public static final int TYPE_TABLE = 2;

	/**
	 * Get parent linked result iterator.
	 * 
	 * @return
	 */
	public ILinkedResult getParent();

	/**
	 * Return result iterator.
	 * 
	 * @return
	 */
	public Object getCurrentResult();

	/**
	 * Return current result type.
	 * 
	 * @return
	 */
	public int getCurrentResultType();

}
