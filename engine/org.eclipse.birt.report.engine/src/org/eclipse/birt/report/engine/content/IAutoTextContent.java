/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content;

/**
 * Provides the interfaces for the AutoText Content.
 * <p>
 * AutoText Content is created at the start of the process, but the text value
 * is set at the end of the process. The typical implementation of AutoText is
 * total page of report.
 * <p>
 * The following types of the AutoText content are predefined:
 * <li><code>TOTAL_PAGE</code></li>
 * <li><code>PAGE_NUMBER</code></li>
 */
public interface IAutoTextContent extends ITextContent {
	public static final int TOTAL_PAGE = 0;
	public static final int PAGE_NUMBER = 1;
	public static final int UNFILTERED_TOTAL_PAGE = 2;
	public static final int UNFILTERED_PAGE_NUMBER = 3;
	public static final int PAGE_VARIABLE = 4;

	/**
	 * Set the type of the AutoText Content. This type must be one of the
	 * predefines.
	 * <li><code>TOTAL_PAGE</code></li>
	 * <li><code>PAGE_NUMBER</code></li>
	 * 
	 * @param type the type of the AutoText Content.
	 */
	void setType(int type);

	/**
	 * Get the type of the AutoText Content.
	 * <p>
	 * The return value must be on of the predefines.
	 * <li><code>TOTAL_PAGE</code></li>
	 * <li><code>PAGE_NUMBER</code></li>
	 */
	int getType();
}
