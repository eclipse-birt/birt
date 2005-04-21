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

/**
 * Provides the interfaces for the Row Content
 * 
 * 
 * @version $Revision: 1.2 $ $Date: 2005/03/15 03:29:37 $
 */
public interface IRowContent extends IStyledElementContent
{

	/**
	 * @return the Bookmark value
	 */
	public String getBookmarkValue( );

	/**
	 * @return Returns the height.
	 */
	public DimensionType getHeight( );
}