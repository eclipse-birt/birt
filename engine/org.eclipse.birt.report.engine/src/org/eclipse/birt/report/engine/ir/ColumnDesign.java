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

package org.eclipse.birt.report.engine.ir;

/**
 * Column define.
 * 
 * @see TableItemDesign
 * @see GridItemDesign
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
public class ColumnDesign extends StyledElementDesign
{

	/**
	 * column define repeated count
	 */
	protected int repeat = 1;
	/**
	 * width of this column
	 */
	protected DimensionType width;

	/**
	 * @return Returns the repeat.
	 */
	public int getRepeat( )
	{
		return repeat;
	}

	/**
	 * @param repeat
	 *            The repeat to set.
	 */
	public void setRepeat( int repeat )
	{
		assert ( repeat >= 1 );
		this.repeat = repeat;
	}

	/**
	 * @return Returns the width.
	 */
	public DimensionType getWidth( )
	{
		return width;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth( DimensionType width )
	{
		this.width = width;
	}
}
