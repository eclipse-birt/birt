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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * This is a test action.Add next level handle to the dimension handle.
 */
public class AddLevelHandleAction extends AbstractCrosstabAction
{

	DimensionViewHandle viewHandle = null;
	private static final String NAME = "add levelview handle";
	private static final String ID = "add_levelViewhandle";
	private static final String TEXT = "add levelview handle";

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public AddLevelHandleAction( DesignElementHandle handle )
	{
		super( handle );
		setId( ID );
		setText( TEXT );
		ExtendedItemHandle extendedHandle = getExtendedItemHandle( handle );
		setHandle( extendedHandle );
		viewHandle = getDimensionViewHandle( extendedHandle );
	}

	private DimensionViewHandle getDimensionViewHandle(
			ExtendedItemHandle extendedHandle )
	{
		AbstractCrosstabItemHandle handle = (AbstractCrosstabItemHandle) CrosstabUtil.getReportItem( extendedHandle );
		while ( handle != null )
		{

			if ( handle instanceof DimensionViewHandle )
			{
				return (DimensionViewHandle) handle;
			}
			handle = handle.getContainer( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		transStar( NAME );
		try
		{
			int viewCount = viewHandle.getLevelCount( );
			DimensionHandle dimensionHandle = viewHandle.getCubeDimension( );
			HierarchyHandle hierarchyHandle = dimensionHandle.getDefaultHierarchy( );
			int count = hierarchyHandle.getLevelCount( );
			if ( count == 0 || viewCount >= count )
			{
				rollBack( );
				return;
			}
			LevelHandle levelHandle = hierarchyHandle.getLevel( viewCount );

			LevelViewHandle levelViewHandle = viewHandle.insertLevel( levelHandle,
					viewCount );

			CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

			// TODO create a data bingding dataitem
			DataItemHandle dataHandle = DesignElementFactory.getInstance( )
					.newDataItem( levelHandle.getName( ) );

			cellHandle.addContent( dataHandle );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			return;
		}
		transEnd( );
	}

	private ExtendedItemHandle getExtendedItemHandle( DesignElementHandle handle )
	{
		// DesignElementHandle temp = handle;
		while ( handle != null )
		{
			if ( handle instanceof ExtendedItemHandle )
			{
				return (ExtendedItemHandle) handle;
			}
			handle = handle.getContainer( );

		}
		return null;
	}
}
