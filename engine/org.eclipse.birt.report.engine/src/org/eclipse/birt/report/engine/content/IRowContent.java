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

package org.eclipse.birt.report.engine.content;

import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.Expression;

/**
 * Provides the interfaces for the Row Content
 * 
 * 
 * @version $Revision$ $Date$
 */
public interface IRowContent extends IStyledElementContent
{

	/**
	 * @return Returns the Bookmark.
	 */
	public Expression getBookmark( );

	/**
	 * @return the Bookmark value
	 */
	public String getBookmarkValue( );

	/**
	 * Set the bookmark value which is calculated in the Executor
	 * 
	 * @param newValue
	 */
	public void setBookmarkValue( String newValue );

	/**
	 * @return Returns the height.
	 */
	public DimensionType getHeight( );
}