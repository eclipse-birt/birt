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
 * @version $Revision: 1.7 $ $Date: 2006/03/14 09:35:24 $
 */
public class ColumnDesign extends StyledElementDesign
{
	/**
	 * width of this column
	 */
	protected DimensionType width;

	/**
	 * suppressDuplicate
	 */
	protected boolean suppressDuplicate = false; 
	
	/**
	 * Visibility property.
	 */
	protected VisibilityDesign visibility;
	
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
	
	/**
	 * @param suppress
	 *            The suppressDuplicate to set.
	 */
	public void setSuppressDuplicate(boolean suppress)
	{
		suppressDuplicate = suppress;
	}
	
	/**
	 * @return Returns the suppressDuplicate.
	 */
	public boolean getSuppressDuplicate()
	{
		return suppressDuplicate;
	}
	
	/**
	 * @return Returns the visibility.
	 */
	public VisibilityDesign getVisibility( )
	{
		return visibility;
	}
	/**
	 * @param visibility The visibility to set.
	 */
	public void setVisibility( VisibilityDesign visibility )
	{
		this.visibility = visibility;
	}
}
