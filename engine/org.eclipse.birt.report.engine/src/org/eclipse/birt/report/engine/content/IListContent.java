/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content;

/**
 * Provides interfaces for List Content.
 */
public interface IListContent extends IContainerContent {
	/**
	 * Get the header of the list.
	 * 
	 * @return the header of the list. Return <code>null</code> if the group does
	 *         not have a header.
	 */
	IListBandContent getHeader();

	/**
	 * Return the value if the list header is repeated.
	 * 
	 * @return the value if the list header is repeated.
	 *         <p>
	 *         <code>true</code>, the list header is repeated. <code>false</code>,
	 *         the list header is not repeated.
	 */
	public boolean isHeaderRepeat();

	/**
	 * Set the value if the list header is repeated.
	 * 
	 * @param repeat if the list header is repeated.
	 */
	public void setHeaderRepeat(boolean repeat);

}
