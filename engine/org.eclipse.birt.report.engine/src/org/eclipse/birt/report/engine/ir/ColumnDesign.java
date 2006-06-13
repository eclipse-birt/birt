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
 * @version $Revision: 1.8 $ $Date: 2006/05/17 05:42:10 $
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
	 * If the is any data item in the detail cell of this column.
	 */
	protected boolean hasDataItemsInDetail = false;

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

	
	/**
	 * @return the hasDataItemsInDetail
	 */
	public boolean hasDataItemsInDetail( )
	{
		return hasDataItemsInDetail;
	}

	/**
	 * @param hasDataItemsInDetail the hasDataItemsInDetail to set
	 */
	public void setHasDataItemsInDetail( boolean hasDataItemsInDetail )
	{
		this.hasDataItemsInDetail = hasDataItemsInDetail;
	}
}
