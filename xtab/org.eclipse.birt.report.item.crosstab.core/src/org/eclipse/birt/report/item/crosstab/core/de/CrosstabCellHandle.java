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

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabCellConstants;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * DimensionViewHandle.
 */
public class CrosstabCellHandle extends AbstractCrosstabItemHandle implements
		ICrosstabCellConstants
{

	/**
	 * 
	 * @param handle
	 */
	protected CrosstabCellHandle( DesignElementHandle handle )
	{
		super( handle );
	}

	/**
	 * Gets the content slot handle of crosstab cell.
	 * 
	 * @return the content slot handle
	 */
	PropertyHandle getContentProperty( )
	{
		return handle.getPropertyHandle( CONTENT_PROP );
	}

	/**
	 * Returns an unmodifiable list of model handles for contents in this cell.
	 * 
	 * @return
	 */
	public List getContents( )
	{
		return Collections.unmodifiableList( getContentProperty( ).getContents( ) );
	}

	/**
	 * Adds content to the last position for cell contents.
	 * 
	 * @param content
	 * @throws SemanticException
	 */
	public void addContent( DesignElementHandle content )
			throws SemanticException
	{
		PropertyHandle ph = getContentProperty( );

		if ( ph != null )
		{
			ph.add( content );
		}
	}

	/**
	 * Adds content to given position for cell contents.
	 * 
	 * @param content
	 * @param newPos
	 * @throws SemanticException
	 */
	public void addContent( DesignElementHandle content, int newPos )
			throws SemanticException
	{
		PropertyHandle ph = getContentProperty( );

		if ( ph != null )
		{
			ph.add( content, newPos );
		}
	}

	/**
	 * Gets the dimension value handle for the cell width.
	 * 
	 * @return cell width dimension value handle
	 */
	public DimensionHandle getWidth( )
	{
		return handle.getDimensionProperty( IReportItemModel.WIDTH_PROP );
	}

	/**
	 * Gets the dimension value handle for the cell height.
	 * 
	 * @return cell height dimension value handle
	 */
	public DimensionHandle getHeight( )
	{
		return handle.getDimensionProperty( IReportItemModel.HEIGHT_PROP );
	}
}
