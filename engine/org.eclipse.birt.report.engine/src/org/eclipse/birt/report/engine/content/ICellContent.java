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
 * Provides the interfaces for Cell Content
 * 
 * @version $Revision: 1.6 $ $Date: 2005/11/17 01:40:45 $
 */
public interface ICellContent extends IContainerContent
{

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan( );

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan( );

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn( );

	/**
	 * 
	 * @return
	 */
	public int getRow( );

	public void setColumn( int column );

	public void setRowSpan( int rowSpan );

	public void setColSpan( int colSpan );

	public boolean isStartOfGroup( );

	public void setStartOfGroup( boolean isStartOfGroup );
}