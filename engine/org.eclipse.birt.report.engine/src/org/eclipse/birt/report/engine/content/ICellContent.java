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
 * Provides the interfaces for Cell Content
 * 
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
	 * @return the column content.
	 */
	public IColumn getColumnInstance( );
	/**
	 * 
	 * @return
	 */
	public int getRow( );

	public void setColumn( int column );

	public void setRowSpan( int rowSpan );

	public void setColSpan( int colSpan );

	public void setDisplayGroupIcon( boolean displayGroupIcon );
	
	public boolean getDisplayGroupIcon( );
	
	public boolean hasDiagonalLine( );

	public int getDiagonalNumber( );

	public String getDiagonalStyle( );

	public DimensionType getDiagonalWidth( );

	public int getAntidiagonalNumber( );

	public String getAntidiagonalStyle( );

	public DimensionType getAntidiagonalWidth( );

	public String getHeaders( );

	public void setHeaders( String headers );

	public String getScope( );

	public void setScope( String scope );

}