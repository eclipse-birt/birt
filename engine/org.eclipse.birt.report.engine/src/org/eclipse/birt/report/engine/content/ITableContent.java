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

/**
 * Provides the interfaces for the Table Content
 * 
 * 
 * @version $Revision$ $Date$
 */
public interface ITableContent extends IReportItemContent
{

	public final static int GRID_CONTENT = 0;
	public final static int TABLE_CONTENT = 1;

	/**
	 * @return Returns the caption.
	 */
	public String getCaption( );

	/**
	 * @param caption
	 *            The caption to set.
	 */
	public void setCaption( String caption );

	/**
	 * @return Returns the type.
	 */
	public int getType( );

	public boolean getRepeatHeader( );
}