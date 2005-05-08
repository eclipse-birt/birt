/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 * @version $Revision: 1.3 $ $Date: 2005/04/21 01:57:06 $
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